package com.avengers.zombiebase.glide

import android.content.Context
import android.util.Log

import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * Created by duo.chen on 2018/7/17
 */
@GlideModule
class CustomGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context,builder: GlideBuilder) {
        builder.setDiskCache(ExternalPreferredCacheDiskCacheFactory(context,(1024 * 1024 * 1024).toLong()))
                .setDefaultRequestOptions(RequestOptions().format(DecodeFormat.PREFER_RGB_565).disallowHardwareConfig())
                .setLogLevel(Log.DEBUG)
    }
}
