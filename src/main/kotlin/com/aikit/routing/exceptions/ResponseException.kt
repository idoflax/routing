package com.aikit.routing.exceptions

import okhttp3.Response

class ResponseException(override val message: String?, val response: Response) : Exception()
