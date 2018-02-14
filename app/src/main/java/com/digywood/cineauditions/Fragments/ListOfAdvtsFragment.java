package com.digywood.cineauditions.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.Adapters.AdsAdapter;
import com.digywood.cineauditions.AdvtInfoScreen;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.HidingScrollListener;
import com.digywood.cineauditions.Pojo.SingleAd;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.ViewAdvtInfo;
import com.digywood.cineauditions.R;
import com.digywood.cineauditions.RecyclerTouchListener;

import java.util.ArrayList;

public class ListOfAdvtsFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_producer_phno;
    int[] _intAdvtlist;
    DBHelper dbHelper;
    public static AdsAdapter adsAdp;
    int count=0;
    FloatingActionButton fab_new;
    RecyclerView.LayoutManager myLayoutManager;
    TextView tv_advtlist,tv_emptydata;
    Typeface myTypeface1,myTypeface2;
    public RecyclerView rv_ads;
    ArrayList<SingleAd> Advtlist;
    String MobileNo, AdvtId;
    Button newadvt;

    final int REQUEST_CODE_GALLERY = 999;

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";

    private OnFragmentInteractionListener mListener;

    public ListOfAdvtsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListOfAdvtsFragment newInstance(String param1, String param2) {
        ListOfAdvtsFragment fragment = new ListOfAdvtsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_list_of_advts, container, false);

        Advtlist = new ArrayList<>();
        tv_producer_phno = (TextView)inflate.findViewById(R.id.tv_producer_phno);
        tv_advtlist = (TextView)inflate.findViewById(R.id.title_advtlist);
        tv_emptydata =inflate.findViewById(R.id.tv_adsemptydata);
        rv_ads = inflate.findViewById(R.id.rv_listofads);
        fab_new=inflate.findViewById(R.id.fab_new);
//        newadvt = (Button)inflate.findViewById(R.id.newadvt);


        myTypeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Medium.ttf");

        tv_advtlist.setTypeface(myTypeface1);
//        newadvt.setTypeface(myTypeface1);

        dbHelper = new DBHelper(getActivity());
        Intent cmgintent = getActivity().getIntent();
        if (cmgintent != null) {
            MobileNo = cmgintent.getStringExtra("mobileNo");
            //tv_producer_phno.setText(MobileNo);
        }

        Log.e("MobileNo1",""+MobileNo);
        Advtlist = dbHelper.getAllAdvts(MobileNo);
        count = dbHelper.getAdvtCount(MobileNo);
        Log.d("MobileNo)", "comes:" +MobileNo);

//        AdvtsLv.setAdapter(new ContactsBaseAdapter(getContext()));

//        newadvt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), AdvtInfoScreen.class);
//                intent.putExtra("mobileNo", MobileNo);
//                startActivity(intent);
//            }
//        });

        fab_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AdvtInfoScreen.class);
                intent.putExtra("mobileNo", MobileNo);
                startActivity(intent);
            }
        });

        if (Advtlist.size() != 0) {
            Log.e("Advtlist.size()", "comes:" + Advtlist.size());
            Log.e("Advtlist.size()", ""+count);
            tv_emptydata.setVisibility(View.GONE);
            adsAdp = new AdsAdapter(Advtlist,getActivity());
            myLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
            rv_ads.setLayoutManager(myLayoutManager);
            rv_ads.setItemAnimator(new DefaultItemAnimator());
            rv_ads.setAdapter(adsAdp);
        } else {
            rv_ads.setVisibility(View.GONE);
            tv_emptydata.setText("No Posted Ads \n tap '+' to create Profile");
            tv_emptydata.setVisibility(View.VISIBLE);
        }

        rv_ads.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),rv_ads,new RecyclerTouchListener.OnItemClickListener(){
            @Override
            public void onClick(View view, int position) {

                SingleAd singleAdvt=Advtlist.get(position);
                Intent intent=new Intent(getActivity(),ViewAdvtInfo.class);
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

//        HidingScrollListener scrollListener = new HidingScrollListener(getActivity(),new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false)) {
//            @Override
//            public void onMoved(int distance) {
//
//            }
//
//            @Override
//            public void onShow() {
//
//            }
//
//            @Override
//            public void onHide() {
//
//            }
//
//            @Override
//            public void onLoadMore() {
//                // you can do your pagination here.
//            }
//        };
//        rv_ads.addOnScrollListener(scrollListener);

        return inflate;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}


