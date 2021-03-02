package com.dabeeo.component.utils.common

import android.content.Context
import android.util.DisplayMetrics

class ConvertUtils {

    companion object {
        fun dpToPx(context: Context, dp: Float): Float {
            val resources = context.resources
            val metrics = resources.displayMetrics
            return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }

}