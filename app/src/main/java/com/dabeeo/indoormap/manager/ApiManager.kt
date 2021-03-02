package com.dabeeo.indoormap.manager

import com.dabeeo.imsdk.model.network.MapRow
import com.dabeeo.imsdk.model.network.ResOAuth
import com.dabeeo.imsdk.network.IMApi


object ApiManager {

    interface ApiCallBack<T> {
        fun onSuccess(callback: T)
        fun onError(message: String)
    }

    /**
     * Login
     *
     * @param id IMStudio ID
     * @param password IMStudio PW
     * @param callback Callback
     *
     */
    fun requestLogin(id: String, password: String, callback: ApiCallBack<ResOAuth>) {
        IMApi.login(id, password, object : IMApi.ApiCallback<ResOAuth> {
            override fun onSuccess(t: ResOAuth) {
                callback.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                callback.onError(e.toString())
            }
        })
    }

    /**
     * MapList
     *
     * @param callback Callback
     *
     */
    fun requestMapList(callback: ApiCallBack<List<MapRow>?>) {
        IMApi.getMapList(object : IMApi.ApiCallback<List<MapRow>?> {
            override fun onSuccess(t: List<MapRow>?) {
                callback.onSuccess(t)
            }

            override fun onError(e: Throwable) {
                callback.onError(e.toString())
            }
        })
    }


}