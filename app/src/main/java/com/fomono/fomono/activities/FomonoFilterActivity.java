package com.fomono.fomono.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.fomono.fomono.models.user.User;
import com.fomono.fomono.utils.FilterUtil;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FomonoFilterActivity extends AppCompatActivity implements FomonoFilterFragment.FilterFragmentListener,
        UserPreferencesFragment.UserPreferencesListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    final String[] FILTER_PAGES = {FomonoApplication.API_NAME_EVENTS, FomonoApplication.API_NAME_EATS};

    ActivityFomonoFilterBinding binding;
    UserPreferencesFragment userPrefsFragment;
    FomonoFilterFragment filtersFragment;
    int currentFilterPage;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fomono_filter);
        toolbar = binding.toolbar;

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_fomono_filter));
        //turn on back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            userPrefsFragment = UserPreferencesFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContent, userPrefsFragment)
                    .commit();
            currentFilterPage = -1;
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

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onComplete(int resultCode) {
        if (resultCode == UserPreferencesFragment.CODE_FILTERS) {
            //show event filters fragment
            showFilterFragment(FomonoApplication.API_NAME_EVENTS);
        }
    }

    /**
     * Shows correct filters fragment based on api and title.
     * Has to be a callback because we have to first get user selected filters from db.
     *
     * @param apiName
     */
    private void showFilterFragment(String apiName) {
        final String title = getApiTitle(apiName);
        final boolean lastPage = isLastPage(apiName);
        try {
            FilterUtil.getInstance().getFilters(apiName, (objects, e) -> {
                if (objects != null) {
                    Filter.initializeFromList(objects);
                } else {
                    objects = new ArrayList<Filter>();
                }
                //get list of categories
                List<ICategory> categories = FilterUtil.getInstance().getCategories(apiName, FomonoFilterActivity.this);
                filtersFragment = FomonoFilterFragment.newInstance(title, apiName, categories, lastPage, objects);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContent, filtersFragment)
                        .commit();
                currentFilterPage = getFilterPage(apiName);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getFilterPage(String apiName) {
        for (int i = 0; i < FILTER_PAGES.length; i++) {
            String api = FILTER_PAGES[i];
            if (apiName.equals(api)) {
                return i;
            }
        }
        return -1;
    }

    private String getApiTitle(String apiName) {
        String title = "";
        switch (apiName) {
            case FomonoApplication.API_NAME_EVENTS:
                title = getString(R.string.filter_title_events);
                break;
            case FomonoApplication.API_NAME_EATS:
                title = getString(R.string.filter_title_food);
                break;
            case FomonoApplication.API_NAME_MOVIES:
            default:
                break;
        }
        return title;
    }

    private boolean isLastPage(String apiName) {
        return getFilterPage(apiName) == FILTER_PAGES.length - 1;
    }

    @Override
    public void onSubmit(int resultCode) {
        switch (resultCode) {
            case FomonoFilterFragment.CODE_DONE:
                Intent i = new Intent(this, FomonoActivity.class);
                startActivity(i);
                break;
            case FomonoFilterFragment.CODE_CANCEL:
                if (currentFilterPage > 0) {
                    //show previous filter page
                    showFilterFragment(FILTER_PAGES[currentFilterPage - 1]);
                } else {
                    if (userPrefsFragment == null) {
                        userPrefsFragment = UserPreferencesFragment.newInstance();
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flContent, userPrefsFragment)
                            .commit();
                    currentFilterPage = -1;
                }
                break;
            case FomonoFilterFragment.CODE_NEXT:
                //show food filters fragment
                showFilterFragment(FomonoApplication.API_NAME_EATS);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FomonoApplication.PERM_LOC_FILTER_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (userPrefsFragment != null) {
                    userPrefsFragment.useCurrentLocation();
                }
            }

            //set a flag on the user
            ParseUser user = ParseUser.getCurrentUser();
            user.put(User.LOC_PERM_SEEN, true);
            user.saveInBackground();
        }
    }
}
