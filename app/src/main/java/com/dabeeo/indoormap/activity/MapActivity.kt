package com.dabeeo.indoormap.activity

import android.os.Bundle
import android.util.Log
import com.dabeeo.indoormap.BaseActivity
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.common.ProjectString
import com.dabeeo.indoormap.databinding.ActivityMapBinding
import com.dabeeo.indoormap.fragment.MapFragment
import com.dabeeo.indoormap.utils.FragmentUtils

class MapActivity : BaseActivity<ActivityMapBinding>() {

    private lateinit var fragment: MapFragment

    override fun getLayoutId(): Int {
        return ProjectLayout.activity_map
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        fragment = MapFragment.newInstance()
        fragment?.let {
            FragmentUtils.replaceFragmentNoBackstack(
                supportFragmentManager,
                it,
                binding.content.id
            )
        }
    }
}

