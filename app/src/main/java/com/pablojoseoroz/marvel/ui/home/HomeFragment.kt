package com.pablojoseoroz.marvel.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pablojoseoroz.marvel.R
import com.pablojoseoroz.marvel.bus.FavoriteEvent
import com.pablojoseoroz.marvel.ui.detail.FavoriteViewModel
import com.pablojoseoroz.marvelapi.MarvelViewModel
import com.pablojoseoroz.marvelapi.dto.Character
import com.pablojoseoroz.marvelapi.dto.CharacterDataContainer
import com.pablojoseoroz.marvelapi.responses.Result
import com.pablojoseoroz.uibase.LoadingListFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.util.*

/**
 * Home fragment encargado de listar los personajes Marvel
 * Permite modificar el modo en que se listan en forma de listado vertical o grid de 3 colummnas
 * Controla la paginación de forma manual
 * Utiliza EventBus para escuchar si hay nuevos favoritos y actualizar el listado asociado
 *
 * @constructor Create empty Home fragment
 */
class HomeFragment : LoadingListFragment<CharactersAdapter>() {

    /**
     * ViewModel principal de Marvel
     */
    private val mViewModel: MarvelViewModel by activityViewModels()

    /**
     * ViewModel encargado de los favoritos
     */
    private val mFavoriteViewModel: FavoriteViewModel by activityViewModels()

    /**
     * MenuItem del modo de la lista
     */
    private lateinit var mModeMenuItem: MenuItem

    /**
     * Listener encargado de controlar si hay q cargar más páginas por arriba/abajo
     */
    protected var mPagerListener: PagerOnScrollListener? = null

