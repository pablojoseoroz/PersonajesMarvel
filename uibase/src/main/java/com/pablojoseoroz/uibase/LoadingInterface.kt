package com.pablojoseoroz.uibase

import android.view.View

interface LoadingInterface {
    /**
     * Hace visible al primer hijo del switcher donde va la vista de carga
     */
    fun mostrarCargando()

    /**
     * Hace visible el segundo hijo del switcher donde va el layout principal
     */
    fun mostrarContenido()

    /**
     * Hace visible el tercer hijo del switcher donde va el layout en caso de q no se haya encontrado contenido
     */
    fun mostrarPlaceholder(vararg onClickListener: View.OnClickListener?)

    companion object {
        const val NO_PLACEHOLDER = 0
    }

}