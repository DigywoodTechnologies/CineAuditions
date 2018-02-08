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

import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.Pojo.SingleInterest;
import com.digywood.cineauditions.Pojo.SingleProducer;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewAdvtInfo extends AppCompatActivity {

    int[] _intAdvtlist,_intInterestList;
    SingleInterest ad;
    SingleProducer pr;
    PopupWindow pwindo;
    TextView captionview,view_startTv,view_endTv,view_description,nameTv,numberTv,view_emailTv;
    ImageView view_img;
    DBHelper dbHelper;
    String time,MobileNo,key=null;
    int advtId;
    byte[] lotsImage=null;
    SingleAdvt myad=null;
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

}
