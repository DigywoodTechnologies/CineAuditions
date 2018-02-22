package com.digywood.cineauditions.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.digywood.cineauditions.AsyncTasks.AsyncCheckInternet;
import com.digywood.cineauditions.AsyncTasks.BagroundTask;
import com.digywood.cineauditions.DBHelper.DBHelper;
import com.digywood.cineauditions.IBagroundListener;
import com.digywood.cineauditions.INetStatus;
import com.digywood.cineauditions.Pojo.SingleProducer;
import com.digywood.cineauditions.R;
import com.digywood.cineauditions.URLClass;

import org.json.JSONException;

import java.util.HashMap;

public class FeedbackFragment extends Fragment {

    private SettingsFragment.OnFragmentInteractionListener mListener;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Animation animationUp;
    private Animation animationDown;
    private final int COUNTDOWN_RUNNING_TIME = 300;

    DBHelper myhelper;
    TextView tv_name,tv_number,tv_email;
    EditText et_subject,et_comment;
    Button btn_submit;
    SingleProducer user;
    String name,number,email,version="";
    HashMap<String,String> hmap=new HashMap<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeedbackFragment() {
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
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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
        View view = inflater.inflate(R.layout.activity_feedback, container, false);
        tv_name=view.findViewById(R.id.tv_feedusername);
        tv_number=view.findViewById(R.id.tv_feednumber);
        tv_email=view.findViewById(R.id.tv_feedmail);
        et_subject=view.findViewById(R.id.et_feedsubject);
        et_comment=view.findViewById(R.id.et_feedcomment);
        btn_submit=view.findViewById(R.id.btn_feedsubmit);

        myhelper=new DBHelper(getActivity());

        Intent cmgintent = getActivity().getIntent();
        if (cmgintent != null) {
            number=cmgintent.getStringExtra("mobileNo");
            tv_number.setText(number);
        }

        if(number!=null){
            Log.e("FeedBack--->",number);
            user= myhelper.getProducer(number);
            if(user!=null){
                Log.e("Landing--->", user.getName());
                name=user.getName();
                email=user.getEmailId();
                tv_name.setText(name);
                tv_email.setText(email);

            }else{
                Log.e("LandingActivity-----","Empty User");
            }

        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
                    version= pInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Log.e("FeedbackActivity---",e.toString());
                }

                if(et_subject.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getActivity(),"Please Enter Subject",Toast.LENGTH_SHORT).show();
                }else{
                    if(et_comment.getText().toString().equalsIgnoreCase("")){
                        Toast.makeText(getActivity(),"Please Enter Feedback Comment",Toast.LENGTH_SHORT).show();
                    }else{

                        hmap.clear();
                        hmap.put("userId",number);
                        hmap.put("email",email);
                        hmap.put("version",version);
                        hmap.put("subject",et_subject.getText().toString());
                        hmap.put("message",et_comment.getText().toString());

                        new AsyncCheckInternet(getActivity(),new INetStatus() {
                            @Override
                            public void inetSatus(Boolean netStatus) {

                                new BagroundTask(URLClass.hosturl+"insert_feedback.php", hmap,getActivity(), new IBagroundListener() {
                                    @Override
                                    public void bagroundData(String json) throws JSONException {

                                        try {
                                            Log.e("FeedbackFragment----",json);
                                            if(json.equalsIgnoreCase("Inserted")){

                                                Toast.makeText(getActivity(),"Feedback Submitted",Toast.LENGTH_SHORT).show();
                                                et_subject.setText("");
                                                et_comment.setText("");

                                            }else{
                                                Toast.makeText(getActivity(),"Unable Insert,Try Again Later",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            Log.e("FeedbackFragment----",e.toString());
                                        }


                                    }
                                }).execute();

                            }
                        }).execute();

                    }
                }

            }
        });

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
