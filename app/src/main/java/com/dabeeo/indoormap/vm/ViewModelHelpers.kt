package com.dabeeo.indoormap.vm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dabeeo.indoormap.BaseViewModel
import kotlin.reflect.KProperty

class ViewModelHelpers<out T : BaseViewModel>(private val targetClass: Class<T>) {
    private var value: T? = null

    operator fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        if (value == null) {
            value = ViewModelProvider(thisRef).get(targetClass)
        }

        return value!!
    }

    operator fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (value == null) {
            value = ViewModelProvider(thisRef).get(targetClass)
        }

        return value!!
    }
}

inline fun <reified T : BaseViewModel> createViewModel() = ViewModelHelpers(T::class.java)

fun <T : ViewModel, A> singleArgumentViewModelFactory(constructor: (A) -> T):
            (A) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg) as V
            }
        }
    }
}