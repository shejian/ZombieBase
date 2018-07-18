package com.avengers.zombiebase.matisse

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.avengers.zombiebase.glide.GlideApp
import com.bumptech.glide.Priority
import com.zhihu.matisse.engine.ImageEngine

/**
 * Created by duo.chen on 2018/7/18
 */
class GlideEngine : ImageEngine {

    override fun loadThumbnail(context: Context,resize: Int,placeholder: Drawable,imageView: ImageView,uri: Uri) {
        GlideApp.with(context)
                .asBitmap()  // some .jpeg files are actually gif
                .load(uri)
                .placeholder(placeholder)
                .override(resize,resize)
                .centerCrop()
                .into(imageView)
    }

    override fun loadGifThumbnail(context: Context,resize: Int,placeholder: Drawable,imageView: ImageView,
                                  uri: Uri) {
        GlideApp.with(context)
                .asBitmap()
                .load(uri)
                .placeholder(placeholder)
                .override(resize,resize)
                .centerCrop()
                .into(imageView)
    }

    override fun loadImage(context: Context,resizeX: Int,resizeY: Int,imageView: ImageView,uri: Uri) {
        GlideApp.with(context)
                .load(uri)
                .override(resizeX,resizeY)
                .priority(Priority.HIGH)
                .fitCenter()
                .into(imageView)
    }

    override fun loadGifImage(context: Context,resizeX: Int,resizeY: Int,imageView: ImageView,uri: Uri) {
        GlideApp.with(context)
                .asGif()
                .load(uri)
                .override(resizeX,resizeY)
                .priority(Priority.HIGH)
                .into(imageView)
    }

    override fun supportAnimatedGif(): Boolean {
        return true
    }

}