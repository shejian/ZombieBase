package com.avengers.zombiebase.aacbase

enum class Status {
    RUNNING,
    SUCCESS,
    CACHED_FAILED,
    EMPTY,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
        var status: Status,
        var msg: String? = null,
        var code: Int? = 0) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun empty(msg: String?) = NetworkState(Status.EMPTY,msg)
        fun cachedError(msg: String?) = NetworkState(Status.CACHED_FAILED,msg)
        fun cachedError(code: Int,msg: String?) = NetworkState(Status.CACHED_FAILED,msg,code)
        fun error(msg: String?) = NetworkState(Status.FAILED,msg)
        fun error(code: Int,msg: String?) = NetworkState(Status.FAILED,msg,code)

    }
}