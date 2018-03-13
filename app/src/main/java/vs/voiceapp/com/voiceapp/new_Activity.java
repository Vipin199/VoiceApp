package vs.voiceapp.com.voiceapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class new_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);
        final WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("file:///android_asset/data.html");
//        myWebView.setWebViewClient(new WebViewClient(){
//            public void onPageFinished(WebView view, String url){
//                myWebView.loadUrl("javascript:init('" +15+"')");
//            }
//        });
    }
}
