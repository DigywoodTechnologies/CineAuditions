package com.digywood.cineauditions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.digywood.cineauditions.DBHelper.DBHelper;

public class SettingsActivity extends AppCompatActivity {

    Button logout;
    DBHelper dbHelper;
    String phno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        phno = getIntent().getStringExtra("phno");

        dbHelper = new DBHelper(SettingsActivity.this);
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteProducer(phno);
                Intent intent = new Intent(SettingsActivity.this, FullscreenActivity.class);
                startActivity(intent);
            }
        });

    }

}
