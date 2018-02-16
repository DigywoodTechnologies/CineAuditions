package com.digywood.cineauditions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.digywood.cineauditions.Adapters.CandidateAdapter;
import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.Pojo.Candidate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by prasa on 2018-01-25.
 */

public class ListofInterests extends AppCompatActivity {

    RecyclerView rv_candidates;
    TextView tv_emptyview;
    int advtid=0;
    CandidateAdapter candiAdp;
    ArrayList<String> nameList;
    ArrayList<String> timeList;
    ArrayList<String> numberList;
    ArrayList<String> emailList;
    ArrayList<String> commentList;
    ArrayList<Candidate> intuserList;
    HashMap<String,String> hmap=new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_layout);

        Intent cmgintent=getIntent();
        if(cmgintent!=null){

            advtid=cmgintent.getIntExtra("advtId",0);

        }

        rv_candidates=findViewById(R.id.rv_listofintrestusers);
        tv_emptyview=findViewById(R.id.tv_intrestusersemptydata);

        nameList=new ArrayList<>();
        timeList=new ArrayList<>();
        numberList=new ArrayList<>();
        emailList=new ArrayList<>();
        commentList=new ArrayList<>();
        intuserList=new ArrayList<>();

        new AsyncCheckInternet(ListofInterests.this, new INetStatus() {
            @Override
            public void inetSatus(Boolean netStatus) {
                if(netStatus){
                    getData();
                }else{
                    Toast.makeText(getApplicationContext(),"No Internet,Please Check Your Connection",Toast.LENGTH_SHORT).show();
                }

            }
        }).execute();

    }

    public void getData(){

        hmap.clear();
        hmap.put("advtId", String.valueOf(advtid));

        new BagroundTask(URLClass.hosturl+"getIntrestedAds.php", hmap, ListofInterests.this,new IBagroundListener() {
            @Override
            public void bagroundData(String json) throws JSONException {

                JSONArray ja_user_des=null,ja_user_details=null;
                JSONObject userdesjo=null,userdetailjo=null;
                try{
                    Log.e("ReturnJson---",json);
                    JSONObject myObj=new JSONObject(json);

                    Object obj1=myObj.get("user_des");

                    if (obj1 instanceof JSONArray)
                    {
                        ja_user_des=myObj.getJSONArray("user_des");
                        if(ja_user_des.length()>0){

                            Log.e("userinterests---",""+ja_user_des.length());
                            int p=0,q=0;
                            for(int i=0;i<ja_user_des.length();i++){

                                userdesjo=ja_user_des.getJSONObject(i);

                                String comment=userdesjo.getString("description");
                                String time = userdesjo.getString("uploadDateTime");
                                Log.e("ListofInterest--->time", time);
                                timeList.add(time);
                                commentList.add(comment);

                            }
                        }else{
                            Log.e("BGTuserinterests--","EmptyJsonArray ");
                        }


                        ja_user_details=myObj.getJSONArray("user_details");
                        if(ja_user_details.length()>0){

                            Log.e("userdetails---",""+ja_user_details.length());
                            int p=0,q=0;
                            for(int i=0;i<ja_user_details.length();i++){

                                userdesjo=ja_user_details.getJSONObject(i);

                                String name=userdesjo.getString("producer_name");
                                String number=userdesjo.getString("phno");
                                String email=userdesjo.getString("emailId");


                                nameList.add(name);
                                numberList.add(number);
                                emailList.add(email);

                            }
                        }else{
                            Log.e("BGTuserdetails--","EmptyJsonArray ");
                        }

                    }
                    else {
                        Log.e("pref--","No Intrestes");
                    }

                    if(timeList.size()!=0){

                        for(int i=0;i<timeList.size();i++){

                            intuserList.add(new Candidate(nameList.get(i),timeList.get(i),numberList.get(i),emailList.get(i),commentList.get(i)));

                        }

                    }
                    setData();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ListofInterests----",e.toString());
                }
            }
        }).execute();

    }

    public void setData(){

        Log.e("Listofinterests-----",""+intuserList.size());
        if (intuserList.size() != 0) {
            tv_emptyview.setVisibility(View.GONE);
            candiAdp = new CandidateAdapter(intuserList,getApplicationContext());
            final RecyclerView.LayoutManager myLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
            rv_candidates.setLayoutManager(myLayoutManager);
            rv_candidates.setItemAnimator(new DefaultItemAnimator());
            rv_candidates.setAdapter(candiAdp);
        } else {
            rv_candidates.setVisibility(View.GONE);
            tv_emptyview.setText("No Responses Found On this Advertisement");
            tv_emptyview.setVisibility(View.VISIBLE);
        }
    }

}
