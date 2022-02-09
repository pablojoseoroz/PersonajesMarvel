package com.pablojoseoroz.uibase

import android.view.*
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import timber.log.Timber

abstract class ListFragment<Adapter : RecyclerView.Adapter<*>?> : BaseFragment(R.layout.fragment_recycler_view) {

    protected var mRecyclerView: RecyclerView? = null

//    override fun getLayout(): Int {
//        return R.layout.fragment_recycler_view
//    }

    fun addOnScrollListener(onScrollListener: RecyclerView.OnScrollListener?) {
        if (mRecyclerView == null) return
        mRecyclerView!!.addOnScrollListener(onScrollListener!!)
    }

    override fun setupViews(view: View) {
        super.setupViews(view)
        mRecyclerView = view.findViewById(getRecyclerViewId())
        setupRecyclerView(view)
    }

    /**
     * Configura el [RecyclerView] q mostrará
     */
    private fun setupRecyclerView(view: View) {
        try {
            mRecyclerView = view.findViewById(getRecyclerViewId())
            if (mRecyclerView == null || mRecyclerView!!.adapter != null) return
            mRecyclerView!!.setPadding(
                getPaddingStart(),
                getPaddingTop(),
                getPaddingEnd(),
                getPaddingBottom()
            )
            if (hasItemDecoration()) {
                mRecyclerView!!.addItemDecoration(getItemDecoration())
            }
            mRecyclerView!!.setHasFixedSize(hasFixedSize())
            mRecyclerView!!.layoutManager = getLayoutManager()
            enableAnimator()

            mRecyclerView!!.adapter = getAdapter()

            // listener para esconder el teclado al hacer scroll
            if (hideKeyboardWhenScroll()) mRecyclerView!!.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    KeyboardUtils.hideKeyBoard(this@ListFragment)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (mRecyclerView != null) setupRecyclerView()
        }
    }

    protected fun setupRecyclerView() {
        Timber.d("")
    }

    protected fun hasItemDecoration(): Boolean {
        return true
    }

    protected fun onMiddleItemtSelected(middleElement: Int) {
        Timber.i("")
    }

    fun scrollToTop() {
        if (mRecyclerView == null) return
        if (mRecyclerView!!.childCount < 100) mRecyclerView!!.smoothScrollToPosition(0) else mRecyclerView!!.scrollToPosition(
            0
        )
    }

    fun getDefinedAdapter(): Adapter? {
        return mRecyclerView?.let {
            if (it.adapter == null) null else mRecyclerView!!.adapter as Adapter?
        }
    }

    protected fun getRecyclerViewId(): Int {
        return R.id.rv
    }

    protected fun getLayoutManager(): RecyclerView.LayoutManager? {
        return mRecyclerView?.let {
            if (it.layoutManager != null) it.layoutManager else LinearLayoutManager(context)
        }
    }

    protected abstract fun getAdapter(): Adapter

    protected fun getItemDecoration(): ItemDecoration {
        return DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    }

    protected fun hideKeyboardWhenScroll(): Boolean {
        return true
    }

    protected fun getPaddingStart(): Int {
        return 0
    }

    protected fun getPaddingEnd(): Int {
        return 0
    }

    protected fun getPaddingTop(): Int {
        return 0
    }

    protected fun getPaddingBottom(): Int {
        return 0
    }

    protected fun hasFixedSize(): Boolean {
        return false
    }

    override fun onDestroy() {
        Timber.i("")
        val adapter = getDefinedAdapter()
        if (adapter != null) {
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