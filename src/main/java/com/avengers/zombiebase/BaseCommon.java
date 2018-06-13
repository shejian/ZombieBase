package com.avengers.zombiebase;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.avengers.mylibrary.CommonUtil;

public class BaseCommon {

    public CommonUtil getEathCommon() {
        return new CommonUtil();
    }

    public void load(ImageView view, String url) {
        Drawable drawable = getEathCommon().loadImg(url);
        getEathCommon().showImg(view,drawable);
    }


}
