package com.dabeeo.indoormap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dabeeo.component.dialog.DabeeoDialog
import com.dabeeo.component.dialog.DabeeoProgress
import com.dabeeo.component.utils.intents.IntentUtils
import java.util.HashMap

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: VB
    private var progressBar: DabeeoProgress? = null
    private lateinit var intentUtils: IntentUtils

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this

        progressBar = DabeeoProgress(this)
        intentUtils = IntentUtils(this)
    }

    /**
     * @param activity 이동한 Activity
     * @param intentEffect 이동간 사용할 효과
     */
    protected fun intent(activity: Class<*>, intentEffect: IntentUtils.EFFECT) {
        val intent = Intent(this, activity)
        startActivity(intent)
        intentUtils.translate(intentEffect)
    }

    /**
     * @param activity 이동한 Activity
     * @param maps 전달한 변수
     * @param intentEffect 이동간 사용할 효과
     */
    protected fun intent(activity: Class<*>, maps: HashMap<String, String>, intentEffect: IntentUtils.EFFECT) {
        val intent = Intent(this, activity)
        val keys: Iterator<String> = maps.keys.iterator()
        while (keys.hasNext()) {
            val key = keys.next()
            intent.putExtra(key, maps.get(key))
        }
        startActivity(intent)
        intentUtils.translate(intentEffect)
    }

    /**
     * @param intentEffect 액티비티 종료시 사용할 효과
     */
    protected fun back(intentEffect: IntentUtils.EFFECT) {
        intentUtils.back(intentEffect)
        finish()
    }

    /**
     *
     */
    protected fun showAlert(message: String) {
        val dialog = DabeeoDialog(this)
        dialog.apply {
            this.setMessage(message)
            show()
        }
    }

    /**
     * show Progress
     */
    protected fun showProgress() {
        progressBar?.run {
            if(!this.isShowing) {
                this.show()
            }
        }
    }

    /**
     * hide Progress
     */
    protected fun hideProgress() {
        progressBar?.run {
            if(this.isShowing) {
                this.hide()
            }
        }
    }

}