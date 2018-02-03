package com.digywood.cineauditions;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Fragments.ItemsFragment;
import com.digywood.cineauditions.Fragments.ListOfAdvtsFragment;
import com.digywood.cineauditions.Fragments.SetPreferencesFragment ;
import com.digywood.cineauditions.Pojo.SingleProducer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LandingActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private String[] activityTitles;
    EditText name_item,price_item,description_item;
    String phno,key=null;
    private Toolbar toolbar;
    TextView userName, userEmail;
    Typeface myTypeface1;
    SingleProducer user;
    private boolean shouldLoadHomeFragOnBackPress = true;
    boolean openF2;
    private ItemsFragment itemsFragment;
    DBHelper dbHelper;
    SetPreferencesFragment obj = new SetPreferencesFragment();
    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_NOTICE_LIST = "notice list";
    private static final String TAG_POST_ADVT = "post advertisement";
    private static final String TAG_PUBLISH = "publish";
    private static final String TAG_ORDERS = "orders";
    private static final String TAG_ORDERS_DATA = "orders data";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_NOTICE_LIST;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        Intent cmgintent = getIntent();
        if(cmgintent!=null){

            phno=cmgintent.getStringExtra("mobileNo");
            key = cmgintent.getStringExtra("key");

        }

        if(key!=null){

            if(key.equalsIgnoreCase("F1")){
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.frame, new ItemsFragment());
                tx.commit();
            }else if(key.equalsIgnoreCase("F2")){
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.frame, new ListOfAdvtsFragment());
                tx.commit();
            }else{
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.frame, new ItemsFragment());
                tx.commit();
            }

        }else{
            Log.e("LandingActivity----","Key is Null");
        }

        dbHelper = new DBHelper(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        userName = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_userName);
        userEmail = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_userEmail);

        user= dbHelper.getProducer(phno);
        if(user!=null){

            Log.e("Landing--->", user.getName());
            userName.setText(user.getName());
            userName.setTypeface(myTypeface1);
            userEmail.setText(user.getEmailId());

        }else{
            Log.e("LandingActivity-----","Empty User");
        }

        setUpNavigationView();

//        if (savedInstanceState == null) {
//            navItemIndex = 0;
//            CURRENT_TAG = TAG_NOTICE_LIST;
//            loadHomeFragment();
//        }
    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
//            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
//        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_NOTICE_LIST;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }



    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                ItemsFragment itemsFragment = new ItemsFragment();
                return itemsFragment;
//                AdvtInfoFragment advtInfoFragment = new AdvtInfoFragment();
//                return advtInfoFragment;
            case 1:

                ListOfAdvtsFragment listOfAdvtsFragment = new ListOfAdvtsFragment();
                return listOfAdvtsFragment;
            case 2:

                SetPreferencesFragment setPreferencesFragment = new SetPreferencesFragment();
                return setPreferencesFragment;
            case 3:
/*                OrdersFragment ordersFragment = new OrdersFragment();
                return ordersFragment;*/
//
//            case 4:
//
//                OrdersdataActivity ordersdataActivity = new OrdersdataActivity();
//                return ordersdataActivity;
//
//            case 5:
//
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
            default:
                return new ItemsFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent com.example.madhu.jcb_admin.activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, FullscreenActivity.class);
            dbHelper.deleteProducer(phno);
            return true;
        }
        if (id == R.id.action_refresh){
            syncData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected (MenuItem menuItem){
                // Handle navigation view item clicks here.
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_items:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_NOTICE_LIST;
                        break;
                    case R.id.nav_dev_points:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_POST_ADVT;
                        break;
                    case R.id.nav_publish:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PUBLISH;
                        break;
//                    case R.id.nav_orders_data:
//                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_ORDERS_DATA;
//                        break;
//                    case R.id.nav_settings:
//                        navItemIndex = 5;
//                        CURRENT_TAG = TAG_SETTINGS;
//                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    private void toggleFab() {
        if (navItemIndex == 0) {//    fab.show();
        }else{}
//            fab.hide();
    }

    public void syncData(){

        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("userId",phno);
        new BagroundTask(URLClass.hosturl+"getAllData.php", hmap, LandingActivity.this, new IBagroundListener() {
            @Override
            public void bagroundData(String json) throws JSONException {

                JSONArray ja_preferences_table,ja_advt_info_table,ja_user_interests;
                JSONObject prefjo,advtjo,interestjo;
                try{
                    JSONObject myObj=new JSONObject(json);

                    long prefdelcount=dbHelper.deleteAllPreferences();
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


                    long advtdelcount=dbHelper.deleteAllAdvts();
                    Log.e("advtdelcount---",""+advtdelcount);

                    Object obj1=myObj.get("advt_info_table");

                    if (obj1 instanceof JSONArray)
                    {
                        ja_advt_info_table=myObj.getJSONArray("advt_info_table");
                        if(ja_advt_info_table.length()>0){

                            Log.e("advtLength---",""+ja_advt_info_table.length());
                            int p=0,q=0;
                            for(int i=0;i<ja_advt_info_table.length();i++){

                                advtjo=ja_advt_info_table.getJSONObject(i);

//                                String image=advtjo.getString("image");
//                                byte[] imgbyte = Base64.decode(image, Base64.DEFAULT);

                                long insertFlag=dbHelper.insertNewAdvt(advtjo.getInt("advtId"),advtjo.getString("orgId"),advtjo.getString("userId"),advtjo.getString("caption"),advtjo.getString("description"),advtjo.getString("startDate"),advtjo.getString("endDate"),advtjo.getString("contactName"),advtjo.getString("contactNumber"),advtjo.getString("emailId"),advtjo.getString("createdTime"),advtjo.getString("status"));
                                if(insertFlag>0){
                                    p++;
                                }else{
                                    q++;
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p);
                        }else{
                            Log.e("BackGroundTask--","EmptyJsonArray ");
                        }
                    }
                    else {
                        Log.e("pref--","No Advt: ");
                    }

                    long interestdelcount=dbHelper.deleteAllInterests();
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

                                long insertFlag=dbHelper.insertInterest(interestjo.getInt("seqId"),interestjo.getString("userId"),interestjo.getInt("advtId"),interestjo.getString("description"),interestjo.getString("flag"));                                if(insertFlag>0){
                                    p++;
                                }else{
                                    q++;
                                }
                            }
                            Log.e("BackGroundTask--","Inserted: "+p);
                        }else{
                            Log.e("BackGroundTask--","EmptyJsonArray ");
                        }
                    }
                    else {
                        Log.e("interest--","No Interests");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("BackGround368--",e.toString());
                }

            }
        }).execute();
    }
}


