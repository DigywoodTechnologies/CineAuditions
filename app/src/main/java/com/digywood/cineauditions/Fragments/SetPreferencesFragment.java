package com.digywood.cineauditions.Fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import com.digywood.cineauditions.AsyncTasks.BagroundAsynkTask;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.IBagroundListener;
import com.digywood.cineauditions.Pojo.SingleAdvt;
import com.digywood.cineauditions.Pojo.SingleCategory;
import com.digywood.cineauditions.Pojo.SingleProducer;
import com.digywood.cineauditions.Pojo.SingleSubCategory;
import com.digywood.cineauditions.R;

public class SetPreferencesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView tv_producer_phno;
    int[] _intSubCat,_intCat;
    DBHelper dbHelper;
    TextView tv_advtlist;
    Typeface myTypeface1,myTypeface2;
    public ListView AdvtsLv;
    ArrayList<SingleAdvt> Advtlist;
    String MobileNo,orgId,url,status,subCatName;
    Button submit_pref;
    ListView advt_lv;
    CheckBox checkBox;
    TextView userName;
    SingleProducer user;
    long count=0;
    ArrayList<SingleCategory> CategoryList = new ArrayList<>();
    ArrayList<String> checkedPrefList = new ArrayList<>();
    ArrayList<String> Allpref = new ArrayList<>();
    ArrayList<SingleSubCategory> SubCategoryList = new ArrayList<>();
    ArrayList<SingleSubCategory> SelectedSubCategoryList = new ArrayList<>();
    final ArrayList<String> CategoryNamesList = new ArrayList<>();
    final ArrayList<String> SubCategoryNamesList = new ArrayList<>();
    ArrayList<String> AdvtprefList = new ArrayList<>();

//    Spinner s1;
//    GridView grid;

    final int REQUEST_CODE_GALLERY = 999;

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "MainActivity";

    private OnFragmentInteractionListener mListener;

    public SetPreferencesFragment() {
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
    public static SetPreferencesFragment newInstance(String param1, String param2) {
        SetPreferencesFragment fragment = new SetPreferencesFragment();
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
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_set_preferences,container,false);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            MobileNo = bundle.getString("mobileNo");
            orgId = "ORG001";
            //tv_producer_phno.setText(MobileNo);
        }
//        setChecked();
        advt_lv=inflate.findViewById(R.id.PreferencesLv);
        submit_pref =inflate.findViewById(R.id.submit_pref);
        userName = (TextView) inflate.findViewById(R.id.pref_userName);
//        View convertView = getView();
//        setChecked();
        dbHelper = new DBHelper(this.getContext());
        CategoryList = dbHelper.getAllCategories();
        SubCategoryList = dbHelper.getAllSubCategories();

        user = dbHelper.getProducer(MobileNo);
        if(user!=null){

            userName.setText(user.getName());
            userName.setTypeface(myTypeface1);

        }else{
            Log.e("SetPreferencesFragment","Empty User");
        }

        _intSubCat= new int[SubCategoryList.size()];
        _intCat= new int[CategoryList.size()];
        AdvtprefList=dbHelper.getActivePref("A",MobileNo);
//        count=dbHelper.getActivePrefCount("A",MobileNo);
//        Log.e("SetPreferenceFrag---",""+count);
        Allpref=dbHelper.getAllPref(MobileNo);
        advt_lv.setAdapter(new ContactsBaseAdapter(getActivity()));
        status="Waiting";
