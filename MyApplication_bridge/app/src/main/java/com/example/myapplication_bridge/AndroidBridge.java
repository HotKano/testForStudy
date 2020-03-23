package com.example.myapplication_bridge;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class AndroidBridge {

    final public Handler handler = new Handler();

    private WebView mAppView;
    private MainActivity mContext;
    final String TAG = "ani_test";

    public AndroidBridge(WebView _mAppView, MainActivity _mContext) {
        mAppView = _mAppView;
        mContext = _mContext;
    }

    @JavascriptInterface
    public void call_log(final String _msg) {
        Log.d(TAG, _msg);
        handler.post(new Runnable() {
            @Override
            public void run() {
                mAppView.loadUrl("javascript:alert( '[" + _msg + "]라고 로그를 남겼습니다.' )");
            }
        });
    }
}
