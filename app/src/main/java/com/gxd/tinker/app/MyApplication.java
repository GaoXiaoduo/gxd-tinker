package com.gxd.tinker.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * enableProxyApplication = true 的情况
 *
 * @author gaoxiaoiduo
 * @version 1.0
 * @date 18/12/20下午6:46
 */
public class MyApplication extends Application
{
    // 注：无须你改造Application，主要是为了降低接入成本，
    // 我们插件会动态替换AndroidMinifest文件中的Application为我们定义好用于反射
    // 真实Application的类（需要您接入SDK 1.2.2版本 和 插件版本 1.0.3以上）

    @Override
    public void onCreate ()
    {

        super.onCreate();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(this, "900029763", false);
    }

    @Override
    protected void attachBaseContext (Context base)
    {

        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }
}