package com.pablojoseoroz.uibase

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.transition.TransitionInflater
import org.greenrobot.eventbus.EventBus
import timber.log.Timber


abstract class BaseFragment(val layout: Int) : Fragment(layout), FragmentViewInterface, BusInterface {

    protected var mOnBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (areSharedElements()) sharedElementEnterTransition = TransitionInflater.from(
            context
        )
            .inflateTransition(android.R.transition.move)
        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(handleBackPressed()) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            }.also { mOnBackPressedCallback = it })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (shouldRegisterBus() && !isBusRegistered) registerBus()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (view != null) return view
//        val layout = getLayout()
        return if (layout == 0) super.onCreateView(
            inflater,
            container,
            savedInstanceState
        ) else postOnCreateView(
            inflater,
            container,
            layout
        )
    }

    private fun postOnCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        layout: Int
    ): View? {
        val context = inflater.context
        var view = inflater.inflate(layout, container, false)
        view = addToolbar(inflater, context, view)
        return view
    }

    protected fun postOnCreateView(inflater: LayoutInflater, view: View?): View? {
        var view = view
        val context = inflater.context
        view = addToolbar(inflater, context, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("")
        setupBinding(view)
        setBackgroundColor(view)
        setupToolbar(view)
        setupViews(view)
        setupListeners(view)
        setupObservers(view)
        view.isClickable = true
        view.isFocusable = true
//        if (automaticCall() && !shouldPostponeCall()) call()
    }

    override fun setupObservers(view: View) {
        Timber.d("")
    }

    override fun setupBinding(view: View) {
        Timber.d("")
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        if (automaticCall() && shouldPostponeCall()) call()
//    }

    override fun setupViews(view: View) {
        Timber.d("")
    }

    override fun setupListeners(view: View) {
        Timber.d("")
    }

    protected fun setText(view: View, id: Int, text: Int) {
        try {
            val textView = view.findViewById<TextView>(id)
            textView?.setText(text)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun setText(view: View, id: Int, text: String?) {
        try {
            val textView = view.findViewById<TextView>(id)
            if (textView != null) textView.text = text
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun setOnClickListener(view: View, id: Int, listener: View.OnClickListener?) {
        view.findViewById<View>(id).setOnClickListener(listener)
    }

    protected fun setOnClickListener(view: View, listener: View.OnClickListener?) {
        view.setOnClickListener(listener)
    }

    override fun removeListeners(view: View) {}

    override fun setBackgroundColor(view: View) {
        Timber.d("")
//        if (setDefaultWindowBackground()) {
//            view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background))
//                    ViewUtils.getThemeColor(getContext(), android.R.attr.textColorTertiary));
//            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
//        }
    }

    //    override fun call() {}
    override fun onRefresh() {}

    override fun onDestroyView() {
        try {
            if (shouldRegisterBus() && isBusRegistered) unregisterBus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroyView()
    }

    override val isBusRegistered: Boolean
        get() = EventBus.getDefault().isRegistered(this)

    override fun registerBus() {
        EventBus.getDefault().register(this)
    }

    override fun unregisterBus() {
        EventBus.getDefault().unregister(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().detach(this).commitAllowingStateLoss()
        super.onConfigurationChanged(newConfig)
        fragmentManager.beginTransaction().attach(this).commitAllowingStateLoss()
    }

    override fun setupToolbar(view: View) {
        Timber.d("")
        try {
            val toolbar = view.findViewById<Toolbar>(getToolbarId()) ?: return
            toolbar.setNavigationOnClickListener { v ->
                Navigation.findNavController(v).navigateUp()
            }
            toolbar.setOnMenuItemClickListener { menuItem ->
                onOptionsItemSelected(menuItem)
                false
            }
            setupToolbar(toolbar)
            setupToolbarMenu(toolbar.menu)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun getToolbarId(): Int {
        return R.id.toolbar
    }

    /**
     * Decide si cargar un layout q contenga la toolbar o no
     *
     * @return
     */
    protected open fun addToolbar(): Boolean {
        return false
    }

    /**
     * Añade una [Toolbar] al contenido
     *
     * @param inflater
     * @param context
     * @param view
     * @return
     */
    protected fun addToolbar(inflater: LayoutInflater, context: Context?, view: View?): View? {
        if (addToolbar()) {
            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            linearLayout.orientation = LinearLayout.VERTICAL
            inflater.inflate(getToolbarLayout(), linearLayout, true)
            linearLayout.addView(view)
            return linearLayout
        }
        return view
    }

    protected fun getToolbarLayout(): Int {
        return R.layout.include_appbar
    }

    protected open fun setupToolbar(toolbar: Toolbar) {
        Timber.d("")
    }

    protected open fun setupToolbarMenu(menu: Menu) {
        Timber.d("")
    }

    protected fun toast(text: String?) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    protected fun toast(text: Int) {
        toast(getString(text))
    }

    protected fun navigate(destination: Int) {
        try {
            Navigation.findNavController(requireView()).navigate(destination)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun navigate(navDirections: NavDirections?) {
        try {
            Navigation.findNavController(requireView()).navigate(navDirections!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun popBackStack() {
        try {
            Navigation.findNavController(requireView()).popBackStack()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    protected fun showProgress() {
//        try {
//            requireView().findViewById<ProgressBar>(R.id.pb_loading).visibility = View.VISIBLE
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    protected fun hideProgress() {
//        try {
//            requireView().findViewById<ProgressBar>(R.id.pb_loading).visibility = View.INVISIBLE
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

}