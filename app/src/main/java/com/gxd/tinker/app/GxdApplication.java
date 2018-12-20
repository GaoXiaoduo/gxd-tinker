package com.gxd.tinker.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * enableProxyApplication = false 的情况
 *
 * @author gaoxiaoiduo
 * @version 1.0
 * @date 18/12/20下午2:40
 */
public class GxdApplication extends TinkerApplication
{
    private static final String TAG = GxdApplication.class.getSimpleName();

    // 注意： 这个类集成TinkerApplication类，这里面不做任何操作，所有Application的代码都会放到ApplicationLike继承类当中
    //       参数解析
    // 参数1：tinkerFlags 表示Tinker支持的类型 dex only、library only or all suuport，default:
    //       TINKER_ENABLE_ALL
    // 参数2：delegateClassName Application代理类 这里填写你自定义的ApplicationLike
    // 参数3：loaderClassName Tinker的加载器，使用默认即可
    // 参数4：tinkerLoadVerifyFlag 加载dex或者lib是否验证md5，默认为false

    public GxdApplication ()
    {

        super(ShareConstants.TINKER_ENABLE_ALL, "com.gxd.tinker.app.GxdApplicationLink",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}