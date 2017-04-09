package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fomono.fomono.R;
import com.fomono.fomono.fragments.FomonoDetailFragment;
import com.fomono.fomono.models.Eats.Business;
import com.fomono.fomono.models.Events.Events.Event;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.movies.Movie;


public class FomonoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbFomono);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_fomono_detail);
        Intent i = getIntent();
        FomonoEvent fEvent = i.getParcelableExtra("FOM_OBJ");
        if (savedInstanceState == null && fEvent instanceof Event) {
            Event e = (Event) fEvent;
            FomonoDetailFragment fomonoDetailFragment = FomonoDetailFragment.newInstance(e);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentEventbriteDetail, fomonoDetailFragment);
            ft.commit();
        }else if (savedInstanceState == null && fEvent instanceof Business) {
            Business b = (Business) fEvent;

        }else  if (savedInstanceState == null && fEvent instanceof Movie) {
            Movie m = (Movie) fEvent;

        }
    }
}