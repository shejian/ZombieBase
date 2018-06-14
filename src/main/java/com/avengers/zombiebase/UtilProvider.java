package com.avengers.zombiebase;

import com.spinytech.macore.MaProvider;

public class UtilProvider extends MaProvider {


    @Override
    protected void registerActions() {
        registerAction("mymd5", new MD5EncryptAction());
    }
}
