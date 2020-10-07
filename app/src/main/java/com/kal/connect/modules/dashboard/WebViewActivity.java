package com.kal.connect.modules.dashboard;

import android.os.Bundle;
import android.webkit.WebView;

import com.kal.connect.R;
import com.kal.connect.customLibs.appCustomization.CustomActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity  extends CustomActivity {

    @BindView(R.id.webView)
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        ButterKnife.bind(this);


        try{
            String url = getIntent().getStringExtra("Url");
            if(url != null && !url.isEmpty()){
                webView.getSettings().setSupportZoom(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(url);

            }else{
                finish();
            }
        }catch (Exception e){
            finish();
        }


    }
}
