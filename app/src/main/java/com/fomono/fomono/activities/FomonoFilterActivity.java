package com.fomono.fomono.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.ActivityFomonoFilterBinding;
import com.fomono.fomono.fragments.FomonoFilterFragment;
import com.fomono.fomono.fragments.UserPreferencesFragment;
import com.fomono.fomono.models.ICategory;
import com.fomono.fomono.models.db.Filter;
import com.fomono.fomono.utils.FilterUtil;
import com.parse.FindCallback;
import com.parse.ParseException;

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
            //show event filters fragment
            showFilterFragment(getString(R.string.filter_title_events), FomonoApplication.API_NAME_EVENTS, false);
        }
    }

    /**
     * Shows correct filters fragment based on api and title.
     * Has to be a callback because we have to first get user selected filters from db.
     * @param title
     * @param apiName
     * @param lastPage
     */
    private void showFilterFragment(String title, String apiName, boolean lastPage) {
        try {
            FilterUtil.getFilters(apiName, new FindCallback<Filter>() {
                @Override
                public void done(List<Filter> objects, ParseException e) {
                    Filter.initializeFromList(objects);
                    //get list of categories
                    List<ICategory> categories = FilterUtil.getCategories(apiName, FomonoFilterActivity.this);
                    filtersFragment = FomonoFilterFragment.newInstance(title, apiName, categories, lastPage, objects);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent, filtersFragment)
                            .commit();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSubmit(int resultCode) {
        switch (resultCode) {
            case FomonoFilterFragment.CODE_DONE:
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
                //show food filters fragment
                showFilterFragment(getString(R.string.filter_title_food), FomonoApplication.API_NAME_EATS, true);
                break;
        }
    }
}
