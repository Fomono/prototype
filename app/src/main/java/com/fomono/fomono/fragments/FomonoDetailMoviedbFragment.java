package com.fomono.fomono.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentMoviedbDetailBinding;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.utils.ConfigUtil;
import com.fomono.fomono.utils.DateUtils;
import com.fomono.fomono.utils.FavoritesUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import butterknife.ButterKnife;

import static com.fomono.fomono.FomonoApplication.API_NAME_MOVIE_GENRE;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoDetailMoviedbFragment extends android.support.v4.app.Fragment {
    public static final String MOVIE_OBJ="movie_obj";
    public static final String YES="Yes";
    public static final String NO="No";

    FragmentMoviedbDetailBinding fragmentBinding;
    Movie movie;
    ImageButton ibFavorite;
    FavoritesUtil favsUtil;

    public interface FomonoEventUpdateListener {
        void onFomonoEventUpdated();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getArguments().getParcelable(MOVIE_OBJ);
        favsUtil = FavoritesUtil.getInstance();

    }

    public static FomonoDetailMoviedbFragment newInstance(Movie movie) {
        FomonoDetailMoviedbFragment fomonoDetailMoviedbFragment = new FomonoDetailMoviedbFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE_OBJ, movie);
        fomonoDetailMoviedbFragment.setArguments(args);
        return fomonoDetailMoviedbFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateDetail(movie);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_moviedb_detail, parent, false);
        View view = fragmentBinding.getRoot();
        ButterKnife.bind(this,view);


        fragmentBinding.rbRating.setRating
                (Double.valueOf(movie.getVoteAverage()/2).floatValue());
        fragmentBinding.tvGenres.setText(getGenres());


        //setListeners
        setAddToCalendarListener();
        setFavoriteIconListener();


        setAdultValue();

        return fragmentBinding.getRoot();

    }

    private void setAdultValue() {

        if(movie.isAdult()){
            fragmentBinding.tvAdult.setText(YES);
        }else{
            fragmentBinding.tvAdult.setText(NO);
        }
    }


    private void setFavoriteIconListener() {

        ibFavorite = fragmentBinding.ivFavoriteIcon;
        favsUtil.isFavorited(movie, isFavorited -> {
            if (isFavorited) {
                ibFavorite.setImageResource(R.drawable.ic_favorite);
            }
        });

        ibFavorite.setOnClickListener(view1 -> {
            favsUtil.isFavorited(movie, isFavorited -> {
                if (isFavorited) {
                    ibFavorite.setImageResource(R.drawable.ic_favorite_grey);
                    favsUtil.removeFromFavorites(movie);
                } else {
                    ibFavorite.setImageResource(R.drawable.ic_favorite);
                    favsUtil.addToFavorites(movie);
                }
            });

            if (getActivity() instanceof FomonoEventUpdateListener) {
                ((FomonoEventUpdateListener) getActivity()).onFomonoEventUpdated();
            }
        });
    }

    private void setAddToCalendarListener() {
        fragmentBinding.tvCalendar.setOnClickListener(v -> addToCalendar());
        fragmentBinding.ivCalendar.setOnClickListener(v -> addToCalendar());

    }



    private String getGenres() {
        String genres ="";
        Set<Map.Entry<String, String>> genreSet = null;
        try {
            genreSet = ConfigUtil.getCategories(API_NAME_MOVIE_GENRE, getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Integer genreId : movie.getGenreIds()) {
                for (Map.Entry<String, String> entry : genreSet){
                    if(genreId.toString().equals(entry.getKey())){
                        if(movie.getGenreIds().size()-1 != movie.getGenreIds().indexOf(genreId)) {
                            genres += entry.getValue() + ", ";
                        } else {
                            genres += entry.getValue();
                        }
                    }
                }
            }
         return genres;
    }


    protected void populateDetail(Movie m) {
        fragmentBinding.setMovie(m);

    }

    public void addToCalendar() {
        String startDate = movie.getReleaseDate();
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        if(DateUtils.convertMovieDatetoMilliSeconds(startDate) != -1) {
            startMillis = DateUtils.convertMovieDatetoMilliSeconds(startDate);
        }

        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, startMillis+7200000 );
        values.put(CalendarContract.Events.TITLE, movie.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, movie.getOverview());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Toast.makeText(getActivity(), "Added to Calendar",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, FomonoApplication.PERM_CAL_MOVIE_REQ_CODE);
        }
    }
}
