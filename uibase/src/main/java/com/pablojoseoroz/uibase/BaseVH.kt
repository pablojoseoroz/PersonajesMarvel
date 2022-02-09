package com.pablojoseoroz.uibase

import android.content.Context
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView.ViewHolder] with utils methods
 */
open class BaseVH<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(position: Int) {}
    open fun bind(item: T) {}

    /**
     * Aviso al reciclarse la vista, limpiar aquí lo posible
     */
    fun limpiar() {}

    val context: Context
        get() = itemView.context

    protected fun getString(@StringRes resId: Int): String {
        try {
            return context.getString(resId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    @ColorInt
    protected fun getColor(color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        try {
            return context.getString(resId, *formatArgs)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun bind(position: Int, payloads: List<*>?) {}
    fun bind(item: T, payloads: List<*>?) {}
}