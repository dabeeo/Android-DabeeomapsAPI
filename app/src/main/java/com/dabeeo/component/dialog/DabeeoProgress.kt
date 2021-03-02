package com.dabeeo.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import com.dabeeo.indoormap.R

class DabeeoProgress(context: Context) : Dialog(context) {

    init {
        val attrs = window!!.attributes
        attrs.dimAmount = 0.6f
        attrs.flags =
            WindowManager.LayoutParams.FLAG_DIM_BEHIND or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        window!!.attributes = attrs
        window!!.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        window!!.setGravity(Gravity.TOP)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.view_progress)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
    }

//    fun showText(msg: String) {
//        binding.messageTextView.visibility = View.VISIBLE
//        binding.messageTextView.text = msg
//    }

//    fun hideText() {
//        binding.messageTextView.visibility = View.GONE
//    }

}