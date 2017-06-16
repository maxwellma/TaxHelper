package com.maxwell.lib.network

import com.google.gson.internal.`$Gson$Types`
import okhttp3.Request
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type



/**
 * Created by maxwellma on 11/06/2017.
 */
abstract class ResultCallback<T> {

    var type : Type? = null

    constructor() {
        getSuperclassTypeParameter(javaClass)
    }

    companion object {
        fun getSuperclassTypeParameter(subclass : Class<*>) : Type {
            var superClass = subclass.genericSuperclass
            if (subclass is Class) {
                throw RuntimeException("Missing type parameter.")
            }
            return `$Gson$Types`.canonicalize((superClass as ParameterizedType).actualTypeArguments[0])
        }
    }

    open fun onBefore(request : Request){}

    open fun onAfter() {}

    open fun inProgress(progress : Float){}

    abstract fun onError(request: Request, e: Exception)

    abstract fun onResponse(response: T)

    open fun onResponse(request: Request, response: T) {
        onResponse(response)
    }

    val DEFAULT_RESULT_CALLBACK: ResultCallback<String> = object : ResultCallback<String>() {
        override fun onError(request: Request, e: Exception) {

        }

        override fun onResponse(response: String) {

        }
    }
}