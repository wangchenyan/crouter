package me.wcy.router

internal class RealInterceptorChain(
        private val interceptors: List<Interceptor>,
        private val index: Int,
        private val request: Request,
        private val call: Call
) : Interceptor.Chain {
    private var calls: Int = 0

    override fun call(): Call {
        return call
    }

    override fun request(): Request {
        return request
    }

    override fun proceed(request: Request): Response {
        if (index >= interceptors.size) throw AssertionError()

        calls++

        // Call the next interceptor in the chain.
        val next = RealInterceptorChain(interceptors, index + 1, request, call)
        val interceptor = interceptors[index]
        val response = interceptor.intercept(next)

        // Confirm that the next interceptor made its required call to chain.proceed().
        if (response == null && index + 1 < interceptors.size && next.calls != 1) {
            throw IllegalStateException("router interceptor " + interceptor
                    + " must call proceed() exactly once")
        }

        // Confirm that the intercepted response isn't null.
        if (response == null) {
            throw NullPointerException("interceptor $interceptor returned null")
        }

        return response
    }
}
