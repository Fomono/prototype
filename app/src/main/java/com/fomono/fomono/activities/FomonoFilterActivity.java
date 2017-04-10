package com.fomono.fomono.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.ActivityFomonoFilterBinding;
import com.fomono.fomono.fragments.FomonoFilterFragment;
import com.fomono.fomono.fragments.UserPreferencesFragment;
import com.fomono.fomono.models.ICategory;

import java.util.ArrayList;
import java.util.List;

public class FomonoFilterActivity extends AppCompatActivity implements FomonoFilterFragment.FilterFragmentListener, UserPreferencesFragment.UserPreferencesListener {

    ActivityFomonoFilterBinding binding;
    UserPreferencesFragment userPrefsFragment;
    FomonoFilterFragment filtersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fomono_filter);
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_fomono_filter));

        if (savedInstanceState == null) {
            userPrefsFragment = UserPreferencesFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContent, userPrefsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filters, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onComplete(int resultCode) {
        if (resultCode == UserPreferencesFragment.CODE_FILTERS) {
            List<ICategory> categories = new ArrayList<>();
            //TODO: get list of event categories
            filtersFragment = FomonoFilterFragment.newInstance(getString(R.string.filter_title_events), categories, false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContent, filtersFragment)
                    .commit();
        }
    }

    @Override
    public void onSubmit(int resultCode) {
        switch (resultCode) {
            case FomonoFilterFragment.CODE_DONE:
                //TODO: save changes
                //intentional fall through
            case FomonoFilterFragment.CODE_CANCEL:
                if (userPrefsFragment == null) {
                    userPrefsFragment = UserPreferencesFragment.newInstance();
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, userPrefsFragment)
                        .commit();
                break;
            case FomonoFilterFragment.CODE_NEXT:
                List<ICategory> categories = new ArrayList<>();
                //TODO: get list of food categories
                filtersFragment = FomonoFilterFragment.newInstance(getString(R.string.filter_title_food), categories, true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, filtersFragment)
                        .commit();
                break;
        }
    }
}