//        AdvtprefList = dbHelper.getAllPreferencesUser(MobileNo);

        for(int i=0;i<SubCategoryList.size();i++){
            subCatName = SubCategoryList.get(i).getLongName();
            //Log.d("1:::AdvtprefList.size()", "" + AdvtprefList.size()+"||subCatName::"+subCatName);
            for(int j=0;j<AdvtprefList.size();j++){
                //Log.d("2:::AdvtprefList.size()", "" +AdvtprefList.get(j).getCategory()+"||subCatName::"+subCatName);
                if(AdvtprefList.get(j).equals(subCatName)) {
                    Log.e("advtlist", "" + AdvtprefList.size()+"||Advtprefsubcat::"+AdvtprefList.get(j)+"||subCatName::"+subCatName);
//                    checkBox.setChecked(true);
                }
                else {
//                    checkBox.setChecked(false);
                }
            }
        }

        submit_pref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("insertDetails:", "" + SelectedSubCategoryList.size());
                url = "http://www.digywood.com/phpfiles/cinesooruProducer/insertPreferencesofUser.php";
                try {
                        BagroundAsynkTask task = new BagroundAsynkTask(url, CategoryList, SelectedSubCategoryList,  MobileNo, orgId, dbHelper, getActivity(), new IBagroundListener() {
                            @Override
                            public void bagroundData(String json) {
                            Log.e("PrefFragment-------", json);
                            if (json.equals("Inserted")) {
//                                dbHelper.deleteAllPrefAdvts();
                                HashMap<String, String> hmap1 = new HashMap<>();
                                url = "http://www.digywood.com/phpfiles/cinesooruProducer/getUserPrefAdvtDetails.php";
                                hmap1.put("userId", MobileNo);
                                try {

                                    Log.e("InnerBackSelectList---",""+SelectedSubCategoryList.size());
                                    Log.e("InnerBackAllPref---",""+Allpref.size());

                                    int inserts=0,updates=0,p=0,q=0,r=0,s=0,x=0,y=0;
                                    for(int i=0;i<SelectedSubCategoryList.size();i++){

                                        SingleSubCategory ssc=SelectedSubCategoryList.get(i);

//                                        Log.e("If Cond---",ssc.getSubCategoryId());

//                                        String subcatname=dbHelper.getSubCategoryName(ssc.getSubCategoryId());

                                        if(Allpref.contains(ssc.getSubCategoryId())){

                                            long updateFlag=dbHelper.updatePref(MobileNo,ssc.getSubCategoryId(),ssc.getStatus());
                                            if(updateFlag>0){
                                                x++;
                                            }else{
                                                y++;
                                            }

                                        }else{

                                            if(ssc.getUploadstatus().equalsIgnoreCase("I")){
                                                inserts++;

                                                long insertFlag=dbHelper.insertPref(ssc.getOrgId(),MobileNo,ssc.getCategoryId(),ssc.getSubCategoryId(),ssc.getStatus());
                                                if(insertFlag>0){
                                                    p++;
                                                }else{
                                                    q++;
                                                }

                                            }else{

                                                updates++;
                                                long updateFlag=dbHelper.updatePref(MobileNo,ssc.getSubCategoryId(),ssc.getStatus());
                                                if(updateFlag>0){
                                                    r++;
                                                }else{
                                                    s++;
                                                }

                                            }
                                        }

                                    }

                                    if(p==inserts){

                                        Log.e("PrefFragment----","Inserted: "+p+" : "+q);
                                    }else{
                                        Log.e("PrefFragment----","Not Inserted: "+p+" : "+q);
                                    }

                                    if(r==updates){

                                        Log.e("PrefFragment----","Updated: "+r+" : "+s);
                                    }else{
                                        Log.e("PrefFragment----","Not Updated: "+r+" : "+s);
                                    }

                                    Log.e("ExistUpdate----","Data: "+x+" : "+y);

                                    BagroundTask task1 = new BagroundTask(url,hmap1,getActivity(),new IBagroundListener() {
                                        @Override
                                        public void bagroundData(String json) {
                                            try {
                                                JSONArray ja = new JSONArray(json);
                                                Log.d("ja", "comes:" + ja);
                                                if (ja.length() != 0) {
                                                    JSONObject jo = null;
                                                    for (int j = 0; j < ja.length(); j++) {
                                                        try {
                                                            jo = ja.getJSONObject(j);

                                                            byte[] imageByte = Base64.decode(jo.getString("image"),Base64.DEFAULT);
                                                            dbHelper.insertPrefAdvt(jo.getInt("advtId"),jo.getString("orgId"),jo.getString("userId"),jo.getString("caption"),
                                                                    jo.getString("description"), imageByte, jo.getString("startDate"), jo.getString("endDate"),
                                                                    jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
                                                                    jo.getString("createdTime"), jo.getString("status"));
                                                            //advtId = Integer.parseInt(jo.getString("advtId"));
                                                            Log.d("ja", "" + jo.getString("advtId")+"Inserted");
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    task1.execute();
                                    submit_pref.setVisibility(View.INVISIBLE);
                                    Fragment fragment = new ItemsFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.framelayout_items, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getActivity(), "Prefrences Updated to Server", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Preferences Updation to Server failed", Toast.LENGTH_SHORT).show();
                            }
                                }
                        });
                        task.execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        myTypeface1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/sans.ttf");
        myTypeface2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/sansbold.ttf");
        return inflate;
    }

    public class ContactsBaseAdapter extends BaseAdapter {

        ArrayList<Boolean> checkValues = new ArrayList<>();

        private static final String TAG = "CONTACTS_BASE_ADAPTER";

        boolean[] checkBoxState = new boolean[SubCategoryList.size()];

        ViewHolder viewHolder;
        SharedPreferences.Editor editor;

        Context context;

        public ContactsBaseAdapter(Context c) {
            context = c;
            checkValues.clear();

            Log.e("AdvtprefList-----",""+AdvtprefList.size());
            Log.e("Allpref----",""+Allpref.size());
            for (int x = 0; x < SubCategoryList.size(); x++) {
                if (AdvtprefList.size() == 0) {
                    checkValues.add(false);
                } else {
                    int a = 0;
                    String l = SubCategoryList.get(x).getSubCategoryId();
                    for (int j = 0; j < AdvtprefList.size(); j++) {
//                        String subcatname=dbHelper.getSubCategoryName(AdvtprefList.get(j));
                        if (AdvtprefList.get(j).equals(l)) {
//                            checkedPrefList.add(AdvtprefList.get(x).getSubCategory());
                            checkedPrefList.add(AdvtprefList.get(j));
//                            SubCategoryList.get(x).
//                            SelectedSubCategoryList.add(SubCategoryList.get(x));
                            checkValues.add(true);
                            a = 1;
                        }
                        if (j == AdvtprefList.size() - 1) {
                            if (a == 0) {
                                checkValues.add(false);
                            }
                            a = 0;
                        }

                    }
                }

            }

        }

        @Override
        public int getCount() {
            return SubCategoryList.size();
        }

        @Override
        public Object getItem(int i) {
            return SubCategoryList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            //SharedPreferences sharedPrefs = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);

            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.custom_grid, null);
            viewHolder = new ViewHolder();

            viewHolder.category = convertView.findViewById(R.id.cat_list);
            viewHolder.subcategory = convertView.findViewById(R.id.subcat_list);
            checkBox=convertView.findViewById(R.id.checkbox_list);


            String categoryId = SubCategoryList.get(position).getCategoryId();

            viewHolder.subcategory.setText(SubCategoryList.get(position).getLongName());

            for (int i = 0; i < CategoryList.size(); i++) {
                if (categoryId.equals(CategoryList.get(i).getCategoryId())) {
                    viewHolder.category.setText(CategoryList.get(i).getLongName());
                }
            }

            checkBox.setTag(position);
            if (checkValues.size() != 0) {
                checkBox.setChecked(checkValues.get(position));
            }
//            editor = sharedPrefs.edit();
//            checkBox.setChecked(sharedPrefs.getBoolean("CheckValue" + position, false));
//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    editor.putBoolean("CheckValue" + position, isChecked);
//                    editor.commit();
//                    int iPos = (Integer) buttonView.getTag();
//                    if (isChecked) {
//                        checkValues.set(iPos, true);
//                    } else {
//                        checkValues.set(iPos, false);
//                    }
//
//                }
//            });

            checkBox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    if (((CheckBox) v).isChecked()) {
                        checkBoxState[position] = true;

                        String subcatname=SubCategoryList.get(position).getSubCategoryId();

//                        String subcatname=dbHelper.getSubCategoryName(SubCategoryList.get(position).getSubCategoryId());
                        if(Allpref.contains(subcatname)){
                            SubCategoryList.get(position).setUploadstatus("U");
                            SubCategoryList.get(position).setStatus("A");
                            SelectedSubCategoryList.add(SubCategoryList.get(position));
                        }else{
                            SubCategoryList.get(position).setUploadstatus("I");
                            SubCategoryList.get(position).setStatus("A");
                            String categoryId = SubCategoryList.get(position).getCategoryId();
                            SubCategoryNamesList.add(SubCategoryList.get(position).getLongName());
                            SelectedSubCategoryList.add(SubCategoryList.get(position));
                            for (int i = 0; i < CategoryList.size(); i++) {
                                if (categoryId.equals(CategoryList.get(i).getCategoryId())) {
                                    CategoryNamesList.add(CategoryList.get(i).getLongName());
                                }
                            }
                        }

                        Log.d("last check", "" + SubCategoryNamesList.size() + ":"+ SubCategoryNamesList);
                        Log.d("last check", "" + CategoryNamesList.size() + ":" + CategoryNamesList);
                    } else {
                        checkBoxState[position] = false;

                        if(checkedPrefList.size()!=0){
//                            Log.e("If Cond---",SubCategoryList.get(position).getSubCategoryId());
                            String subcatname=SubCategoryList.get(position).getSubCategoryId();
//                            String subcatname=dbHelper.getSubCategoryName(SubCategoryList.get(position).getSubCategoryId());
//                            Log.e("If Cond---",subcatname);

                            if(Allpref.contains(subcatname)){
                                SubCategoryList.get(position).setUploadstatus("U");
                                SubCategoryList.get(position).setStatus("D");
                                SelectedSubCategoryList.add(SubCategoryList.get(position));
                            }else{
                                String categoryId = SubCategoryList.get(position).getCategoryId();
                                SubCategoryNamesList.remove(SubCategoryList.get(position).getLongName());
                                SelectedSubCategoryList.remove(SubCategoryList.get(position));
                                for (int i = 0; i < CategoryList.size(); i++) {
                                    if (categoryId.equals(CategoryList.get(i).getCategoryId())) {
                                        CategoryNamesList.remove(CategoryList.get(i).getLongName());
                                    }
                                }
                            }

                        }else{
                            String categoryId = SubCategoryList.get(position).getCategoryId();
                            SubCategoryNamesList.remove(SubCategoryList.get(position).getLongName());
                            SelectedSubCategoryList.remove(SubCategoryList.get(position));
                            for (int i = 0; i < CategoryList.size(); i++) {
                                if (categoryId.equals(CategoryList.get(i).getCategoryId())) {
                                    CategoryNamesList.remove(CategoryList.get(i).getLongName());
                                }
                            }

                            Log.d("last check", "" + SubCategoryNamesList.size() + ":"
                                    + SubCategoryNamesList);
                            Log.d("last check", "" + CategoryNamesList.size() + ":"
                                    + CategoryNamesList);
                        }

                    }

                    if(SelectedSubCategoryList.size()!=0){

                        Log.e("PrefFragment------",""+SelectedSubCategoryList.size());

                    }
                }
            });

            return convertView;
        }

        public class ViewHolder {
            public TextView category, subcategory;
        }

    }
        public void setChecked() {
            HashMap<String, String> hmap1 = new HashMap<>();
            url = "http://www.digywood.com/phpfiles/cinesooruProducer/getUserPreferences.php";
            hmap1.put("userId", MobileNo);
            try {
                BagroundTask task1 = new BagroundTask(url, hmap1, getActivity(), new IBagroundListener() {
                    @Override
                    public void bagroundData(String json) {
                        try {
                            int checkFlag = 0;
                            JSONArray ja = new JSONArray(json);
                            Log.d("ja", "comes:" + ja);
                            if (ja.length() != 0) {
                                JSONObject jo = null;
                                for (int j = 0; j < ja.length(); j++) {
                                    try {
                                        jo = ja.getJSONObject(j);
//                                    byte[] imageByte = Base64.decode(jo.getString("image"), Base64.DEFAULT);
                                        checkFlag =dbHelper.checkPreferencesExist(MobileNo);
                                        if (checkFlag != 0) {
                                            dbHelper.updatePreference(jo.getString("orgId"),jo.getString("userId"),jo.getString("category"), jo.getString("subCategory"),
                                                    jo.getString("createdBy"),jo.getString("createdDate"),jo.getString("modifiedBy"), jo.getString("modifiedDate"), jo.getString("status"));
                                        } else {
                                            dbHelper.insertNewPreference(jo.getString("orgId"),jo.getString("userId"), jo.getString("category"), jo.getString("subCategory"),
                                                    jo.getString("createdBy"), jo.getString("createdDate"), jo.getString("modifiedBy"), jo.getString("modifiedDate"), jo.getString("status"));
                                        }

                                        Log.d("test",""+dbHelper.checkPreferencesExist(MobileNo));
                                    /*dbHelper.insertPrefAdvt(jo.getString("orgId"), jo.getString("userId"), jo.getString("caption"),
                                            jo.getString("description"), imageByte, jo.getString("startDate"), jo.getString("endDate"),
                                            jo.getString("contactName"), jo.getString("contactNumber"), jo.getString("emailId"),
                                            jo.getString("createdTime"), jo.getString("status"));
                                    //advtId = Integer.parseInt(jo.getString("advtId"));
*/
                                        Log.d("ja", "" + jo.getString("advtId") + "Inserted");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                task1.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}


