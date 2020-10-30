package com.mallowtech.convertx;

import com.mallowtech.helper.AppConstants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

// TODO: Auto-generated Javadoc
/**
 * The Class SplashScreenActivity.
 */
public class SplashScreenActivity extends Activity {

	/** The context. */
	Context context;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_splash);
			context = this.getApplicationContext();

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}

			}, AppConstants.SPLASH_TIME_OUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
