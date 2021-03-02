package com.dabeeo.indoormap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dabeeo.component.dialog.DabeeoDialog
import com.dabeeo.component.dialog.DabeeoProgress
import com.dabeeo.component.utils.intents.IntentUtils
import java.util.HashMap

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    protected lateinit var binding: B
    private lateinit var intentUtils: IntentUtils
    private var progressBar: DabeeoProgress? = null

    abstract fun setupViews()

    @LayoutRes
    abstract fun getLayoutResourceId() : Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        activity?.run {
            intentUtils = IntentUtils(this)
            progressBar = DabeeoProgress(this)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResourceId(), container, false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        setupViews()

        return binding.root
    }

    /**
     * @param activity 이동한 Activity
     * @param intentEffect 이동간 사용할 효과
     */
    protected fun intent(activity: Class<*>, intentEffect: IntentUtils.EFFECT) {
        val intent = Intent(getActivity(), activity)
        startActivity(intent)
        intentUtils.translate(intentEffect)
    }

    /**
     * @param activity 이동한 Activity
     * @param maps 전달한 변수
     * @param intentEffect 이동간 사용할 효과
     */
    protected fun intent(activity: Class<*>, maps: HashMap<String, String>, intentEffect: IntentUtils.EFFECT) {
        val intent = Intent(getActivity(), activity)
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
        activity?.run {
            finish()
        }
    }

    /**
     * alert Message
     *
     * @param 노출 문구
     *
     */
    protected fun showAlert(message: String) {
        context?.run {
            val dialog = DabeeoDialog(this)
            dialog.apply {
                this.setMessage(message)
                show()
            }
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
