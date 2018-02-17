package com.digywood.cineauditions;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.Pojo.SingleInterest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by prasa on 2018-02-05.
 */

public class ViewInterestAdInfo extends AppCompatActivity {

    TextView captionview,view_startTv,view_endTv,view_description,nameTv,numberTv,view_emailTv;
    ImageView view_img;
    DBHelper dbHelper;
    String time,MobileNo,key=null,category=null;
    int advtId;
    byte[] lotsImage=null;
    SingleAdvt myad=null;
    ArrayList<String> subcatList;
    ArrayList<String> catNameList=new ArrayList<>();
    ArrayList<SingleAdvt> Advtlist,Interested;
    ArrayList<SingleInterest> InterestList;
    TextView tv_interest,tv_interestdate,tv_cat,tv_subcat;
    Typeface myTypeface1;
    LinearLayout linearLayout;
    Dialog mydialog;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    HashMap<String,String> hmap=new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_interest_advt);

        dbHelper = new DBHelper(this);
        Intent cmgintent = getIntent();
        if (cmgintent != null) {
            time = cmgintent.getStringExtra("time");
            MobileNo = cmgintent.getStringExtra("mobileNo");
            advtId = cmgintent.getIntExtra("advtId",0);
        }

        Advtlist = new ArrayList<>();
        InterestList = new ArrayList<>();
        subcatList=new ArrayList<>();

        linearLayout = findViewById(R.id.interest_layout);
        captionview = findViewById(R.id.interest_view_caption);
        view_startTv = findViewById(R.id.interest_view_start_dateTv);
        view_endTv = findViewById(R.id.interest_view_end_dateTv);
        view_description = findViewById(R.id.interest_view_description);
        nameTv = findViewById(R.id.interest_personnameTv);
        numberTv = findViewById(R.id.interest_personnumberTv);
        view_emailTv = findViewById(R.id.interest_view_emailTv);
        view_img = findViewById(R.id.interest_view_imgView1);
        tv_cat =findViewById(R.id.interest_tv_rescategory);
        tv_subcat =findViewById(R.id.interest_tv_ressubcategory);
        tv_interest=findViewById(R.id.interest_tv_interest);
        tv_interestdate=findViewById(R.id.interest_tv_interestdate);
        mCollapsingToolbarLayout = findViewById(R.id.interest_collapsingToolbar);

        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        Log.e("ViewInterestAdInfo---",""+advtId);

        myad = dbHelper.getLocalInterestedAd(advtId);

        captionview.setTypeface(myTypeface1);

        if(myad!=null){

            try {
                if(!myad.getFilename().equalsIgnoreCase("")){
                    Bitmap bmp = BitmapFactory.decodeFile(URLClass.interestedpath+myad.getFilename());
                    view_img.setImageBitmap(bmp);
                }else{
                    Log.e("ViewInterestAdInfo---","No Image for Ad");
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ViewInterestAdInfo---",e.toString());
            }

            captionview.setText("" + myad.getCaption() + " ");
            view_startTv.setText(myad.getStartDate());
            view_endTv.setText(myad.getEndDate());
            view_description.setText(myad.getDescription());
            nameTv.setText(myad.getContactName());
            numberTv.setText(myad.getContactNumber());
            view_emailTv.setText(myad.getEmailId());
            new AsyncCheckInternet(ViewInterestAdInfo.this,new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        getCatSubcatInterestMsg();
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
        }else{
            Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_SHORT).show();
        }

        view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!myad.getFilename().equalsIgnoreCase("")){
//                        Intent i=new Intent(getApplicationContext(),ImageActivity.class);
//                        i.putExtra("imageurl",myad.getFilename());
//                        i.putExtra("key","interest");
//                        startActivity(i);
                        popUp();
                    }else{
                        Log.e("ViewAdvtInfo---","No image for Ad");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ViewAdvtInfo---",e.toString());
                }
            }
        });

    }

    public void getCatSubcatInterestMsg(){

        String url = URLClass.hosturl+"getSingleInterest.php";
        HashMap<String, String> hmap1 = new HashMap<>();

        hmap1.put("userId", MobileNo);
        hmap1.put("advtId", String.valueOf(advtId));

        try {
            new BagroundTask(url,hmap1,ViewInterestAdInfo.this,new IBagroundListener() {
                @Override
                public void bagroundData(String json) {

                    JSONArray ja_catsubcat=null;
                    JSONObject adcatsubcatjo=null;

                    try {
                        Log.e("AdvtInfo------", json);

                        JSONObject myObj=new JSONObject(json);
                        JSONObject jo=null;
                        String des=null;
                        String date=null;

                        Object obj1=myObj.get("user_interest");

                        if (obj1 instanceof JSONArray)
                        {
                            JSONArray ja=myObj.getJSONArray("user_interest");
                            for(int i=0;i<ja.length();i++){
                                jo=ja.getJSONObject(i);
                                des=jo.getString("description");
                                date=jo.getString("uploadDateTime");
                            }
                            if(des.equals("")){
                                tv_interestdate.setText(date);
                            }else{
                                tv_interest.setText(des);
                                tv_interestdate.setText(date);
                            }

                        }
                        else {
                            Log.e("Interest--","No Interest");
                        }

                        Object obj2=myObj.get("Advt_CatSubcat");

                        if (obj2 instanceof JSONArray)
                        {
                            ja_catsubcat=myObj.getJSONArray("Advt_CatSubcat");
                            if(ja_catsubcat.length()>0){

                                Log.e("interestLength---",""+ja_catsubcat.length());
                                int p=0,q=0;
                                for(int i=0;i<ja_catsubcat.length();i++){

                                    adcatsubcatjo=ja_catsubcat.getJSONObject(i);

                                    category=adcatsubcatjo.getString("catName");

                                    if(catNameList.size()!=0){
                                        if(catNameList.contains(category)){

                                        }else{
                                            catNameList.add(category);
                                        }
                                    }else{
                                        catNameList.add(category);
                                    }
                                    String subcat=adcatsubcatjo.getString("subCatName");
                                    subcatList.add(subcat);

                                }

                            }else{
                                Log.e("BackGroundTask--","EmptyJsonArray");
                            }
                        }
                        else {
                            Log.e("CatSubcat--","No Categories and SubCategories");
                        }

                        setData();

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("RespondAvtInfo----",e.toString());
                    }
                }
            }).execute();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData(){

        Log.e("RespondAvtInfo--","catListSize--"+catNameList.size());

        if(catNameList.size()!=0){
            String catstr=null;
            for(int i=0;i<catNameList.size();i++){
                if(i==0){
                    catstr=""+catNameList.get(i);
                }else{
                    catstr=catstr+","+catNameList.get(i);
                }
            }
            tv_cat.setText(catstr);
        }else{
            tv_cat.setText("No Selection");
        }

        if(subcatList.size()!=0){

            String subcatstr=null;
            for(int i=0;i<subcatList.size();i++){
                if(i==0){
                    subcatstr=""+subcatList.get(i);
                }else{
                    subcatstr=subcatstr+","+subcatList.get(i);
                }
            }
            tv_subcat.setText(subcatstr);
        }else{
            tv_subcat.setText("No Selection");
        }

    }

    public void popUp(){
        mydialog = new Dialog(ViewInterestAdInfo.this);
        mydialog.getWindow();
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.setContentView(R.layout.activity_ivdialouge);
        mydialog.show();
        mydialog.setCanceledOnTouchOutside(false);

        ImageView iv_img = mydialog.findViewById(R.id.iv_dialougimg);
        ImageView iv_close =mydialog.findViewById(R.id.iv_close);

        try{
            Bitmap bmp = BitmapFactory.decodeFile(URLClass.interestedpath+myad.getFilename());
            iv_img.setImageBitmap(bmp);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("ViewAdvtInfo---",e.toString());
        }

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mydialog!=null && mydialog.isShowing()){
            mydialog.cancel();
        }else{
            finish();
        }
        super.onBackPressed();
    }

}
