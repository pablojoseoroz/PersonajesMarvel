package com.pablojoseoroz.marvel.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.LibraryGlideModule
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 *
 * [LibraryGlideModule] para registrar un cliente [OkHttpClient] a [GlideApp]
 */
@GlideModule
class OkHttpLibraryGlideModule : LibraryGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(getOkHttpClient(context))
        )
    }

    private fun getOkHttpClient(context: Context): OkHttpClient {
        val dispatcher = Dispatcher(Executors.newFixedThreadPool(10))
        dispatcher.maxRequests = 100
        dispatcher.maxRequestsPerHost = 100

        // crear instancia de okhttpclient
        return OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, CACHE_MEGABYTES.toLong()))
            .dispatcher(dispatcher)
            .connectionPool(ConnectionPool(100, 1, TimeUnit.MINUTES))
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    companion object {
        private const val CACHE_MEGABYTES = 10 * 1024 * 1024
    }
}