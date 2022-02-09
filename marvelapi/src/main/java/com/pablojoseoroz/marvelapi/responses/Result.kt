package com.pablojoseoroz.marvelapi.responses

/**
 * Result of a server call:
 * - [Success]
 * - [Error]
 * - [Loading]
 *
 * @param T
 * @property data
 * @property throwable
 * @constructor Create empty Result
 */
sealed class Result<T>(val data: T? = null, val throwable: Throwable? = null) {

    /**
     * Success result contains data
     *
     * @param T
     * @constructor
     *
     * @param data
     */
    class Success<T>(data: T?) : Result<T>(data) {
        fun isEmpty() = data == null
    }

    /**
     * Error result contains throwable
     *
     * @param T
     * @constructor
     *
     * @param throwable
     */
    class Error<T>(throwable: Throwable? = null) :
        Result<T>(null, throwable)

    /**
     * Loading status before an api call
     *
     * @param T
     * @constructor Create empty Loading
     */
    class Loading<T> : Result<T>()

}