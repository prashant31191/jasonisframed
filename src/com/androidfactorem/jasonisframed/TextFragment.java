package com.androidfactorem.jasonisframed;







import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class TextFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	OnTextFragmentSelectedListener mCallback;

	private static String str_where = JSONDBProvider.KEY_TYPE
			+ " = 'text' and " + JSONDBProvider.KEY_VALID + " = 'Y'";

	public interface OnTextFragmentSelectedListener {
		public void onTextItemClickSelected();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnTextFragmentSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTextFragmentSelectedListener");
		}

	}

	SimpleCursorAdapter adapter;
	Activity activity;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		String[] columns = new String[] { JSONDBProvider.KEY_DATA_ID,
				JSONDBProvider.KEY_TEXT };
		int[] viewfields = new int[] { R.id.fuzz_id, R.id.fuzz_text };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
				R.layout.textrow, null, columns, viewfields);

		setListAdapter(adapter);
		
		getListView().setDivider(new ColorDrawable(R.color.black));
		getListView().setDividerHeight(1);

		getListView().setOnItemClickListener(myOnItemClickListener);
		getLoaderManager().initLoader(0, null, this);

	}

	private OnItemClickListener myOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			mCallback.onTextItemClickSelected();

		}
	};

	private class CustomViewBinder implements ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

			if (columnIndex == cursor
					.getColumnIndex(JSONDBProvider.KEY_IMAGE)
					&& (cursor.getString(cursor
							.getColumnIndex(JSONDBProvider.KEY_TYPE)))
							.equals("image")) {
				int is_text = cursor.getColumnIndex(JSONDBProvider.KEY_IMAGE);
				if (is_text != 0) {
					// Set left side icon
					ImageView fuzz_graphic = (ImageView) view
							.findViewById(R.id.fuzz_graphic);
					if (cursor.getString(cursor
							.getColumnIndex(JSONDBProvider.KEY_IMAGE)) != null) {
						WebImage retImage = new WebImage();
						Bitmap bitmp = null;

						try {
							bitmp = retImage
									.execute(
											cursor.getString(cursor
													.getColumnIndex(JSONDBProvider.KEY_IMAGE)))
									.get();
						} catch (Exception e) {
							Log.e("ImageLoader", e.getMessage());
						}
						fuzz_graphic.setImageBitmap(bitmp);
					} else {
						fuzz_graphic.setImageResource(R.drawable.no_icon);
					}

					fuzz_graphic.setFocusable(false);

					view.setVisibility(View.VISIBLE);
					fuzz_graphic.setFocusable(false);
					fuzz_graphic.setFocusableInTouchMode(false);
					return true;
				}
			} else {
				return false;
			}
			return false;

		}

	}

	Handler handler = new Handler();

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = new String[] { JSONDBProvider.KEY_ID,
				JSONDBProvider.KEY_DATA_ID, JSONDBProvider.KEY_TYPE,
				JSONDBProvider.KEY_TEXT };

		String[] argz = new String[] { "'text'" };

		CursorLoader loader = new CursorLoader(getActivity(),
				JSONDBProvider.CONTENT_URI, projection, str_where, null, null);

		return loader;

	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		((SimpleCursorAdapter) this.getListAdapter()).swapCursor(cursor);

	}

	public void onLoaderReset(Loader<Cursor> loader) {
		((SimpleCursorAdapter) this.getListAdapter()).swapCursor(null);
	}

}
