package com.digywood.cineauditions.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.digywood.cineauditions.Adapters.MyAdapter;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.FullscreenActivity;
import com.digywood.cineauditions.IBagroundListener;
import com.digywood.cineauditions.LandingActivity;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.R;
import com.digywood.cineauditions.URLClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InterestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InterestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InterestsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String url,MobileNo;
    DBHelper dbHelper;
    MyAdapter mAdapter;
    ListView InterestsLv;
    FullscreenActivity obj = new FullscreenActivity();
    ArrayList<SingleAdvt> Advtlist = new ArrayList<>();
    ArrayList<Integer> Interestlist = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public InterestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InterestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterestsFragment newInstance(String param1, String param2) {
        InterestsFragment fragment = new InterestsFragment();
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

            View inflate = inflater.inflate(R.layout.fragment_interests, container, false);
            InterestsLv = (ListView)inflate.findViewById(R.id.InterestsLv);
            dbHelper = new DBHelper(this.getContext());
        try {
            Intent cmgintent = getActivity().getIntent();
            if (cmgintent != null) {
                MobileNo = cmgintent.getStringExtra("mobileNo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Interestlist = dbHelper.getAllInterests(MobileNo);
            getAllItemsDetailsFromHost();
            mAdapter = new MyAdapter(getContext(),Advtlist,MobileNo);
            InterestsLv.setAdapter(mAdapter);

            return inflate;
        }

    public void getAllItemsDetailsFromHost()
    {
        dbHelper.deleteAllPrefAdvts();
        HashMap<String, String> hmap1 = new HashMap<>();
        url = URLClass.hosturl+"getUserPrefAdvtDetails.php";
        hmap1.put("userId", MobileNo);
        Advtlist.clear();
        try {
            new BagroundTask(url,hmap1,getActivity(),new IBagroundListener() {
                @Override
                public void bagroundData(String json) {
                    try {
                        Log.e("output",json);
                        if(json!=null){
                            JSONArray ja = new JSONArray(json);
                            Log.d("ja", "comes:" + ja);
                            if (ja.length() != 0) {
                                JSONObject jo ;
                                for (int j = 0; j < ja.length(); j++) {
                                    try {
                                        jo = ja.getJSONObject(j);
                                        byte[] imageByte = Base64.decode(jo.getString("image"), Base64.DEFAULT);
                                    /*dbHelper.insertPrefAdvt(jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
                                            jo.getString("description"), imageByte, jo.getString("startDate"), jo.getString("endDate"),
                                            jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
                                            jo.getString("createdTime"), jo.getString("status"));*/
                                        SingleAdvt newadvt=new SingleAdvt(jo.getInt("advtId"),jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
                                                jo.getString("description"),imageByte,jo.getString("startDate"), jo.getString("endDate"),
                                                jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
                                                jo.getString("createdTime"), jo.getString("status"));
                                        if(Interestlist.contains(newadvt.getAdvtRefNo())){
                                            Advtlist.add(newadvt);
                                        }
                                        //advtId = Integer.parseInt(jo.getString("advtId"));
/*                                        Log.e("InterestsFragment --->", ""+Advtlist);
                                        Log.d("ja", "" + jo.getString("advtId")+"Inserted");*/
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
/*                                mAdapter = new MyAdapter(getContext(),Advtlist,MobileNo);
                                ItemLv.setAdapter(mAdapter);*/
                            }
                        }else{

                            Log.e("InterestsFragment----","Empty Advt List");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
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
