package com.pablojoseoroz.uibase

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver

/**
 *
 * [RecyclerView.AdapterDataObserver] para avisarnos si el adapter se queda vacio o rellenado
 */
abstract class EmptyRecyclerViewObserver(private val mAdapter: RecyclerView.Adapter<*>) :
    AdapterDataObserver() {
    override fun onChanged() {
        super.onChanged()
        checkEmpty()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        checkEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        checkEmpty()
    }

    private fun checkEmpty() {
        isAdapterEmpty(mAdapter.itemCount == 0)
    }

    abstract fun isAdapterEmpty(empty: Boolean)
}