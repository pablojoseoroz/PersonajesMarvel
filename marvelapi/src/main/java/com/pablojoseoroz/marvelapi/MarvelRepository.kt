package com.pablojoseoroz.marvelapi

import android.content.Context
import com.google.gson.GsonBuilder
import com.itkacher.okhttpprofiler.OkHttpProfilerInterceptor
import com.pablojoseoroz.marvelapi.dto.CharacterDataWrapper
import com.pablojoseoroz.marvelapi.api.MarvelApi
import com.pablojoseoroz.marvelapi.config.MarvelApiSetup
import com.pablojoseoroz.marvelapi.interceptors.AuthInterceptor
import com.pablojoseoroz.marvelapi.interceptors.ResponseInterceptor
import com.pablojoseoroz.marvelapi.requests.Parameters
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Implements [MarvelApi] using [Retrofit].
 * Configure [OkHttpClient] using [MarvelApiSetup]
 *
 * @constructor
 *
 * @param context
 */
class MarvelRepository(context: Context) {

    private var cacheFile: File
    private var api: MarvelApi
    private var setup: MarvelApiSetup

    init {
        setup = MarvelApiSetup(context)
        cacheFile = context.cacheDir
        api = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(createOkHttpClient())
            .build()
            .create(MarvelApi::class.java)
    }

    /**
     * Create ok http client using [MarvelApiSetup] for timeouts and cache.
     * Add these interceptors:
     * - [ResponseInterceptor]
     * - [OfflineInterceptor]
     * - [OnlineInterceptor]
     * - [AuthInterceptor]
     * - [LocationInterceptor]
     *
     * @return
     */
    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .dispatcher(Dispatcher(Executors.newFixedThreadPool(5)).apply {
                maxRequests = 50
                maxRequestsPerHost = 50
            })
            .connectionPool(ConnectionPool(50, 1, TimeUnit.MINUTES))
            .connectTimeout(setup.requestConnectionTimeOut, TimeUnit.SECONDS)
            .readTimeout(setup.requestReadTimeOut, TimeUnit.SECONDS)
            .writeTimeout(setup.requestWriteTimeOut, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor(ResponseInterceptor())
            .addInterceptor(AuthInterceptor())
            .apply {
                if (BuildConfig.DEBUG)
                    addInterceptor(OkHttpProfilerInterceptor())
            }
            .build()
    }

    /**
     * Fetch characters
     */
    suspend fun fetchCharacters(parameters: Parameters?): Response<CharacterDataWrapper> {
        return api.getCharacters(
            parameters?.offset,
            parameters?.limit
        )
    }

    /**
     * Get character by id
     */
    suspend fun getCharacter(id: Int): Response<CharacterDataWrapper> {
        return api.getCharacter(id)
    }

}