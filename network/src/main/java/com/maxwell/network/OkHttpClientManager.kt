package com.maxwell.network

import android.os.Build
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.maxwell.lib.network.ResultCallback
import okhttp3.*
import org.apache.http.conn.ssl.StrictHostnameVerifier
import java.io.IOException
import java.io.InputStream
import java.lang.reflect.Modifier
import java.security.*
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import javax.net.ssl.*


/**
 * Created by maxwellma on 16/06/2017.
 */
class OkHttpClientManager {

    private var mInstance: OkHttpClientManager = getInstance()
    private var mOkHttpClient: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(object : CookieJar {
                val cookieStore = HashMap<HttpUrl, List<Cookie>>()
                override fun loadForRequest(url: HttpUrl?): List<Cookie> {
                    return cookieStore.get(url) ?: listOf<Cookie>()
                }

                override fun saveFromResponse(url: HttpUrl?, cookies: List<Cookie>?) {
                    cookieStore.put(url!!, cookies!!)
                }

            })
            .hostnameVerifier(StrictHostnameVerifier())
            .build()
    private var mDelivery: Handler = Handler(Looper.getMainLooper())
    private var mGson: Gson? = null

    private constructor() {
        val sdk = Build.VERSION.SDK_INT
        if (sdk >= 23) {
            val gsonBuilder = GsonBuilder()
                    .excludeFieldsWithModifiers(
                            Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC)
            mGson = gsonBuilder.create()
        } else {
            mGson = Gson()
        }
    }

    fun getInstance(): OkHttpClientManager {
        if (mInstance == null) {
            synchronized(OkHttpClientManager::class.java) {
                if (mInstance == null) {
                    mInstance = OkHttpClientManager()
                }
            }
        }
        return mInstance
    }


    fun execute(request: Request, callback: ResultCallback<*>?) {
        var callback = callback
        if (callback == null) callback = ResultCallback.DEFAULT_RESULT_CALLBACK
        val resCallBack = callback
        resCallBack!!.onBefore(request)
        try {
            mOkHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException?) {
                    sendFailResultCallback(call.request(), e as Exception, resCallBack)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response == null) {
                        return
                    }
                    if (resCallBack != null) {
                        resCallBack!!.mStatusCode = response!!.code()
                    }
                    if (response.code() in 400..600) {
                        try {
                            sendFailResultCallback(request, RuntimeException(response!!.body()?.string()), resCallBack)
                        } catch (e: Exception) {
                        }

                        return
                    }

                    try {
                        val string = response!!.body()!!.string()
                        if (resCallBack!!.mType === String::class.java) {
                            sendSuccessResultCallback(request, string, resCallBack)
                        } else {
                            val o = mGson!!.fromJson(string, resCallBack!!.mType.javaClass)
                            sendSuccessResultCallback(request, o, resCallBack)
                        }
                    } catch (e: Exception) {
                        sendFailResultCallback(response!!.request(), e, resCallBack)
                    }
                }
            })
        } catch (e: Exception) {

        }

    }

    @Throws(IOException::class)
    fun <T> execute(request: Request, clazz: Class<T>): T {
        val call = mOkHttpClient.newCall(request)
        val execute = call.execute()
        val respStr = execute.body()?.string()
        return mGson!!.fromJson(respStr, clazz)
    }

    fun sendFailResultCallback(call: Call, e: Exception, callback: ResultCallback<*>) {
        sendFailResultCallback(call.request(), e, callback)
    }

    fun sendFailResultCallback(request: Request, e: Exception, callback: ResultCallback<*>) {
        if (callback == null) return

        mDelivery.post {
            callback!!.onError(request, e)
            callback!!.onAfter()
        }
    }

    fun sendSuccessResultCallback(`object`: Any, callback: ResultCallback<*>) {
        sendSuccessResultCallback(null, `object`, callback)
    }

    fun sendSuccessResultCallback(request: Request?, `object`: Any, callback: ResultCallback<*>?) {
        if (callback == null) return
        mDelivery.post {
            //callback!!.onResponse(request,  `object`)
            callback!!.onAfter()
        }
    }


    fun addCertificates(vararg certificates: InputStream) {
        setCertificates(certificates.toList() as ArrayList<InputStream>, null as InputStream?, null as String?)
    }

    private fun prepareTrustManager(certificates: ArrayList<InputStream>): Array<TrustManager> {
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null)
        certificates.withIndex().forEach {
            run {
                keyStore.setCertificateEntry(it.index.toString(), certificateFactory.generateCertificate(it.value))
                try {
                    it.value?.close()
                } catch (e: Exception) {
                }
            }
        }
        var trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)

        val trustManagers = trustManagerFactory.trustManagers

        return trustManagers
    }

    private fun prepareKeyManager(bksFile: InputStream, password: String): Array<KeyManager>? {
        try {
            val clientKeyStore = KeyStore.getInstance("BKS")
            clientKeyStore.load(bksFile, password.toCharArray())
            val keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            keyManagerFactory.init(clientKeyStore, password.toCharArray())
            return keyManagerFactory.getKeyManagers()

        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun setCertificates(certificates: ArrayList<InputStream>, bksFile: InputStream?, password: String?) {
        try {
            if (bksFile == null || password == null) return
            val trustManagers = prepareTrustManager(certificates)
            val keyManagers = prepareKeyManager(bksFile, password)
            val sslContext = SSLContext.getInstance("TLS")

            sslContext.init(keyManagers, arrayOf<TrustManager>(MyTrustManager(chooseTrustManager(trustManagers))), SecureRandom())
            mOkHttpClient.newBuilder().sslSocketFactory(sslContext.socketFactory)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        }

    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
        for (trustManager in trustManagers) {
            if (trustManager is X509TrustManager) {
                return trustManager
            }
        }
        return null
    }


    private inner class MyTrustManager @Throws(NoSuchAlgorithmException::class, KeyStoreException::class)
    constructor(private val localTrustManager: X509TrustManager?) : X509TrustManager {
        override fun checkClientTrusted(p0: Array<out java.security.cert.X509Certificate>?, p1: String?) {
        }

        override fun checkServerTrusted(chain: Array<out java.security.cert.X509Certificate>?, authType: String?) {
            // This path is for check self-generated cert.
            //            try
            //            {
            //                defaultTrustManager.checkServerTrusted(chain, authType);
            //            } catch (CertificateException ce)
            //            {
            //                localTrustManager.checkServerTrusted(chain, authType);
            //            }

            // This path is for https validation.
            localTrustManager?.checkServerTrusted(chain, authType)
        }

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
            return arrayOf()
        }

        private val defaultTrustManager: X509TrustManager?

        init {
            val var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            var4.init(null as? KeyStore)
            defaultTrustManager = chooseTrustManager(var4.trustManagers)
        }
    }

}
