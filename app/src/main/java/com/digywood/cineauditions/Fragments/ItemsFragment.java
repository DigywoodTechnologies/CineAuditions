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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.Adapters.MyAdapter;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.HidingScrollListener;
import com.digywood.cineauditions.IBagroundListener;
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

    int[] _intAdvtlist;
    int advtId;
    ArrayList<SingleAdvt> Advtlist = new ArrayList<>();;
    ArrayList<SingleCategory> CategoryList = new ArrayList<SingleCategory>();
    ArrayList<SingleSubCategory> SubCategoryList = new ArrayList<SingleSubCategory>();
    ArrayList<SinglePreference> AdvtprefList = new ArrayList<SinglePreference>();
    TextView title;
    EditText name_item,price_item,description_item;
    public ImageView imageView;
    public ListView ItemLv;
    Button upload,submit;
    Typeface myTypeface1,myTypeface2,myTypeface3,myTypeface4;
    String name_itemSt,price_itemSt,description_itemSt,shortName,ImageName,tax,group,status,createdby, createdDate, modifiedBy, modifiedDate,group1;
    ArrayList<SingleItem> ItemsList = new ArrayList<>();
    Dialog d,d1;
    MyAdapter mAdapter;
    DBHelper dbHelper;
    Uri selectedImageUri;
    String MobileNo,url;
    Spinner groupType,grpType;
    private String[] arraySpinner;


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

            getAllItemsDetailsFromHost();
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
        //ItemLv.setAdapter(new LsAdapter());

        myTypeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/sans.ttf");
        myTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/sansbold.ttf");
        myTypeface3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/exolight.otf");
        myTypeface4 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/exobold.otf");

        return inflate;
    }

    public class ContactsBaseAdapter extends BaseAdapter {
        private static final String TAG = "CONTACTS_BASE_ADAPTER";

        Context context;
        ContactsBaseAdapter(Context c){
            context = c;
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
        public View getView(int i, View convertView, ViewGroup parent) {
            final ContactsBaseAdapter.ViewHolder holder;

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.custom_noticelayout,parent, false);
            holder = new ContactsBaseAdapter.ViewHolder();
            holder.post_date = rowView.findViewById(R.id.date_notice);
            holder.caption = rowView.findViewById(R.id.caption_notice);
            holder.imageView = rowView.findViewById(R.id.photo);
//            holder.start_date = (TextView) rowView.findViewById(R.id.start_dateTv);
//            holder.end_date = (TextView) rowView.findViewById(R.id.end_dateTv);

            _intAdvtlist= new int[Advtlist.size()];
            //Log.d("BrochuresInfoList.size", "comes:" + _intAdvtlist.length);
            for (int x = 0; x < _intAdvtlist.length; x++) {

                holder.post_date.setText(Advtlist.get(i).getCreatedTime());
                holder.caption.setText(Advtlist.get(i).getCaption());

                byte[] lotsImage = Advtlist.get(i).getImage();
                Bitmap bitmap = BitmapFactory.decodeByteArray(lotsImage, 0, lotsImage.length);
                holder.imageView.setImageBitmap(bitmap);

//                holder.start_date.setText(Advtlist.get(i).getStartDate());
//                holder.end_date.setText(Advtlist.get(i).getEndDate());

            }
            final int final_i = i;

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SingleAdvt singlead = Advtlist.get(final_i);

                    Intent intent = new Intent(getActivity(),RespondAvtInfo.class);
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
                    startActivity(intent);

//                    d1=new Dialog(ListOfLots.this);
//                    d1.getWindow();
//                    d1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    d1.setContentView(R.layout.new_lot_window);
//                    d1.show();
                }
            });
            return rowView;
        }
        public class ViewHolder
        {
            public TextView post_date,caption;
            public ImageView imageView;
        }
    }


    public boolean isInternetConnected() {
        boolean iNetFlag = false;
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            iNetFlag = (connection.getResponseCode() == 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return iNetFlag;
    }

    public void getAllItemsDetailsFromHost()
    {
        dbHelper.deleteAllPrefAdvts();
        HashMap<String, String> hmap1 = new HashMap<>();
        url = URLClass.hosturl+"getUserPrefAdvtDetails.php";
        hmap1.put("userId", MobileNo);

        try {
            new BagroundTask(url,hmap1,getActivity(),new IBagroundListener() {
                @Override
                public void bagroundData(String json) {
                    try {
                        Log.e("output",json);
                        JSONArray ja = new JSONArray(json);
                        Log.d("ja", "comes:" + ja);
                        if (ja.length() != 0) {
                            JSONObject jo = null;
                            for (int j = 0; j < ja.length(); j++) {
                                try {
                                    jo = ja.getJSONObject(j);
                                    byte[] imageByte = Base64.decode(jo.getString("image"), Base64.DEFAULT);
                                    /*dbHelper.insertPrefAdvt(jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
                                            jo.getString("description"), imageByte, jo.getString("startDate"), jo.getString("endDate"),
                                            jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
                                            jo.getString("createdTime"), jo.getString("status"));*/
                                    SingleAdvt newadvt=new SingleAdvt(jo.getInt("advtId"),jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
                                            jo.getString("description"),imageByte, jo.getString("startDate"), jo.getString("endDate"),
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
//                            ItemLv.setAdapter(new ContactsBaseAdapter(getContext()));
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

