package me.wcy.router

import me.wcy.router.interceptor.EmptyInterceptor
import me.wcy.router.interceptor.IntentInterceptor
import me.wcy.router.interceptor.TargetInterceptor
import me.wcy.router.interceptor.UriInterceptor
import java.util.*

internal class RealCall private constructor(private val client: RouterClient, private val originalRequest: Request) : Call {
    // Guarded by this.
    private var executed: Boolean = false

    companion object {
        fun newRealCall(client: RouterClient, originalRequest: Request): RealCall {
            // Safely publish the Call instance to the EventListener.
            return RealCall(client, originalRequest)
        }
    }

    override fun request(): Request {
        return originalRequest
    }

    override fun execute(): Response {
        if (executed) {
            throw IllegalStateException("Already Executed")
        }
        executed = true
        return getResponseWithInterceptorChain()
    }

    override fun isExecuted(): Boolean {
        return executed
    }

    private fun getResponseWithInterceptorChain(): Response {
        // Build a full stack of interceptors.
        val interceptors = ArrayList<Interceptor>()
        interceptors.addAll(client.interceptors())
        interceptors.add(IntentInterceptor())
        interceptors.add(TargetInterceptor())
        interceptors.add(UriInterceptor())
        interceptors.add(EmptyInterceptor())

        val chain = RealInterceptorChain(interceptors, 0, originalRequest, this)

        return chain.proceed(originalRequest)
    }
}
