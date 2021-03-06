package com.digywood.cineauditions;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.AsyncTasks.DownloadFileAsync;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Fragments.FeedbackFragment;
import com.digywood.cineauditions.Fragments.HelpFragment;
import com.digywood.cineauditions.Fragments.InterestsFragment;
import com.digywood.cineauditions.Fragments.ItemsFragment;
import com.digywood.cineauditions.Fragments.ListOfAdvtsFragment;
import com.digywood.cineauditions.Fragments.SetPreferencesFragment ;
import com.digywood.cineauditions.Fragments.SettingsFragment;
import com.digywood.cineauditions.Pojo.SingleProducer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.digywood.cineauditions.AdvtInfoScreen.RequestPermissionCode;

public class LandingActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private String[] activityTitles;
    String phno,key=null;
    private Toolbar toolbar;
    TextView userName, userEmail;
    Typeface myTypeface1;
    SingleProducer user;
    AlertDialog.Builder builder;
    private boolean shouldLoadHomeFragOnBackPress = true;
    DBHelper dbHelper;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_NOTICE_LIST = "Notice List";
    private static final String TAG_POST_ADVT = "Post Advertisement";
    private static final String TAG_PUBLISH = "Publish";
    private static final String TAG_ORDERS_DATA = "Orders Data";
    private static final String TAG_SETTINGS = "Settings";
    private static final String TAG_HELP = "FAQ";
    private static final String TAG_FEEDBACK = "FeedBack";
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
                navItemIndex=0;
            }else if(key.equalsIgnoreCase("F2")){
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.frame, new ListOfAdvtsFragment());
                tx.commit();
                navItemIndex=1;
            }else{
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.frame, new ItemsFragment());
                tx.commit();
                navItemIndex=0;
            }

        }else{
            Log.e("LandingActivity----","Key is Null");
        }

        dbHelper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");
        userName = navigationView.getHeaderView(0).findViewById(R.id.nav_userName);
        userEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_userEmail);

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
                fragmentTransaction.replace(R.id.frame, fragment,CURRENT_TAG);
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

        builder = new AlertDialog.Builder(LandingActivity.this);
        builder.setMessage("Do you want to exit?")
                .setTitle("Exit");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent i=new Intent(getApplicationContext(),FullscreenActivity.class);
                startActivity(i);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setCancelable(false);
        builder.create().show();
