package com.dabeeo.indoormap.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dabeeo.imsdk.model.network.MapRow
import com.dabeeo.indoormap.BaseActivity
import com.dabeeo.indoormap.R
import com.dabeeo.indoormap.databinding.ActivityMapListBinding
import com.dabeeo.indoormap.fragment.MapListFragment
import com.dabeeo.indoormap.manager.ApiManager
import com.dabeeo.indoormap.utils.FragmentUtils
import com.dabeeo.indoormap.vm.MapListViewModel

class MapListActivity : BaseActivity<ActivityMapListBinding>(){

    private val fragment = MapListFragment.newInstance()


    override fun getLayoutId(): Int {
        return R.layout.activity_map_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        requestMapList()
    }

    private fun init() {
        fragment.let {
            FragmentUtils.replaceFragmentNoBackstack(
                supportFragmentManager,
                it,
                binding.content.id
            )
        }
    }


    private fun requestMapList() {
        showProgress()
        ApiManager.requestMapList(object : ApiManager.ApiCallBack<List<MapRow>?> {
            override fun onSuccess(callback: List<MapRow>?) {
                hideProgress()
                callback?.run {
                    fragment.setMapList(this)
                }
            }

            override fun onError(message: String) {
                hideProgress()
                showAlert(message)
            }
        })
    }
}