package com.digywood.cineauditions.Adapters;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import com.digywood.cineauditions.Pojo.SingleFAQ;
import com.digywood.cineauditions.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasa on 2018-02-19.
 */

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.MyViewHolder>{

    private List<SingleFAQ> faqList;
    Context mycontext;
    private Animation animationUp;
    private Animation animationDown;
    private final int COUNTDOWN_RUNNING_TIME = 300;
    private ArrayList<String> chknumberList=new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_ques,tv_answ;

        public MyViewHolder(View view) {
            super(view);
            tv_ques =view.findViewById(R.id.tv_question);
            tv_answ = view.findViewById(R.id.tv_answer);
        }
    }


    public FAQAdapter(List<SingleFAQ> qaList,Context c,Animation animup,Animation animdown) {
        this.faqList = qaList;
        this.mycontext=c;
        this.animationUp=animup;
        this.animationDown=animdown;
    }


    public void updateList(List<SingleFAQ> list){
        faqList = list;
        notifyDataSetChanged();
    }

    public FAQAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_faqitem, parent, false);
        return new FAQAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FAQAdapter.MyViewHolder holder, final int position) {
        final SingleFAQ singleFAQ = faqList.get(position);

        try {
            holder.tv_ques.setText(singleFAQ.getQues());
            holder.tv_answ.setText(singleFAQ.getAnws());

            holder.tv_ques.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (holder.tv_answ.isShown()) {
                        holder.tv_answ.startAnimation(animationUp);

                        CountDownTimer countDownTimerStatic = new CountDownTimer(COUNTDOWN_RUNNING_TIME,16) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                holder.tv_answ.setVisibility(View.GONE);
                            }
                        };
                        countDownTimerStatic.start();

                    } else {
                        holder.tv_answ.setVisibility(View.VISIBLE);
                        holder.tv_answ.startAnimation(animationDown);

                    }

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public ArrayList<String> getNumberList() {
        notifyDataSetChanged();
        return chknumberList;

    }

    public int getItemCount() {
        return faqList.size();
    }

}
