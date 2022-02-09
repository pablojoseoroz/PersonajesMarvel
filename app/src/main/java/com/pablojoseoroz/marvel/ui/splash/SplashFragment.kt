package com.pablojoseoroz.marvel.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import com.pablojoseoroz.marvel.R
import com.pablojoseoroz.uibase.BaseFragment

/**
 * Splash fragment para mostrar el logo de Marvel durante 1seg
 *
 * @constructor Create empty Splash fragment
 */
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            navigate(R.id.action_splashFragment_to_homeFragment)
        }, 1000)
    }
}