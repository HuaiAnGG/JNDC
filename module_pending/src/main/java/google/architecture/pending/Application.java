package google.architecture.pending;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.just.auth.iscs.impl.ISCSAuthImpl;
import com.just.core.common.CoreConfig;
import com.showjoy.android.storage.SHStorageManager;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseApplication;
import google.architecture.common.util.Utils;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-13 14:57
 */
public class Application extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);

        // 请在manifest注册你的Application并在Application#onCreate()调用CoreConfig.Ext.init(app)
        CoreConfig.Ext.init(this);
        // 注入
        CoreConfig.setAuthGenerator(new ISCSAuthImpl());

        // 统一存储模块
        SHStorageManager.init(this);

        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }
}
