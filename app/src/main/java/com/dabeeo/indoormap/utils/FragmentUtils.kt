package com.dabeeo.indoormap.utils

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

object FragmentUtils {

    fun replaceFragment(fm: FragmentManager?, newFragment: Fragment?, @IdRes containerId: Int) {
        if (fm != null && newFragment != null) {
            fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(containerId, newFragment, newFragment.javaClass.simpleName)
                .addToBackStack(newFragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        }
    }

    fun replaceFragmentNoBackstack(
        fm: FragmentManager?,
        newFragment: Fragment?, @IdRes containerId: Int
    ) {
        if (fm != null && newFragment != null) {
            fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(containerId, newFragment, newFragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        }
    }

    fun addFragment(
        fm: FragmentManager?,
        curFragment: Fragment,
        newFragment: Fragment?, @IdRes containerId: Int
    ) {
        if (fm != null && newFragment != null) {
            fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .hide(curFragment)
                .add(containerId, newFragment, newFragment.javaClass.simpleName)
                .addToBackStack(newFragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        }
    }

    fun addFragmentNoBackstack(
        fm: FragmentManager?,
        curFragment: Fragment,
        newFragment: Fragment?, @IdRes containerId: Int
    ) {
        if (fm != null && newFragment != null) {
            fm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .hide(curFragment)
                .add(containerId, newFragment, newFragment.javaClass.simpleName)
                .commitAllowingStateLoss()
        }
    }
}// nothing
