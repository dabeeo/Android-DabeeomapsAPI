package com.dabeeo.indoormap.fragment

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.dabeeo.component.utils.common.ConvertUtils
import com.dabeeo.indoormap.common.ProjectLayout
import com.dabeeo.indoormap.databinding.DialogNavigationBinding

class NavigationDialog(val info: String) : DialogFragment(), View.OnClickListener {
    var onOrigin: (() -> Unit)? = null
    var onDestination: (() -> Unit)? = null

    private lateinit var binding: DialogNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            ProjectLayout.dialog_navigation,
            container,
            false
        )

        binding.originButton.setOnClickListener(this)
        binding.destinationButton.setOnClickListener(this)
        binding.infoTextView.text = info

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        context?.run {
            val metrics: DisplayMetrics = this.resources.displayMetrics
            val width = metrics.widthPixels
            val params: WindowManager.LayoutParams = dialog!!.window!!.attributes
            params.width = (width - ConvertUtils.dpToPx(this, 46.0f)).toInt()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog!!.window!!.attributes = params
        }
    }

    override fun onClick(v: View) {
        when (v) {
            binding.originButton -> {
                onOrigin!!.invoke()
                dismiss()
            }
            binding.destinationButton -> {
                onDestination!!.invoke()
                dismiss()
            }
        }
    }
}