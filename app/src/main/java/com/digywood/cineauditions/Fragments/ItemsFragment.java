package com.digywood.cineauditions.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.Adapters.MyAdapter;
import com.digywood.cineauditions.AdvtInfoScreen;
import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.HidingScrollListener;
import com.digywood.cineauditions.IBagroundListener;
import com.digywood.cineauditions.INetStatus;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.Pojo.SingleCategory;
import com.digywood.cineauditions.Pojo.SingleItem;
import com.digywood.cineauditions.Pojo.SinglePreference;
import com.digywood.cineauditions.Pojo.SingleSubCategory;
import com.digywood.cineauditions.R;
import com.digywood.cineauditions.RespondAvtInfo;
import com.digywood.cineauditions.Fragments.SetPreferencesFragment;
import com.digywood.cineauditions.URLClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<SingleAdvt> Advtlist = new ArrayList<>();
    ArrayList<SinglePreference> AdvtprefList = new ArrayList<SinglePreference>();
    public ListView ItemLv;
    HashMap<String,String> hmap=new HashMap<>();
    TextView tv_emptydata;
    boolean flag_loading=false;
    MyAdapter mAdapter;
    DBHelper dbHelper;
    String MobileNo,url;

    final int REQUEST_CODE_GALLERY = 999;

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";

    private OnFragmentInteractionListener mListener;

    public ItemsFragment() {
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
    public static ItemsFragment newInstance(String param1, String param2) {
        ItemsFragment fragment = new ItemsFragment();
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
        View inflate = inflater.inflate(R.layout.fragment_items, container, false);

        ItemLv = (ListView) inflate.findViewById(R.id.ItemsLv);
        tv_emptydata=inflate.findViewById(R.id.tv_emptydata);
        dbHelper = new DBHelper(this.getContext());
        Intent cmgintent=getActivity().getIntent();
        if (cmgintent != null) {
            MobileNo = cmgintent.getStringExtra("mobileNo");
            //tv_producer_phno.setText(MobileNo);
        }
        SetPreferencesFragment obj = new SetPreferencesFragment();
        obj.setChecked();
        //Checking for user preference locally
        int checkFlag = 0;
        checkFlag = (int) dbHelper.checkPreferencesExist(MobileNo);
        if (checkFlag!=0) {
            //dbHelper.deleteAllPreferences();
            AdvtprefList = dbHelper.getAllPreferencesUser(MobileNo);
            Log.d("AdvtprefList.size", "comes:" + AdvtprefList.size()+"||"+MobileNo);
//            Advtlist = dbHelper.getPrefAdvtProducer();
//            Advtlist = dbHelper.getPrefAdvtProducer();
            //contact server to get all the advertisements

            new AsyncCheckInternet(getActivity(), new INetStatus() {
                @Override
                public void inetSatus(Boolean netStatus) {
                    if(netStatus){
                        hmap.put("userId",MobileNo);
                        hmap.put("offset",String.valueOf(0));
                        getAllItemsDetailsFromHost(hmap);
                    }else{
                        Toast.makeText(getActivity(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
            Log.d("Advtlist.size", "comes:" + Advtlist.size()+"||"+MobileNo);

        }else{
            //Toast.makeText(getActivity(), "Preferences Not Exist", Toast.LENGTH_SHORT).show();
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            builder.setTitle("");
            builder.setMessage("Please Select Preferences");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Fragment fragment = new SetPreferencesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    //SetPreferencesFragment setPreferencesFragment = new SetPreferencesFragment();
                    //return setPreferencesFragment;
//                    Intent intent=new Intent(getActivity(),SetPreferencesFragment.class);
//                    startActivity(intent);
                }
            });
            android.support.v7.app.AlertDialog alert1 = builder.create();
            alert1.show();
        }

        //_intRadio= new int[ItemsList.size()];
        //ItemLv.setAdapter(new LsAdapter())

//        ItemLv.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                Log.e("ItemsFragment----","first: "+firstVisibleItem+" : visibleCount: "+visibleItemCount+" : totalItemCount: "+totalItemCount);
//                if(firstVisibleItem+visibleItemCount==totalItemCount && totalItemCount!=0){
//
//                    if(flag_loading == false){
//
//                        flag_loading=true;
//                        hmap.clear();
//                        hmap.put("userId",MobileNo);
//                        hmap.put("offset",String.valueOf(Advtlist.size()));
//                        Log.e("OffSet",""+Advtlist.size());
//                        pgetAllItemsDetailsFromHost(hmap);
//                    }
//                }
//            }
//        });

        ItemLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;
            private LinearLayout lBelow;


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;


            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {

                    new AsyncCheckInternet(getActivity(), new INetStatus() {
                        @Override
                        public void inetSatus(Boolean netStatus) {
                            if(netStatus){
                                hmap.clear();
                                hmap.put("userId",MobileNo);
                                hmap.put("offset",String.valueOf(Advtlist.size()));
                                Log.e("OffSet",""+Advtlist.size());
                                pgetAllItemsDetailsFromHost(hmap);
                            }else{
                                Toast.makeText(getActivity(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).execute();

                }
            }
        });


        return inflate;
    }

//    public void addItems(){
//
//        int value=Advtlist.size();
//        SingleAdvt myad=Advtlist.get(value-1);
//
//        HashMap<String, String> hmap1 = new HashMap<>();
//        url = URLClass.hosturl+"sample.php";
//        hmap1.put("userId", MobileNo);
//        hmap1.put("advtId",String.valueOf(myad.getAdvtRefNo()));
//
//        try {
//            new BagroundTask(url,hmap1,getActivity(),new IBagroundListener() {
//                @Override
//                public void bagroundData(String json) {
//                    try {
//                        Log.e("output",json);
//                        if(json!=null){
//
//                        }else{
//
//                        }
//                        JSONArray ja = new JSONArray(json);
//                        Log.d("ja", "comes:" + ja);
//                        if (ja.length() != 0) {
//                            JSONObject jo = null;
//                            for (int j = 0; j < ja.length(); j++) {
//                                try {
//                                    jo = ja.getJSONObject(j);
//                                    byte[] imageByte = Base64.decode(jo.getString("image"), Base64.DEFAULT);
//                                    /*dbHelper.insertPrefAdvt(jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
//                                            jo.getString("description"), imageByte, jo.getString("startDate"), jo.getString("endDate"),
//                                            jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
//                                            jo.getString("createdTime"), jo.getString("status"));*/
//                                    SingleAdvt newadvt=new SingleAdvt(jo.getInt("advtId"),jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
//                                            jo.getString("description"),imageByte, jo.getString("startDate"), jo.getString("endDate"),
//                                            jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
//                                            jo.getString("createdTime"), jo.getString("status"));
//                                    Advtlist.add(newadvt);
//                                    //advtId = Integer.parseInt(jo.getString("advtId"));
//                                    Log.d("ja", "" + jo.getString("advtId")+"Inserted");
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
////                            mAdapter.updateList(Advtlist);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).execute();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        flag_loading=false;
//
//    }

    public void getAllItemsDetailsFromHost(HashMap<String,String> hmap)
    {
//        dbHelper.deleteAllPrefAdvts();

        url = URLClass.hosturl+"getUserPrefAdvtDetails.php";

        try {
            new BagroundTask(url,hmap,getActivity(),new IBagroundListener() {
                @Override
                public void bagroundData(String json) {
                    try {
                        Log.e("output",json);
                        if(!json.equalsIgnoreCase("No-Records-Found")){
                            JSONArray ja = new JSONArray(json);
                            Log.d("ja", "comes:" + ja);
                            if (ja.length() != 0) {
                                JSONObject jo = null;
                                for (int j = 0; j < ja.length(); j++) {
                                    try {
                                        jo = ja.getJSONObject(j);
                                        SingleAdvt newadvt=new SingleAdvt(jo.getInt("advtId"),jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
                                                jo.getString("description"),jo.getString("fileType"),jo.getString("fileName"),jo.getString("filePath"),jo.getString("startDate"), jo.getString("endDate"),
                                                jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
                                                jo.getString("createdTime"), jo.getString("status"));
                                        Advtlist.add(newadvt);
                                        //advtId = Integer.parseInt(jo.getString("advtId"));
                                        Log.d("ja", "" + jo.getString("advtId")+"Inserted");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                mAdapter = new MyAdapter(getContext(),Advtlist,MobileNo);
                                ItemLv.setAdapter(mAdapter);
                            }
                        }else{
                            tv_emptydata.setVisibility(View.VISIBLE);
                            ItemLv.setSystemUiVisibility(View.GONE);
                            tv_emptydata.setText("No Recent Posts Found");
                            Log.e("ItemsFragment----","Empty Advt List");

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

    public void pgetAllItemsDetailsFromHost(HashMap<String,String> hmap)
    {

        url = URLClass.hosturl+"getUserPrefAdvtDetails.php";

        try {
            new BagroundTask(url,hmap,getActivity(),new IBagroundListener() {
                @Override
                public void bagroundData(String json) {
                    try {
                        Log.e("output",json);
                        if(!json.equalsIgnoreCase("No-Records-Found")){
                            JSONArray ja = new JSONArray(json);
                            Log.d("ja", "comes:" + ja);
                            if (ja.length() != 0) {
                                JSONObject jo = null;
                                for (int j = 0; j < ja.length(); j++) {
                                    try {
                                        jo = ja.getJSONObject(j);
                                        SingleAdvt newadvt=new SingleAdvt(jo.getInt("advtId"),jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
                                                jo.getString("description"),jo.getString("fileType"),jo.getString("fileName"),jo.getString("filePath"),jo.getString("startDate"), jo.getString("endDate"),
                                                jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
                                                jo.getString("createdTime"), jo.getString("status"));
                                        Advtlist.add(newadvt);
                                        //advtId = Integer.parseInt(jo.getString("advtId"));
                                        Log.d("ja", "" + jo.getString("advtId")+"Inserted");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(Advtlist.size()!=0){
                                    Log.e("ItemsFragment----","AdvtListSize: "+Advtlist.size());
                                    for(int i=0;i<Advtlist.size();i++){

                                        Log.e("AdvtId: ",""+Advtlist.get(i).getAdvtRefNo());

                                    }
                                }

                                mAdapter.updateList(Advtlist);
                            }
                        }else{
//                            tv_emptydata.setVisibility(View.VISIBLE);
//                            ItemLv.setSystemUiVisibility(View.GONE);
//                            tv_emptydata.setText("No Recent Posts Found");
                            Log.e("ItemsFragment----","Empty Advt List");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        flag_loading=false;
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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

