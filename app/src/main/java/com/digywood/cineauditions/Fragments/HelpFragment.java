package com.digywood.cineauditions.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.digywood.cineauditions.Adapters.CandidateAdapter;
import com.digywood.cineauditions.Adapters.FAQAdapter;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.FullscreenActivity;
import com.digywood.cineauditions.Pojo.SingleFAQ;
import com.digywood.cineauditions.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by prasa on 2018-02-19.
 */

public class HelpFragment extends Fragment{

    private SettingsFragment.OnFragmentInteractionListener mListener;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Animation animationUp;
    private Animation animationDown;
    private final int COUNTDOWN_RUNNING_TIME = 300;

    TextView tv_q1,tv_a1;
    FAQAdapter faqAdp;
    ArrayList<SingleFAQ> faqList=new ArrayList<>();
    RecyclerView rv_listoffaqs;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HelpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpFragment newInstance(String param1, String param2) {
        HelpFragment fragment = new HelpFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_help, container, false);
        rv_listoffaqs=view.findViewById(R.id.rv_listoffaqs);
//        tv_a1=view.findViewById(R.id.tv_a1);
        animationUp = AnimationUtils.loadAnimation(getActivity(),R.anim.slideup);
        animationDown = AnimationUtils.loadAnimation(getActivity(),R.anim.slidedown);
        String[] Ques = getResources().getStringArray(R.array.Questions);
        String[] Answ = getResources().getStringArray(R.array.Answers);

        List<String> quesList = Arrays.asList(Ques);
        List<String> ansList = Arrays.asList(Answ);

        for (int i=0;i<quesList.size();i++){

            faqList.add(new SingleFAQ(quesList.get(i),ansList.get(i)));
        }

        faqAdp = new FAQAdapter(faqList,getActivity(),animationUp,animationDown);
        final RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv_listoffaqs.setLayoutManager(myLayoutManager);
        rv_listoffaqs.setItemAnimator(new DefaultItemAnimator());
        rv_listoffaqs.setAdapter(faqAdp);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
