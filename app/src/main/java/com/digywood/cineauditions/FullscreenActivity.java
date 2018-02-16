package com.digywood.cineauditions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.AsyncTasks.MyBagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleAdvt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FullscreenActivity extends AppCompatActivity {
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

    public TextView appName,reg_link;
    EditText phno;
    Typeface myTypeface1,myTypeface2;
    Button login;
    long count=0;
    CheckBox cb_remember;
    DBHelper dbHelper;
    SharedPreferences mypreferences,myretrievepreferences;
    SharedPreferences.Editor editor=null;
    String email,pasword,uname,url,url1,gpsAddress,EmailId1,Password1,MobileNo;
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
   /* private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };/*
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                //delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        myTypeface2 = Typeface.createFromAsset(getAssets(), "fonts/MontserratAlternates-Medium.ttf");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.et_mobileNo, "^([7-9]{1})([0-9]{9})$", R.string.mobileerror);

        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);
        //app_title=(TextView)findViewById(R.id.app_title);
        login = (Button)findViewById(R.id.btn_submit);
        appName = (TextView)findViewById(R.id.appName);
        reg_link=(TextView)findViewById(R.id.reglink_tv);
        cb_remember = (CheckBox)findViewById(R.id.cb_remember);
        phno = (EditText)findViewById(R.id.et_mobileNo);
        dbHelper = new DBHelper(this);

        mypreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = mypreferences.edit();
        editor.putInt("appkey",1);
        editor.commit();

        myretrievepreferences= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mypreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = mypreferences.edit();
        phno.setText(myretrievepreferences.getString("username",""));



        MobileNo = phno.getText().toString();

        //app_title.setTypeface(myTypeface1);
        login.setTypeface(myTypeface1);
        appName.setTypeface(myTypeface2);
        cb_remember.setTypeface(myTypeface1);

        reg_link.setText(Html.fromHtml("<u>SignUp Here</u> "));
        reg_link.setTypeface(myTypeface1);

        reg_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullscreenActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                FullscreenActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (awesomeValidation.validate()) {

                    if(cb_remember.isChecked()){

                        editor.putString("username",phno.getText().toString());
//            editor.putString("password",et2.getText().toString());
                        editor.commit();
                        cb_remember.setChecked(false);

                    }

                    new AsyncCheckInternet(FullscreenActivity.this, new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus){
                                MobileNo = phno.getText().toString();
                                int checkFlag = 0;
                                checkFlag =dbHelper.checkProducerExists(MobileNo);
                                if (checkFlag == 1) {
                                    //Toast.makeText(MainFullscreenActivity.this, "Already Exist", Toast.LENGTH_SHORT).show();
                                    int checkOTPFlag = 0;
                                    String st = dbHelper.getOTPStatus(MobileNo);
                                    if (st.equals("verified") || st.equals("skipped")) {
                                        count=dbHelper.checkCategoryExists();
                                        if(count>0){
                                            Intent intent = new Intent(FullscreenActivity.this,LandingActivity.class);
                                            intent.putExtra("mobileNo", MobileNo);
                                            intent.putExtra("key", "F1");
                                            startActivity(intent);
                                        }else{
                                            syncData(MobileNo);
                                        }
                                    } else {
                                        Toast.makeText(FullscreenActivity.this, "User Does not exist", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    MobileNo = phno.getText().toString();
                                    HashMap<String, String> hmap1 = new HashMap<>();
                                    hmap1.put("phno", MobileNo);

                                    url = URLClass.hosturl+"checkProducerExist.php";

                                    new BagroundTask(url,hmap1,FullscreenActivity.this,new IBagroundListener() {

                                        @Override
                                        public void bagroundData(String json) {
                                            try {
                                                Log.e("ja", "comes:" + json);
                                                if (json.equals("User_Not_Exist")) {

                                                    Toast.makeText(FullscreenActivity.this, "User_Not_Exist", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    JSONArray ja = new JSONArray(json);
                                                    for(int i =0; i< ja.length();i++) {
                                                        JSONObject jo = ja.getJSONObject(i);
                                                        long insertFlag=dbHelper.insertNewProducer(jo.getString("producer_name"), jo.getString("address"), jo.getString("city"), jo.getString("state"), jo.getString("contact_person"),
                                                                jo.getString("phno"), jo.getString("emailId"), jo.getString("otpNo"), jo.getString("dateofRegistration"), jo.getString("status"));
                                                        if(insertFlag>0){
                                                            Toast.makeText(getApplicationContext(),"Interested",Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"Not Interested",Toast.LENGTH_SHORT).show();
                                                        }

                                                    }

                                                    syncData(MobileNo);

//                                                    count=dbHelper.checkCategoryExists();
//                                                    if(count>0){
//                                                        Intent intent = new Intent(FullscreenActivity.this,LandingActivity.class);
//                                                        intent.putExtra("mobileNo", MobileNo);
//                                                        intent.putExtra("key", "F1");
//                                                        startActivity(intent);
//                                                    }else{
//                                                        syncData(MobileNo);
//                                                    }
                                                }
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    }).execute();

                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).execute();
                }
            }
        });

    }
    public boolean isInternetConnected() {
        boolean iNetFlag = false;
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            iNetFlag = (connection.getResponseCode() == 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iNetFlag;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //delayedHide(100);
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

   public void syncData(String number){

       HashMap<String,String> hmap=new HashMap<>();
       Log.e("FullScreen---",""+number);
       hmap.put("userId",number);
       new BagroundTask(URLClass.hosturl+"getAllData.php", hmap, FullscreenActivity.this,new IBagroundListener() {
           @Override
           public void bagroundData(String json) throws JSONException {

               JSONArray ja_category_table,ja_sub_category_table,ja_preferences_table,ja_advt_info_table,ja_user_interests,ja_interestedaads;
               JSONObject catjo,subcatjo,prefjo,advtjo,interestjo,interestedadsjo;
               try{
                   JSONObject myObj=new JSONObject(json);

                   long catdelcount=dbHelper.deleteAllCategories();
                   Log.e("catdelcount---",""+catdelcount);

                   ja_category_table=myObj.getJSONArray("category_table");
                   if(ja_category_table!=null){
                       Log.e("catLength---",""+ja_category_table.length());
                       int p=0,q=0;
                       for(int i=0;i<ja_category_table.length();i++){

                           catjo=ja_category_table.getJSONObject(i);
                           long insertFlag=dbHelper.insertNewCategory(catjo.getString("orgId"),catjo.getString("categoryId"),
                                   catjo.getString("longName"),catjo.getString("shortName"),
                                   catjo.getString("createdBy"),catjo.getString("createdDate"),
                                   catjo.getString("modifiedBy"),catjo.getString("modifiedDate"),
                                   catjo.getString("status"));

                           if(insertFlag>0){
                               p++;
                           }else{
                               q++;
                           }

                       }
                       Log.e("BackGroundTask--","Inserted: "+p);
                   }

                   long subcatdelcount=dbHelper.deleteAllSubCategories();
                   Log.e("subcatdelcount---",""+subcatdelcount);

                   ja_sub_category_table=myObj.getJSONArray("sub_category_table");
                   if(ja_sub_category_table!=null){
                       Log.e("subcatLength---",""+ja_sub_category_table.length());
                       int p=0,q=0;
                       for(int i=0;i<ja_sub_category_table.length();i++){

                           subcatjo=ja_sub_category_table.getJSONObject(i);
                           long insertFlag=dbHelper.insertNewSubCategory(subcatjo.getString("orgId"),subcatjo.getString("categoryId"),subcatjo.getString("subCategoryId"),
                                   subcatjo.getString("longName"),subcatjo.getString("shortName"),
                                   subcatjo.getString("createdBy"),subcatjo.getString("createdDate"),
                                   subcatjo.getString("modifiedBy"),subcatjo.getString("modifiedDate"),
                                   subcatjo.getString("status"));

                           if(insertFlag>0){
                               p++;
                           }else{
                               q++;
                           }

                       }
                       Log.e("BackGroundTask--","Inserted: "+p);

                   }

                   long prefdelcount=dbHelper.deleteAllPreferences(MobileNo);
                   Log.e("prefdelcount---",""+prefdelcount);

                   Object obj=myObj.get("preferences_table");

                   if (obj instanceof JSONArray)
                   {
                       ja_preferences_table=myObj.getJSONArray("preferences_table");
                       if(ja_preferences_table!=null && ja_preferences_table.length()>0){
                           Log.e("prefLength---",""+ja_preferences_table.length());
                           int p=0,q=0;
                           for(int i=0;i<ja_preferences_table.length();i++){

                               prefjo=ja_preferences_table.getJSONObject(i);
                               long insertFlag=dbHelper.insertPref(prefjo.getString("orgId"),prefjo.getString("userId"),prefjo.getString("category"),prefjo.getString("subCategory"),prefjo.getString("status"));
                               if(insertFlag>0){
                                   p++;
                               }else {
                                   q++;
                               }
                           }
                           Log.e("BackGroundTask--","Inserted: "+p);
                       }else{
                           Log.e("pref--","Empty Json Array: ");
                       }
                   }
                   else {
                       Log.e("pref--","No Preferences: ");
                   }


                   long interestdelcount=dbHelper.deleteAllInterests(MobileNo);
                   Log.e("interestdelcount---",""+interestdelcount);

                   Object obj2=myObj.get("user_intrests");

                   if (obj2 instanceof JSONArray)
                   {
                       ja_user_interests=myObj.getJSONArray("user_intrests");
                       if(ja_user_interests.length()>0){

                           Log.e("interestLength---",""+ja_user_interests.length());
                           int p=0,q=0;
                           for(int i=0;i<ja_user_interests.length();i++){

                               interestjo=ja_user_interests.getJSONObject(i);
                               long insertFlag=dbHelper.insertInterest(interestjo.getInt("seqId"),interestjo.getString("userId"),interestjo.getInt("advtId"),interestjo.getString("description"),interestjo.getString("flag"));
                               if(insertFlag>0){
                                   p++;
                               }else{
                                   q++;
                               }
                           }
                           Log.e("BackGroundTask--","Inserted: "+p);
                       }else{
                           Log.e("BackGroundTask--","EmptyJsonArray");
                       }
                   }
                   else {
                       Log.e("interest--","No Interests");
                   }


                   Intent intent = new Intent(FullscreenActivity.this,LandingActivity.class);
                   intent.putExtra("mobileNo", MobileNo);
                   intent.putExtra("key", "F1");
                   startActivity(intent);

               }catch (Exception e){
                   e.printStackTrace();
                   Log.e("BackGround--",e.toString());
               }

           }
       }).execute();
   }
}
