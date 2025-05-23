package com.example.onlinecoursesapp.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.onlinecoursesapp.R;

public class VideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        WebView webView = findViewById(R.id.youtubeWebView);
        ProgressBar loadingIndicator = findViewById(R.id.loadingIndicator);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                loadingIndicator.setVisibility(View.GONE);
            }
        });


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false); // Cho phép tự động phát video
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        String videoId = getIntent().getStringExtra("videoId");
        String html = "<html><body style='margin:0'><iframe width='100%' height='100%' src='https://www.youtube.com/embed/" +
                videoId + "?autoplay=1&enablejsapi=1' frameborder='0' allowfullscreen></iframe></body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
    }
}
