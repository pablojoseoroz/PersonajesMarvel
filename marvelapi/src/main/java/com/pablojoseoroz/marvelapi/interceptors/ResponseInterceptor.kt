package com.pablojoseoroz.marvelapi.interceptors

import com.pablojoseoroz.marvelapi.exceptions.*
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

/**
 * [Interceptor] to launch [IOException] if response code is 400,401,403 or 404
 */
class ResponseInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())
        return when (response.code) {
            409 -> if (response.message.contains("Missing API Key", false)) {
                throw MissingApiKeyException()
            } else if (response.message.contains("Missing Hash", false)) {
                throw MissingApiKeyException()
            } else if (response.message.contains("Missing Timestamp", false)) {
                throw MissingApiKeyException()
            } else {
                throw IOException()
            }
            401 -> if (response.message.contains("Invalid Referer", false)) {
                throw InvalidRefererException()
            } else if (response.message.contains("Invalid Hash", false)) {
                throw InvalidHashException()
            } else {
                throw IOException()
            }
            405 -> throw MethodNotAllowedException()
            403 -> throw ForbiddenException()
            else -> response
        }
    }

}