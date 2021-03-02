package com.dabeeo.indoormap.manager

import android.util.Log
import com.dabeeo.imsdk.model.network.MapRow


class MapManager{

//    companion object {
//        @Volatile private var instance: MapManager? = null
//
//        @JvmName("getInstance1")
//        @JvmStatic fun getInstance(): MapManager =
//                instance ?: synchronized(this) {
//                    instance ?: MapManager().also {
//                        instance = it
//                    }
//                }
//    }

    private var mapRowList = ArrayList<MapRow>()

    fun setMapRowList(list: ArrayList<MapRow>) {
        mapRowList = list.clone() as ArrayList<MapRow>
    }

    fun getMapList(keyword: String): ArrayList<MapRow> {
        val mapRowList = ArrayList<MapRow>()
        val k = keyword.toLowerCase().trim()
        for (mapRow in this.mapRowList) {
            val name: String = mapRow.name.toLowerCase().trim()
            if (name.contains(k)) {
                mapRowList.add(mapRow)
            }
        }
        return mapRowList
    }

}