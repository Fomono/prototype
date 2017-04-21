package com.fomono.fomono.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bumptech.glide.Glide;
import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.adapters.FomonoMainPagerAdapter;
import com.fomono.fomono.databinding.ActivityFomonoBinding;
import com.fomono.fomono.fragments.BaseSortFragment;
import com.fomono.fomono.fragments.EatsFragment;
import com.fomono.fomono.fragments.EatsSortFragment;
import com.fomono.fomono.fragments.EventFragment;
import com.fomono.fomono.fragments.EventSortFragment;
import com.fomono.fomono.fragments.FavoritesFragment;
import com.fomono.fomono.fragments.MovieFragment;
import com.fomono.fomono.fragments.MovieSortFragment;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.network.client.EventBriteClientRetrofit;
import com.fomono.fomono.network.client.MovieDBClientRetrofit;
import com.fomono.fomono.network.client.YelpClientRetrofit;
import com.fomono.fomono.services.UserService;
import com.fomono.fomono.supportclasses.NavigationDrawerClass;
import com.fomono.fomono.utils.FavoritesUtil;
import com.fomono.fomono.utils.FilterUtil;
import com.parse.GetCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FomonoActivity extends AppCompatActivity implements BaseSortFragment.OnFragmentInteractionListener {
    public static final int REQUEST_CODE_DETAILS = 20;
    private FomonoMainPagerAdapter fomonoMainPagerAdapter;
    private final static String TAG = "Fomono Activity";
    private NavigationView nvView;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private Toolbar toolbar;
    private ViewPager fomonoPager;
    private PagerSlidingTabStrip fomonoTabStrip;
    private int ActiveViewPagerPagePosition = 0;
    private int eventSortRadioButtonSelected = -1;
    private int eatsSortRadioButtonSelected = -1;
    private int movieSortRadioButtonSelected = -1;
    private ImageView navHeaderProfileImage;
    private TextView tvNavName;

    public static final String ACTION_DETAIL = "launch_detail";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFomonoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_fomono);
        toolbar = binding.fomonoToolbarId;
        nvView = binding.fomonoNavViewId;
        mDrawer = binding.fomonoDrawerLayoutId;
        fomonoTabStrip = binding.fomonoTabsId;
        fomonoPager = binding.fomonoViewpagerId;

        View headerView = nvView.inflateHeaderView(R.layout.fomono_nav_drawer_header);
        navHeaderProfileImage = (ImageView) headerView.findViewById(R.id.fomonoNavImageId);
        tvNavName = (TextView) headerView.findViewById(R.id.fomonoNavNameId);

        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);

        NavigationDrawerClass navigationDrawerClass = new NavigationDrawerClass(FomonoActivity.this, mDrawer);
        navigationDrawerClass.setupDrawerContent(nvView);
        setupDrawerMenu();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        fomonoMainPagerAdapter = new FomonoMainPagerAdapter(getSupportFragmentManager());
        fomonoPager.setAdapter(fomonoMainPagerAdapter);
        //Tells the view pager to not destroy the fragment more than one tab away
        fomonoPager.setOffscreenPageLimit(getResources().getInteger(R.integer.NUM_MAINLIST_FRAGMENTS) - 1);
        fomonoTabStrip.setViewPager(fomonoPager);

        fomonoPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ActiveViewPagerPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navHeaderProfileImage.setOnClickListener(v -> {
            if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                Intent profileIntent = new Intent(FomonoActivity.this, UserProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        //clear filter dirty flags here since we're creating a new fomono activity and everything will be refreshed
        FilterUtil.getInstance().clearDirty();

        //process intent from notification or elsewhere
        processOutOfAppIntent();
    }

    private void setupDrawerMenu() {
        ParseUser user = ParseUser.getCurrentUser();
        if (ParseAnonymousUtils.isLinked(user)) {
            tvNavName.setText(getString(R.string.guest_name));
        } else {
            tvNavName.setText(user.getString("firstName") + " " + user.getString("lastName"));
        }
        if (user.get("profilePicture") != null) {
            String fileUrl = user.get("profilePicture").toString();
            Glide.with(this).load(fileUrl)
                    .into(navHeaderProfileImage);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FilterUtil.getInstance().isDirty()) {
            fomonoMainPagerAdapter.refreshFragments();
        }
        if (UserService.getInstance().isUserUpdated()) {
            setupDrawerMenu();
            UserService.getInstance().setUserUpdated(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menuSortId:
                String name = makeFragmentName(fomonoPager.getId(), ActiveViewPagerPagePosition);
                Fragment viewPagerFragment = getSupportFragmentManager().findFragmentByTag(name);

                if (viewPagerFragment != null) {
                    // Do something with your Fragment
                    if (viewPagerFragment.isResumed()) {

                        FragmentManager fm = getSupportFragmentManager();
                        if (viewPagerFragment instanceof EventFragment) {
                            EventSortFragment sortFragmentObject = EventSortFragment.newInstance(eventSortRadioButtonSelected);
                            sortFragmentObject.show(fm, "fragment_edit_name");
     //                       //FIXME - Insert events filter fragment here

                        } else if(viewPagerFragment instanceof EatsFragment) {
                            EatsSortFragment sortFragmentObject = EatsSortFragment.newInstance(eatsSortRadioButtonSelected);
                           sortFragmentObject.show(fm, "fragment_edit_name");
   //                         //FIXME - Insert eats filter fragment here

                        } else if(viewPagerFragment instanceof MovieFragment) {
                            MovieSortFragment sortFragmentObject = MovieSortFragment.newInstance(movieSortRadioButtonSelected);
                            sortFragmentObject.show(fm, "fragment_edit_name");
                        }
                    } else {
                        Log.d(TAG, "Fragment not resumed");
                    }
                } else {
                    fomonoMainPagerAdapter.refreshFragments();
                }
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

    @Override
    public void onFinishSortDialog(String sortString, int radioButtonCheckedId) {

        String name = makeFragmentName(fomonoPager.getId(), ActiveViewPagerPagePosition);
        Fragment viewPagerFragment = getSupportFragmentManager().findFragmentByTag(name);

        if (viewPagerFragment != null) {
            if (viewPagerFragment.isResumed()) {
                if (viewPagerFragment instanceof EventFragment) {
                    eventSortRadioButtonSelected = radioButtonCheckedId;
                    EventFragment mEventFragment = (EventFragment) viewPagerFragment;
                    mEventFragment.refreshEventList(sortString);
                } else if (viewPagerFragment instanceof EatsFragment) {
                    eatsSortRadioButtonSelected = radioButtonCheckedId;
                    EatsFragment mEatsFragment = (EatsFragment) viewPagerFragment;
                    mEatsFragment.refreshEatsList(sortString);
                } else if (viewPagerFragment instanceof MovieFragment) {
                    movieSortRadioButtonSelected = radioButtonCheckedId;
                    MovieFragment mMovieFragment = (MovieFragment) viewPagerFragment;
                    mMovieFragment.refreshMovieList(sortString);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fomono, menu);
        MenuItem searchItem = menu.findItem(R.id.menuSearchId);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String name = makeFragmentName(fomonoPager.getId(), ActiveViewPagerPagePosition);
                Fragment viewPagerFragment = getSupportFragmentManager().findFragmentByTag(name);

                if (viewPagerFragment != null) {
                    if (viewPagerFragment.isResumed()) {
                        if (viewPagerFragment instanceof EventFragment) {
                            EventFragment mEventFragment = (EventFragment) viewPagerFragment;
                            mEventFragment.searchEventList(query);
                        } else if (viewPagerFragment instanceof EatsFragment) {
                            EatsFragment mEatsFragment = (EatsFragment) viewPagerFragment;
                            mEatsFragment.searchEatsList(query);
                        } else if (viewPagerFragment instanceof MovieFragment) {
                            MovieFragment mMovieFragment = (MovieFragment) viewPagerFragment;
                            mMovieFragment.searchMovieList(query);
                        }
                    }
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, (R.string.open_drawer), R.string.close_drawer);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    private void processOutOfAppIntent() {
        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Log.d("DEBUG", "got out of app intent, action: " + action + " type: " + type);

        if (action != null && action.equals(ACTION_DETAIL)) {
            String apiName = intent.getStringExtra("apiName");
            String id = intent.getStringExtra("id");
            //setup current user
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (ParseAnonymousUtils.isLinked(currentUser)) {
                FavoritesUtil.getInstance();
                handleLaunchDetailView(apiName, id);
            } else {
                currentUser.fetchInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        FavoritesUtil.getInstance();
                        handleLaunchDetailView(apiName, id);
                    }
                });
            }
        }
    }

    private void handleLaunchDetailView(String apiName, String id) {
        switch (apiName) {
            case FomonoApplication.API_NAME_EVENTS:
                handleLaunchEventDetail(id);
                break;
            case FomonoApplication.API_NAME_EATS:
                handleLaunchBusinessDetail(id);
                break;
            case FomonoApplication.API_NAME_MOVIES:
                handleLaunchMovieDetail(id);
                break;
        }
    }

    private void handleLaunchEventDetail(String id) {
        EventBriteClientRetrofit eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();
        Map<String, String> data = new HashMap<>();
        data.put("token", getResources().getString(R.string.event_brite_user_key));
        Call<Event> call = eventBriteClientRetrofit.EBRetrofitClientFactory()
                .getEventById(id, data);

        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                Event event = response.body();
                if (event == null) {
                    Log.d(TAG, "Call " + call.request().toString() + " did not return an event. " + response.message());
                } else {
                    Log.d("DEBUG", "got back event: " + event.getId());
                    Intent i = new Intent(FomonoActivity.this, FomonoDetailActivity.class);
                    i.putExtra("FOM_OBJ", event);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Log.d(TAG, "Getting event by id failed " + t.getMessage());
            }
        });
    }

    private void handleLaunchBusinessDetail(String id) {
        YelpClientRetrofit yelpClientRetrofit = YelpClientRetrofit.getInstance();
        Call<Business> call = yelpClientRetrofit.YelpRetrofitClientFactory()
                .getYelpBusinessById(id);

        call.enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                Business business = response.body();
                if (business == null) {
                    Log.d(TAG, "Call " + call.request().toString() + " did not return a yelp business. " + response.message());
                } else {
                    Intent i = new Intent(FomonoActivity.this, FomonoDetailActivity.class);
                    i.putExtra("FOM_OBJ", business);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                Log.d(TAG, "Getting yelp business by id failed " + t.getMessage());
            }
        });
    }

    private void handleLaunchMovieDetail(String id) {
        MovieDBClientRetrofit movieDBClientRetrofit = MovieDBClientRetrofit.getInstance();

        Map<String, String> data = new HashMap<>();
        data.put("api_key", getResources().getString(R.string.movieDB_api_key));
        Call<Movie> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                .getMovieById(id, data);

        callMovie.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                if (movie == null) {
                    Log.d(TAG, "Call " + call.request().toString() + " did not return a movie. " + response.message());
                } else {
                    Intent i = new Intent(FomonoActivity.this, FomonoDetailActivity.class);
                    i.putExtra("FOM_OBJ", movie);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d(TAG, "Getting movie by id failed " + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DETAILS) {
            boolean updated = data.getBooleanExtra("updated", false);
            int position = data.getIntExtra("position", 0);
            FomonoEvent fEvent = data.getParcelableExtra("fEvent");
            if (updated) {
                fomonoMainPagerAdapter.refreshFomonoEvent(fEvent, position);
            }
        }
    }

    public void onFomonoEventUpdated(FomonoEvent fEvent, String fragmentName) {
        if (fragmentName.equals(FavoritesFragment.TAG)) {
            fomonoMainPagerAdapter.refreshFomonoEvent(fEvent, -1);
        }
    }
}
