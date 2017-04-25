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
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.fragments.FomonoDetailEventbriteFragment;
import com.fomono.fomono.fragments.FomonoDetailMoviedbFragment;
import com.fomono.fomono.fragments.FomonoDetailYelpFragment;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.models.movies.VideoResponse;
import com.fomono.fomono.models.user.User;
import com.fomono.fomono.network.client.MovieDBClientRetrofit;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


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
    int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fomono_detail);
        Intent i = getIntent();
        Transition a = TransitionInflater.from(this).inflateTransition(R.transition.slide_bottom);
        getWindow().setEnterTransition(a);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator
                (ContextCompat.getDrawable(FomonoDetailActivity.this, R.drawable.ic_arrow_back));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        screenWidth = calculateScreenWidth();
        findViewById(R.id.youtube_fragment).setVisibility(View.GONE);


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
            playYouTubeVideo(m.getId());


        }
    }

    private int calculateScreenWidth() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        return (int) (pxWidth / displayMetrics.density);
    }

    private void loadImage(FomonoEvent fEvent) {
        int height = (int) (getResources().getDimension(R.dimen.detail_top_bar_height) / getResources().getDisplayMetrics().density);
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        if (fEvent instanceof Event) {
            Event e = (Event) fEvent;
            if (e.getLogo() != null) {
                Picasso.with(this).load(e.getLogo().getUrl())
                        .placeholder(R.drawable.ic_image_placeholder)
                        .resize(screenWidth, height)
                        .centerCrop()
                        .into(imageView);
//                Glide.with(this).load(e.getLogo().getUrl())
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .centerCrop()
//                        .into(imageView);
            }
        } else if (fEvent instanceof Movie) {
            Movie m = (Movie) fEvent;
            Picasso.with(this).load(m.getBackdropPath())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .resize(screenWidth, height)
                    .centerCrop()
                    .into(imageView);
//            Glide.with(this).load(m.getBackdropPath())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .into(imageView);
        } else if (fEvent instanceof Business) {
            Business b = (Business) fEvent;
            Picasso.with(this).load(b.getImageUrl())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .resize(screenWidth, height)
                    .centerCrop()
                    .into(imageView);
//            Glide.with(this).load(b.getImageUrl())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .into(imageView);
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
                    //nothing to do here
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
        } else if (requestCode == FomonoApplication.PERM_PHO_BUS_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //launch phone
                if (fomonoDetailYelpFragment != null) {
                    fomonoDetailYelpFragment.launchPhone();
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
                finishAfterTransition();
                break;
            case R.id.menuShare:
                callShareIntent();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callShareIntent() {
        Intent sharingIntent = null;
        Event e = null;
        Business b = null;
        Movie m = null;
        String subjectShare = "";
        String bodyShare = "";

        if (fEvent != null) {
            if (fEvent instanceof Event) {
                e = (Event) fEvent;
                if (e.getName() != null && e.getDescription() != null) {
                    subjectShare = e.getName().getText();
                    bodyShare = e.getDescription().getText().toString();
                }
            } else if (fEvent instanceof Business) {
                b = (Business) fEvent;
                if (b.getName() != null && b.getUrl() != null) {
                    subjectShare = b.getName();
                    bodyShare = b.getUrl();
                }
            } else if (fEvent instanceof Movie) {
                    m = (Movie) fEvent;
                    if (m.getTitle() != null && m.getOverview() != null) {
                        subjectShare = m.getTitle();
                        bodyShare = m.getOverview();
                    }
                }
            }
            sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subjectShare);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, bodyShare);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }

    private void playYouTubeVideo(long movie) {

        MovieDBClientRetrofit movieDBClientRetrofit = MovieDBClientRetrofit.getInstance();

        Map<String, String> data = new HashMap<>();
        data.put("api_key", getResources().getString(R.string.movieDB_api_key));
        retrofit2.Call<VideoResponse> callMovie = movieDBClientRetrofit.MovieDBRetrofitClientFactory()
                .getMovieVideosById(Long.toString(movie), data);
        callMovie.enqueue(new retrofit2.Callback<VideoResponse>() {
            @Override
            public void onResponse(retrofit2.Call<VideoResponse> call, retrofit2.Response<VideoResponse> response) {
                VideoResponse vResponse = response.body();
                if (vResponse.getResults() != null && vResponse.getResults().isEmpty() && vResponse.getResults().get(0) != null) {
                    findViewById(R.id.youtube_fragment).setVisibility(View.VISIBLE);
                    String videoKey = vResponse.getResults().get(0).getKey();
                    cueYouTubeTrailer(videoKey);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<VideoResponse> call, Throwable t) {
                Log.d(TAG, "Getting movie by id failed " + t.getMessage());
            }
        });
    }

    private void cueYouTubeTrailer(String videoKey) {

        YouTubePlayerSupportFragment youtubeFragment = (YouTubePlayerSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.youtube_fragment);
        youtubeFragment.initialize(getResources().getString(R.string.youTube_api_key),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(videoKey);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(FomonoDetailActivity.this,"Youtube load failed" + youTubeInitializationResult.toString(), Toast.LENGTH_LONG).show();

                    }

                });

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