package com.avengers.zombiebase;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.avengers.zombielibrary.BuildConfig;
import com.spinytech.macore.MaActionResult;
import com.spinytech.macore.MaApplication;
import com.spinytech.macore.router.LocalRouter;
import com.spinytech.macore.router.RouterRequest;
import com.spinytech.macore.router.RouterResponse;

public class ZombieBaseUtils {

    public ZombieBaseUtils() {
    }

    public void getZombieLibraryInfo() {

        Log.d("shejian", BuildConfig.VERSION_NAME);

    }

    public static LocalRouter getLocalRoute() {
        return LocalRouter.getInstance(MaApplication.getMaApplication());
    }

    public static RouterResponse onLocalRoute(Context context, @NonNull RouterRequest routerRequest) throws Exception {
        return getLocalRoute().route(context, routerRequest);
    }


    public static void startNavigation(String routerPath, Bundle bundle) {
        ARouter.getInstance().build(routerPath).with(bundle).navigation();
    }

    public static void startNavigation(String routerPath) {
        startNavigation(routerPath, null);
    }

    public static Postcard startNavigationBuild(String routerPath) {
        return ARouter.getInstance().build(routerPath);
    }


}
