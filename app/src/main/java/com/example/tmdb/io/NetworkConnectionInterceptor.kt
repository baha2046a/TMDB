package com.example.tmdb.io

import android.util.Log
import okhttp3.*

/**
 *  Ensure the App work normally when no network environment
 *  Set to httpClient by [OkHttpClient.Builder.addInterceptor]
 */
class NetworkConnectionInterceptor : Interceptor {
    companion object {
        /**
         * Set [onNoNetworkConnection] to show message at UI
         */
        var onNoNetworkConnection: () -> Unit = { }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // No Network Available
        if (!NetworkTracker.isAvailable) {
            Log.d("NetworkConnectionInterceptor", "Error : No Network")
            return createMockResponse(chain)
        }

        val builder = chain.request().newBuilder()

        return kotlin.runCatching {
            chain.proceed(builder.build())
        }.onFailure {
            Log.d("NetworkConnectionInterceptor", "Error : ${it.message}")
        }.getOrElse { createMockResponse(chain) }
    }

    private fun createMockResponse(chain: Interceptor.Chain): Response {
        onNoNetworkConnection.invoke()
        return Response.Builder()
            .addHeader("content-type", "application/json")
            .body(ResponseBody.create(MediaType.parse("application/json"), ""))
            .code(404)
            .message("Mock response")
            .protocol(Protocol.HTTP_1_0)
            .request(chain.request())
            .build()
    }
}