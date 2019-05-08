package google.architecture.login;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.just.auth.iscs.impl.ISCSAuthImpl;
import com.just.core.common.CoreConfig;

import google.architecture.common.base.BaseApplication;
import google.architecture.common.util.Utils;

/**
 * Created by dxx on 2017/11/13.
 */

public class Application extends BaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        CoreConfig.Ext.init(this);
        CoreConfig.setAuthGenerator(new ISCSAuthImpl());


        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
    }
}
