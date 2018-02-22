package com.digywood.cineauditions.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digywood.cineauditions.Pojo.SingleAd;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasa on 2018-01-25.
 */

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.MyViewHolder>{

    private List<SingleAd> surveyList;
    Context mycontext;
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_postdate,tv_caption,tv_startdate,tv_enddate,tv_advtId;

        public MyViewHolder(View view) {
            super(view);
            tv_postdate =view.findViewById(R.id.cust_post_date);
            tv_caption =view.findViewById(R.id.cust_caption);
            Typeface myTypeface1 = Typeface.createFromAsset(mycontext.getApplicationContext().getAssets(), "fonts/MontserratAlternates-Medium.ttf");
            tv_caption.setTypeface(myTypeface1);
            tv_startdate =view.findViewById(R.id.start_dateTv);
            tv_advtId=view.findViewById(R.id.tv_advtid);
        }
    }


    public AdsAdapter(List<SingleAd> surveyList, Context c) {
        this.surveyList = surveyList;
        this.mycontext=c;
    }

    public void updateList(List<SingleAd> list){
        surveyList = list;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_advtlayout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SingleAd singlead = surveyList.get(position);

        Log.e("Adp----",""+singlead.getAdvtid());
        holder.tv_postdate.setText(singlead.getCreatetime());
        holder.tv_caption.setText(singlead.getCaption());
        holder.tv_startdate.setText(singlead.getStarttime()+" --> "+singlead.getEndtime());
        holder.tv_advtId.setText(String.valueOf(singlead.getAdvtid()));


    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return surveyList.size();
    }

}
