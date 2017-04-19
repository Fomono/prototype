package com.fomono.fomono.fragments;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentEventbriteDetailBinding;
import com.fomono.fomono.models.events.events.Address;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.Logo;
import com.fomono.fomono.models.events.events.Start;
import com.fomono.fomono.models.events.events.Venue;
import com.fomono.fomono.models.user.User;
import com.fomono.fomono.network.client.EventBriteClientRetrofit;
import com.fomono.fomono.utils.DateUtils;
import com.fomono.fomono.utils.FavoritesUtil;
import com.fomono.fomono.utils.RoundedTransformation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    ProgressDialog pd;
    ImageButton ibFavorite;
    FavoritesUtil favsUtil;
    public int screenWidthDetail;

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
    private static void setImageUrl(ImageView view, String imageUrl, int screenWidthDetail) {
        //Glide.with(view.getContext()).load(imageUrl).placeholder(R.drawable.botaimage).
        //      error(R.drawable.botaimage).into(view);
        Picasso.with(view.getContext()).load(imageUrl).transform(new RoundedTransformation(6, 3)).
                placeholder(R.drawable.ic_fomono_big).
                resize(screenWidthDetail, 0).into(view);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentEventbriteDetailBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_eventbrite_detail, parent, false);

        View view = fragmentEventbriteDetailBinding.getRoot();
        ButterKnife.bind(this, view);

        fragmentEventbriteDetailBinding.tvSiteLink.setClickable(true);
        fragmentEventbriteDetailBinding.tvSiteLink.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href=" + event.getUrl() + ">" + "CLICK HERE" + "</a>";
        fragmentEventbriteDetailBinding.tvSiteLink.setText(Html.fromHtml(text));

        Start start = event.getStart();
        if (start != null) {
            fragmentEventbriteDetailBinding.tvEventDate.setText(DateUtils.getFormattedDateForHeader(start.getUtc()));
            start.setLocal(DateUtils.getFormattedDate(start.getUtc()));
            event.saveOrUpdate();
        }

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        screenWidthDetail = (int) (pxWidth / displayMetrics.density);

        Log.d(TAG, "width is " + screenWidthDetail);

        Logo logo = event.getLogo();
        if (logo != null) {
            setImageUrl(fragmentEventbriteDetailBinding.ivEventImage, logo.getUrl(), screenWidthDetail);
        }

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
                if (event.getStart() != null && event.getEnd() != null) {
                    addToCalendar(event.getStart().getUtc(), event.getEnd().getUtc());
                }
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
        if (favsUtil.isFavorited(event)) {
            ibFavorite.setImageResource(R.drawable.ic_favorite);
        }
        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favsUtil.isFavorited(event)) {
                    ibFavorite.setImageResource(R.drawable.ic_favorite_grey);
                    favsUtil.removeFromFavorites(event);
                } else {
                    ibFavorite.setImageResource(R.drawable.ic_favorite);
                    favsUtil.addToFavorites(event);
                }
            }
        });

        return fragmentEventbriteDetailBinding.getRoot();
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
        if (event.getName() != null) {
            values.put(CalendarContract.Events.TITLE, event.getName().getText());
        }
        if (event.getDescription() != null) {
            values.put(CalendarContract.Events.DESCRIPTION, event.getDescription().getText().toString());
        }
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
        mMapView.onLowMemory();
    }

    public void enableMapLocation() {
        int fineLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fineLocationPerm == PackageManager.PERMISSION_GRANTED || coarseLocationPerm == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
    }

}
