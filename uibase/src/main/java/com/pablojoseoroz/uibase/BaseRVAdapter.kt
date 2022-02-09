package com.pablojoseoroz.uibase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * [RecyclerView.Adapter] con implementaciones genéricas
 * @param <T>
 * @param <Item>
</Item></T> */
abstract class BaseRVAdapter<T, Item : BaseVH<T>> : RecyclerView.Adapter<BaseVH<T>>,
    BusInterface {

    /**
     * Setea los items q se mostrarán
     *
     * @param list
     */
    var items: ArrayList<T>? = null
    protected lateinit var recyclerView: RecyclerView
//    protected lateinit var mRequestManager: RequestManager

    constructor() {}

    constructor(items: ArrayList<T>?) {
        this.items = items
    }

    protected fun inflateItem(viewGroup: ViewGroup, layout: Int): View {
        return getInflater(viewGroup.context).inflate(layout, viewGroup, false)
    }

    protected fun inflateItem(context: Context, layout: Int): View {
        return getInflater(context).inflate(layout, null, false)
    }

    private fun getInflater(context: Context): LayoutInflater {
        return LayoutInflater.from(context)
    }

    /**
     * @return cantidad de [.items]
     */
    fun getItemsSize(): Int {
        return items?.size ?: 0
    }

    fun getItem(position: Int): T {
        return items!![position]
    }

    /**
     * [.setItems] y actualiza el [RecyclerView.Adapter]
     *
     * @param items
     */
    fun update(items: ArrayList<T>?) {
        this.items = items
        update()
    }

    fun insert(novedades: ArrayList<T>) {
        items!!.addAll(novedades)
        notifyItemRangeInserted(itemCount, novedades.size)
    }

    fun insertFirst(novedades: ArrayList<T>) {
        items!!.addAll(0, novedades)
        notifyItemRangeInserted(0, novedades.size)
    }

    fun update() {
        notifyDataSetChanged()
    }

    fun add(item: T) {
        if (items == null) items = ArrayList()
        items!!.add(item)
    }

    fun add(index: Int, item: T) {
        if (items == null) items = ArrayList()
        items!!.add(index, item)
    }

    fun add(result: ArrayList<T>?) {
        if (items == null) items = ArrayList()
        items!!.addAll(result!!)
    }

    fun add(index: Int, result: ArrayList<T>?) {
        if (items == null) items = ArrayList()
        items!!.addAll(index, result!!)
    }

    fun indexOf(item: T): Int {
        return items!!.indexOf(item)
    }

    protected operator fun set(index: Int, item: T) {
        items!![index] = item
    }

    /**
     * Vacía la lista de items en caso de existir
     */
    fun clear() {
        if (items != null && items!!.size > 0) items!!.clear()
    }

    fun remove(item: T) {
        items!!.remove(item)
    }

    override fun getItemCount(): Int {
        return getItemsSize()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseVH<T> {
        return crearItem(viewGroup, i)
    }

    protected abstract fun crearItem(viewGroup: ViewGroup, i: Int): Item

    protected fun onBindItem(holder: Item, position: Int, payloads: MutableList<Any>) {
        holder.bind(position, payloads)
        holder.bind(getItem(position), payloads)
    }

    override fun onBindViewHolder(holder: BaseVH<T>, i: Int) {
        try {
            onBindItem(holder as Item, i)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBindViewHolder(holder: BaseVH<T>, position: Int, payloads: MutableList<Any>) {
        try {
            if (payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads)
                return
            }
            onBindItem(holder as Item, position, payloads)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Preparar los items contenedores uno a uno
     *
     * @param position Automaticamente determina si es i - 1 o i según tenga o no cabecera
     */
    fun onBindItem(holder: Item, position: Int) {
        holder!!.bind(position)
        holder.bind(getItem(position))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
//        mRequestManager = GlideApp.with(recyclerView.context)
        if (shouldRegisterBus() && !isBusRegistered) registerBus()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (shouldRegisterBus() && isBusRegistered) unregisterBus()
    }

    override fun onViewRecycled(holder: BaseVH<T>) {
        super.onViewRecycled(holder)
        try {
            holder.limpiar()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFailedToRecycleView(holder: BaseVH<T>): Boolean {
        return super.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: BaseVH<T>) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: BaseVH<T>) {
        super.onViewDetachedFromWindow(holder)
    }

    protected fun areItems(): Boolean {
        return items != null && items!!.size > 0
    }

    protected fun areNoItems(): Boolean {
        return !areItems()
    }

    /**
     * Borra todos los items que tuviese la lista
     */
    protected fun clearItems() {
        if (items != null && items!!.size > 0) items!!.clear()
    }

    /**
     *
     */
    fun clean() {
        try {
            if (itemCount == 0) return
            //            scrollToTop();
            val itemCount = itemCount
            clearItems()
            notifyItemRangeRemoved(0, itemCount)
            //            notifyDataSetChanged();
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun scrollToTop() {
        try {
            //            mRecyclerView.scrollTo(0,0);
            recyclerView.scrollToPosition(0)
            //            mRecyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        mRecyclerView.scrollToPosition(0);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 300);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addItem(item: T) {
        if (contains(item)) return
        add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun removeItem(item: T) {
        if (notContains(item)) return
        val index = indexOf(item)
        remove(item)
        notifyItemRemoved(index)
    }

    fun updateItem(item: T) {
        if (notContains(item)) return
        val index = indexOf(item)
        set(index, item)
        notifyItemChanged(index, "")
    }

    fun updateItem(item: T, payload: Any?) {
        if (notContains(item)) return
        val index = indexOf(item)
        set(index, item)
        notifyItemChanged(index, payload)
    }

    operator fun contains(item: T): Boolean {
        return items != null && items!!.contains(item)
    }

    private fun notContains(item: T): Boolean {
        return !contains(item)
    }

    fun destroy() {
        recyclerView.adapter = null
    }

    override val isBusRegistered: Boolean
        get() = EventBus.getDefault().isRegistered(this)

    override fun registerBus() {
        EventBus.getDefault().register(this)
    }

    override fun unregisterBus() {
        EventBus.getDefault().unregister(this)
    }
}