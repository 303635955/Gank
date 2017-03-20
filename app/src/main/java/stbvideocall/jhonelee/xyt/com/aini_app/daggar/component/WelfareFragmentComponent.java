package stbvideocall.jhonelee.xyt.com.aini_app.daggar.component;

import android.app.Activity;

import dagger.Component;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.PerFragment;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment.WelfareFragment;

/**
 * Created by Administrator on 2017/3/14.
 */

@PerFragment
@Component(dependencies = AppComponent.class)
public interface WelfareFragmentComponent {
    void inject(WelfareFragment welfareFragment);
}
