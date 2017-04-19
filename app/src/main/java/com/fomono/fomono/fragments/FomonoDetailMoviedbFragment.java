package com.fomono.fomono.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentMoviedbDetailBinding;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.utils.ConfigUtil;
import com.fomono.fomono.utils.DateUtils;
import com.fomono.fomono.utils.FavoritesUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.fomono.fomono.FomonoApplication.API_NAME_MOVIE_GENRE;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoDetailMoviedbFragment extends android.support.v4.app.Fragment {
    FragmentMoviedbDetailBinding fragmentBinding;

    Movie movie;
    ImageButton ibFavorite;
    FavoritesUtil favsUtil;
    String movieKey;
    String movieDBURL = "https://www.themoviedb.org/movie?language=en";






    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getArguments().getParcelable("movie_obj");
        favsUtil = FavoritesUtil.getInstance();

    }

    public static FomonoDetailMoviedbFragment newInstance(Movie movie) {
        FomonoDetailMoviedbFragment fomonoDetailMoviedbFragment = new FomonoDetailMoviedbFragment();
        Bundle args = new Bundle();
        args.putParcelable("movie_obj", movie);
        fomonoDetailMoviedbFragment.setArguments(args);
        return fomonoDetailMoviedbFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateDetail(movie);

    }


    //  @BindingAdapter({"imageUrl"})
    private static void setImageUrl(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).placeholder(R.drawable.ic_fomono_big).
                error(R.drawable.ic_fomono_big).into(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_moviedb_detail, parent, false);
        View view = fragmentBinding.getRoot();
        ButterKnife.bind(this,view);

       // callYouTube();
        fragmentBinding.tvSiteLink.setClickable(true);
        fragmentBinding.tvSiteLink.setMovementMethod(LinkMovementMethod.getInstance());
        fragmentBinding.tvSiteLink.setText(Html.fromHtml("<a href=" + movieDBURL +  ">" + "CLICK HERE" + "</a>"));

        fragmentBinding.tvEventDate.setText("");
        fragmentBinding.tvRatingText.setText(Double.valueOf(movie.getVoteAverage()/2).toString()+ "/5");

        setImageUrl(fragmentBinding.ivEventImage, movie.getPosterPath());
        fragmentBinding.rbMovierating.setRating
                (Double.valueOf(movie.getVoteAverage()/2).floatValue());

        fragmentBinding.tvClockCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar(movie.getReleaseDate(), movie.getReleaseDate());
            }
        });

        fragmentBinding.tvGenres.setText(getGenres());


        fragmentBinding.ivMessageShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri smsUri = Uri.parse("tel:" + "");
                Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                intent.putExtra("address", "");
                if (movie.getTitle() != null && movie.getTitle()!= null) {
                    intent.putExtra("sms_body", movie.getTitle()+ "\n" + movie.getTitle());
                }
                intent.setType("vnd.android-dir/mms-sms");//here setType will set the previous data null.
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        fragmentBinding.ivTwitterShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // Check if the Twitter app is installed on the phone.
                    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
                    intent.setType("text/plain");
                    if (movie.getTitle() != null) {
                        intent.putExtra(Intent.EXTRA_TEXT, movie.getTitle());
                    }
                    startActivity(intent);

                } catch (Exception e) {
                    String url = "";
                    if (movie.getTitle() != null) {
                        url = "http://www.twitter.com/intent/tweet?url="
                                + movie.getTitle() + "&text=" + movie.getTitle();
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            }
        });

        fragmentBinding.ivEmailShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"some@email.address"});
                if (movie.getTitle() != null) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
                     intent.putExtra(Intent.EXTRA_TEXT, movie.getTitle());
                }
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, ""));
                }
            }
        });

        ibFavorite = fragmentBinding.ivFavoriteIcon;
        if (favsUtil.isFavorited(movie)) {
            ibFavorite.setImageResource(R.drawable.ic_favorite);
        }
        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favsUtil.isFavorited(movie)) {
                    ibFavorite.setImageResource(R.drawable.ic_favorite_grey);
                    favsUtil.removeFromFavorites(movie);
                } else {
                    ibFavorite.setImageResource(R.drawable.ic_favorite);
                    favsUtil.addToFavorites(movie);
                }
            }
        });

        fragmentBinding.ivFBShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupFacebookShareIntent();
            }
        });

        return fragmentBinding.getRoot();

    }

    public void setupFacebookShareIntent() {
        ShareDialog shareDialog;
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent;

        if(movie.getTitle()!=null ){
            linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(movie.getTitle())
                    .setContentUrl(Uri.parse(movieDBURL))
                    .build();
        }else{
            linkContent = new ShareLinkContent.Builder()
                    .build();
        }

        shareDialog.show(linkContent);
    }


    private String getGenres() {
        String genres ="";
        Set<Map.Entry<String, String>> genreSet = null;
        try {
            genreSet = ConfigUtil.getCategories(API_NAME_MOVIE_GENRE, getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Long genreId : movie.getGenreIds()) {
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

    public void addToCalendar(String startDate, String endDate) {
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        startMillis = DateUtils.convertUTCtoMilliSeconds(startDate);
        endMillis = DateUtils.convertUTCtoMilliSeconds(endDate);


        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, movie.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, movie.getOverview());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "UTC");
        // TODO: Consider calling
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Toast.makeText(getActivity(), "Added to Calendar",
                    Toast.LENGTH_LONG).show();

            return;
        }
    }
}
