package com.avengers.zombiebase;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.avengers.zombielibrary.BuildConfig;
import com.bty.retrofit.net.RetrofitManager;
import com.bty.retrofit.net.serurity.Sign;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jvis
 * @date 2018-06-15
 */
public class ApplicationInitBase {

    /**
     * 初始化ARouter
     *
     * @param app Application
     */
    public static void initARouter(Application app) {
        // 这两行必须写在init之前，否则这些配置在init过程中将无效
        if (BuildConfig.DEBUG) {
            // 打印日志
            ARouter.openLog();
            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug();
        }
        // 尽可能早，推荐在Application中初始化
        ARouter.init(app);
    }


    public static void initWebServer(String baseurl) {
        RetrofitManager.Config.Builder builder = new RetrofitManager.Config.Builder();

        HashMap<String, String> headers = new HashMap<>(2);
       // headers.put("pt", "android");
       // headers.put("timestamp", String.valueOf(System.currentTimeMillis()));

        builder.setBaseUrl(baseurl).
                /*            setSignFactory(new Sign.Factory() {
                                @Override
                                public Sign get() {
                                    return new Sign() {
                                        @Override
                                        public Map<String, Object> sign(Map<String, Object> map, String signKey) {
                                            return SignUtil.sign(map, signKey);
                                        }

                                        @Override
                                        public String sign(String source, String signKey) {
                                            return SignUtil.sign(source, signKey);
                                        }

                                        @Override
                                        public String getSignKey() {
                                            return BuildConfig.SECRET_KEY;
                                        }
                                    };
                                }
                            }).*/
                        setLog(true).
                setHeaders(headers);

        RetrofitManager.init(builder.build());


    }


}
