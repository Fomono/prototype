package com.fomono.fomono.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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


public class FomonoDetailActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        FomonoDetailEventbriteFragment.FomonoEventUpdateListener,
        FomonoDetailYelpFragment.FomonoEventUpdateListener,
        FomonoDetailMoviedbFragment.FomonoEventUpdateListener {

    FomonoDetailEventbriteFragment fomonoDetailEventbriteFragment;
    FomonoDetailYelpFragment fomonoDetailYelpFragment;
    FomonoDetailMoviedbFragment fomonoDetailMoviedbFragment;

    FomonoEvent fEvent;
    int position;
    boolean updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fomono_detail);
        Intent i = getIntent();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator
                (ContextCompat.getDrawable(FomonoDetailActivity.this, R.drawable.ic_arrow_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        fEvent = i.getParcelableExtra("FOM_OBJ");
        position = i.getIntExtra("position", -1);
        updated = false;

        if (savedInstanceState == null && fEvent instanceof Event) {
            Event e = (Event) fEvent;
            loadImage(e);
            fomonoDetailEventbriteFragment = FomonoDetailEventbriteFragment.newInstance(e);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailEventbriteFragment);
            ft.commit();
        } else if (savedInstanceState == null && fEvent instanceof Business) {
            Business b = (Business) fEvent;
            loadImage(b);
            fomonoDetailYelpFragment = FomonoDetailYelpFragment.newInstance(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailYelpFragment);
            ft.commit();

        } else if (savedInstanceState == null && fEvent instanceof Movie) {
            Movie m = (Movie) fEvent;
            loadImage(m);
            fomonoDetailMoviedbFragment = FomonoDetailMoviedbFragment.newInstance(m);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailMoviedbFragment);
            ft.commit();

        }
    }

    private void loadImage(FomonoEvent fEvent) {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        if(fEvent instanceof Event) {
            Event e = (Event) fEvent;
            if (e.getLogo() != null) {
                Glide.with(this).load(e.getLogo().getUrl()).centerCrop().into(imageView);
            }
        } else if(fEvent instanceof Movie){
            Movie m = (Movie) fEvent;
            Glide.with(this).load(m.getBackdropPath()).centerCrop().into(imageView);
        } else if(fEvent instanceof Business){
            Business b = (Business) fEvent;
            Glide.with(this).load(b.getImageUrl()).centerCrop().into(imageView);
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
        } else if (requestCode == FomonoApplication.PERM_CAL_EVENT_REQ_CODE || requestCode == FomonoApplication.PERM_CAL_MOVIE_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //add to calendar
                if (requestCode == FomonoApplication.PERM_CAL_EVENT_REQ_CODE && fomonoDetailEventbriteFragment != null) {
                    fomonoDetailEventbriteFragment.addToCalendar();
                }
                if (requestCode == FomonoApplication.PERM_CAL_MOVIE_REQ_CODE && fomonoDetailMoviedbFragment != null) {
                    fomonoDetailMoviedbFragment.addToCalendar();
                }
            }
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
                setFinishData();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setFinishData();
        super.onBackPressed();
    }

    private void setFinishData() {
        Intent data = new Intent();
        data.putExtra("updated", updated);
        data.putExtra("position", position);
        data.putExtra("fEvent", fEvent);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onFomonoEventUpdated() {
        updated = true;
    }
}