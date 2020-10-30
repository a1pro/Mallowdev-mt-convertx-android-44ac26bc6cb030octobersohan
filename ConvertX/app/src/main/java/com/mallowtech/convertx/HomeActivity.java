package com.mallowtech.convertx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mallowtech.helper.AppConstants;
import com.mallowtech.helper.DBHelper;
import com.mallowtech.helper.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc

/**
 * The Class HomeActivity.
 */
@SuppressLint("SimpleDateFormat")
public class HomeActivity extends Activity {

    /**
     * The audio image.
     */
    ImageView audioImage;

    /**
     * The shared preferences.
     */
    SharedPreferences sharedPreferences;

    /**
     * The name shared pre.
     */
    String nameSharedPre = "ButtonClickSound";

    /**
     * The editor.
     */
    Editor editor;

    /**
     * The window manager.
     */
    WindowManager windowManager;

    /**
     * The config.
     */
    Configuration config;

    /**
     * The device total size.
     */
    int deviceWidth, deviceHeight, deviceTotalSize;

    /**
     * The convert value.
     */
    ArrayList<String> convertFrom, convertTo, convertValue;

    /**
     * The user info.
     */
    SharedPreferences userInfo;

    /**
     * The database helper.
     */
    DBHelper databaseHelper;

    /**
     * The context.
     */
    Context context;

    /**
     * The list main container.
     */
    RelativeLayout converterLayout, calculatorLayout, favoriteLayout, SocialLayout, listMainContainer;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        context = this.getApplicationContext();

        // initializing variables
        audioImage = (ImageView) findViewById(R.id.audio_imageView);
        converterLayout = (RelativeLayout) findViewById(R.id.converter_layout);
        calculatorLayout = (RelativeLayout) findViewById(R.id.calculator_layout);
        favoriteLayout = (RelativeLayout) findViewById(R.id.favorite_layout);
        SocialLayout = (RelativeLayout) findViewById(R.id.social_layout);
        listMainContainer = (RelativeLayout) findViewById(R.id.list_layout);
        sharedPreferences = getSharedPreferences(nameSharedPre, 0);
        editor = sharedPreferences.edit();
        try {
            databaseHelper = new DBHelper(this, AppConstants.databaseName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseHelper.openDataBase();

        // get shared pref button click boolean value
        if (sharedPreferences.getBoolean("buttonClick", true)) {
            audioImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_audio));
        } else {
            audioImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_audio_mute));
        }

        //get device width and height
        try {
            windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
            config = getResources().getConfiguration();
            deviceWidth = windowManager.getDefaultDisplay().getWidth();
            deviceHeight = windowManager.getDefaultDisplay().getHeight();
            deviceTotalSize = deviceWidth + deviceHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // for fetching currency conversion
        convertFrom = new ArrayList<String>();
        convertTo = new ArrayList<String>();
        convertValue = new ArrayList<String>();
        userInfo = getSharedPreferences("USERINFO", 0);
        String savedTime = userInfo.getString("sessionCreated", "1970-17-03 09:40:39");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //date format conversion
        String currentDateandTime = sdf.format(new Date());
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(savedTime);
            date2 = sdf.parse(currentDateandTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long mills = date2.getTime() - date1.getTime();
        long hours = mills / (60 * 60 * 1000);
        try {
            if (hours >= 8) {
                SharedPreferences.Editor editor = userInfo.edit();
                editor.putString("sessionCreated", currentDateandTime);
                editor.putBoolean("isMainActivityFirstTime", false);
                editor.commit();

                String stringUrl = AppConstants.currencyConversionUrl;

                if (Utilities.getNetworkStatus(context)) {
                    new UpdateFromCurrencyData().execute(stringUrl);
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //load the animation for list
        Animation animation_main;
        animation_main = AnimationUtils.loadAnimation(this, R.anim.move);
        listMainContainer.startAnimation(animation_main);

        //handling  click event for converter,calculator,favorite and social layout
        converterLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                converterLayout.setBackgroundColor(Color.WHITE);
                AppConstants.fromfavoriteScreen = 0;
                Intent converterIntent = new Intent(HomeActivity.this, ConverterActivity.class);
                startActivity(converterIntent);
            }
        });
        converterLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                converterLayout.setBackgroundColor(Color.LTGRAY);
                return false;
            }
        });

        favoriteLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                favoriteLayout.setBackgroundColor(Color.WHITE);
                Intent favoriteIntent = new Intent(HomeActivity.this, FavoriteActivity.class);
                startActivity(favoriteIntent);
            }
        });
        favoriteLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                favoriteLayout.setBackgroundColor(Color.LTGRAY);
                return false;
            }
        });

        calculatorLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                calculatorLayout.setBackgroundColor(Color.WHITE);
                Intent calculatorIntent = new Intent(HomeActivity.this, CalculatorActivity.class);
                startActivity(calculatorIntent);

            }
        });
        calculatorLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                calculatorLayout.setBackgroundColor(Color.LTGRAY);
                return false;
            }
        });

        SocialLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent socialIntent = new Intent(HomeActivity.this, SocialActivity.class);
                startActivity(socialIntent);
            }
        });

        audioImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    if (AppConstants.audioStatus) {
                        audioImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_audio_mute));
                        Toast.makeText(HomeActivity.this, AppConstants.btnSoundOff, Toast.LENGTH_SHORT).show();
                        AppConstants.audioStatus = false;
                        AppConstants.buttonClick = false;
                        editor.putBoolean("buttonClick", false);
                        editor.commit();
                    } else {
                        audioImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_audio));
                        Toast.makeText(HomeActivity.this, AppConstants.btnSoundOn, Toast.LENGTH_SHORT).show();
                        AppConstants.audioStatus = true;
                        AppConstants.buttonClick = true;
                        editor.putBoolean("buttonClick", true);
                        editor.commit();
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //    DownloadWebpageTask
    class UpdateFromCurrencyData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... params) {
            URL url;
            //create url object to point to the file location on internet
            try {
                url = new URL(params[0]);
                //make a request to server
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //get InputStream instance
                InputStream is = con.getInputStream();
                //create BufferedReader object
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                //read content of the file line by line
                String currencyValueArray = null;
                try {
                    while ((line = br.readLine()) != null) {
                        currencyValueArray = currencyValueArray + "," + line;
                    }
                    br.close();
                } catch (Exception e) {

                }
                if (currencyValueArray != null) {
                    List<String> tempValues = Arrays.asList(currencyValueArray.split(","));
                    for (int i = 1; i < tempValues.size(); i++) {
                        convertFrom.add(tempValues.get(i++));
                        convertTo.add(tempValues.get(i++));
                        convertValue.add(tempValues.get(i));
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                databaseHelper.deleteCurrencyEntries();
                databaseHelper.insertCurrencyEntries(convertFrom, convertTo, convertValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public void onBackPressed() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
            alertDialogBuilder.setTitle(AppConstants.alertTitle);// Set Title
            String message = AppConstants.alertMessage;
            alertDialogBuilder.setMessage(message).setCancelable(false);
            alertDialogBuilder.setPositiveButton(AppConstants.okMessage, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            alertDialogBuilder.setNegativeButton(AppConstants.cancelMessage, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();// Cancel the dialog box
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show(); // Show the dialog
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
