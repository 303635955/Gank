package stbvideocall.jhonelee.xyt.com.aini_app.ui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import stbvideocall.jhonelee.xyt.com.aini_app.AiniAppliction;

/**
 * Created by Administrator on 2017/2/28.
 */

public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        if(null == getFragmentManager().findFragmentByTag(tag)) {
            getFragmentManager().beginTransaction()
                    .replace(containerViewId, fragment, tag)
                    .commit();
        }
    }
}
