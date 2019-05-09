package google.architecture.login.common;

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

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseApplication;
import google.architecture.common.util.Utils;
import google.architecture.login.R;
import google.architecture.login.activities.LoginActivity;

/**
 * Created by dxx on 2017/11/13.
 */

public class Application extends BaseApplication{

    SharedPreferences sp;

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


        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        sp = getSharedPreferences("token_table", Context.MODE_PRIVATE);
        if (sp != null) {
            String token = sp.getString("user_token", "");
            if (!TextUtils.isEmpty(token)) {

                ARouter.getInstance()
                        .build(ARouterPath.MainAty)
                        // 可以针对性跳转跳转动画
                        .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
                        .navigation(getApplicationContext());
            }
        }
    }
}
