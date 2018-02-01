package com.digywood.cineauditions;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.digywood.cineauditions.Adapters.InterestsBaseAdapter;
import com.digywood.cineauditions.Pojo.SingleInterest;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class InterestedUsersActivity extends AppCompatActivity {

    ArrayList<SingleInterest> interests;
    ListView interestList;
    InterestsBaseAdapter iadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        interests = (ArrayList<SingleInterest>) getIntent().getSerializableExtra("mylist");
        Log.e("size", ""+interests.size());
        iadapter = new InterestsBaseAdapter(InterestedUsersActivity.this, interests);
        interestList = (ListView)findViewById(R.id.interests);
        interestList.setAdapter(new InterestsBaseAdapter(getApplicationContext(),interests));

    }

}
