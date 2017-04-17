package com.fomono.fomono.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.fragments.FomonoDetailEventbriteFragment;
import com.fomono.fomono.fragments.FomonoDetailMoviedbFragment;
import com.fomono.fomono.fragments.FomonoDetailYelpFragment;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.models.user.User;
import com.parse.ParseUser;


public class FomonoDetailActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    FomonoDetailEventbriteFragment fomonoDetailEventbriteFragment;
    FomonoDetailYelpFragment fomonoDetailYelpFragment;
    FomonoDetailMoviedbFragment fomonoDetailMoviedbFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fomono_detail);
        Intent i = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbFomono);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_fomono_detail));

        //turn on back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FomonoEvent fEvent = i.getParcelableExtra("FOM_OBJ");

        if (savedInstanceState == null && fEvent instanceof Event) {
            Event e = (Event) fEvent;
            fomonoDetailEventbriteFragment = FomonoDetailEventbriteFragment.newInstance(e);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailEventbriteFragment);
            ft.commit();
        } else if (savedInstanceState == null && fEvent instanceof Business) {
            Business b = (Business) fEvent;
            fomonoDetailYelpFragment = FomonoDetailYelpFragment.newInstance(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailYelpFragment);
            ft.commit();

        } else if (savedInstanceState == null && fEvent instanceof Movie) {
            Movie m = (Movie) fEvent;
            fomonoDetailMoviedbFragment = FomonoDetailMoviedbFragment.newInstance(m);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailMoviedbFragment);
            ft.commit();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FomonoApplication.PERM_LOC_EVENT_REQ_CODE ||
                requestCode == FomonoApplication.PERM_LOC_BUS_REQ_CODE ||
                requestCode == FomonoApplication.PERM_LOC_MOVIE_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //refresh map
                if (requestCode == FomonoApplication.PERM_LOC_EVENT_REQ_CODE && fomonoDetailEventbriteFragment != null) {
                    fomonoDetailEventbriteFragment.enableMapLocation();
                }
                if (requestCode == FomonoApplication.PERM_LOC_BUS_REQ_CODE && fomonoDetailYelpFragment != null) {
                    fomonoDetailYelpFragment.enableMapLocation();
                }
                if (requestCode == FomonoApplication.PERM_LOC_MOVIE_REQ_CODE && fomonoDetailMoviedbFragment != null) {
                    //TODO
                }
            }

            //set a flag on the user
            ParseUser user = ParseUser.getCurrentUser();
            user.put(User.LOC_PERM_SEEN, true);
            user.saveInBackground();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}