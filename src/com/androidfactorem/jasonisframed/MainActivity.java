package com.androidfactorem.jasonisframed;



import com.androidfactorem.jasonisframed.ImageFragment.OnImageFragmentSelectedListener;
import com.androidfactorem.jasonisframed.TextFragment.OnTextFragmentSelectedListener;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements
		AllFragment.AllFragmentSelectedListener,
		OnTextFragmentSelectedListener, OnImageFragmentSelectedListener,
		TabListener {
	RelativeLayout rl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_bar_main);
		requestJSONdata();
		try {
			rl = (RelativeLayout) findViewById(R.id.mainLayout);
			fragTrans = getFragmentManager().beginTransaction();
			ActionBar bar = getActionBar();
			bar.addTab(bar.newTab().setText("ALL").setTabListener(this));
			bar.addTab(bar.newTab().setText("IMAGES").setTabListener(this));
			bar.addTab(bar.newTab().setText("TEXT").setTabListener(this));

			bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
					| ActionBar.DISPLAY_USE_LOGO);
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			bar.setDisplayShowHomeEnabled(true);
			bar.setDisplayShowTitleEnabled(false);
			bar.show();

		} catch (Exception e) {
			e.getMessage();
		}

	}

	FragmentTransaction fragTrans = null;
	AllFragment frag1;
	ImageFragment frag2;
	TextFragment frag3;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_action_bar_main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		if (tab.getText().equals("ALL")) {
			try {
				rl.removeAllViews();
			} catch (Exception e) {
			}
			frag1 = new AllFragment();
			fragTrans.addToBackStack(null);
			fragTrans = getFragmentManager().beginTransaction();
			fragTrans.add(rl.getId(), frag1);
			fragTrans.commit();
		} else if (tab.getText().equals("IMAGES")) {
			try {
				rl.removeAllViews();
			} catch (Exception e) {
			}
			frag2 = new ImageFragment();
			fragTrans.addToBackStack(null);
			fragTrans = getFragmentManager().beginTransaction();
			fragTrans.add(rl.getId(), frag2);
			fragTrans.commit();
		} else if (tab.getText().equals("TEXT")) {
			try {
				rl.removeAllViews();
			} catch (Exception e) {
			}
			frag3 = new TextFragment();
			fragTrans.addToBackStack(null);
			fragTrans = getFragmentManager().beginTransaction();
			fragTrans.add(rl.getId(), frag3);
			fragTrans.commit();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	public void alert(String toastmsg) {
		Toast.makeText(getApplicationContext(), toastmsg, Toast.LENGTH_LONG)
				.show();
	}

	public void requestJSONdata() {

		RESTAPI fuzzTask = new RESTAPI(MainActivity.this);
		try {

			fuzzTask.execute(""); 
		} catch (Exception e) {
			fuzzTask.cancel(true);
			alert(getResources().getString(R.string.no_fuzz));
		}

	}

	public void onAllItemClickSelected() {
		webIntent();
	}

	@Override
	public void onTextItemClickSelected() {
		webIntent();

	}

	@Override
	public void onImageItemClickSelected() {
		webIntent();

	}

	private void webIntent() {

		Intent intent = new Intent(this, ViewWeb.class);
		intent.putExtra("URL", "http://www.google.com/");
		startActivity(intent);
	}

}
