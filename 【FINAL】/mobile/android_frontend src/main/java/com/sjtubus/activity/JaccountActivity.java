package com.sjtubus.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sjtubus.App;
import com.sjtubus.R;
import com.sjtubus.network.cookie.BusCookieJar;
import com.sjtubus.user.UserManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Allen on 2018/7/15.
 */

public class JaccountActivity extends BaseActivity {
    @BindView(R.id.webview_auth)
    WebView auth_webview;

    private String redirect_url;
    private String auth_url;
    private String bus_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setStatusBarColor(this, getResources().getColor(R.color.primary_red));
        bus_url = getString(R.string.sjtubus_host);
        redirect_url = bus_url + "auth/redirect";
        auth_url = bus_url + "auth/jaccount";
        initWebview();
    }

    @Override
    public int getContentViewId(){
        return R.layout.activity_jaccount;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebview() {
        auth_webview.getSettings().setJavaScriptEnabled(true);
        auth_webview.getSettings().setUserAgentString(((App) App.getInstance()).getUserAgent());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(auth_webview,true);
        cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean value) {

            }
        });
        String cookieString = loadCookies();
        cookieManager.setCookie(auth_url, cookieString);
        auth_webview.setWebViewClient(new AuthWebClient());
        auth_webview.loadUrl(auth_url);
    }

    private String loadCookies() {
        BusCookieJar cookieJar = BusCookieJar.getInstance();
        return cookieJar.getCookieString(HttpUrl.parse(redirect_url));
    }

    class AuthWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.i("JaccountActivity", "Start loading: " + url);
//            if (true || !url.split("\\?")[0].equals(successUrl)){
//                return;
//            }
//            view.stopLoading();
//            saveCookies();
//            UserManager.getInstance().refresh();
//            finish();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CookieManager cookieManager = CookieManager.getInstance();
            String CookieStr = cookieManager.getCookie(url);
            if(CookieStr!=null)
            {
                Log.i("cookie", CookieStr);
            }
            if(url.split("\\?")[0].equals(redirect_url)){
                saveCookies();
                UserManager.getInstance().refresh();
                Intent mainIntent = new Intent(JaccountActivity.this,MainActivity.class);
                startActivity(mainIntent);
            }
            super.onPageFinished(view, url);
        }

        private void saveCookies() {
            CookieManager cookieManager = CookieManager.getInstance();
            BusCookieJar cookieJar = BusCookieJar.getInstance();

            HttpUrl busUrl = HttpUrl.parse(bus_url);

            String[] cookieStrings = cookieManager.getCookie(redirect_url)!=null?
                    cookieManager.getCookie(redirect_url).split(";"):new String[0];

            List<Cookie> cookies = new ArrayList<>(cookieStrings.length);
            for (String cookieString : cookieStrings) {
                Log.i("JACCOUNT-COOKIE",cookieString);
                assert busUrl != null;
                Cookie cookie = Cookie.parse(busUrl, cookieString);
                cookies.add(cookie);
            }
            cookieJar.clear(); // prevent duplicated cookie names
            assert busUrl != null;
            cookieJar.saveFromResponse(busUrl, cookies);
        }
    }

    @Override
    public void onBackPressed() {
        if (auth_webview.canGoBack()) {
            auth_webview.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
