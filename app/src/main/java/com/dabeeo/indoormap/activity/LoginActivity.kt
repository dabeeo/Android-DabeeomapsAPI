package com.dabeeo.indoormap.activity

import android.os.Bundle
import android.view.View
import com.dabeeo.component.utils.intents.IntentUtils
import com.dabeeo.indoormap.BaseActivity
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>(), View.OnClickListener{

    override fun getLayoutId(): Int {
        return ProjectLayout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        binding.mapButton.setOnClickListener(this)
        binding.vpathButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.mapButton -> {
                intent(MapActivity::class.java, IntentUtils.EFFECT.SLIDE_UP_DOWN)
            }
            binding.vpathButton -> {
                intent(VPATHActivity::class.java, IntentUtils.EFFECT.SLIDE_UP_DOWN)
            }
        }
    }
}