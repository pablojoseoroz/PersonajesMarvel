package com.pablojoseoroz.marvelapi.exceptions

import java.io.IOException

/**
 * Unauthorized exception
 *
 * @constructor
 *
 * @param description
 */
class UnauthorizedException(description: String? = null) :
    IOException("Http 403 Unauthorized" + " " + description)

class MissingApiKeyException() :
    IOException("409\tMissing API Key\tOccurs when the apikey parameter is not included with a request.")

class MissingHashException() :
    IOException("409\tMissing Hash\tOccurs when an apikey parameter is included with a request, a ts parameter is present, but no hash parameter is sent. Occurs on server-side applications only.")

class MissingTimestampException() :
    IOException("409\tMissing Timestamp\tOccurs when an apikey parameter is included with a request, a hash parameter is present, but no ts parameter is sent. Occurs on server-side applications only.")

class InvalidRefererException() :
    IOException("401\tInvalid Referer\tOccurs when a referrer which is not valid for the passed apikey parameter is sent.")

class InvalidHashException() :
    IOException("401\tInvalid Hash\tOccurs when a ts, hash and apikey parameter are sent but the hash is not valid per the above hash generation rule.")

class MethodNotAllowedException() :
    IOException("405\tMethod Not Allowed\tOccurs when an API endpoint is accessed using an HTTP verb which is not allowed for that endpoint.")

class ForbiddenException() :
    IOException("403\tForbidden\tOccurs when a user with an otherwise authenticated request attempts to access an endpoint to which they do not have access.")
