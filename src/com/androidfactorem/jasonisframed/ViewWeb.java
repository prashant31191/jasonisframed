package com.androidfactorem.jasonisframed;








import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewWeb extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);

		Uri url = Uri.parse(getIntent().getStringExtra("URL"));
		WebView webView = (WebView) findViewById(R.id.wvLink);
		webView.setWebViewClient(new Callback());

		if (url != null) {
			webView.loadUrl(url.toString());
		} else {
			webView.loadUrl(getString(R.string.str_url));
		}
	}

	private class Callback extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}
	}
}
