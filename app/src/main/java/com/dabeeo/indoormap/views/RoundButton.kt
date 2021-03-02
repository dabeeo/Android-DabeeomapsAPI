package com.dabeeo.indoormap.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.dabeeo.indoormap.R
import com.dabeeo.indoormap.databinding.ViewRoundButtonBinding

class RoundButton : LinearLayout {

    lateinit var binding: ViewRoundButtonBinding

    enum class ROUND {
        ALL,
        TOP,
        BOTTOM,
        NONE
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_round_button,
            this,
            false
        )
        binding.buttonLayout.elevation = context.resources.getDimension(R.dimen.elevation_zoom_round_button)
        addView(binding.root)
    }

    fun setImage(resource: Int) {
        binding.imageView.visibility = View.VISIBLE
        binding.imageView.setImageResource(resource)
    }

    fun setText(text: String) {
        binding.textView.visibility = View.VISIBLE
        binding.textView.text = text
    }

    fun setNone() {
        binding.buttonLayout.setBackgroundColor(Color.TRANSPARENT)
    }

    fun setTextColor(color: Int) {
        binding.textView.setTextColor(color)
    }

    fun getTextView() : TextView {
        return binding.textView
    }

    fun setRound(round: ROUND) {
        when (round) {
            ROUND.ALL -> {
                binding.buttonLayout.setBackgroundResource(R.drawable.background_round_button)
            }
            ROUND.TOP -> {
                binding.buttonLayout.setBackgroundResource(R.drawable.background_round_button_top)
            }
            ROUND.BOTTOM -> {
                binding.buttonLayout.setBackgroundResource(R.drawable.background_round_button_bottom)
            }
            ROUND.NONE -> {
                binding.buttonLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.taupe))
            }
        }
    }

}