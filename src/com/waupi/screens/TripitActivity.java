package com.waupi.screens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.waupi.constants.Constant;

public class TripitActivity extends BaseScreen {

	private WebView webView;
	boolean loadingFinished = true;
	boolean redirect = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tripit_webview);
		
		initWebView();
	}
	
	private void initWebView() {

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setBackgroundColor(Color.parseColor("#808080"));

		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setBuiltInZoomControls(false);

		webView.getSettings().setAppCacheMaxSize(1024 * 8);
		webView.getSettings().setAppCacheEnabled(true);

		webView.setWebChromeClient(new WebChromeClient());
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				System.out.println("!123--current url " + url);
				webView.loadUrl(url);
				if (url.contains("http://waupi.com/api/v1/users/auth/tripit/callback")) {

					final String[] key = url.split("=");

					System.out.println("!12345454545--current url " + url);
					Constant.isLoginWithTripit = true;
					finish();

				}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {

				System.out.println("!--Error" + error);
				handler.proceed();
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}

			@Override
			public void onPageFinished(WebView view, final String url) {

				if(!redirect){
			          loadingFinished = true;
			    }
			    if(loadingFinished && !redirect){
			        view.setVisibility(View.VISIBLE);
			        
			    } else{
			        redirect = false; 
			    } 
			}

			@SuppressWarnings("deprecation")
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				System.out.println("!--errorCode" + errorCode);

			}
		});
		webView.loadUrl("http://waupi.com/api/v1/users/auth/tripit");
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent mIntent = new Intent(TripitActivity.this,Login.class);
		startActivity(mIntent);
		finish();
	}
}
