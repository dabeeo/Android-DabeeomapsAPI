package com.dabeeo.component.utils.intents

import android.app.Activity
import com.dabeeo.indoormap.R

class IntentUtils(private val activity: Activity) {

    enum class EFFECT {
        NONE,
        SLIDE_LEFT_RIGHT,
        SLIDE_UP_DOWN,
        FADE_IN_OUT
    }

    fun translate(intentStyle: EFFECT) {
        when {
            intentStyle === EFFECT.NONE -> {
                activity.overridePendingTransition(0, 0)
            }
            intentStyle === EFFECT.SLIDE_LEFT_RIGHT -> {
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.fix)
            }
            intentStyle === EFFECT.SLIDE_UP_DOWN -> {
                activity.overridePendingTransition(R.anim.slide_in_up, R.anim.fix)
            }
            intentStyle === EFFECT.FADE_IN_OUT -> {
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    fun back(intentStyle: EFFECT) {
        when {
            intentStyle === EFFECT.NONE -> {
                activity.overridePendingTransition(0, 0)
            }
            intentStyle === EFFECT.SLIDE_LEFT_RIGHT -> {
                activity.overridePendingTransition(R.anim.fix, R.anim.slide_out_right)
            }
            intentStyle === EFFECT.SLIDE_UP_DOWN -> {
                activity.overridePendingTransition(R.anim.fix, R.anim.slide_out_down)
            }
            intentStyle === EFFECT.FADE_IN_OUT -> {
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

}
