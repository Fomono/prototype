package com.fomono.fomono.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Geocoder;
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
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentMoviedbDetailBinding;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.utils.DateUtils;
import com.fomono.fomono.utils.FavoritesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.ButterKnife;

import static android.R.attr.x;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoDetailMoviedbFragment extends android.support.v4.app.Fragment {
    FragmentMoviedbDetailBinding fragmentMoviedbDetailBinding;

    private GoogleMap googleMap;
    MapView mMapView;
    Movie movie;
    ImageButton ibFavorite;
    FavoritesUtil favsUtil;


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
        Glide.with(view.getContext()).load(imageUrl).placeholder(R.drawable.botaimage).
                error(R.drawable.botaimage).into(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentMoviedbDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_moviedb_detail, parent, false);
        View view = fragmentMoviedbDetailBinding.getRoot();
        ButterKnife.bind(this,view);

        fragmentMoviedbDetailBinding.tvSiteLink.setClickable(true);
        fragmentMoviedbDetailBinding.tvSiteLink.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href=" + "https://www.themoviedb.org/movie?language=en" +  ">" + "CLICK HERE" + "</a>";
        fragmentMoviedbDetailBinding.tvSiteLink.setText(Html.fromHtml(text));


        fragmentMoviedbDetailBinding.tvEventDate.setText(movie.getReleaseDate());

        setImageUrl(fragmentMoviedbDetailBinding.ivEventImage, movie.getPosterPath());

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }else {
                    googleMap.setMyLocationEnabled(true);
                }
                LatLng latlng;
                // For dropping a marker at a point on the Map

                    latlng = getLocationFromAddress("121 Albright Way, Los Gatos, CA 95032");
                googleMap.addMarker(new MarkerOptions().position(latlng).title(movie.getTitle()).snippet("Marker Desc"));
                // snippet(event.getVenue().getAddress().getAddress1() + event.getVenue().getAddress().getCity() +
                //   event.getVenue().getAddress().getCountry()  ));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


        fragmentMoviedbDetailBinding.tvClockCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar(movie.getReleaseDate(), movie.getReleaseDate());
            }
        });


        fragmentMoviedbDetailBinding.ivMessageShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra(movie.getTitle(), x);
                startActivity(sendIntent);

            }
        });

        fragmentMoviedbDetailBinding.ivTwitterShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    // Check if the Twitter app is installed on the phone.
                    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, movie.getTitle());
                    startActivity(intent);

                }
                catch (Exception e)
                {
                    String url = "http://www.twitter.com/intent/tweet?url=YOURURL&text=YOURTEXT";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            }
        });

        fragmentMoviedbDetailBinding.ivEmailShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "some@email.address" });
                intent.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, movie.getTitle());
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, ""));
                }
            }
        });

        ibFavorite = fragmentMoviedbDetailBinding.ivFavoriteIcon;
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

        return fragmentMoviedbDetailBinding.getRoot();
    }


    protected void populateDetail(Movie m) {
        fragmentMoviedbDetailBinding.setMovie(m);

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

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        try {
            ArrayList<android.location.Address> adresses = (ArrayList<android.location.Address>) coder.getFromLocationName(strAddress, 50);
            for (android.location.Address add : adresses) {
                if (add.getCountryCode().equals("US") || add.getCountryCode().equals("USA") ) {//Controls to ensure it is right address such as country etc.
                    double longitude = add.getLongitude();
                    double latitude = add.getLatitude();
                    return new LatLng(latitude,longitude);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
