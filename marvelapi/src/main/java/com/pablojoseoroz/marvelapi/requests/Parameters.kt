package com.pablojoseoroz.marvelapi.requests

/**
 * Optional parameters to use with api calls
 *
 * @constructor Create empty Parameters
 */
class Parameters(val limit: Int? = null, var offset: Int? = null, var orderBy: String? = null)