package com.digywood.cineauditions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.Pojo.SingleInterest;
import com.digywood.cineauditions.Pojo.SingleProducer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewAdvtInfo extends AppCompatActivity {

    int[] _intAdvtlist,_intInterestList;
    SingleInterest ad;
    SingleProducer pr;
    PopupWindow pwindo;
    TextView captionview,view_startTv,view_endTv,view_description,nameTv,numberTv,view_emailTv,tv_cat,tv_subcat;
    ImageView view_img;
    DBHelper dbHelper;
    String time,MobileNo,key=null,category=null;
    int advtId;
    byte[] lotsImage=null;
    SingleAdvt myad=null;
    ArrayList<String> catNameList=new ArrayList<>();
    ArrayList<String> subcatList=new ArrayList<>();
    ArrayList<SingleAdvt> Advtlist,Interested;
    ArrayList<SingleInterest> InterestList;
    Typeface myTypeface1;
    Button view_interests,btnClosePopup;
    LinearLayout linearLayout;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    HashMap<String,String> hmap=new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_advt_info);

        dbHelper = new DBHelper(this);
        Intent cmgintent = getIntent();
        if (cmgintent != null) {
            time = cmgintent.getStringExtra("time");
            MobileNo = cmgintent.getStringExtra("mobileNo");
            advtId = cmgintent.getIntExtra("advtId",0);
        }

        Advtlist = new ArrayList<>();
        InterestList = new ArrayList<>();

        linearLayout = findViewById(R.id.layout);
        captionview = findViewById(R.id.view_caption);
        view_startTv = findViewById(R.id.view_start_dateTv);
        view_endTv = findViewById(R.id.view_end_dateTv);
        view_description = findViewById(R.id.view_description);
        nameTv = findViewById(R.id.personnameTv);
        numberTv = findViewById(R.id.personnumberTv);
        view_emailTv = findViewById(R.id.view_emailTv);
        view_img = findViewById(R.id.view_imgView1);
        view_interests = findViewById(R.id.view_interests);
        mCollapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        tv_cat =findViewById(R.id.tv_ownadrescategory);
        tv_subcat =findViewById(R.id.tv_ownadressubcategory);

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
                    Log.e("ViewAdvtInfo---","No image for Ad");
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.e("ViewAdvtInfo---",e.toString());
            }

            captionview.setText("" + myad.getCaption() + " ");
            view_startTv.setText(myad.getStartDate());
            view_endTv.setText(myad.getEndDate());
            view_description.setText(myad.getDescription());
            nameTv.setText(myad.getContactName());
            numberTv.setText(myad.getContactNumber());
            view_emailTv.setText(myad.getEmailId());

            new AsyncCheckInternet(ViewAdvtInfo.this,new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        getInterestData();
                    }else{
                        Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();

        }else{
            Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_SHORT).show();
        }


        view_interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAdvtInfo.this,ListofInterests.class);
                intent.putExtra("mobileno",MobileNo);
                intent.putExtra("advtId",advtId);
                startActivity(intent);
            }
        });

    }

    public void getInterestData(){
        String url = URLClass.hosturl+"getSingleInterest.php";
        HashMap<String, String> hmap1 = new HashMap<>();

        Log.e("ResponseAvtInfo---",MobileNo+" : "+advtId);
        hmap1.put("userId", MobileNo);
        hmap1.put("advtId", String.valueOf(advtId));

        try {
            new BagroundTask(url,hmap1,ViewAdvtInfo.this,new IBagroundListener() {
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
            tv_cat.setText("Category: "+catstr);
        }else{
            tv_cat.setText("Category: No Selection");
        }



        if(subcatList.size()!=0){

//            subcatNames=dbHelper.getSubCatNames(subcatList);
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
