package com.mallowtech.helper;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utilities {
	static MediaPlayer mediaPlayer;
	static SharedPreferences sharedPreferences;
	static String nameSharedPre = "ButtonClickSound";

	public static void buttonClickSound(Context context) {
		sharedPreferences = context.getSharedPreferences(nameSharedPre, 0);
		if (sharedPreferences.getBoolean("buttonClick", true)) {
			try {
				AssetFileDescriptor descriptor = context.getAssets().openFd("Tock.mp3");
				long start = descriptor.getStartOffset();
				long end = descriptor.getLength();
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
				mediaPlayer.prepare();
				mediaPlayer.start();

				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mp) {
						mp.release();

					};
				});
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Context getDialogContext(Activity act) {
		Context context = null;
		try {
			if (act.getParent() != null)
				context = act.getParent();
			else
				context = act;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return context;
	}

	public static boolean getNetworkStatus(Context con) {
		boolean status = false;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public static void callAlert(Context context) {
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder.setTitle("Error");
			alertDialogBuilder.setMessage("Could not connect to server. Please check network connection.").setCancelable(false).setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {

					dialog.cancel();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
