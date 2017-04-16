package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fomono.fomono.R;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.utils.ConfigUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String testPropValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Intent i = new Intent(this, LoginUserActivity.class);
       //  Intent i = new Intent(this, FomonoActivity.class);

        FomonoEvent fEvent = new Business();
        //Sample Read test property file entries
        try {
            testPropValue = ConfigUtil.getProperty("115",getApplicationContext());
        } catch (IOException e) {
            testPropValue ="No Entry in prop file";
            e.printStackTrace();
        }
//        i.putExtra("FOM_OBJ", fEvent);
        startActivity(i);
    }
}
