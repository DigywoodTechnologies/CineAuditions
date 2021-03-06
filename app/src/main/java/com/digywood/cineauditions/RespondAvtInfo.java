package com.digywood.cineauditions;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
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

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.digywood.cineauditions.AdvtInfoScreen.RequestPermissionCode;

public class RespondAvtInfo extends AppCompatActivity {

    TextView captionview,view_startTv,view_endTv,view_description,nameTv,numberTv,view_emailTv,tv_interest,tv_interestdate,tv_cat,tv_subcat,resp_adId;
    String cmcaption,cmstart,cmend,cmdes,cmname,cmnumber,cmemail,category,cmdownloadUrl=null,cmfileName=null,cmfileType=null,cmcreatetime=null,cmstatus=null;
    String cmproducerid;
    ImageView view_img;
    DBHelper dbHelper;
    String time,MobileNo;
    int advtId=0;
    LinearLayout ll_interest;
    ArrayList<String> catNameList=new ArrayList<>();
    ArrayList<String> subcatList;
    ArrayList<SingleAdvt> Advtlist;
    ArrayList<String> subcatNames=new ArrayList<>();
    Typeface myTypeface1;
    CheckBox interested;
    EditText comment;
    Button submit;
    Dialog mydialog;
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
            cmproducerid=getextras.getString("producerid");
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

        Advtlist = new ArrayList<>();
        subcatList=new ArrayList<>();
        captionview =  findViewById(R.id.view_caption);
        view_startTv =  findViewById(R.id.view_start_dateTv);
        view_startTv =  findViewById(R.id.view_start_dateTv);
        tv_cat =findViewById(R.id.tv_rescategory);
        ll_interest =findViewById(R.id.interest_ll);
        tv_subcat =findViewById(R.id.tv_ressubcategory);
        tv_interest=findViewById(R.id.tv_interest);
        tv_interestdate=findViewById(R.id.tv_interestdate);
        view_endTv =  findViewById(R.id.view_end_dateTv);
        view_description = findViewById(R.id.view_description);
        nameTv =  findViewById(R.id.personnameTv);
        numberTv =  findViewById(R.id.personnumberTv);
        view_emailTv =  findViewById(R.id.view_emailTv);
        resp_adId = findViewById(R.id.resp_adId);
        view_img =  findViewById(R.id.view_imgView);
//        iv_full=findViewById(R.id.iv_full);
        mCollapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        comment =  findViewById(R.id.comment);
        interested = findViewById(R.id.cb_interested);
        submit =  findViewById(R.id.submit);
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
                URL url = new URL(cmdownloadUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                view_img.setImageBitmap(bmp);
            }else{
                Log.e("RespondAvtInfo---","No image for Ad");
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.e("RespondAdvtInfo---",e.toString());
        }

        captionview.setText(cmcaption);
        resp_adId.setText(""+advtId);
        view_startTv.setText(cmstart);
        view_endTv.setText(cmend);
        view_description.setText(cmdes);
        nameTv.setText(cmname);
        numberTv.setText(cmnumber);
        view_emailTv.setText(cmemail);

        new AsyncCheckInternet(RespondAvtInfo.this,new INetStatus() {
            @Override
            public void inetSatus(Boolean netStatus) {
                if(netStatus){
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
                                        interested.setVisibility(View.GONE);
                                        ll_interest.setVisibility(View.VISIBLE);
                                        JSONArray ja=myObj.getJSONArray("user_interest");
                                        for(int i=0;i<ja.length();i++){
                                            jo=ja.getJSONObject(i);
                                            des=jo.getString("description");
                                            date=jo.getString("uploadDateTime");
                                        }
                                        try {
                                            if(des.equals("")){
                                                tv_interest.setText("No Response");
                                                tv_interestdate.setText(date);
                                            }else{
                                                tv_interest.setText(des);
                                                tv_interestdate.setText(date);
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
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
                }else{
                    Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                }

            }
        }).execute();

        view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(!cmdownloadUrl.equalsIgnoreCase("")){
//                        Intent i=new Intent(getApplicationContext(),ImageActivity.class);
//                        i.putExtra("imageurl",cmdownloadUrl);
//                        i.putExtra("key","notice");
//                        startActivity(i);
                        popUp();
                    }else{
                        Log.e("RespondAvtInfo---","No image for Ad");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("RespondAdvtInfo---",e.toString());
                }
            }
        });

//        iv_full.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    if(!cmdownloadUrl.equalsIgnoreCase("")){
//                        popUp();
//                    }else{
//                        Log.e("RespondAvtInfo---","No image for Ad");
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Log.e("RespondAdvtInfo---",e.toString());
//                }
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(interested.isChecked()){
                    new AsyncCheckInternet(RespondAvtInfo.this,new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus){
                                final String url = URLClass.hosturl+"insertUserIntrest.php";
                                HashMap<String, String> hmap1 = new HashMap<>();
                                String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(Calendar.getInstance().getTime());
                                hmap1.put("userId", MobileNo);
                                hmap1.put("advtId", String.valueOf(advtId));
                                hmap1.put("description", comment.getText().toString());
                                hmap1.put("uploadDateTime",timeStamp);
                                hmap1.put("flag","A");

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

                                                if(checkPermission()){
                                                    configureInterestAds();
                                                }else{
                                                    requestPermission();
                                                }

                                            }
                                        }
                                    });
                                    task.execute();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).execute();
                }else{
                    Toast.makeText(getApplicationContext(),"Please Check 'Interested' Checkbox,Inorder to send your interest",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == RequestPermissionCode){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_CODE_GALLERY);
                configureInterestAds();
            }
            else {
                Toast.makeText(RespondAvtInfo.this, "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void configureInterestAds(){

        if(!cmdownloadUrl.equalsIgnoreCase("")){
            String[] urlList={cmdownloadUrl};
            String[] nameList={cmfileName};

            new DownloadFileAsync(RespondAvtInfo.this,URLClass.interestedpath, urlList, nameList,new IDownloadStatus() {
                @Override
                public void downloadStatus(String status) {

                    try{
                        if(status.equalsIgnoreCase("Completed")){

                            long insertFlag=dbHelper.insertInterestedAdvt(advtId,"ORG0001",cmproducerid,MobileNo,cmcaption,cmdes,cmfileType,cmfileName,cmdownloadUrl,cmstart,cmend,cmname,cmnumber,cmemail,cmcreatetime,cmstatus);
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
            long insertFlag1=dbHelper.insertInterestedAdvt(advtId,"ORG0001",cmproducerid,MobileNo,cmcaption,cmdes,cmfileType,cmfileName,cmdownloadUrl,cmstart,cmend,cmname,cmnumber,cmemail,cmcreatetime,cmstatus);
            if(insertFlag1>0){
                Toast.makeText(getApplicationContext(),"Interest Inserted in Local",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Interest Not Inserted in Local",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(RespondAvtInfo.this, new
                String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},RequestPermissionCode);
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

//            subcatNames=dbHelper.getSubCatNames(subcatList);
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
        mydialog = new Dialog(RespondAvtInfo.this);
        mydialog.getWindow();
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.setContentView(R.layout.activity_ivdialouge);
        mydialog.show();
        mydialog.setCanceledOnTouchOutside(false);

        ImageView iv_img = mydialog.findViewById(R.id.iv_dialougimg);
        ImageView iv_close =mydialog.findViewById(R.id.iv_close);

        try{
            URL url=new URL(cmdownloadUrl);
            new AsyncTaskLoadImage(iv_img,url).execute();
        }catch (Exception e){
            e.printStackTrace();
            Log.e("FullImageScreen---",e.toString());
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
