package com.fomono.fomono.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

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

    public static final String EVENT_OBJECT = "event_obj";
    public static final String TOKEN = "token";
    FragmentEventbriteDetailBinding fragmentBinding;
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
        event = getArguments().getParcelable(EVENT_OBJECT);
        favsUtil = FavoritesUtil.getInstance();
    }

    public static FomonoDetailEventbriteFragment newInstance(Event event) {
        FomonoDetailEventbriteFragment fomonoDetailEventbriteFragment = new FomonoDetailEventbriteFragment();
        Bundle args = new Bundle();
        args.putParcelable(EVENT_OBJECT, event);
        fomonoDetailEventbriteFragment.setArguments(args);
        return fomonoDetailEventbriteFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
       fragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_eventbrite_detail, parent, false);
        View view = fragmentBinding.getRoot();
        ButterKnife.bind(this, view);

        if(event.getDescription() !=null){
            String eventDesc = event.getDescription().getText().toString();
            fragmentBinding.tvDescription.setText(StringUtil.stripNewlinesExtraSpaces(eventDesc));
        }

        //SetUp Listeners
        setSourceSiteLinkIntentListener();
        setAddToCalendarListener();
        setFavoriteIconListener();

        setEventDateTime();
        populateBindingDetail(event);
        initializeMap(view,savedInstanceState);
        getAddressFromAPI(event);

        return view;
    }




    protected void populateBindingDetail(Event e) {
        fragmentBinding.setEvent(e);
    }

    private void setFavoriteIconListener() {

        ibFavorite = fragmentBinding.ivFavoriteIcon;
        favsUtil.isFavorited(event, isFavorited -> {
            if (isFavorited) {
                ibFavorite.setImageResource(R.drawable.ic_favorite);
            }
        });

        ibFavorite.setOnClickListener(view -> {
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
        });
    }

    private void setAddToCalendarListener() {
        fragmentBinding.tvCalendar.setOnClickListener(v -> addToCalendar());
    }

    private void initializeMap(View view, Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.mapEBView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEventDateTime() {
        Start start = event.getStart();
        if (start != null && start.getLocal() != null) {
            fragmentBinding.tvClockEventDate.
                    setText(DateUtils.convertEventDatetoDisplayFormat(start.getLocal()));
            fragmentBinding.tvEventMonth.setText(DateUtils.
                    getFormattedMonthForHeader(start.getLocal()));
            fragmentBinding.tvEventDay.setText(DateUtils.
                    getFormattedDayForHeader(start.getLocal()));
        }

    }

    private void setSourceSiteLinkIntentListener() {

    }


    private void getAddressFromAPI(Event e) {

        eventBriteClientRetrofit = EventBriteClientRetrofit.getInstance();
        Map<String, String> data = new HashMap<>();
        data.put(TOKEN, getResources().getString(R.string.eventbrite_api_key));
        Call<Venue> call = eventBriteClientRetrofit.EBRetrofitClientFactory().
                getVenueFromServer(e.getVenueId(), data);

        call.enqueue(new Callback<Venue>() {
            @Override
            public void onResponse(Call<Venue> call, Response<Venue> response) {
                Address address = response.body().getAddress();
                Venue venue = response.body();
                if (address == null) {
                    Log.d(TAG, "No Venue Info retreived from API ");
                } else {
                    e.setVenue(response.body());
                    e.saveOrUpdate();
                    generateAddressString(venue,0);
                    populateAddressMap(e);
                }
            }

            @Override
            public void onFailure(Call<Venue> call, Throwable t) {
                Log.d(TAG, "Venue Request Failed " + t.getMessage());
            }
        });


    }

    private String generateAddressString(Venue venue, int type) {
        String eventAddress ="";

        if(venue.getName()!= null && type != 1){
            eventAddress += event.getVenue().getName() + ":" +"\n";
        }
        if(venue.getAddress()!= null){
            Address a=  venue.getAddress();
            if(a.getAddress1()!=null){
                eventAddress += a.getAddress1();
             }
            if(a.getCity()!=null){
                eventAddress += " " + a.getCity();
            }
            if(a.getCountry()!=null){
                eventAddress += " " + a.getCountry();
            }
            if(a.getPostalCode()!=null){
                eventAddress += " " +a.getPostalCode();
            }
        }
        fragmentBinding.tvLocation.setText(eventAddress);
        return eventAddress;
    }


    protected void populateAddressMap(Event e) {
        if (event.getVenue() == null) {
            return;
        }
        Address address = event.getVenue().getAddress();
        //reset fragment's databinding
        fragmentBinding.setEvent(e);

        mMapView.getMapAsync(mMap -> {
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
                    snippet(generateAddressString(event.getVenue(),1)));


            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(11).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            long eventID = Long.parseLong(uri.getLastPathSegment());
            Toast.makeText(getActivity(), "Added to Calendar",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_CALENDAR}, FomonoApplication.PERM_CAL_EVENT_REQ_CODE);
        }
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
    }

    public void enableMapLocation() {
        int fineLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fineLocationPerm == PackageManager.PERMISSION_GRANTED || coarseLocationPerm == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }

}
