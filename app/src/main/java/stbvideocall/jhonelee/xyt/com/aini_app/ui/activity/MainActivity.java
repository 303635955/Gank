package stbvideocall.jhonelee.xyt.com.aini_app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import stbvideocall.jhonelee.xyt.com.aini_app.R;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment.AndroidFragment;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment.FrontEndFragment;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment.IOSFragment;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment.TodayGankFragment;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment.VideoFragment;
import stbvideocall.jhonelee.xyt.com.aini_app.ui.fragment.WelfareFragment;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setContext(R.id.nav_today);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        setContext(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setContext(int id){
        switch (id) {
            case R.id.nav_today:
                if(null == getFragmentManager().findFragmentByTag(TodayGankFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, TodayGankFragment.newInstance(), TodayGankFragment.TAG);
                    setTitle(R.string.app_name);
                }
                break;
            case R.id.nav_welfare:
                if(null == getFragmentManager().findFragmentByTag(WelfareFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, WelfareFragment.newInstance(), WelfareFragment.TAG);
                    setTitle(R.string.nav_welfare);
                }
                break;
            case R.id.nav_android:
                if(null == getFragmentManager().findFragmentByTag(AndroidFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, AndroidFragment.newInstance(), AndroidFragment.TAG);
                    setTitle(R.string.nav_android);
                }
                break;
            case R.id.nav_ios:
                if(null == getFragmentManager().findFragmentByTag(IOSFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, IOSFragment.newInstance(), IOSFragment.TAG);
                    setTitle(R.string.nav_ios);
                }
                break;
            case R.id.nav_front_end:
                if(null == getFragmentManager().findFragmentByTag(FrontEndFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, FrontEndFragment.newInstance(), FrontEndFragment.TAG);
                    setTitle(R.string.nav_front_end);
                }
                break;
            case R.id.nav_video:
                if(null == getFragmentManager().findFragmentByTag(VideoFragment.TAG)) {
                    replaceFragment(R.id.fragment_container, VideoFragment.newInstance(), VideoFragment.TAG);
                    setTitle(R.string.nav_video);
                }
                break;
            case R.id.nav_about:
                break;
            case R.id.nav_feedback:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this,"进来啦啦啦",Toast.LENGTH_SHORT).show();
        setContext(resultCode);
    }
}
