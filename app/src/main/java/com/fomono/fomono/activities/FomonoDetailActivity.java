package com.fomono.fomono.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbFomono);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_fomono_detail);
        Intent i = getIntent();
        FomonoEvent fEvent = i.getParcelableExtra("FOM_OBJ");
        // FomonoEvent fEvent = generateFakeEventObject();
        // FomonoEvent fEvent = generateFakeEatObject();

        if (savedInstanceState == null && fEvent instanceof Event) {
            Event e = (Event) fEvent;
            fomonoDetailEventbriteFragment = FomonoDetailEventbriteFragment.newInstance(e);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailEventbriteFragment);
            ft.commit();
        }else if (savedInstanceState == null && fEvent instanceof Business) {
            Business b = (Business) fEvent;
            fomonoDetailYelpFragment = FomonoDetailYelpFragment.newInstance(b);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragmentDetail, fomonoDetailYelpFragment);
            ft.commit();

        }else  if (savedInstanceState == null && fEvent instanceof Movie) {
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
                    fomonoDetailEventbriteFragment.enableMapLocation();
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

    //
//    private FomonoEvent generateFakeEventObject() {
//
//        Event e = new Event();
//
//        Start startDate = new Start();
//        End endDate = new End();
//        startDate.setUtc("2017-05-20T12:00:00Z");
//        endDate.setUtc("2017-05-21T12:00:00Z");
//        Name name = new Name();
//        name.setText("Holi Montreux");
//        Description d = new Description();
//        d.setText("Holi is an Indian inspired festivity, created to celebrate the rebirth of Spring and new beginnings!");
//        Logo l = new Logo();
//        l.setUrl("https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F29366453%2F140913306390%2F1%2Foriginal.jpg?h=200&w=450&rect=177%2C410%2C1090%2C545&s=8aa1cb74651213865e999a3d9fd51eb0");
//
//        e.setName(name);
//        e.setStart(startDate);
//        e.setEnd(endDate);
//        e.setDescription(d);
//        e.setLogo(l);
//        e.setVenueId("18800935");
//        e.setUrl("https://www.eventbrite.com/e/holi-montreux-tickets-32905167241?aff=ebapi");
//        return e;
//
//
//    }
//    private FomonoEvent generateFakeMovieObject() {
//        Movie m = new Movie();
//
//        m.setPosterPath("http://i.imgur.com/hgXLjzr.png");
//        m.setTitle("50 Shades of crazy");
//        m.setOverview("Some random description ...........................................");
//        m.setReleaseDate("2017-05-20T12:00:00Z");
//        return m;
//    }
//
//
//    private FomonoEvent generateFakeEatObject() {
//        Business b = new Business();
//        b.setName("Yummy Food Restaurant");
//        b.setImageUrl("http://i.imgur.com/hgXLjzr.png");
//        b.setUrl("https://www.yelp.com/oakland");
//        Location l = new Location();
//        Coordinates c = new Coordinates();
//        c.setLatitude(37.7980312);
//        c.setLongitude(-122.4100467);
//        b.setCoordinates(c);
//        return b;
//    }

}