package stbvideocall.jhonelee.xyt.com.aini_app.daggar.Module;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.PerActivity;

/**
 * Created by Administrator on 2017/3/13.
 */
@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity){
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return activity;
    }




}
