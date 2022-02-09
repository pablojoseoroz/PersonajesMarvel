package com.pablojoseoroz.marvelapi.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.security.Timestamp
import java.util.*
import java.math.BigInteger
import java.security.MessageDigest

/**
 * [Interceptor] para añadir las querys necesarias para poder realizar las conexiones con la api de Marvel
 */
class AuthInterceptor() : Interceptor {

    val md = MessageDigest.getInstance("MD5")
    val privateKey = "f718db0ae6f1695f7980b7bbd2c0c25172598fe4"
    val publicKey = "fbca78518abe5877e1da895a88c9798c"

    override fun intercept(chain: Interceptor.Chain): Response {
        val ts = Date().time.toString()
        val input = ts + privateKey + publicKey

        return chain.proceed(
            chain.request()
                .newBuilder()
                .url(
                    chain
                        .request()
                        .url
                        .newBuilder()
                        .addQueryParameter("ts", ts)
                        .addQueryParameter("apikey", publicKey)
                        .addQueryParameter(
                            "hash",
                            BigInteger(1, md.digest(input.toByteArray())).toString(16)
                                .padStart(32, '0')
                        ).build()
                )
                .build()
        )
    }

}