package com.pablojoseoroz.uibase

import android.view.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import timber.log.Timber

/**
 *
 * [LoadingFragment] customizado que contiene un [RecyclerView]
 *
 * @param <Adapter> [RecyclerView.Adapter]
 * @see LoadingFragment
*/
abstract class LoadingListFragment<Adapter : RecyclerView.Adapter<*>> : LoadingFragment() {

    protected var mRecyclerView: RecyclerView? = null
    private var mEmptyObserver: EmptyRecyclerViewObserver? = null

    override fun getContentLayout(): Int {
        return R.layout.fragment_recycler_view
    }

    override fun setupViews(view: View) {
        super.setupViews(view)
        setupRecyclerView(view)
    }

    override fun getPlaceHolderLayout(): Int {
        return R.layout.placeholder
    }

    /**
     * Configura el [RecyclerView] q mostrará
     */
    private fun setupRecyclerView(view: View) {
        try {
            mRecyclerView = view.findViewById(getRecyclerViewId())
            mRecyclerView?.let {
                if (it.adapter != null) return
                it.setPadding(
                    getPaddingStart(),
                    getPaddingTop(),
                    getPaddingEnd(),
                    getPaddingBottom()
                )
                if (hasItemDecoration()) {
                    val itemDecoration = getItemDecoration()
                    it.addItemDecoration(itemDecoration)
                }
                it.setHasFixedSize(hasFixedSize())
                it.layoutManager = getLayoutManager()
                enableAnimator()
                val adapter = getAdapter()
                it.adapter = adapter

                // listener para esconder el teclado al hacer scroll
                if (hideKeyboardWhenScroll()) it.addOnScrollListener(object :
                    RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        KeyboardUtils.hideKeyBoard(this@LoadingListFragment)
                    }
                })

                // observador de contenido para mostrar el contenido/placeholder autoamticamente
                adapter.registerAdapterDataObserver(object : EmptyRecyclerViewObserver(adapter) {
                    override fun isAdapterEmpty(empty: Boolean) {
                        if (empty) mostrarPlaceholder() else mostrarContenido()
                    }
                }.also { mEmptyObserver = it })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (mRecyclerView != null) setupRecyclerView()
        }
    }

    protected fun setupRecyclerView() {
        Timber.d("")
    }

    protected open fun hasItemDecoration(): Boolean {
        return true
    }

    protected fun onMiddleItemtSelected(middleElement: Int) {
        Timber.i("")
    }

    public open fun getDefinedAdapter(): Adapter? {
        return if (mRecyclerView == null || mRecyclerView!!.adapter == null) null else mRecyclerView!!.adapter as Adapter
    }

    protected fun getRecyclerViewId(): Int {
        return R.id.rv
    }

    protected open fun getLayoutManager(): RecyclerView.LayoutManager? {
        return mRecyclerView?.let {
            if (it.layoutManager != null) it.layoutManager else LinearLayoutManager(context)
        }
    }

    protected abstract fun getAdapter(): Adapter

    protected open fun getItemDecoration(): ItemDecoration {
        return DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    }

    protected fun hideKeyboardWhenScroll(): Boolean {
        return true
    }

    protected open fun getPaddingStart(): Int {
        return 0
    }

    protected open fun getPaddingEnd(): Int {
        return 0
    }

    protected open fun getPaddingTop(): Int {
        return 0
    }

    protected open fun getPaddingBottom(): Int {
        return 0
    }

    protected open fun hasFixedSize(): Boolean {
        return false
    }

    protected fun clearOnScrollListeners() {
        if (mRecyclerView != null) {
            mRecyclerView!!.clearOnScrollListeners()
            if (hideKeyboardWhenScroll()) mRecyclerView!!.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (KeyboardUtils.isShown(context)) KeyboardUtils.hideKeyBoard(this@LoadingListFragment)
                }
            })
        }
    }

    override fun onDestroy() {
        Timber.i("")
        val adapter = getDefinedAdapter()
        if (adapter != null) {
            if (mEmptyObserver != null) {
                adapter.unregisterAdapterDataObserver(mEmptyObserver!!)
                mEmptyObserver = null
            }
            if (mRecyclerView != null) {
                mRecyclerView!!.clearOnScrollListeners()
                mRecyclerView!!.adapter = null
                mRecyclerView = null
            }
        }
        super.onDestroy()
    }

    protected fun disableAnimator() {
        if (mRecyclerView == null) return
        if (mRecyclerView!!.itemAnimator == null) return
        mRecyclerView!!.itemAnimator = null
    }

    protected fun enableAnimator() {
        if (mRecyclerView == null) return
        if (mRecyclerView!!.itemAnimator != null) return
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
    }
}