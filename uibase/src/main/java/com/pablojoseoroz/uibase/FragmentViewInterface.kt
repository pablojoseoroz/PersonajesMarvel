package com.pablojoseoroz.uibase

import android.view.View
import androidx.annotation.LayoutRes

interface FragmentViewInterface {

//    @LayoutRes
//    fun getLayout(): Int {
//        return 0
//    }


    /**
     * Método lanzado dsp de [android.app.Activity.setContentView] para configurar las
     * [View]s
     */
    fun setupViews(view: View)

    /**
     * Método lanzado dsp de [android.app.Activity.setContentView] donde añadir Listeners
     */
    fun setupListeners(view: View)
    fun setupObservers(view: View)
    fun removeListeners(view: View)
    fun setBackgroundColor(view: View)
    fun setDefaultWindowBackground(): Boolean {
        return false
    }

//    fun call()
    fun onRefresh()
    fun setupBinding(view: View)
    fun areSharedElements(): Boolean {
        return false
    }

    fun handleBackPressed(): Boolean {
        return false
    }

    fun onBackPressed() {}
    fun setupToolbar(view: View) {}
}