package com.digywood.cineauditions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.net.URL;

public class ImageActivity extends AppCompatActivity {

    String imageurl,key;
    ImageView iv_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }

        if(Build.VERSION.SDK_INT>=21) {

            final Drawable upArrow = getApplicationContext().getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);

        }

        Intent cmgintent=getIntent();
        if(cmgintent!=null){
            imageurl=cmgintent.getStringExtra("imageurl");
            key=cmgintent.getStringExtra("key");
        }

        iv_img = findViewById(R.id.iv_img);

        if(key.equalsIgnoreCase("notice")){
            try{
                URL url=new URL(imageurl);
                new AsyncTaskLoadImage(iv_img,url).execute();
            }catch (Exception e){
                e.printStackTrace();
                Log.e("FullImageScreen---",e.toString());
            }
        }else if(key.equalsIgnoreCase("ownad")){
            try{
                Bitmap bmp = BitmapFactory.decodeFile(URLClass.myadspath+imageurl);
                iv_img.setImageBitmap(bmp);
            }catch (Exception e){
                e.printStackTrace();
                Log.e("FullImageScreen---",e.toString());
            }
        }else if(key.equalsIgnoreCase("interest")){
            try{
                Bitmap bmp = BitmapFactory.decodeFile(URLClass.interestedpath+imageurl);
                iv_img.setImageBitmap(bmp);
            }catch (Exception e){
                e.printStackTrace();
                Log.e("FullImageScreen---",e.toString());
            }

        }

    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return super.onNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
