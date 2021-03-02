package com.dabeeo.indoormap.activity

import android.os.Bundle
import com.dabeeo.indoormap.BaseActivity
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.databinding.ActivityVpathBinding
import com.dabeeo.indoormap.fragment.VPATHFragment
import com.dabeeo.indoormap.utils.FragmentUtils

class VPATHActivity : BaseActivity<ActivityVpathBinding>() {

    private lateinit var fragment: VPATHFragment

    override fun getLayoutId(): Int {
        return ProjectLayout.activity_vpath
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        fragment = VPATHFragment.newInstance()
        fragment?.let {
            FragmentUtils.replaceFragmentNoBackstack(
                supportFragmentManager,
                it,
                binding.content.id
            )
        }
    }
}

