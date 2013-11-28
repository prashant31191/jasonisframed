package com.androidfactorem.jasonisframed;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class WebImage extends AsyncTask<String, Void, Bitmap> {
	Bitmap bitmp = null;

	@Override
	protected Bitmap doInBackground(String... params) {
		try {
			Bitmap bitmp1;
			HttpURLConnection urlConn = (HttpURLConnection) new URL(params[0])
					.openConnection();
			urlConn.connect();
			// urlConn.setUseCaches(true);
			InputStream is = urlConn.getInputStream();
			bitmp1 = BitmapFactory.decodeStream(new ImageFlushedIOStream(is));

			return bitmp1;
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		bitmp = result;
	}

	public Bitmap getImageBm() {
		return bitmp;
	}

}
