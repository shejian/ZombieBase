package com.avengers.zombiebase.glide

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import java.util.concurrent.Executor


/**
 * Created by duo.chen on 2018/7/17
 */
object GlideImageUtil {

    fun show(context: Context,url: String,imageView: ImageView,radius: Int = 0) {
        showImage(GlideApp.with(context),url,imageView,radius)
    }

    fun show(activity: Activity,url: String,imageView: ImageView,radius: Int = 0) {
        showImage(GlideApp.with(activity),url,imageView,radius)
    }

    fun show(activity: FragmentActivity,url: String,imageView: ImageView,radius: Int = 0) {
        showImage(GlideApp.with(activity),url,imageView,radius)
    }

    fun show(fragment: Fragment,url: String,imageView: ImageView,radius: Int = 0) {
        showImage(GlideApp.with(fragment),url,imageView,radius)
    }

    fun show(view: View,url: String,imageView: ImageView,radius: Int = 0) {
        showImage(GlideApp.with(view),url,imageView,radius)
    }


    private fun showImage(glideRequests: GlideRequests,url: String,imageView: ImageView,radius: Int = 0) {
        var glideRequest = glideRequests.load(url)
        if (radius != 0) {
            glideRequest = glideRequest.transform(RoundedCorners(radius))
        }
        glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
    }

    fun clearCache(context: Context,executor: Executor) {
        executor.execute { GlideApp.get(context).clearDiskCache() }
    }

}
