package com.pablojoseoroz.marvel.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.pablojoseoroz.marvel.R
import com.pablojoseoroz.marvel.databinding.ActivityMainBinding
import com.pablojoseoroz.navigation.NavHostFragment

/**
 * Main activity que contiene el navigation de la app
 * Utiliza el NavigationUI para gestionar la toolbar
 * Esconde la toolbar en caso de estar en el SplashFragment
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.toolbar)

        setupNavController()

        setupNavigationUI()
    }

    private fun setupNavController() {
        mNavController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            mBinding.toolbar.visibility.apply {
                when (destination.id) {
                    R.id.splashFragment -> {
                        supportActionBar?.hide()
                        View.GONE
                    }
                    else -> {
                        window.setBackgroundDrawableResource(R.color.white)
                        supportActionBar?.show()
                        View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupNavigationUI() {
        NavigationUI.setupActionBarWithNavController(
            this,
            mNavController,
            AppBarConfiguration(
                setOf(
                    R.id.homeFragment,
                )
            )
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp()
    }
}