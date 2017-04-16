package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fomono.fomono.R;

public class MainActivity extends AppCompatActivity {

    String testPropValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Intent i = new Intent(this, LoginUserActivity.class);
       //  Intent i = new Intent(this, FomonoActivity.class);

        startActivity(i);
    }
}
