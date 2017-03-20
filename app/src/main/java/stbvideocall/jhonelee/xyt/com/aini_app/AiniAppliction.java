package stbvideocall.jhonelee.xyt.com.aini_app;

import android.app.Application;

import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.AppComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.Module.AppModule;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.component.DaggerAppComponent;
import stbvideocall.jhonelee.xyt.com.aini_app.util.AppUtil;

/**
 * Created by Administrator on 2017/2/28.
 */

public class AiniAppliction extends Application{

    private static AppComponent mAppComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.init(this);
        initInjector();
    }

    private void initInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
