package debug;

import com.facebook.drawee.backends.pipeline.Fresco;

import google.architecture.common.base.BaseApplication;

public class TodoAplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
