package stbvideocall.jhonelee.xyt.com.aini_app.daggar.component;

import dagger.Component;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.Module.ActivityModule;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.PerActivity;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.PerFragment;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.activity.PictureActivity;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment.IOSFragment;

/**
 * Created by Administrator on 2017/3/14.
 */

@PerActivity
@Component(dependencies = AppComponent.class,modules = ActivityModule.class)
public interface PictureActivityComonent extends AppComponent{
    void inject(PictureActivity pictureActivity);
}
