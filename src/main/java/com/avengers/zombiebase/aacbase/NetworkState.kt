package com.avengers.zombiebase.aacbase

enum class Status {
    RUNNING,
    SUCCESS,
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
        fun error(msg: String?) = NetworkState(Status.FAILED,msg)
        fun error(code: Int,msg: String?) = {
            NetworkState(Status.FAILED,msg,code)
        }
    }
}