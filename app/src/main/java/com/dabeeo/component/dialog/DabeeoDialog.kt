package com.dabeeo.component.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import com.dabeeo.component.utils.common.ConvertUtils
import com.dabeeo.indoormap.R


class DabeeoDialog(var context: Context) {

    private lateinit var dialog: Dialog
    private var builder: Builder

    private lateinit var messageTextView: TextView
    private lateinit var positiveButton: TextView
    private lateinit var negativeButton: TextView

    private var positiveClickListener: View.OnClickListener? = null
    private var negativeClickListener: View.OnClickListener? = null

    enum class DialogType {
        ALERT,
        CONFIRM
    }

    init {
        builder = Builder()
    }

    fun setMessage(message: String): DabeeoDialog {
        messageTextView.text = message
        return this
    }

    fun setPositive(buttonName: String, listener: View.OnClickListener): DabeeoDialog {
        positiveButton.text = buttonName
        positiveClickListener = listener
        return this
    }

    fun setPositive(buttonName: String): DabeeoDialog {
        positiveButton.text = buttonName
        return this
    }

    fun setNegative(buttonName: String, listener: View.OnClickListener): DabeeoDialog {
        negativeButton.text = buttonName
        negativeClickListener = listener
        return this
    }

    fun setNegative(buttonName: String): DabeeoDialog {
        negativeButton.text = buttonName
        return this
    }


    fun setCancelable(enable: Boolean): DabeeoDialog {
        dialog.setCancelable(enable)
        return this
    }

    fun setCanceledOnTouchOutside(enable: Boolean): DabeeoDialog {
        dialog.setCanceledOnTouchOutside(enable)
        return this
    }


    fun setDialogType(dialogType: DialogType): DabeeoDialog {
        when (dialogType) {
            DialogType.ALERT -> {
                negativeButton.visibility = View.GONE
            }
            DialogType.CONFIRM -> {
                negativeButton.visibility = View.VISIBLE
            }
        }
        return this
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

    inner class Builder {
        init {
            dialog = Dialog(context)
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.view_dialog)
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            val metrics: DisplayMetrics = context.resources.displayMetrics
            val width = metrics.widthPixels
            val height = metrics.heightPixels

            messageTextView = dialog.findViewById(R.id.messageTextView)
            positiveButton = dialog.findViewById(R.id.positiveButton)
            negativeButton = dialog.findViewById(R.id.negativeButton)

            val attrs: WindowManager.LayoutParams = dialog.window!!.attributes
            attrs.dimAmount = 0.6f
            attrs.flags =
                WindowManager.LayoutParams.FLAG_DIM_BEHIND or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            dialog.window!!.setAttributes(attrs)
            dialog.window!!.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        or View.SYSTEM_UI_FLAG_IMMERSIVE
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val params: WindowManager.LayoutParams = dialog.window!!.attributes
            params.width = (width - ConvertUtils.dpToPx(context, 46.0f)).toInt()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT

            dialog.window!!.attributes = params
            dialog.window!!.setBackgroundDrawableResource(R.drawable.background_transparent)

            positiveButton.setOnClickListener {
                if(positiveClickListener != null) {
                    positiveClickListener!!.onClick(it)
                }
                dismiss()
            }

            negativeButton.setOnClickListener {
                if(negativeClickListener != null) {
                    negativeClickListener!!.onClick(it)
                }
                dismiss()
            }

            positiveButton.setOnTouchListener(View.OnTouchListener { v: View?, event: MotionEvent ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        positiveButton.alpha = 0.5f
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        positiveButton.alpha = 1.0f
                    }
                }
                false
            })

            negativeButton.setOnTouchListener(View.OnTouchListener { v: View?, event: MotionEvent ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        negativeButton.alpha = 0.5f
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        negativeButton.alpha = 1.0f
                    }
                }
                false
            })


        }
    }

}