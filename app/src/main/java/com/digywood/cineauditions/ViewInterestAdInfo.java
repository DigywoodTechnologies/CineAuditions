package com.digywood.cineauditions;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    ArrayList<SingleAdvt> Advtlist,Interested;
    ArrayList<SingleInterest> InterestList;
    TextView tv_interest,tv_cat,tv_subcat;
    Typeface myTypeface1;
    Button view_interests,btnClosePopup;
    LinearLayout linearLayout;
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
        mCollapsingToolbarLayout = findViewById(R.id.interest_collapsingToolbar);

        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

        Log.e("ViewAdvtInfo---",""+advtId);

        myad = dbHelper.getLocalAd(advtId);

        captionview.setTypeface(myTypeface1);

        if(myad!=null){

            try {
                if(!myad.getFilename().equalsIgnoreCase("")){
                    Bitmap bmp = BitmapFactory.decodeFile(URLClass.myadspath+myad.getFilename());
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
            getCatSubcatInterestMsg();
        }else{
            Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_SHORT).show();
        }

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
                                tv_interest.setText("Interested,No comment"+"\n"+"Date :"+date);
                            }else{
                                tv_interest.setText("Des: "+des+"\n"+"Date :"+date);
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

                                    category=adcatsubcatjo.getString("category");
                                    String subcat=adcatsubcatjo.getString("subCategory");
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

        if(category!=null){
            tv_cat.setText("Category: "+category);
        }else{
            tv_cat.setText("Category: No Selection");
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
            tv_subcat.setText("Sub-Categories: "+subcatstr);
        }else{
            tv_subcat.setText("Sub-Categories: No Selection");
        }

    }

}
