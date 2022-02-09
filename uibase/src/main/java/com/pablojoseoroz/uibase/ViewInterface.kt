package com.pablojoseoroz.uibase

interface ViewInterface {
    fun setupToolbar()

    /**
     * Método lanzado dsp de [android.app.Activity.setContentView] para configurar las
     * [View]s
     */
    fun setupViews()

    /**
     * Método lanzado dsp de [android.app.Activity.setContentView] donde añadir Listeners
     */
    fun setupListeners()

    /**
     * Método lanzado dsp de [android.app.Activity.setContentView] donde definir el binding
     */
    fun setupBinding()
}