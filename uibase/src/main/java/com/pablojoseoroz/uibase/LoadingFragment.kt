package com.pablojoseoroz.uibase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ViewFlipper
import timber.log.Timber

/**
 *
 * [BaseFragment] generico para mostrar un loading mientras se carga algo
 * Se veré un loading central hasta q llamemos [.mostrarContenido]<br></br>
 * El [androidx.fragment.app.Fragment] que herede de esta clase debe implementar el método
 * [.getContentLayout] para indicar la vista a cargar al llamar a [.mostrarContenido]
 */
abstract class LoadingFragment : BaseFragment(R.layout.fragment_loading), LoadingInterface {

    protected lateinit var mViewSwitcher: ViewFlipper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loading, container, false)
        mViewSwitcher = view.findViewById(R.id.vs)

        // inflar layout principal
        inflater.inflate(getContentLayout(), mViewSwitcher)

        // inflar placeholder si lo hay
        if (getPlaceHolderLayout() != LoadingInterface.NO_PLACEHOLDER) inflater.inflate(
            getPlaceHolderLayout(), mViewSwitcher
        )
        return super.postOnCreateView(inflater, view)
    }

    open fun getPlaceHolderLayout(): Int {
        return LoadingInterface.NO_PLACEHOLDER
    }

    abstract fun getContentLayout(): Int

    override fun mostrarCargando() {
        try {
            if (mViewSwitcher.displayedChild == 0) return
            mViewSwitcher.displayedChild = 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun mostrarContenido() {
        try {
            Timber.i("")
            if (mViewSwitcher.displayedChild == 1) return
            mViewSwitcher.displayedChild = 1
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun mostrarPlaceholder(vararg onClickListener: View.OnClickListener?) {
        try {
            Timber.i("")
            if (getPlaceHolderLayout() == LoadingInterface.NO_PLACEHOLDER) return
            if (isMostrandoPlaceholder()) return
            mViewSwitcher.displayedChild = 2
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun isMostrandoCargando(): Boolean = mViewSwitcher.displayedChild == 0
    protected fun isMostrandoContenido(): Boolean = mViewSwitcher.displayedChild == 1
    protected fun isMostrandoPlaceholder(): Boolean = mViewSwitcher.displayedChild == 2
}