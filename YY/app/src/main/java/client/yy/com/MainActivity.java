package client.yy.com;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

public class MainActivity extends Activity {
    private WebView myWebview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebview = (WebView)findViewById(R.id.yywebview);
        myWebview.loadUrl("http://47.94.209.108:7003/yy/#/login/page");

//        配置
        WebSettings webSettings = myWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAllowContentAccess(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccessFromFileURLs(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){//
            webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }








        myWebview.setWebChromeClient(new MyWebChromeClient());
        myWebview.setWebViewClient(new MyWebViewClient());
    }

    private Uri imageUri;

    class MyWebChromeClient extends WebChromeClient{
        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            WebCameraHelper.getInstance().mUploadMessage = uploadMsg;
            WebCameraHelper.getInstance().showOptions(MainActivity.this);
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            WebCameraHelper.getInstance().mUploadMessage = uploadMsg;
            WebCameraHelper.getInstance().showOptions(MainActivity.this);
        }

        // For Android > 5.0支持多张上传
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> uploadMsg,
                                         FileChooserParams fileChooserParams) {
            WebCameraHelper.getInstance().mUploadCallbackAboveL = uploadMsg;
            WebCameraHelper.getInstance().showOptions(MainActivity.this);
            return true;
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        WebCameraHelper.getInstance().onActivityResult(requestCode, resultCode, intent);
    }


    class MyWebViewClient extends WebViewClient {

        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            System.out.print("thjwebview----"+url);
//            Log.d("thjwebview----",url);
            view.loadUrl(url);
            return true;

        }

    }


    @Override
    public void onBackPressed() {

        if (myWebview.canGoBack()){
            myWebview.goBack();
        }else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //如果按下的是回退键且历史记录里确实还有页面
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebview.canGoBack()) {
            myWebview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setNavigationBar(Activity activity, int visible){
        View decorView = activity.getWindow().getDecorView();
        //显示NavigationBar
        if (View.GONE == visible){
            int option = SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);

        }
    }
}
