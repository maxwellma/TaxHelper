package com.maxwell.taxhelper

import com.maxwell.mclib.util.PinyinUtil
import com.maxwell.taxhelper.bean.City
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * Created by maxwellma on 05/11/2017.
 */
class CityListProvider {

    companion object {
        private var cityListProvider: CityListProvider? = null

        fun getInstance(): CityListProvider {
            synchronized(CityListProvider::class.java) {
                if (cityListProvider == null) {
                    cityListProvider = CityListProvider()
                }
                return cityListProvider!!
            }
        }
    }

    private var cityStrList = listOf("北京", "天津", "太原", "呼和浩特", "石家庄", "上海", "南京", "杭州", "济南", "苏州", "福州", "厦门", "合肥", "青岛", "武汉", "南昌", "长沙", "郑州", "广州", "深圳", "南宁", "海口", "珠海"
            , "沈阳", "长春", "哈尔滨", "西安", "银川", "兰州", "西宁", "乌鲁木齐", "重庆", "成都", "昆明", "贵阳")

    var cityList: List<City> = generateCityList()
        get() {
            return field
        }
        private set(value) {
            field = value
        }

    private fun generateCityList(): List<City> {
        var cityList = ArrayList<City>()
        cityStrList.forEach { city ->
            if (StringUtils.equalsIgnoreCase("重庆", city)) {
                cityList.add(City(city, "CHONGQING"))
            } else if (StringUtils.equalsIgnoreCase("长沙", city)) {
                cityList.add(City(city, "CHANGSHA"))
            } else if (StringUtils.equalsIgnoreCase("长春", city)) {
                cityList.add(City(city, "CHANGCHUN"))
            } else {
                cityList.add(City(city, PinyinUtil.getPinyin(city)))
            }
        }
        return cityList
    }

    fun isCitySupported(city : String) : Boolean {
        return StringUtils.isNotEmpty(city) && cityStrList.contains(city.removeSuffix("市"))
    }

    private constructor()
}