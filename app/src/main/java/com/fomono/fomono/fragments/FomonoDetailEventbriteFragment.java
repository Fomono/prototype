package com.fomono.fomono.fragments;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentEventbriteDetailBinding;
import com.fomono.fomono.models.events.events.Address;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.Venue;
import com.fomono.fomono.network.client.EventBriteClientRetrofit;
import com.fomono.fomono.utils.DateUtils;
import com.fomono.fomono.utils.FavoritesUtil;
import com.fomono.fomono.utils.RoundedTransformation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.x;
import static android.content.ContentValues.TAG;
import static com.fomono.fomono.fragments.MainListFragment.screenWidth;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoDetailEventbriteFragment extends android.support.v4.app.Fragment {
    FragmentEventbriteDetailBinding fragmentEventbriteDetailBinding;

    private GoogleMap googleMap;
    MapView mMapView;
    EventBriteClientRetrofit eventBriteClientRetrofit;
    Event event;
    ProgressDialog pd;
    ImageButton ibFavorite;
    FavoritesUtil favsUtil;

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

        eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();
        Map<String, String> data = new HashMap<>();
        data.put("token", getResources().getString(R.string.eventbrite_api_key));
        Call<Venue> call = eventBriteClientRetrofit.EBRetrofitClientFactory().
                getVenueFromServer(e.getVenueId(),data);
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();

        call.enqueue(new Callback<Venue>() {
            @Override
            public void onResponse(Call<Venue> call, Response<Venue> response) {
                pd.dismiss();
               Address address = response.body().getAddress();
                if (address == null) {
                    Log.d(TAG, "MO MATCH ");
                } else {
                    e.setVenue(response.body());
                    e.saveOrUpdate();
                    populateAddressMap(e);
                }

            }

            @Override
            public void onFailure(Call<Venue> call, Throwable t) {
                pd.dismiss();
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });


    }

    //  @BindingAdapter({"imageUrl"})
    private static void setImageUrl(ImageView view, String imageUrl) {
        //Glide.with(view.getContext()).load(imageUrl).placeholder(R.drawable.botaimage).
          //      error(R.drawable.botaimage).into(view);
        Picasso.with(view.getContext()).load(imageUrl).transform(new RoundedTransformation(6,3)).
                placeholder(R.drawable.ic_fomono_big).
                resize(screenWidth, 0).into(view);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentEventbriteDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_eventbrite_detail, parent, false);

        View view = fragmentEventbriteDetailBinding.getRoot();
        ButterKnife.bind(this,view);

        fragmentEventbriteDetailBinding.tvSiteLink.setClickable(true);
        fragmentEventbriteDetailBinding.tvSiteLink.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href=" + event.getUrl() +  ">" + "CLICK HERE" + "</a>";
        fragmentEventbriteDetailBinding.tvSiteLink.setText(Html.fromHtml(text));


        fragmentEventbriteDetailBinding.tvEventDate.setText(DateUtils.getFormattedDateForHeader(event.getStart().getUtc()));
        event.getStart().setLocal(DateUtils.getFormattedDate(event.getStart().getUtc()));

        setImageUrl(fragmentEventbriteDetailBinding.ivEventImage, event.getLogo().getUrl());

        populateDetail(event);

        mMapView = (MapView) view.findViewById(R.id.mapEBView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        getAddressFromAPI(event);


        fragmentEventbriteDetailBinding.tvClockCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCalendar(event.getStart().getUtc(), event.getEnd().getUtc());
            }
        });


        fragmentEventbriteDetailBinding.ivMessageShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra(event.getUrl(), x);
                startActivity(sendIntent);

            }
        });

        fragmentEventbriteDetailBinding.ivTwitterShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    // Check if the Twitter app is installed on the phone.
                    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setClassName("com.twitter.android", "com.twitter.android.composer.ComposerActivity");
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, event.getUrl());
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

        fragmentEventbriteDetailBinding.ivEmailShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "some@email.address" });
                intent.putExtra(Intent.EXTRA_SUBJECT, event.getName().getText());
                intent.putExtra(Intent.EXTRA_TEXT, event.getUrl());
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, ""));
                }
            }
        });

        ibFavorite = fragmentEventbriteDetailBinding.ivFavoriteIcon;
        if (favsUtil.isFavorited(event)) {
            ibFavorite.setImageResource(R.drawable.ic_favorite_on);
        }
        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favsUtil.isFavorited(event)) {
                    ibFavorite.setImageResource(R.drawable.ic_favorite_off);
                    favsUtil.removeFromFavorites(event);
                } else {
                    ibFavorite.setImageResource(R.drawable.ic_favorite_on);
                    favsUtil.addToFavorites(event);
                }
            }
        });

        return fragmentEventbriteDetailBinding.getRoot();
    }


    protected void populateDetail(Event e) {
        fragmentEventbriteDetailBinding.setEvent(e);

    }

    protected void populateAddressMap(Event e){
           //reset fragment's databinding
        Address address = event.getVenue().getAddress();
        fragmentEventbriteDetailBinding.setEvent(e);
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
                if(event.getVenue() != null) {
                    latlng = new LatLng(Double.parseDouble(address.getLatitude()),
                            Double.parseDouble(address.getLongitude()));
                }else {
                    latlng = new LatLng(0,0);
                }
                googleMap.addMarker(new MarkerOptions().position(latlng).title(event.getVenue().getName()).
                        snippet(address.getAddress1() + ", " +address.getCity() + ", " +
                                address.getCountry() + "," +  address.getPostalCode()).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_pin)));


                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
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
        values.put(CalendarContract.Events.TITLE, event.getName().getText());
        values.put(CalendarContract.Events.DESCRIPTION, event.getDescription().getText().toString());
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
