package com.maxwell.projectfoundation.provider

/**
 * Created by maxwellma on 28/08/2017.
 */
internal class RouterMapper {
    private var routeTable: MutableMap<String, String> = mutableMapOf()

    private constructor()

    companion object {

        private var routerMapper: RouterMapper? = null

        @Synchronized fun getInstance(): RouterMapper {
            if (routerMapper == null) {
                routerMapper = RouterMapper()
            }
            return routerMapper!!
        }
    }

    fun getModuleInfo(moduleName: String): String? {
        return routeTable[moduleName]
    }

    fun isModuleExist(moduleName: String): Boolean {
        return routeTable.containsKey(moduleName)
    }

    fun addModuleInfo(moduleName: String, moduleInfo: String) {
        routeTable.put(moduleName, moduleInfo)
    }
}