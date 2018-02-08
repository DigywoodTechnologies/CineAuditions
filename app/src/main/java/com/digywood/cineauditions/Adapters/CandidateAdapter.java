package com.digywood.cineauditions.Adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.digywood.cineauditions.Pojo.Candidate;
import com.digywood.cineauditions.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasa on 2018-01-25.
 */

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.MyViewHolder> {

    private List<Candidate> candidateList;
    Context mycontext;
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name,tv_number,tv_mail,tv_comment,tv_time;
        public FloatingActionButton fab_play;

        public MyViewHolder(View view) {
            super(view);
            tv_name =view.findViewById(R.id.tv_username);
            tv_time = view.findViewById(R.id.tv_time);
            tv_number =view.findViewById(R.id.tv_usernumber);
            tv_mail =view.findViewById(R.id.tv_email);
            tv_comment =view.findViewById(R.id.tv_comment);
        }
    }


    public CandidateAdapter(List<Candidate> surveyList, Context c) {
        this.candidateList = surveyList;
        this.mycontext=c;
    }


    public void updateList(List<Candidate> list){
        candidateList = list;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidate_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Candidate singlecandi = candidateList.get(position);

        try {
            holder.tv_name.setText("Name: "+singlecandi.getName());
            holder.tv_time.setText("Time: "+singlecandi.getTime());
            holder.tv_number.setText("Number: "+singlecandi.getNumber());
            holder.tv_mail.setText("Mail: "+singlecandi.getMail());
            holder.tv_comment.setText("Review: "+singlecandi.getComment());
        }catch (Exception e){
            e.printStackTrace();
            Log.e("CandiAdapter-----",e.toString());
        }

    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return candidateList.size();
    }


}
