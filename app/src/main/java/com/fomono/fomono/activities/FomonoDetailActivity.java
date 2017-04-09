package com.fomono.fomono.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.fomono.fomono.R;
import com.fomono.fomono.fragments.FomonoDetailEventbriteFragment;
import com.fomono.fomono.fragments.FomonoDetailMoviedbFragment;
import com.fomono.fomono.fragments.FomonoDetailYelpFragment;
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
            FomonoDetailEventbriteFragment fomonoDetailEventbriteFragment = FomonoDetailEventbriteFragment.newInstance(e);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailEventbriteFragment);
            ft.commit();
        }else if (savedInstanceState == null && fEvent instanceof Business) {
            Business b = (Business) fEvent;
             FomonoDetailYelpFragment fomonoDetailYelpFragment =
                     FomonoDetailYelpFragment.newInstance(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailYelpFragment);
            ft.commit();

        }else  if (savedInstanceState == null && fEvent instanceof Movie) {
            Movie m = (Movie) fEvent;
            FomonoDetailMoviedbFragment fomonoDetailMoviedbFragment =
                    FomonoDetailMoviedbFragment.newInstance(m);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailMoviedbFragment);
            ft.commit();

        }
    }
}