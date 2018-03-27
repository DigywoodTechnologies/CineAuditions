package com.digywood.cineauditions.Adapters;

/**
 * Created by Shashank on 17-02-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.digywood.cineauditions.AsyncTaskLoadImage;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Fragments.ItemsFragment;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.R;
import com.digywood.cineauditions.RespondAvtInfo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {

    private static final String TAG = "CONTACTS_BASE_ADAPTER";
    CardView mCardView;
    Context context;
    String MobileNo;
    int advtId;
    DBHelper myhelper;
    ArrayList<Integer> allAdIds;
    ArrayList<SingleAdvt> Advtlist = new ArrayList<>();
    ViewHolder holder = new ViewHolder() ;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context c, ArrayList<SingleAdvt> Advtlist,String MobileNo) {
        this.context = c;
        myhelper=new DBHelper(c);
        allAdIds=myhelper.getAllInterests(MobileNo);
        this.MobileNo = MobileNo;
        this.advtId = advtId;
        this.Advtlist = Advtlist;
    }

    @Override
    public int getCount() {
        return Advtlist.size();
    }

    @Override
    public Object getItem(int i) {
        return Advtlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.advertisement_card,parent, false);
        Typeface myTypeface1 = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/MontserratAlternates-Medium.ttf");
//        holder.imageView = rowView.findViewById(R.id.photo);
        holder.caption =  rowView.findViewById(R.id.caption_notice);
        holder.caption.setTypeface(myTypeface1);
        holder.tv_adid=rowView.findViewById(R.id.tv_adId);
        holder.post_date = rowView.findViewById(R.id.date_notice);
        holder.check = rowView.findViewById(R.id.check);

        if(allAdIds.size()!=0){

            SingleAdvt singlead=Advtlist.get(position);

            try {
                holder.post_date.setText(singlead.getCreatedTime());
                holder.caption.setText(singlead.getCaption());
                holder.tv_adid.setText(String.valueOf(singlead.getAdvtRefNo()));
                if(allAdIds.contains(singlead.getAdvtRefNo())){
                    holder.check.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.e("MyAdapter---",e.toString());
            }

        }else{

            SingleAdvt singlead=Advtlist.get(position);

            try {
                holder.post_date.setText(singlead.getCreatedTime());
                holder.caption.setText(singlead.getCaption());
                holder.tv_adid.setText("AdvtId: " + String.valueOf(singlead.getAdvtRefNo()));
            }catch (Exception e){
                e.printStackTrace();
                Log.e("MyAdapter---",e.toString());
            }
        }

//        SingleAdvt singlead=Advtlist.get(position);
//
//        try {
////            URL url = new URL(singlead.getDownloadUrl());
//////            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//////            holder.imageView.setImageBitmap(bmp);
////            new AsyncTaskLoadImage(holder.imageView,url).execute();
//            holder.post_date.setText(singlead.getCreatedTime());
//            holder.caption.setText(singlead.getCaption());
//            holder.tv_adid.setText("AdvtId: " + String.valueOf(singlead.getAdvtRefNo()));
//        }catch (Exception e){
//            e.printStackTrace();
//            Log.e("MyAdapter---",e.toString());
//        }


        final int final_i = position;

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String time = Advtlist.get(final_i).getCreatedTime();
                int advtId = Advtlist.get(final_i).getAdvtRefNo();

                SingleAdvt singlead = Advtlist.get(final_i);
                Intent intent = new Intent(context,RespondAvtInfo.class);
                intent.putExtra("mobileNo",MobileNo);
                intent.putExtra("time",singlead.getCreatedTime());
                intent.putExtra("advtId",singlead.getAdvtRefNo());
                intent.putExtra("key","notice");
                Bundle extras=new Bundle();
                extras.putString("producerid",singlead.getProducer_id());
                extras.putString("url",singlead.getDownloadUrl());
                extras.putString("filename",singlead.getFilename());
                extras.putString("caption",singlead.getCaption());
                extras.putString("start",singlead.getStartDate());
                extras.putString("end",singlead.getEndDate());
                extras.putString("description",singlead.getDescription());
                extras.putString("name",singlead.getContactName());
                extras.putString("number",singlead.getContactNumber());
                extras.putString("email",singlead.getEmailId());
                extras.putString("filetype",singlead.getFileType());
                extras.putString("createtime",singlead.getCreatedTime());
                extras.putString("status",singlead.getStatus());
                intent.putExtras(extras);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        return rowView;
    }

    public class ViewHolder
    {
        public TextView post_date,caption,tv_adid;
        public ImageView imageView,check;
    }

    public void updateList(ArrayList<SingleAdvt> SingleAdList){

        Advtlist=SingleAdList;
        notifyDataSetChanged();

    }

}
