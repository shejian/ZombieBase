package com.avengers.zombiebase

import android.util.Log
import com.avengers.zombielibrary.BuildConfig

object LogU {

    private const val DEFAULT_TAG = "Avengers"

    private val isDebug = BuildConfig.DEBUG

    fun v(msg: String) {
        v(DEFAULT_TAG, msg)
    }

    fun v(TAG: String, msg: String) {
        if (isDebug) {
            Log.v(TAG, msg)
        }
    }

    fun i(msg: String) {
        i(DEFAULT_TAG, msg)
    }


    fun i(TAG: String, msg: String) {
        if (isDebug) {
            Log.i(TAG, msg)
        }
    }

    fun d(msg: String) {
        d(DEFAULT_TAG, msg)
    }

    fun d(TAG: String, msg: String) {
        if (isDebug) {
            Log.d(TAG, msg)
        }
    }

    fun e(msg: String) {
        e(DEFAULT_TAG, msg)
    }

    fun e(TAG: String, msg: String) {
        if (isDebug) {
            Log.e(TAG, msg)
        }
    }

    fun e(TAG: String, s: String, e: Exception) {
        if (isDebug) {
            Log.e(TAG, s, e)
        }
    }

}
