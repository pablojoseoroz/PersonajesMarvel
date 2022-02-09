package com.pablojoseoroz.marvel

import android.app.Application
import androidx.room.Room
import com.pablojoseoroz.marvel.room.AppDatabase
import timber.log.Timber

/**
 * Marvel app para configurar el Timber
 *
 * @constructor Create empty Marvel app
 */
class MarvelApp : Application() {

    private lateinit var mDatabase: AppDatabase

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    /**
     * Setup timber to show only logs if BuildConfig#DEBUG
     *
     */
    private fun setupTimber() {
        Timber.plant(if (BuildConfig.DEBUG) Timber.DebugTree() else object : Timber.Tree() {
            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {}
        })
    }

}