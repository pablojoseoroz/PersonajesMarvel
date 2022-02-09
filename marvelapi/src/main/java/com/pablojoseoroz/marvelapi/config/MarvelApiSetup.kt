package com.pablojoseoroz.marvelapi.config

import android.content.Context
import com.pablojoseoroz.marvelapi.R

/**
 * Contains parameters to configure [MarvelRepository]
 * Mdantory fields:
 * - [appKey]
 * Optionals fields:
 * - [requestConnectionTimeOut]
 * - [requestReadTimeOut]
 * - [requestWriteTimeOut]
 * - [cacheEnabled]
 * - [cacheSize]
 * - [cacheMaxStale]
 * - [cacheMaxAge]
 *
 * Default implementation loads configuration from resources
 * @constructor
 *
 * @param context
 */
class MarvelApiSetup(context: Context) {

    /**
     * Http request connection time out: in seconds
     */
    val requestConnectionTimeOut: Long

    /**
     * Http request read time out : in seconds
     */
    val requestReadTimeOut: Long

    /**
     * Http request write time out : in seconds
     */
    val requestWriteTimeOut: Long

    init {
        requestConnectionTimeOut =
            context.resources.getInteger(R.integer.marvel_http_request_connection_time_out)
                .toLong()
        requestReadTimeOut =
            context.resources.getInteger(R.integer.marvel_http_request_read_time_out).toLong()
        requestWriteTimeOut =
            context.resources.getInteger(R.integer.marvel_http_request_write_time_out)
                .toLong()
    }

}