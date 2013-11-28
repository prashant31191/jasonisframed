package com.androidfactorem.jasonisframed;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class RESTAPI extends AsyncTask<String, Integer, String> {

	private Context context;
	private ProgressDialog progress_dialog;
	private MainActivity activity;

	String url;
	JSONObject json;

	public RESTAPI(MainActivity activity) {
		super();
		this.activity = activity;
		this.context = this.activity.getApplicationContext();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progress_dialog = ProgressDialog.show(this.activity,
				activity.getString(R.string.str_search),
				activity.getString(R.string.str_search), true, false);
	}

	@Override
	protected String doInBackground(String... params) {

		try {
			String result = DBContract.downloadedJSON(params);
			return result;
		} catch (Exception e) {
			return new String();
		}
	}

	@Override
	protected void onPostExecute(String result) {

		clearItems();
		formatJSONdata(result);
		progress_dialog.dismiss();
		ContentResolver cr = this.activity.getContentResolver();

		String w = JSONDBProvider.KEY_TYPE + " = 'image'";
		String[] projection = new String[] { JSONDBProvider.KEY_DATA_ID,
				JSONDBProvider.KEY_IMAGE };

		Cursor query = cr.query(JSONDBProvider.CONTENT_URI, projection, w,
				null, null);

		if (query.getCount() > 0) {
			query.moveToFirst();
			while (query.isAfterLast() == false) {
				WebImage retImage = new WebImage();
				Bitmap bitmp = null;

				try {
					bitmp = null;
					bitmp = retImage
							.execute(
									query.getString(query
											.getColumnIndex(JSONDBProvider.KEY_IMAGE)))
							.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				String tempID = null;

				if (bitmp == null) {
//					Log.e("FuzzAPI",
//							"Checking Images: "
//									+ query.getString(query
//											.getColumnIndex(JSONDataProvider.KEY_IMAGE)));
					tempID = query.getString(query
							.getColumnIndex(JSONDBProvider.KEY_DATA_ID));

					UpdateImages(tempID);
				}
				query.moveToNext();
			}
			query.close();
		}
	}

	private void clearItems() {
		ContentResolver cr = this.activity.getContentResolver();
		cr.delete(JSONDBProvider.CONTENT_URI_CLEAR, null, null);

	}

	private void addNewItem(Jason quake) {
		ContentResolver cr = this.activity.getContentResolver();

		String w = JSONDBProvider.KEY_DATA_ID + " = " + quake.getid();

		Cursor query = cr.query(JSONDBProvider.CONTENT_URI, null, w, null,
				null);

		if (query.getCount() == 0) {
			ContentValues values = new ContentValues();

			values.put(JSONDBProvider.KEY_DATA_ID, quake.getid());
			values.put(JSONDBProvider.KEY_TYPE, quake.gettype());
			values.put(JSONDBProvider.KEY_TEXT, quake.gettext());

			values.put(JSONDBProvider.KEY_IMAGE, quake.getimage());
			values.put(JSONDBProvider.KEY_LINK, quake.getLink());

			String strValid = "Y";
			if (quake.gettype().equals("text") && quake.gettext() == null) {
				strValid = "N";
			}

			values.put(JSONDBProvider.KEY_VALID, strValid);
			cr.insert(JSONDBProvider.CONTENT_URI, values);
		}
		query.close();
	}

	public void formatJSONdata(String result) {

		String tempResult = result;
		String fuzzID = "";
		String fuzzType = "";
		String fuzzText = "";
		String fuzzImage = "";
		String fuzzLink = "http://www.google.com";
		String fuzzValid = "Y";

		try {
			final JSONArray jsonrow = new JSONArray(tempResult); // has json
			for (int i = 0; i < jsonrow.length(); i++) {
				fuzzID = null;
				fuzzType = null;
				fuzzText = null;
				fuzzImage = null;
				JSONObject myrow = jsonrow.getJSONObject(i);

				try {
					fuzzID = myrow.getString("id");
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					fuzzType = myrow.getString("type");
				} catch (Exception e) {
				}
				if (fuzzType != null) {
					if (fuzzType.equals("text")) {
						try {
							fuzzText = myrow.getString("data");
						} catch (Exception e) {
						}
					} else if (fuzzType.equals("image")) {
						try {
							fuzzImage = myrow.getString("data");
						} catch (Exception e) {
						}
					} // else {
						// try {
						// linkUrl = myrow.getString("data");
						// Log.i("FuzzAPI image", artistUrl);
						// } catch (Exception e) {
						// }
						// }
				}

				Jason jasonrow = new Jason(fuzzID, fuzzType, fuzzText,
						fuzzImage, fuzzLink, fuzzValid);
				addNewItem(jasonrow);
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	private void UpdateImages(String rowID) {
		ContentResolver cr = this.activity.getContentResolver();

		ContentValues values = new ContentValues();
		String[] updatearg = new String[] { rowID };
		int updcount = 0;
		values.put(JSONDBProvider.KEY_VALID, "N");
		updcount = cr.update(JSONDBProvider.CONTENT_URI_UPDATE, values,
				JSONDBProvider.KEY_DATA_ID + "=?", updatearg);
	}

}
