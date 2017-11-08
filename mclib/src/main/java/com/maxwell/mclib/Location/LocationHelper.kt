package com.maxwell.mclib.Location

import android.content.Context
import android.text.TextUtils
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationListener
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by maxwellma on 22/10/2017.
 */
class LocationHelper {

    companion object {
        private val LOCATION_INFO = "TAX_LOCATION_INFO"
        private val LOCATION_TYPE = "LocationType"
        private val LATITUDE = "Latitude"
        private val LONGITUDE = "Longitude"
        private val ACCURACY = "Accuracy"
        private val ADDRESS = "Address"
        private val COUNTRY = "Country"
        private val PROVINCE = "Province"
        private val CITY = "City"
        private val ALTITUDE = "Altitude"
        private val DISTRICT = "District"
        private val ROAD = "Road"
        private val CITYCODE = "CityCode"
        private val ADCODE = "AdCode"
        private val LOCATION_TIME = "LastLocationTime"

        private var mLocationClient: AMapLocationClient? = null

        fun startLocation(context: Context) {
            startLocation(context, null)
        }

        fun startLocation(context: Context, aMapLocationListener: AMapLocationListener?) {
            mLocationClient = AMapLocationClient(context)
            mLocationClient!!.setLocationListener { aMapLocation: AMapLocation? ->
                if (aMapLocation?.errorCode == 0) {
                    saveLocationInfo(context, aMapLocation)
                }
                mLocationClient!!.stopLocation()
                mLocationClient!!.onDestroy()
                mLocationClient = null
                aMapLocationListener?.onLocationChanged(aMapLocation)
            }
            val mLocationOption = AMapLocationClientOption()
            // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.locationMode = AMapLocationMode.Hight_Accuracy
            // 设置是否返回地址信息（默认返回地址信息）
            mLocationOption.isNeedAddress = true
            // 设置是否只定位一次,默认为false
            mLocationOption.isOnceLocation = true
            // 设置是否强制刷新WIFI，默认为强制刷新
            // 设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.isMockEnable = false
            // 设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.interval = 2000
            // 给定位客户端对象设置定位参数
            mLocationClient!!.setLocationOption(mLocationOption)
            // 启动定位
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient!!.startLocation()
        }

        private fun saveLocationInfo(context: Context, amapLocation: AMapLocation) {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = Date(amapLocation.time)
            df.format(date)// 定位时间

            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            var locationType = ""
            when (amapLocation.locationType) {
                1 -> locationType = "GPS"
                2 -> locationType = "FAST"
                3 -> locationType = "SAME_REQ"
                4 -> locationType = "FIX_CACHE"
                5 -> locationType = "WIFI"
                6 -> locationType = "CELL"
                7 -> locationType = "AMAP"
            }

            editor.putString(LOCATION_TYPE, locationType)// 获取当前定位结果来源，如网络定位结果，详见定位类型表
            editor.putString(LATITUDE, amapLocation.latitude.toString() + "")// 获取经度
            editor.putString(LONGITUDE, amapLocation.longitude.toString() + "")// 获取纬度
            editor.putString(ALTITUDE, amapLocation.altitude.toString() + "")// 获取海拔高度
            editor.putString(ADDRESS, amapLocation.address)// 地址
            editor.putString(ACCURACY, amapLocation.accuracy.toString() + "")// 定位精度
            editor.putString(COUNTRY, amapLocation.country)// 国家信息
            editor.putString(PROVINCE, amapLocation.province)// 省信息
            editor.putString(CITY, amapLocation.city)// 城市信息
            editor.putString(DISTRICT, amapLocation.district)// 城区信息
            editor.putString(CITYCODE, amapLocation.cityCode)// 城市编码
            editor.putString(ADCODE, amapLocation.adCode)// 地区编码
            editor.putString(LOCATION_TIME, df.toString())// 地区编码
            editor.commit()
        }


        /**
         * 定位类型
         * @param context
         * *
         * @return
         */
        fun getLocationType(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(LOCATION_TYPE, "")
        }

        /**
         * 经度
         * @param context
         * *
         * @return
         */
        fun getLatitude(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(LATITUDE, "")
        }

        /**
         * 纬度
         * @param context
         * *
         * @return
         */
        fun getLongitude(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(LONGITUDE, "")
        }

        /**
         * 海拔
         * @param context
         * *
         * @return
         */
        fun getAltitude(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(ALTITUDE, "")
        }

        /**
         * 详细地址
         * @param context
         * *
         * @return
         */
        fun getAddress(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(ADDRESS, "")
        }

        /**
         * 定位精度
         * @param context
         * *
         * @return
         */
        fun getAccuracy(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(ACCURACY, "")
        }

        /**
         * 国家
         * @param context
         * *
         * @return
         */
        fun getCountry(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(COUNTRY, "")
        }

        /**
         * 省份信息
         * @param context
         * *
         * @return
         */
        fun getProvince(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(PROVINCE, "")
        }

        /**
         * 城市信息
         * @param context
         * *
         * @return
         */
        fun getCity(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(CITY, "")
        }

        /**
         * 城区信息
         * @param context
         * *
         * @return
         */
        fun getDistrict(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(DISTRICT, "")
        }

        /**
         * 街道信息
         * @param context
         * *
         * @return
         */
        fun getRoad(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(ROAD, "")
        }

        /**
         * 城市编码
         * @param context
         * *
         * @return
         */
        fun getCityCode(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(CITYCODE, "")
        }

        /**
         * 地区编码
         * @param context
         * *
         * @return
         */
        fun getAdCode(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(ADCODE, "")
        }

        /**
         * 上次定位的时间
         * @param context
         * *
         * @return
         */
        fun getLastLocationTime(context: Context): String {
            val preferences = context.getSharedPreferences(LOCATION_INFO, Context.MODE_PRIVATE)
            return preferences.getString(LOCATION_TIME, "")
        }

        private val VALID_TIME = 10 * 60 * 1000

        /**
         * 用来判断上次定位的信息是否有效。如果无效，请重新发起定位。定位成功后会更新所有定位信息。
         * @return
         */
        fun isInvaildLocationInfo(context: Context): Boolean {
            val time = getLastLocationTime(context)
            if (TextUtils.isEmpty(time)) return false

            try {
                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val date = df.parse(time)
                val lastTime = date.time
                val currentTime = Date().time

                return !(lastTime > currentTime || currentTime - lastTime > VALID_TIME)

            } catch (e: Exception) {
            }

            return false
        }
    }


}