package com.digywood.cineauditions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.AsyncTasks.DownloadFileAsync;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleAdvt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class RespondAvtInfo extends AppCompatActivity {

    int[] _intAdvtlist;
    TextView captionview,view_startTv,view_endTv,view_description,nameTv,numberTv,view_emailTv,tv_interest,tv_cat,tv_subcat;
    String cmcaption,cmstart,cmend,cmdes,cmname,cmnumber,cmemail,category,cmdownloadUrl=null,cmfileName=null,cmfileType=null,cmcreatetime=null,cmstatus=null;
    String key=null;
    ImageView view_img;
    DBHelper dbHelper;
    String time,MobileNo;
    int advtId=0;
    ArrayList<String> subcatList;
    ArrayList<SingleAdvt> Advtlist;
    Typeface myTypeface1;
    CheckBox interested;
    EditText comment;
    Button submit;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respond_avt_info);

        dbHelper = new DBHelper(this);
        Intent cmgintent = getIntent();
        if (cmgintent != null) {
            time = cmgintent.getStringExtra("time");
            MobileNo = cmgintent.getStringExtra("mobileNo");
            advtId = cmgintent.getIntExtra("advtId",0);
            Log.e("ViewAdvtInfo-----",""+advtId);
            Bundle getextras=cmgintent.getExtras();
            cmdownloadUrl=getextras.getString("url");
            cmfileType=getextras.getString("filetype");
            cmfileName=getextras.getString("filename");
            cmcaption=getextras.getString("caption");
            cmstart=getextras.getString("start");
            cmend=getextras.getString("end");
            cmdes=getextras.getString("description");
            cmname=getextras.getString("name");
            cmnumber=getextras.getString("number");
            cmemail=getextras.getString("email");
            cmcreatetime=getextras.getString("createtime");
            cmstatus=getextras.getString("status");
        }

        Advtlist = new ArrayList<SingleAdvt>();
        subcatList=new ArrayList<>();
        captionview = (TextView) findViewById(R.id.view_caption);
        view_startTv = (TextView) findViewById(R.id.view_start_dateTv);
        view_startTv = (TextView) findViewById(R.id.view_start_dateTv);
        tv_cat =findViewById(R.id.tv_rescategory);
        tv_subcat =findViewById(R.id.tv_ressubcategory);
        tv_interest=findViewById(R.id.tv_interest);
        view_endTv = (TextView) findViewById(R.id.view_end_dateTv);
        view_description = (TextView) findViewById(R.id.view_description);
        nameTv = (TextView) findViewById(R.id.personnameTv);
        numberTv = (TextView) findViewById(R.id.personnumberTv);
        view_emailTv = (TextView) findViewById(R.id.view_emailTv);
        view_img = (ImageView) findViewById(R.id.view_imgView);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsingToolbar);
        comment = (EditText) findViewById(R.id.comment);
        interested = (CheckBox) findViewById(R.id.cb_interested);
        submit = (Button) findViewById(R.id.submit);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        myTypeface1 = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Medium.ttf");

//        Advtlist = dbHelper.getAllAdvtProducer(advtId);
////        Advtlist = dbHelper.getAllPrefAdvts();
//        _intAdvtlist = new int[Advtlist.size()];

        captionview.setTypeface(myTypeface1);
        tv_cat.setTypeface(myTypeface1);
        tv_subcat.setTypeface(myTypeface1);

        try{
            Log.e("RespondAdvtInfo---",cmdownloadUrl);
            if(!cmdownloadUrl.equalsIgnoreCase("")){
                Log.e("RespondAvtInfo---","not null");
                URL url = new URL(cmdownloadUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                view_img.setImageBitmap(bmp);
            }else{
                Log.e("RespondAvtInfo---","null");
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("RespondAdvtInfo---",e.toString());
        }

        captionview.setText("" + cmcaption + " ");
        view_startTv.setText(cmstart);
        view_endTv.setText(cmend);
        view_description.setText(cmdes);
        nameTv.setText(cmname);
        numberTv.setText(cmnumber);
        view_emailTv.setText(cmemail);

        String url = URLClass.hosturl+"getSingleInterest.php";
        HashMap<String, String> hmap1 = new HashMap<>();

        Log.e("ResponseAvtInfo---",MobileNo+" : "+advtId);
        hmap1.put("userId", MobileNo);
        hmap1.put("advtId", String.valueOf(advtId));

        try {
            new BagroundTask(url,hmap1,RespondAvtInfo.this,new IBagroundListener() {
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
                            interested.setChecked(true);
                            interested.setEnabled(false);
                            tv_interest.setVisibility(View.VISIBLE);
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
                            comment.setVisibility(View.GONE);
                            submit.setVisibility(View.GONE);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String url = URLClass.hosturl+"insertUserIntrest.php";
                HashMap<String, String> hmap1 = new HashMap<>();
                String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
                hmap1.put("userId", MobileNo);
                hmap1.put("advtId", String.valueOf(advtId));
                hmap1.put("description", comment.getText().toString());
                hmap1.put("uploadDateTime",timeStamp);
                hmap1.put("flag", "A");

                try {
                    BagroundTask task = new BagroundTask(url,hmap1,RespondAvtInfo.this,new IBagroundListener() {
                        @Override
                        public void bagroundData(String json) {
                            Log.e("AdvtInfo------",json);
                            if (json.equalsIgnoreCase("Not Inserted")) {
                                Toast.makeText(getApplicationContext(), "Interest Insertion failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Interest Inserted", Toast.LENGTH_SHORT).show();
                                int interestId=Integer.parseInt(json);
                                long insertFlag=dbHelper.insertInterest(interestId,MobileNo,advtId,comment.getText().toString(),"A");
                                if(insertFlag>0){
                                    Toast.makeText(getApplicationContext(), "Inserted in Local", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), "Unable to Inserted in Local", Toast.LENGTH_SHORT).show();
                                }

                                if(!cmdownloadUrl.equalsIgnoreCase("")){
                                    String[] urlList={cmdownloadUrl};
                                    String[] nameList={cmfileName};

                                    new DownloadFileAsync(RespondAvtInfo.this,URLClass.interestedpath, urlList, nameList, new IDownloadStatus() {
                                        @Override
                                        public void downloadStatus(String status) {

                                            try{
                                                if(status.equalsIgnoreCase("Completed")){

                                                    long insertFlag=dbHelper.insertInterestedAdvt(advtId,"ORG0001",MobileNo,cmcaption,cmdes,cmfileType,cmfileName,cmdownloadUrl,cmstart,cmend,cmname,cmnumber,cmemail,cmcreatetime,cmstatus);
                                                    if(insertFlag>0){
                                                        Toast.makeText(getApplicationContext(),"Interest Inserted",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }else{
                                                        Toast.makeText(getApplicationContext(),"Interest Not Inserted",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }

                                                }else{

                                                }

                                            }catch (Exception e){

                                                e.printStackTrace();
                                                Log.e("DownloadFile----",e.toString());
                                            }
                                        }
                                    }).execute();

                                }else{
                                    long insertFlag1=dbHelper.insertInterestedAdvt(advtId,"ORG0001",MobileNo,cmcaption,cmdes,cmfileType,cmfileName,cmdownloadUrl,cmstart,cmend,cmname,cmnumber,cmemail,cmcreatetime,cmstatus);
                                    if(insertFlag1>0){
                                        Toast.makeText(getApplicationContext(),"Interest Inserted in Local",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Interest Not Inserted in Local",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }

                            }
                        }
                    });
                    task.execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