    /**
     * [Paginable] donde guardar el estado en el q se encuentra la llamada, items, páginas, etc
     */
    protected var mPaginable: CharacterDataContainer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.home, menu)
        mModeMenuItem = menu.findItem(R.id.mode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mode -> {
                getDefinedAdapter()?.let {
                    var itemPos = -1
                    if (mRecyclerView!!.layoutManager is GridLayoutManager) {
                        itemPos =
                            (mRecyclerView!!.layoutManager as GridLayoutManager?)!!.findFirstVisibleItemPosition()
                    } else if (mRecyclerView!!.layoutManager is LinearLayoutManager) {
                        itemPos =
                            (mRecyclerView!!.layoutManager as LinearLayoutManager?)!!.findFirstCompletelyVisibleItemPosition()
                    }

                    mRecyclerView!!.layoutManager =
                        if (it.mIsModeGrid)
                            LinearLayoutManager(requireContext())
                        else
                            GridLayoutManager(requireContext(), 3)

                    it.mIsModeGrid = !it.mIsModeGrid
                    mRecyclerView?.adapter = it
                    it.notifyDataSetChanged()
                    if (itemPos != -1) {
                        if (mRecyclerView!!.layoutManager is GridLayoutManager) {
                            (mRecyclerView!!.layoutManager as GridLayoutManager?)!!.scrollToPosition(
                                itemPos
                            )
                        } else if (mRecyclerView!!.layoutManager is LinearLayoutManager) {
                            (mRecyclerView!!.layoutManager as LinearLayoutManager?)!!.scrollToPosition(
                                itemPos
                            )
                        }
                    }
                    item.setIcon(if (it.mIsModeGrid) R.drawable.ic_baseline_view_list_24 else R.drawable.ic_baseline_grid_view_24)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun hasItemDecoration(): Boolean {
        return false
    }

    override fun hasFixedSize(): Boolean {
        return true
    }

    override fun getAdapter(): CharactersAdapter {
        return CharactersAdapter({
            navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    it.id
                )
            )
        })
    }

    override fun shouldRegisterBus(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public fun onFavoriteEvent(event: FavoriteEvent) {
        mFavoriteViewModel
            .getFavorites()
            .observe(viewLifecycleOwner, {
                getDefinedAdapter()?.updateFavorites(it)
            })
    }

    override fun setupObservers(view: View) {
        super.setupObservers(view)
        mFavoriteViewModel
            .getFavorites()
            .observe(viewLifecycleOwner, {
                getDefinedAdapter()?.setFavorites(it)
            })
        mViewModel
            .fetchCharacters()
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Result.Loading -> {
                        mostrarCargando()
                    }
                    is Result.Success -> {
                        mostrarContenido()
                        getDefinedAdapter()?.update(it.data?.data?.results as ArrayList<Character>)
                        updatePager(it.data?.data)
                    }
                    is Result.Error -> {
                        mostrarPlaceholder()
                    }
                }
            })
    }

    /**
     * LLamada a realizar después de la primera llamada para actualizar el paginable
     * inicial
     *
     * @param paginable
     * @param omitScrollToTop si es true no scrolleara al primer item en caso de no estarlo
     */
    protected fun updatePager(paginable: CharacterDataContainer?, vararg omitScrollToTop: Boolean) {
        try {
            mRecyclerView?.let {
                if (omitScrollToTop == null || omitScrollToTop.size < 1 || !omitScrollToTop[0]) // scrolleo hasta arriba la lista si no lo estaba
                    if (it.computeVerticalScrollOffset() > 0) it.scrollToPosition(
                        0
                    )
                mPaginable = paginable
                if (mPagerListener != null) it.removeOnScrollListener(mPagerListener!!)
                mPagerListener = PagerOnScrollListener(
                    mPaginable!!.offset,
                    mPaginable!!.limit,
                    mPaginable!!.total
                )
                it.addOnScrollListener(mPagerListener!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun nextPage() {
        Timber.d("")
//        mViewModel.parameters.limit = mViewModel.parameters.limit?.plus(mViewModel.defaultOffset)
        mViewModel.parameters.offset = mViewModel.parameters.offset?.plus(mViewModel.defaultOffset)
        mViewModel
            .fetchCharacters()
            .observe(viewLifecycleOwner, {
                when (it) {
                    is Result.Success -> {
                        updateDown(it.data?.data!!)
                    }
                }
            })
    }

    /**
     * Actualiza la lista para que se vean insertados los objetos al final
     *
     * @param data último paginable descargado
     */
    protected fun updateDown(data: CharacterDataContainer) {
        Timber.d("")
        try {

            // añade los item al final de la lista: indicar pos inicial dnd se meten y cuantos
            getDefinedAdapter()?.insert(data.results as ArrayList<Character>)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                // actualizo la última página cargada
                mPagerListener!!.finishedWork()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        try {
            Timber.i("")
            if (mPagerListener != null) {
                mRecyclerView!!.removeOnScrollListener(mPagerListener!!)
                mPagerListener = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()
    }

    /**
     *
     * [RecyclerView.OnScrollListener] customizado para controlar la paginación
     */
    inner class PagerOnScrollListener(
        /**
         * última página cargada
         */
        var lastPage: Int, limit: Int, count: Int
    ) :
        RecyclerView.OnScrollListener() {
        /**
         * primera página a cargar
         */
        var firstPage: Int

        /**
         * total de páginas
         */
        private val totalPage: Int

        /**
         * flag para saber si está trabajando y no hacer caso a nuevos eventos de scroll
         */
        private var working = false

        /**
         * umbral para lanzar antes la llamada de la siguiete página
         */
        private val treshold: Int

        /**
         * númeoro de items a cargar por página
         */
        private val limite: Int
        override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
            try {

                // si está trabajando salimos
                if (working) return

                // a trabajar
                working = true

                // obtención de primer y útimo items visibles dependiendo el layoutmanager
                val recyclerViewLayoutManager = rv.layoutManager
                var firstVisibleItemPosition = -1
                var lastVisibleItemPosition = -1
                if (recyclerViewLayoutManager is GridLayoutManager) {
                    val layoutManager = recyclerViewLayoutManager
                    firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                } else if (recyclerViewLayoutManager is LinearLayoutManager) {
                    val layoutManager = recyclerViewLayoutManager
                    firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                }

                // si no se han conseguido los items visibles salimos
                if (firstVisibleItemPosition == -1 && lastVisibleItemPosition == -1) {
                    working = false
                    return
                }

                // si último item visible + el extra es mayor q el total, recargamos siguiente página
                if (lastVisibleItemPosition + treshold >= rv.adapter!!.itemCount && lastPage < totalPage) {
                    nextPage()
                    return
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // en caso de no lanzarse "nextPage()" no estamos trabajando
            working = false
        }

        fun finishedWork() {
            Timber.d("")
            working = false
        }

        init {
            firstPage = lastPage
            limite = limit
            totalPage = count / limite + if (count % limite > 0) 1 else 0
            treshold = limite / 3
        }
    }

}