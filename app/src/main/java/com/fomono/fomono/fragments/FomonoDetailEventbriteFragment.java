package com.fomono.fomono.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentEventbriteDetailBinding;
import com.fomono.fomono.models.events.events.Address;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.Start;
import com.fomono.fomono.models.events.events.Venue;
import com.fomono.fomono.models.user.User;
import com.fomono.fomono.network.client.EventBriteClientRetrofit;
import com.fomono.fomono.utils.DateUtils;
import com.fomono.fomono.utils.FavoritesUtil;
import com.fomono.fomono.utils.StringUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoDetailEventbriteFragment extends android.support.v4.app.Fragment {
    FragmentEventbriteDetailBinding fragmentEventbriteDetailBinding;

    private GoogleMap googleMap;
    MapView mMapView;
    EventBriteClientRetrofit eventBriteClientRetrofit;
    Event event;
    ImageButton ibFavorite;
    FavoritesUtil favsUtil;

    public interface FomonoEventUpdateListener {
        void onFomonoEventUpdated();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = getArguments().getParcelable("event_obj");
        favsUtil = FavoritesUtil.getInstance();
    }

    public static FomonoDetailEventbriteFragment newInstance(Event event) {
        FomonoDetailEventbriteFragment fomonoDetailEventbriteFragment = new FomonoDetailEventbriteFragment();
        Bundle args = new Bundle();
        args.putParcelable("event_obj", event);
        fomonoDetailEventbriteFragment.setArguments(args);
        return fomonoDetailEventbriteFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getAddressFromAPI(Event e) {

        eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();
        Map<String, String> data = new HashMap<>();
        data.put("token", getResources().getString(R.string.eventbrite_api_key));
        Call<Venue> call = eventBriteClientRetrofit.EBRetrofitClientFactory().
                getVenueFromServer(e.getVenueId(), data);

        call.enqueue(new Callback<Venue>() {
            @Override
            public void onResponse(Call<Venue> call, Response<Venue> response) {
                Address address = response.body().getAddress();
                Venue venue = response.body();
                if (address == null) {
                    Log.d(TAG, "MO MATCH ");
                } else {
                    e.setVenue(response.body());
                    e.saveOrUpdate();
                    generateAddressString(venue);
                    populateAddressMap(e);
                }
            }

            @Override
            public void onFailure(Call<Venue> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());
            }
        });


    }

    private void generateAddressString(Venue venue) {
            String eventAddress ="";
            if(venue.getName()!= null){
                eventAddress += event.getVenue().getName() + ":" +"\n";
            }
            if(venue.getAddress()!= null && venue.getAddress().getAddress1()!=null){
                eventAddress += venue.getAddress().getAddress1();
            }
            if(venue.getAddress()!= null && venue.getAddress().getCity()!=null){
                eventAddress += " " + venue.getAddress().getCity();
            }
            if(venue.getAddress()!= null && venue.getAddress().getCountry()!=null){
                eventAddress += " " + venue.getAddress().getCountry();
            }
            if(venue.getAddress()!= null && venue.getAddress().getPostalCode()!=null){
                eventAddress += " " +venue.getAddress().getPostalCode();
            }
            fragmentEventbriteDetailBinding.tvLocation.setText(eventAddress);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
       Start start = event.getStart();
       String eventAddress="";

       fragmentEventbriteDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_eventbrite_detail, parent, false);

        View view = fragmentEventbriteDetailBinding.getRoot();
        ButterKnife.bind(this, view);
        if(event.getDescription() !=null){
            String eventDesc = event.getDescription().getText().toString();
            fragmentEventbriteDetailBinding.tvDescription.setText(StringUtil.stripNewlinesExtraSpaces(eventDesc));
        }

        fragmentEventbriteDetailBinding.ivSiteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(event.getUrl()));
                startActivity(intent);
            }
        });

       if (start != null && start.getLocal() != null) {
            fragmentEventbriteDetailBinding.tvClockEventDate.
                    setText(DateUtils.convertEventDatetoDisplayFormat(start.getLocal()));
            fragmentEventbriteDetailBinding.tvEventMonth.setText(DateUtils.
                    getFormattedMonthForHeader(start.getLocal()));
           fragmentEventbriteDetailBinding.tvEventDay.setText(DateUtils.
                   getFormattedDayForHeader(start.getLocal()));
        }

        populateDetail(event);

        mMapView = (MapView) view.findViewById(R.id.mapEBView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getAddressFromAPI(event);


        fragmentEventbriteDetailBinding.ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar();
            }
        });


        fragmentEventbriteDetailBinding.ivMessageShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri smsUri = Uri.parse("tel:" + "");
                Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
                intent.putExtra("address", "");
                if (event.getName() != null && event.getUrl() != null) {
                    intent.putExtra("sms_body", event.getName().getText() + "\n" + event.getUrl());
                }
                intent.setType("vnd.android-dir/mms-sms");//here setType will set the previous data null.
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        fragmentEventbriteDetailBinding.ivTwitterShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    // Check if the Twitter app is installed on the phone.
                    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
                    intent.setType("text/plain");
                    if (event.getUrl() != null) {
                        intent.putExtra(Intent.EXTRA_TEXT, event.getUrl());
                    }
                    startActivity(intent);

                } catch (Exception e) {
                    String url = "";
                    if (event.getUrl() != null) {
                        url = "http://www.twitter.com/intent/tweet?url="
                                + event.getUrl() + "&text=" + event.getName().getText();
                    }
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            }
        });

        fragmentEventbriteDetailBinding.ivEmailShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"some@email.address"});
                if (event.getName() != null) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, event.getName().getText());
                }
                if (event.getUrl() != null) {
                    intent.putExtra(Intent.EXTRA_TEXT, event.getUrl());
                }
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, ""));
                }
            }
        });

        fragmentEventbriteDetailBinding.ivFBShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupFacebookShareIntent();
            }
        });

        ibFavorite = fragmentEventbriteDetailBinding.ivFavoriteIcon;
        favsUtil.isFavorited(event, isFavorited -> {
            if (isFavorited) {
                ibFavorite.setImageResource(R.drawable.ic_favorite);
            }
        });

        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favsUtil.isFavorited(event, isFavorited -> {
                    if (isFavorited) {
                        ibFavorite.setImageResource(R.drawable.ic_favorite_grey);
                        favsUtil.removeFromFavorites(event);
                    } else {
                        ibFavorite.setImageResource(R.drawable.ic_favorite);
                        favsUtil.addToFavorites(event);
                    }
                });
                if (getActivity() instanceof FomonoEventUpdateListener) {
                    ((FomonoEventUpdateListener) getActivity()).onFomonoEventUpdated();
                }
            }
        });

        return view;
    }

    public void setupFacebookShareIntent() {
        ShareDialog shareDialog;
        shareDialog = new ShareDialog(this);

        ShareLinkContent linkContent;

        if(event.getName()!=null && event.getUrl()!=null && event.getDescription()!=null){
            linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(event.getName().getText())
                    .setContentDescription(
                            event.getDescription().getText().toString())
                    .setContentUrl(Uri.parse(event.getUrl()))
                    .build();
        }else{
            linkContent = new ShareLinkContent.Builder()
                    .build();
        }

        shareDialog.show(linkContent);
    }

    protected void populateDetail(Event e) {
        fragmentEventbriteDetailBinding.setEvent(e);

    }

    protected void populateAddressMap(Event e) {
        //reset fragment's databinding
        if (event.getVenue() == null) {
            return;
        }
        Address address = event.getVenue().getAddress();
        fragmentEventbriteDetailBinding.setEvent(e);
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                Criteria locationCritera = new Criteria();
                locationCritera.setAccuracy(Criteria.ACCURACY_COARSE);
                locationCritera.setAltitudeRequired(false);
                locationCritera.setBearingRequired(false);
                locationCritera.setCostAllowed(true);
                locationCritera.setPowerRequirement(Criteria.NO_REQUIREMENT);

                // For showing a move to my location button
                int fineLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                int coarseLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
                if (fineLocationPerm != PackageManager.PERMISSION_GRANTED && coarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    ParseUser user = ParseUser.getCurrentUser();
                    if (!user.getBoolean(User.LOC_PERM_SEEN)) {
                        Toast.makeText(getContext(), getString(R.string.pref_turn_on_location), Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, FomonoApplication.PERM_LOC_EVENT_REQ_CODE);
                    }
                } else {
                    googleMap.setMyLocationEnabled(true);
                }
                LatLng latlng;
                // For dropping a marker at a point on the Map
                if (event.getVenue() != null) {
                    latlng = new LatLng(Double.parseDouble(address.getLatitude()),
                            Double.parseDouble(address.getLongitude()));
                } else {
                    latlng = new LatLng(0, 0);
                }
                googleMap.addMarker(new MarkerOptions().position(latlng).title(event.getVenue().getName()).
                        snippet(address.getAddress1() + ", " + address.getCity() + ", " +
                                address.getCountry() + "," + address.getPostalCode()));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    public void addToCalendar() {
        if (event.getStart() == null || event.getEnd() == null) {
            return;
        }
        String startDate = event.getStart().getLocal();
        String endDate = event.getEnd().getLocal();
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        startMillis = DateUtils.convertEventDatetoMilliSeconds(startDate);
        endMillis = DateUtils.convertEventDatetoMilliSeconds(endDate);


        ContentResolver cr = getActivity().getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        if (event.getName() != null) {
            values.put(CalendarContract.Events.TITLE, event.getName().getText());
        }
        if (event.getDescription() != null) {
            values.put(CalendarContract.Events.DESCRIPTION, event.getDescription().getText().toString());
        }
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        // TODO: Consider calling
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Toast.makeText(getActivity(), "Added to Calendar",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, FomonoApplication.PERM_CAL_EVENT_REQ_CODE);
        }
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getContext());
        try {
            ArrayList<android.location.Address> adresses = (ArrayList<android.location.Address>) coder.getFromLocationName(strAddress, 50);
            for (android.location.Address add : adresses) {
                if (add.getCountryCode().equals("US") || add.getCountryCode().equals("USA")) {//Controls to ensure it is right address such as country etc.
                    double longitude = add.getLongitude();
                    double latitude = add.getLatitude();
                    return new LatLng(latitude, longitude);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
//        mMapView.onLowMemory();
    }

    public void enableMapLocation() {
        int fineLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fineLocationPerm == PackageManager.PERMISSION_GRANTED || coarseLocationPerm == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }

}
