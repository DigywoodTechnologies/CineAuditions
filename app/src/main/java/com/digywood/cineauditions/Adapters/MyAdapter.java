package com.digywood.cineauditions.Adapters;

/**
 * Created by Shashank on 17-02-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
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

import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Fragments.ItemsFragment;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.R;
import com.digywood.cineauditions.RespondAvtInfo;

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
    public View getView(int i,View convertView,ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.advertisement_card,parent, false);
        Typeface myTypeface1 = Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/MontserratAlternates-Medium.ttf");
        holder.imageView = (ImageView)rowView.findViewById(R.id.photo);
        holder.caption = (TextView) rowView.findViewById(R.id.caption_notice);
        holder.caption.setTypeface(myTypeface1);
        holder.tv_adid=rowView.findViewById(R.id.tv_adId);
        holder.post_date = (TextView)rowView.findViewById(R.id.date_notice);
        holder.check = rowView.findViewById(R.id.check);

        if(allAdIds.size()!=0){

            int []_intAdvtlist = new int[Advtlist.size()];
            //Log.d("BrochuresInfoList.size", "comes:" + _intAdvtlist.length);
            for (int x = 0; x < _intAdvtlist.length; x++) {

                byte[] lotsImage = Advtlist.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(lotsImage, 0, lotsImage.length);
                holder.imageView.setImageBitmap(bitmap);
                holder.post_date.setText(Advtlist.get(i).getCreatedTime());
                holder.caption.setText(Advtlist.get(i).getCaption());
                holder.tv_adid.setText("AdvtId: "+String.valueOf(Advtlist.get(i).getAdvtRefNo()));
                if(allAdIds.contains(Advtlist.get(i).getAdvtRefNo())){
                    holder.check.setVisibility(View.VISIBLE);
                }
            }

        }else{

            int []_intAdvtlist = new int[Advtlist.size()];
            //Log.d("BrochuresInfoList.size", "comes:" + _intAdvtlist.length);
            for (int x = 0; x < _intAdvtlist.length; x++) {

                byte[] lotsImage = Advtlist.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(lotsImage, 0, lotsImage.length);
                holder.imageView.setImageBitmap(bitmap);
                holder.post_date.setText(Advtlist.get(i).getCreatedTime());
                holder.caption.setText(Advtlist.get(i).getCaption());
                holder.tv_adid.setText("AdvtId: "+String.valueOf(Advtlist.get(i).getAdvtRefNo()));
            }

        }


        final int final_i = i;

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String time = Advtlist.get(final_i).getCreatedTime();
                int advtId = Advtlist.get(final_i).getAdvtRefNo();

//                Intent intent = new Intent(context.getApplicationContext(), RespondAvtInfo.class);
//                intent.putExtra("mobileNo", MobileNo);
//                intent.putExtra("time", time);
//                intent.putExtra("advtId",advtId);
//                Log.d("advtId",""+Advtlist.get(final_i).getAdvtRefNo());
//                context.startActivity(intent);

                SingleAdvt singlead = Advtlist.get(final_i);
                Intent intent = new Intent(context,RespondAvtInfo.class);
                intent.putExtra("mobileNo",MobileNo);
                intent.putExtra("time",singlead.getCreatedTime());
                intent.putExtra("advtId",singlead.getAdvtRefNo());
                intent.putExtra("key","notice");
                Bundle extras=new Bundle();
                extras.putByteArray("image",singlead.getImage());
                extras.putString("caption",singlead.getCaption());
                extras.putString("start",singlead.getStartDate());
                extras.putString("end",singlead.getEndDate());
                extras.putString("description",singlead.getDescription());
                extras.putString("name",singlead.getContactName());
                extras.putString("number",singlead.getContactNumber());
                extras.putString("email",singlead.getEmailId());
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
