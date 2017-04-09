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
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Description;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.Logo;
import com.fomono.fomono.models.events.events.Name;
import com.fomono.fomono.models.events.events.Start;
import com.fomono.fomono.models.movies.Movie;


public class FomonoDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbFomono);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_fomono_detail);
        Intent i = getIntent();
        //FomonoEvent fEvent = i.getParcelableExtra("FOM_OBJ");
        FomonoEvent fEvent = generateFakeEventObject();

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

    private FomonoEvent generateFakeEventObject() {

        Event e = new Event();

        Start startDate = new Start();
        startDate.setLocal("2017-05-20T14:00:00");
        Name name = new Name();
        name.setText("Holi Montreux");
        Description d = new Description();
        d.setText("Holi is an Indian inspired festivity, created to celebrate the rebirth of Spring and new beginnings!");
        Logo l = new Logo();
        l.setUrl("https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F29366453%2F140913306390%2F1%2Foriginal.jpg?h=200&w=450&rect=177%2C410%2C1090%2C545&s=8aa1cb74651213865e999a3d9fd51eb0");

        e.setName(name);
        e.setStart(startDate);
        e.setDescription(d);
        e.setLogo(l);
        e.setVenueId("18800935");
        return e;


    }


}