package stbvideocall.jhonelee.xyt.com.aini_app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import stbvideocall.jhonelee.xyt.com.aini_app.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class WebviewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String EXTRA_URL = "URL";
    private static final String EXTRA_TITLE = "TITLE";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;


    private String mUrl;
    private String mTitle;

    public static void openUrl(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);

        setUpWebView();

        if(null != getIntent()) {
            mUrl = getIntent().getStringExtra(EXTRA_URL);
            mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        }
        setTitle(mTitle);

        webview.loadUrl(mUrl);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(null != webview) {
            webview.onPause();
        }
    }


    public void setUpWebView(){
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        webview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                refreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                refreshLayout.setRefreshing(false);
            }

        });

        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress >= 80){
                    refreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()){
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(webview.canGoBack()) {
                    webview.goBack();
                    return true;
                }
                finish();
                break;
            case R.id.action_share:
                sharePage();
                return true;
            case R.id.action_open_in_browser:
                openInBrowser();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void sharePage() {
        String title = webview.getTitle();
        String url = webview.getUrl();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_page, title, url));
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    private void openInBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(webview.getUrl());
        intent.setData(uri);
        startActivity(intent);
    }


    @Override
    public void onRefresh() {
        webview.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != webview) {
            // After Android 5.1, there has a problem in Webview:
            // if onDetach is called after destroy, AwComponentCallbacks object will be leaked.
            if(null != webview.getParent()) {
                ((ViewGroup)webview.getParent()).removeView(webview);
            }
            webview.destroy();
        }
    }
}
