package me.wcy.router

interface Call {
    fun request(): Request

    fun execute(): Response

    fun isExecuted(): Boolean

    interface Factory {
        fun newCall(request: Request): Call
    }
}
