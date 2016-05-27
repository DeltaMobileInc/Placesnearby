package com.POI.main;

import com.POI.main.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChildDialogue extends Activity {
	private static final String LOG_TAG = ChildDialogue.class.getSimpleName();
	private TextView addBar;
	private ImageButton close;
	private ProgressBar smallBar ;
	private Dialog dialog;
	private WebView webview;
	// private Button btn;
	private Activity Childactivity;

	public ChildDialogue(Activity activity) {
		Childactivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	

	private void openChilDialgoue(String url, boolean isadsUrl) {

		dialog = new Dialog(Childactivity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCancelable(true);
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
		// Building the layout for childView.

		LinearLayout.LayoutParams backParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams FrwdParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
		LinearLayout.LayoutParams closeParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams wvParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		// layout params for progress bar small
		LinearLayout.LayoutParams RefreshParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams FullLoaderParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		// set a main linear layout params
		LinearLayout main = new LinearLayout(Childactivity);
		main.setBackgroundColor(Color.parseColor("#607c8c"));
		main.setOrientation(LinearLayout.VERTICAL);
		// progress bar
	    smallBar = new ProgressBar(
				Childactivity.getApplicationContext(), null,
				android.R.attr.progressBarStyle);
		// smallBar.setPadding(20, 20, 20, 20);
		if (isadsUrl) {
			smallBar.setLayoutParams(FullLoaderParams);
		} else {
			smallBar.setLayoutParams(RefreshParams);
		}

		smallBar.setId(4);
		smallBar.setVisibility(View.GONE);

		// make toolbar
		LinearLayout toolBar = new LinearLayout(Childactivity);
		toolBar.setOrientation(LinearLayout.HORIZONTAL);
		// set back image button
		ImageButton back = new ImageButton(Childactivity);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(LOCATION_SERVICE, "BACK CLICKED");
				if (webview.canGoBack()) {
					webview.goBack();
				}
			}
		});
		try {
			back.setImageResource(R.drawable.icon_arrow_left);
		} catch (Exception e) {
			// TODO: handle exception

		}
		back.setId(1);
		back.setLayoutParams(backParams);
		// set forward image button.
		ImageButton frwd = new ImageButton(Childactivity);
		frwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d(LOG_TAG, "FORWARD BUTTON CLICKED");
				if (webview.canGoForward()) {
					webview.goForward();
				}
			}
		});
		try {
			frwd.setImageResource(R.drawable.icon_arrow_right);
		} catch (Exception e) {
			// TODO: handle exception

		}
		frwd.setId(2);
		frwd.setLayoutParams(FrwdParams);

		// set edit toolbar
		addBar = new TextView(Childactivity);
		addBar.setBackgroundColor(Color.WHITE);
		// addBar.setPadding(0, 10, 0,40);
		addBar.setSingleLine(true);
		// addBar.setGravity(Gravity.BOTTOM);
		addBar.setTextSize(18);
		addBar.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				return false;
			}
		});
		addBar.setId(3);
		addBar.setSingleLine(true);
		addBar.setText(url);
		addBar.setGravity(Gravity.BOTTOM);
		editParams.setMargins(0, 5, 0, 5);
		addBar.setLayoutParams(editParams);
		// set close button
	    close = new ImageButton(Childactivity);
		close.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(LOG_TAG, "CLOSE BUTTON CLICKED");
				closeDialog();
			}
		});
		close.setId(4);
		close.setImageResource(R.drawable.icon_close);
		close.setLayoutParams(closeParams);
		// add controls to toolbar
		toolBar.addView(back);
		toolBar.addView(frwd);
		toolBar.addView(addBar);
		toolBar.addView(close);
		toolBar.addView(smallBar);
		// webview
		webview = new WebView(Childactivity);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(url);
		webview.setId(5);
		webview.setInitialScale(200);
		webview.setLayoutParams(wvParams);
		webview.requestFocus();
		// webview.requestFocusFromTouch();
		webview.setWebViewClient(new ChilWebViewClient());
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		// add toolbar to main view
		if (isadsUrl) {
			webview.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					return true;
				}
			});
			main.addView(webview);
		} else {
			main.addView(toolBar);
			main.addView(webview);
		}
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		dialog.setContentView(main);
		dialog.getWindow().setAttributes(lp);
		dialog.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;
		dialog.show();
	}

	/***
	 * check if url contains http if not append httpl.
	 * 
	 * @param cUrl
	 * @return
	 */
	private String checkUrl(String cUrl) {
		String newLoc;
		if (cUrl.startsWith("http:") || cUrl.startsWith("https:")) {
			newLoc = cUrl;
		} else {

			newLoc = "http://" + cUrl;
			Log.d(LOG_TAG, "appending http to url ..." + newLoc);
		}
		return newLoc;
	}

	/***
	 * open child dialog box
	 * 
	 * @param url
	 */
	public void OpenChildDialogAsWebBrowser(String url) {
		openChilDialgoue(checkUrl(url), false);
	}
	/**
	 * open full ads page
	 * 
	 * @param url
	 */
	public void OpenAdsUrl(String url) {
		openChilDialgoue(checkUrl(url), true);
	}

	/***
	 * close dialog
	 */
	public void closeDialog() {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public class ChilWebViewClient extends WebViewClient {
		private String CatchOldUrl = "";

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			System.out.println("onPageStarted: " + url);
			close.setVisibility(View.GONE);
			smallBar.setVisibility(View.VISIBLE);
			addBar.setText(url);
			
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView webView, String url) {
			System.out.println("shouldOverrideUrlLoading: " + url);
			if (url.contains("about:blank")) {
				
				webView.loadUrl(CatchOldUrl);
				
				
			} else if (url.contains("http") || url.contains("https")) {


				webView.loadUrl(url);
				CatchOldUrl = url;
				addBar.setText(url);
			}
			return true;
		}

		@Override
		public void onPageFinished(WebView webView, String url) {
			smallBar.setVisibility(View.GONE);
			close.setVisibility(View.VISIBLE);
			System.out.println("onPageFinished: " + url);
			
			
		}
		
		@SuppressLint("NewApi")
		@Override
		public void onReceivedLoginRequest(WebView view, String realm,
				String account, String args) {
			// TODO Auto-generated method stub
			super.onReceivedLoginRequest(view, realm, account, args);
			System.out.println("inside the login function");
		}
		
	}
}
