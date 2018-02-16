package com.digywood.cineauditions.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.Adapters.AdsAdapter;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.Pojo.SingleAd;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.R;
import com.digywood.cineauditions.RecyclerTouchListener;
import com.digywood.cineauditions.ViewAdvtInfo;
import com.digywood.cineauditions.ViewInterestAdInfo;

import java.util.ArrayList;

/**
 * Created by prasa on 2018-02-05.
 */

public class InterestsFragment extends Fragment {

    ArrayList<SingleAd> Advtlist;
    public static AdsAdapter adsAdp;
    DBHelper myhelper;
    int count=0;
    String MobileNo="";
    RecyclerView rv_ads;
    TextView tv_empty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_interested, container, false);

        rv_ads=inflate.findViewById(R.id.rv_listofinterestedads);
        tv_empty=inflate.findViewById(R.id.tv_interestedadsemptydata);
        Advtlist=new ArrayList<>();
        myhelper=new DBHelper(getActivity());

        Intent cmgintent = getActivity().getIntent();
        if (cmgintent != null) {
            MobileNo = cmgintent.getStringExtra("mobileNo");
            //tv_producer_phno.setText(MobileNo);
        }

        Log.e("InterestsFragment---",MobileNo);

        Advtlist = myhelper.getInterestedAdvts(MobileNo);
        count = myhelper.getInterestedAdvtCount(MobileNo);

        if (Advtlist.size() != 0) {
            Log.e("InterestAdDetCount----",""+Advtlist.size());
            Log.e("InterestAdCount----",""+count);
            tv_empty.setVisibility(View.GONE);
            adsAdp = new AdsAdapter(Advtlist,getActivity());
            RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
            rv_ads.setLayoutManager(myLayoutManager);
            rv_ads.setItemAnimator(new DefaultItemAnimator());
            rv_ads.setAdapter(adsAdp);
        } else {
            rv_ads.setVisibility(View.GONE);
            tv_empty.setText("No Interested Ads for User");
            tv_empty.setVisibility(View.VISIBLE);
        }

        rv_ads.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),rv_ads,new RecyclerTouchListener.OnItemClickListener(){
            @Override
            public void onClick(View view, int position) {

                SingleAd singleAdvt=Advtlist.get(position);
                Intent intent=new Intent(getActivity(),ViewInterestAdInfo.class);
                intent.putExtra("mobileNo",MobileNo);
                intent.putExtra("time",singleAdvt.getCreatetime());
                intent.putExtra("advtId",singleAdvt.getAdvtid());
                startActivity(intent);
                Toast.makeText(getActivity(),MobileNo+" : "+Advtlist.get(position).getAdvtid(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return inflate;
    }
}
