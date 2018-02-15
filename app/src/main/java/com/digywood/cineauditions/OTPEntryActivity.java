package com.digywood.cineauditions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class OTPEntryActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */

    public TextView app_title,tv_verifytitle,tv_phno,timer,skip,tv1_phno;
    Typeface myTypeface1,myTypeface2,myTypeface3,myTypeface4;
    Button otp_submit,otp_cancel;
    EditText et1_otp;
    DBHelper dbHelper;

    String otpnumber,otpNo,regDate,url,url1,gpsAddress,EmailId1,Password1,MobileNo;
    JSONObject jo;
    private AwesomeValidation awesomeValidation;

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }

        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpentry);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/MontserratAlternates-Medium.ttf");
        myTypeface2 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");


        app_title = (TextView)findViewById(R.id.appName);
        otp_submit = (Button)findViewById(R.id.otp_submit);
        otp_cancel = (Button)findViewById(R.id.otp_cancel);
        tv_verifytitle = (TextView)findViewById(R.id.tv_verifytitle);
        tv_phno = (TextView)findViewById(R.id.tv_phno);
        tv1_phno = (TextView)findViewById(R.id.tv1_phno);
        timer = (TextView) findViewById(R.id.timer);
        skip = (TextView) findViewById(R.id.skip);
        et1_otp = (EditText)findViewById(R.id.et1_otp);
        dbHelper = new DBHelper(this);

        app_title.setTypeface(myTypeface1);
        otp_submit.setTypeface(myTypeface2);
        otp_cancel.setTypeface(myTypeface2);
        tv_verifytitle.setTypeface(myTypeface2);
        tv1_phno.setTypeface(myTypeface2);
        et1_otp.setTypeface(myTypeface2);
        tv_phno.setTypeface(myTypeface2);
        timer.setTypeface(myTypeface2);
        skip.setTypeface(myTypeface2);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            MobileNo = bundle.getString("mobileNo");
            tv1_phno.setText(MobileNo);
        }


        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("00: " + millisUntilFinished / 1000 +" ");
            }

            public void onFinish() {
                //timer.setText("done!");
                timer.setVisibility(View.INVISIBLE);
                skip.setVisibility(View.VISIBLE);
                skip.setText(Html.fromHtml("<u>Skip & Continue</u> "));
                skip.setTypeface(myTypeface1);
            }
        }.start();

        otp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsyncCheckInternet(OTPEntryActivity.this, new INetStatus() {
                    @Override
                    public void inetSatus(Boolean netStatus) {
                        if(netStatus){
                            otpnumber = et1_otp.getText().toString();
                            HashMap<String, String> hmap = new HashMap<String, String>();
                            hmap.put("phno", MobileNo);

                            url = URLClass.hosturl+"checkProducerExist.php";

                            BagroundTask task1 = new BagroundTask(url, hmap, OTPEntryActivity.this, new IBagroundListener() {

                                @Override
                                public void bagroundData(String json) {
                                    try {
                                        Log.d("ja", "comes:" + json);
                                        if (json.equals("User_Not_Exist")) {
                                            Toast.makeText(OTPEntryActivity.this, "UserId does not exist", Toast.LENGTH_LONG).show();
                                        } else {
                                            //Toast.makeText(FullscreenActivity.this, "Order Insertion failed", Toast.LENGTH_SHORT).show();}
                                            JSONArray ja = new JSONArray(json);
                                            Log.d("ja", "comes:" + ja);
                                            if (ja.length() != 0) {
                                                jo = null;
                                                for (int j = 0; j < ja.length(); j++) {
                                                    try {
                                                        jo = ja.getJSONObject(j);
                                                        Log.d("OTPEntry--->", "ServerOTP:" + jo.getString("otpNo")+":::Localotpnumber:"+otpnumber);
                                                        otpNo = jo.getString("otpNo");
                                                        if (otpnumber.equals(otpNo)) {

                                                            Calendar c1 = Calendar.getInstance();
                                                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                            regDate = sdf1.format(c1.getTime());

                                                            long updateFlag = 0;

                                                            //Toast.makeText(OTPEntry.this, "status update success ", Toast.LENGTH_LONG).show();
                                                            HashMap<String, String> hmap = new HashMap<String, String>();

                                                            Calendar c2 = Calendar.getInstance();
                                                            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                            regDate = sdf2.format(c2.getTime());

                                                            hmap.put("phno", MobileNo);
                                                            hmap.put("otpNo", otpNo);
                                                            hmap.put("status", "verified");
                                                            hmap.put("dateOfRegistration", regDate);

                                                            url = URLClass.hosturl+"updateProducerDetails.php";

                                                            final String str = "";
                                                            try {
                                                                BagroundTask task = new BagroundTask(url,hmap,OTPEntryActivity.this, new IBagroundListener() {
                                                                    @Override
                                                                    public void bagroundData(String json) {
                                                                        Log.d("OTP", "comes:" + json);
                                                                        if (json.equals("Updated")) {
                                                                            dbHelper.updateProducer(MobileNo,otpNo,"verified",regDate);
                                                                            Intent intent = new Intent(OTPEntryActivity.this, FullscreenActivity.class);
                                                                            intent.putExtra("mobileNo",MobileNo);
                                                                            startActivity(intent);
                                                                        }
                                                                        else {
                                                                            Toast.makeText(OTPEntryActivity.this, "OTP Status updation failed", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                                task.execute();
                                                                Log.v("jo", str);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        //Toast.makeText(OTPEntry.this, "OTP Successfull", Toast.LENGTH_SHORT).show();

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                            task1.execute();
                        }else{
                            Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).execute();
            }
        });

        otp_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OTPEntryActivity.this, FullscreenActivity.class);
                startActivity(intent);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(OTPEntryActivity.this, ListOfAdvts.class);
//                intent.putExtra("mobileNo", MobileNo);
//                startActivity(intent);

                new AsyncCheckInternet(OTPEntryActivity.this,new INetStatus() {
                    @Override
                    public void inetSatus(Boolean netStatus) {
                        if(netStatus){
                            otpnumber = et1_otp.getText().toString();
                            HashMap<String, String> hmap = new HashMap<String, String>();
                            hmap.put("phno", MobileNo);

                            url = URLClass.hosturl+"checkProducerExist.php";

                            BagroundTask task1 = new BagroundTask(url, hmap, OTPEntryActivity.this, new IBagroundListener() {

                                @Override
                                public void bagroundData(String json) {
                                    try {
                                        Log.d("ja", "comes:" + json);
                                        if (json.equals("User_Not_Exist")) {
                                            Toast.makeText(OTPEntryActivity.this, "UserId is not exist", Toast.LENGTH_LONG).show();
                                        } else {
                                            //Toast.makeText(FullscreenActivity.this, "Order Insertion failed", Toast.LENGTH_SHORT).show();}
                                            JSONArray ja = new JSONArray(json);
                                            Log.d("ja", "comes:" + ja);
                                            if (ja.length() != 0) {
                                                jo = null;
                                                for (int j = 0; j < ja.length(); j++) {
                                                    try {
                                                        jo = ja.getJSONObject(j);
                                                        Log.d("ja", "comes:" + jo.getString("otpNo")+":::otpnumber:"+otpnumber);
                                                        otpNo = jo.getString("otpNo");
                                                        //if (otpnumber.equals(otpNo)) {

                                                        Calendar c1 = Calendar.getInstance();
                                                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                        regDate = sdf1.format(c1.getTime());

                                                        long updateFlag = 0;
                                                        updateFlag = dbHelper.updateProducer(MobileNo,otpNo,"skipped",regDate);
                                                        if (updateFlag != 1) {
                                                            //Toast.makeText(OTPEntry.this, "status update failed ", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            //Toast.makeText(OTPEntry.this, "status update success ", Toast.LENGTH_LONG).show();
                                                            HashMap<String, String> hmap = new HashMap<String, String>();

                                                            Calendar c2 = Calendar.getInstance();
                                                            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                                            regDate = sdf2.format(c2.getTime());

                                                            hmap.put("phno", MobileNo);
                                                            hmap.put("otpNo", otpNo);
                                                            hmap.put("status", "skipped");
                                                            hmap.put("dateOfRegistration", regDate);

                                                            url = URLClass.hosturl+"updateProducerDetails.php";

                                                            final String str = "";
                                                            try {
                                                                BagroundTask task = new BagroundTask(url, hmap, OTPEntryActivity.this, new IBagroundListener() {
                                                                    @Override
                                                                    public void bagroundData(String json) {
                                                                        Log.d("ja", "comes:" + json);
                                                                        if (json.equals("Updated")) {
                                                                            //Toast.makeText(OTPEntry.this, "OTP Updated Successfull", Toast.LENGTH_LONG).show();
                                                                        }
                                                                        else {
                                                                            //Toast.makeText(OTPEntry.this, "OTP updation failed", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                                task.execute();
                                                                Log.v("jo", str);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        //Toast.makeText(OTPEntry.this, "OTP Successfull", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(OTPEntryActivity.this,FullscreenActivity.class);
                                                        intent.putExtra("mobileNo", MobileNo);
                                                        startActivity(intent);
                                                        //}else {
                                                        // Toast.makeText(OTPEntry.this, "OTP not Successfull", Toast.LENGTH_SHORT).show();
                                                        //}

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                            task1.execute();
                        }else{
                            Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).execute();

            }
        });


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