//        super.onBackPressed();
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
            case 1:
                ListOfAdvtsFragment listOfAdvtsFragment = new ListOfAdvtsFragment();
                return listOfAdvtsFragment;
            case 2:
                SetPreferencesFragment setPreferencesFragment = new SetPreferencesFragment();
                return setPreferencesFragment;
            case 3:
                InterestsFragment interestsFragment = new InterestsFragment();
                return interestsFragment;
            case 4:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            case 5:
                HelpFragment helpFragment= new HelpFragment();
                return helpFragment;
            case 6:
                FeedbackFragment feedFragment= new FeedbackFragment();
                return feedFragment;
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
        if (id == R.id.action_refresh){

            if(checkPermission()){
                refresh();
            }else{
                requestPermission();
            }
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
                        loadHomeFragment();
                        break;
                    case R.id.nav_dev_points:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_POST_ADVT;
                        loadHomeFragment();
                        break;
                    case R.id.nav_publish:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_PUBLISH;
                        loadHomeFragment();
                        break;
                    case R.id.nav_interestedads:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_ORDERS_DATA;
                        loadHomeFragment();
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        loadHomeFragment();
                        break;
                    case R.id.nav_help:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_HELP;
                        loadHomeFragment();
                        break;
                    case R.id.nav_report:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_FEEDBACK;
                        loadHomeFragment();
                        break;
                    case R.id.nav_logout:
                        builder = new AlertDialog.Builder(LandingActivity.this);
                        builder.setMessage(R.string.dialog_message)
                                .setTitle(R.string.dialog_title);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                Intent in = new Intent(LandingActivity.this,FullscreenActivity.class);
                                startActivity(in);
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                        builder.setCancelable(false);
                        builder.create().show();
                        break;
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

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.openDrawer, R.string.closeDrawer) {

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

    public void refresh(){
        if(navItemIndex==0){
            new AsyncCheckInternet(LandingActivity.this, new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        syncData();
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
        }else if(navItemIndex==1){
            new AsyncCheckInternet(LandingActivity.this, new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        syncOwnAds();
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
        }else if(navItemIndex==2){
            new AsyncCheckInternet(LandingActivity.this, new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        syncData();
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
        }else if(navItemIndex==3){
            new AsyncCheckInternet(LandingActivity.this, new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        syncInterestedAds();
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
        }else{

        }
    }

    public void syncOwnAds(){
        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("userId",phno);
        new BagroundTask(URLClass.hosturl+"getAdsByUser.php", hmap, LandingActivity.this, new IBagroundListener() {
            @Override
            public void bagroundData(String json) throws JSONException {

                JSONArray ja_ownads;
                JSONObject ownadjo;
                ArrayList<String> ownadNames=new ArrayList<>();
                ArrayList<String> finalUrls=new ArrayList<>();
                ArrayList<String> finalNames=new ArrayList<>();
                try{
                    Log.e("OwnAds---","comes: "+json);
                    if(!json.equalsIgnoreCase("NoData")){

                        long ownaddelcount=dbHelper.deleteAllAdvts(phno);
                        Log.e("ownaddelcount---",""+ownaddelcount);

                        ja_ownads=new JSONArray(json);
                        Log.e("OwnAdsLength--",""+ja_ownads.length());
                        int p=0,q=0;
                        for(int i=0;i<ja_ownads.length();i++){

                            ownadjo=ja_ownads.getJSONObject(i);
//                            String url=ownadjo.getString("filePath");
                            String name=ownadjo.getString("fileName");
                            if(!name.equalsIgnoreCase("")){
//                                ownadUrls.add(url);
                                ownadNames.add(name);
                            }else{

                            }
                            long insertFlag=dbHelper.insertNewAdvt(ownadjo.getInt("advtId"),ownadjo.getString("orgId"),ownadjo.getString("userId"),ownadjo.getString("caption"),ownadjo.getString("description"),ownadjo.getString("fileType"),ownadjo.getString("fileName"),ownadjo.getString("filePath"),ownadjo.getString("startDate"),ownadjo.getString("endDate"),ownadjo.getString("contactName"),ownadjo.getString("contactNumber"),ownadjo.getString("emailId"),ownadjo.getString("createdTime"),ownadjo.getString("status"));
                            if(insertFlag>0){
                                p++;
                            }else{
                                q++;
                            }

                        }
                        Log.e("LandingActivity----","Inserted--"+p+" : "+"Not Inserted: "+q);
                    }else{
                        Log.e("LandingActivity----","No Interested Ads");
                    }

                    if(ownadNames.size()!=0){
                        for(int i=0;i<ownadNames.size();i++){

                            File myFile = new File(URLClass.myadspath+ownadNames.get(i));
                            if(myFile.exists()){

                            }else{
                                finalUrls.add(URLClass.imageurl+ownadNames.get(i));
                                finalNames.add(ownadNames.get(i));
                            }

                        }

                    }else{
                        Log.e("LandingActivity----","No Downloaded Images");
                    }

                    if(finalNames.size()!=0){

                        String[] urlList=new String[finalUrls.size()];
                        urlList = finalUrls.toArray(urlList);
                        String[] nameList = new String[finalNames.size()];
                        nameList = finalNames.toArray(nameList);

                        new DownloadFileAsync(LandingActivity.this,URLClass.myadspath,urlList,nameList,new IDownloadStatus() {
                            @Override
                            public void downloadStatus(String status) {

                                try{
                                    if(status.equalsIgnoreCase("Completed")){

                                    }else{

                                    }

                                }catch (Exception e){

                                    e.printStackTrace();
                                    Log.e("DownloadFile----",e.toString());
                                }
                            }
                        }).execute();
                    }else{
                        Log.e("LandingActivity----","All OwnAd Images Downloaded");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("LandingActivity----",e.toString());
                }

                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.frame, new ListOfAdvtsFragment());
                tx.commit();
                navItemIndex=1;
            }
        }).execute();
    }

    public void syncInterestedAds(){

        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("userId",phno);
        new BagroundTask(URLClass.hosturl+"getInterestedAdsofUser.php",hmap,LandingActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) throws JSONException {

                JSONArray ja_interestads;
                JSONObject interestjo;
                ArrayList<String> interestadNames=new ArrayList<>();
                ArrayList<String> interestUrls=new ArrayList<>();
                ArrayList<String> finalNames=new ArrayList<>();

                try{
                    if(!json.equalsIgnoreCase("NoData")){

                        long interestaddelcount=dbHelper.deleteAllInterestedAdvts(phno);
                        Log.e("interestdelcount---",""+interestaddelcount);

                        ja_interestads=new JSONArray(json);
                        Log.e("InterestedAds Json--",""+ja_interestads.toString());
                        Log.e("InterestedAds Length--",""+ja_interestads.length());
                        int p=0,q=0;
                        for(int i=0;i<ja_interestads.length();i++){

                            interestjo=ja_interestads.getJSONObject(i);
                            String name=interestjo.getString("fileName");
                            if(!name.equalsIgnoreCase("")){
                                interestadNames.add(name);
                            }else{

                            }
                            Log.e("LandInterestAdId:---- ",""+interestjo.getInt("advtId"));
                            long insertFlag=dbHelper.insertInterestedAdvt(interestjo.getInt("advtId"),interestjo.getString("orgId"),interestjo.getString("userId"),phno,interestjo.getString("caption"),interestjo.getString("description"),interestjo.getString("fileType"),interestjo.getString("fileName"),interestjo.getString("filePath"),interestjo.getString("startDate"),interestjo.getString("endDate"),interestjo.getString("contactName"),interestjo.getString("contactNumber"),interestjo.getString("emailId"),interestjo.getString("createdTime"),interestjo.getString("status"));
                               if(insertFlag>0){
                                   p++;
                               }else{
                                   q++;
                               }

                        }
                        Log.e("LandingActivity----","Inserted--"+p+" : "+"Not Inserted: "+q);
                    }else{
                        Log.e("LandingActivity----","No Interested Ads");
                    }

                    if(interestadNames.size()!=0){
                        for(int i=0;i<interestadNames.size();i++){

                            File myFile = new File(URLClass.interestedpath+interestadNames.get(i));
                            if(myFile.exists()){

                            }else{
                                interestUrls.add(URLClass.imageurl+interestadNames.get(i));
                                finalNames.add(interestadNames.get(i));
                            }

                        }

                    }else{
                        Log.e("LandingActivity----","No Downloaded Images");
                    }

                    if(finalNames.size()!=0){

                        String[] urlList=new String[interestUrls.size()];
                        urlList = interestUrls.toArray(urlList);
                        String[] nameList = new String[finalNames.size()];
                        nameList = finalNames.toArray(nameList);

                        new DownloadFileAsync(LandingActivity.this,URLClass.interestedpath,urlList,nameList,new IDownloadStatus() {
                            @Override
                            public void downloadStatus(String status) {

                                try{
                                    if(status.equalsIgnoreCase("Completed")){

                                    }else{

                                    }

                                }catch (Exception e){

                                    e.printStackTrace();
                                    Log.e("DownloadFile----",e.toString());
                                }
                            }
                        }).execute();
                    }else{
                        Log.e("LandingActivity----","All OwnAd Images Downloaded");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("LandingActivity----",e.toString());
                }
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.frame, new InterestsFragment());
                tx.commit();
                navItemIndex=3;

            }
        }).execute();
    }

    public void syncData(){

        HashMap<String,String> hmap=new HashMap<>();
        hmap.put("userId",phno);
        new BagroundTask(URLClass.hosturl+"getAllData.php", hmap,LandingActivity.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) throws JSONException {

                JSONArray ja_preferences_table,ja_advt_info_table,ja_user_interests;
                JSONObject prefjo,advtjo,interestjo;
                try{
                    JSONObject myObj=new JSONObject(json);

                    Object obj=myObj.get("preferences_table");

                    if (obj instanceof JSONArray)
                    {
                        long prefdelcount=dbHelper.deleteAllPreferences(phno);
                        Log.e("prefdelcount---",""+prefdelcount);
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

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("BackGround--",e.toString());
                }

            }
        }).execute();
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(LandingActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RequestPermissionCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                refresh();
            }
            else {
                Toast.makeText(LandingActivity.this, "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}


