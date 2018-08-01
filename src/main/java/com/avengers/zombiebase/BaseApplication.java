package com.avengers.zombiebase;

import com.facebook.stetho.Stetho;
import com.spinytech.macore.MaApplication;

public abstract class BaseApplication extends MaApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        DeviceUtil.initScreenParams(getResources());
        MIUIUtil.INSTANCE.initMIUIInfo();
    }


}