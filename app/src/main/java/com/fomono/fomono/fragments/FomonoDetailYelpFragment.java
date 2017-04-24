package com.fomono.fomono.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentYelpDetailBinding;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.eats.BusinessDetail;
import com.fomono.fomono.models.eats.Coordinates;
import com.fomono.fomono.models.eats.Location;
import com.fomono.fomono.models.eats.Open;
import com.fomono.fomono.models.user.User;
import com.fomono.fomono.network.client.YelpClientRetrofit;
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

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoDetailYelpFragment extends android.support.v4.app.Fragment {

    public static final String OPEN_NOW = "Open now";
    public static final String CLOSED ="Closed now";
    public static final String UNAVAILABLE ="Unavailable";
    public static final int ZERO = 0;
    public static final int ONE =1;
    public static final int TEN =10;


    FragmentYelpDetailBinding fragmentBinding;

    private GoogleMap googleMap;
    MapView mMapView;
    YelpClientRetrofit yelpClientRetrofit;
    Business business;
    ImageButton ibFavorite;
    FavoritesUtil favsUtil;
    public int screenWidthDetail;
    LinearLayout llGallery;

    public interface FomonoEventUpdateListener {
        void onFomonoEventUpdated();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        business = getArguments().getParcelable("business_obj");
        favsUtil = FavoritesUtil.getInstance();
    }

    public static FomonoDetailYelpFragment newInstance(Business business) {
        FomonoDetailYelpFragment fomonoDetailYelpFragment = new FomonoDetailYelpFragment();
        Bundle args = new Bundle();
        args.putParcelable("business_obj", business);
        fomonoDetailYelpFragment.setArguments(args);
        return fomonoDetailYelpFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        fragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_yelp_detail, parent, false);

        View view = fragmentBinding.getRoot();
        ButterKnife.bind(this, view);

        fragmentBinding.tvLocation.setText(getLocationAddress());
        fragmentBinding.rbRating.setRating(Double.valueOf(business.getRating()).floatValue());


        //SetupListeners
        setSourceSiteLinkIntentListener();
        setPhoneCallIntentListener();
        setFavoriteIconListener();


        calculateScreenDimensions();
        populateBindingDetail();
        getAdditionalDetailFromAPI();
        initializeMap(view,savedInstanceState);
        populateAddressOnMap();

        return fragmentBinding.getRoot();

    }

    private void initializeMap(View view, Bundle savedInstanceState) {

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateScreenDimensions() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        screenWidthDetail = (int) (pxWidth / displayMetrics.density);
    }

    private void setSourceSiteLinkIntentListener() {
        fragmentBinding.ivSiteLink.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            if(business.getUrl()!=null) {
                intent.setData(Uri.parse(business.getUrl()));
            }
            startActivity(intent);
        });
    }

    private void setFavoriteIconListener() {

        ibFavorite = fragmentBinding.ivFavoriteIcon;
        favsUtil.isFavorited(business, isFavorited -> {
            if (isFavorited) {
                ibFavorite.setImageResource(R.drawable.ic_favorite);
            }
        });

        ibFavorite.setOnClickListener(view -> {
            favsUtil.isFavorited(business, isFavorited -> {
                if (isFavorited) {
                    ibFavorite.setImageResource(R.drawable.ic_favorite_grey);
                    favsUtil.removeFromFavorites(business);
                } else {
                    ibFavorite.setImageResource(R.drawable.ic_favorite);
                    favsUtil.addToFavorites(business);
                }
            });

            if (getActivity() instanceof FomonoEventUpdateListener) {
                ((FomonoEventUpdateListener) getActivity()).onFomonoEventUpdated();
            }
        });
    }

    private void setPhoneCallIntentListener() {

        fragmentBinding.tvPhone.setOnClickListener(v -> {

            int checkPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, FomonoApplication.PERM_PHO_BUS_REQ_CODE);
            } else {
                launchPhone();
            }
        });
    }

    public void launchPhone() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        if(business.getPhone()!=null) {
            callIntent.setData(Uri.parse("tel:" + business.getPhone()));
        }
        startActivity(callIntent);
    }

    private static void setImageUrl(ImageView view, String imageUrl, int screenWidthDetail) {
        Picasso.with(view.getContext()).load(imageUrl).transform(new RoundedTransformation(6, 3)).
                placeholder(R.drawable.ic_image_placeholder).
                resize(screenWidthDetail, ZERO).into(view);

    }


    private View insertPictureIntoScrollView(String url) {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(415, 300));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 300));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        setImageUrl(imageView,url,screenWidthDetail);

        layout.addView(imageView);
        return layout;
    }



    private void getAdditionalDetailFromAPI() {

        yelpClientRetrofit = YelpClientRetrofit.getInstance();
        Call<BusinessDetail> call = yelpClientRetrofit.YelpRetrofitClientFactory().getYelpBusinessDetailById
                (business.getId());

        call.enqueue(new Callback<BusinessDetail>() {
            @Override
            public void onResponse(Call<BusinessDetail> call, Response<BusinessDetail> response) {
                BusinessDetail bDetail = response.body();
                if (bDetail == null) {
                    Log.d(TAG, "NO MATCH ");
                } else {
                    business.setBusinessDetail(bDetail);
                    populateBindingDetail();
                }
            }

            @Override
            public void onFailure(Call<BusinessDetail> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());
            }
        });
    }

    private void populateBindingDetail() {

        fragmentBinding.setBusiness(business);
        if(business.getBusinessDetail() !=null) {
            fragmentBinding.tvHours.setText(createHoursOpenString());
            if(checkIfOpen()){
                fragmentBinding.tvOpenNow.setText(OPEN_NOW);
                fragmentBinding.tvOpenNow.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorGreen));
            }else{
                fragmentBinding.tvOpenNow.setText(CLOSED);
                fragmentBinding.tvOpenNow.setTextColor(Color.RED);
            }
        }
        if(business.getBusinessDetail() !=null) {
            if (business.getBusinessDetail().getPhotos() != null) {
                for (String url : business.getBusinessDetail().getPhotos()) {
                    fragmentBinding.llGallery.addView(insertPictureIntoScrollView(url));
                }
            } else {
                fragmentBinding.hsvGallery.setVisibility(View.GONE);

            }
        }
        fragmentBinding.executePendingBindings();
    }


    private String getLocationAddress() {
        String address = "";
        if( business.getLocation()!=null) {
            Location l = business.getLocation();
            address = l.getAddress1() + " " + l.getCity() + " " + l.getLocationState()
                    + " " + l.getCountry() + " " + l.getZipCode();
        }
        return address;
    }


    private boolean checkIfOpen() {

        if (business.getBusinessDetail() != null && business.getBusinessDetail().getHours() != null
                && business.getBusinessDetail().getHours().get(ZERO) != null
                && business.getBusinessDetail().getHours().get(ZERO).getOpen() != null) {
            List<Open> openList = business.getBusinessDetail().getHours().get(ZERO).getOpen();

            for (Open open : openList) {
                if (open.getDay() >= ZERO && open.getStart() != null && open.getEnd() != null) {
                    int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - ONE;
                    String hourOfDay;
                    String minOfDay;
                    if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) <TEN){
                        hourOfDay = ZERO + String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    } else{
                        hourOfDay = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    }

                    if(Calendar.getInstance().get(Calendar.MINUTE) <TEN){
                        minOfDay = ZERO + String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
                    } else{
                        minOfDay = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
                    }
                    int currentTime = Integer.parseInt(hourOfDay+minOfDay) ;
                    int startHour = Integer.parseInt(open.getStart());
                    int endHour = Integer.parseInt(open.getEnd());

                    if (dayOfWeek == open.getDay() && currentTime < endHour && currentTime > startHour) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String createHoursOpenString(){
       String hoursOpen="";
        if(business.getBusinessDetail()!=null && business.getBusinessDetail().getHours() !=null
                && business.getBusinessDetail().getHours().get(ZERO)!=null
                && business.getBusinessDetail().getHours().get(ZERO).getOpen() !=null) {
            List<Open> openList = business.getBusinessDetail().getHours().get(ZERO).getOpen();
            for (Open open : openList) {
                String startDate = DateUtils.convertMilitarytoStandard(open.getStart());
                String endDate = DateUtils.convertMilitarytoStandard(open.getEnd());

                if (open.getDay() == ZERO) {
                    hoursOpen += "Monday: " + startDate + " - " + endDate + "\n";
                } else if (open.getDay() == 1) {
                    hoursOpen += "Tuesday: " + startDate + " - " + endDate + "\n";
                } else if (open.getDay() == 2) {
                    hoursOpen += "Wednesday: " + startDate + " - " + endDate + "\n";
                } else if (open.getDay() == 3) {
                    hoursOpen += "Thursday: " + startDate + " - " + endDate + "\n";
                } else if (open.getDay() == 4) {
                    hoursOpen += "Friday: " + startDate + " - " + endDate + "\n";
                } else if (open.getDay() == 5) {
                    hoursOpen += "Saturday: " + startDate + " - " + endDate + "\n";
                } else if (open.getDay() == 6) {
                    hoursOpen += "Sunday: " + startDate + " - " + endDate + "\n";
                }
            }
        } else{
            hoursOpen=UNAVAILABLE;
        }
       return hoursOpen;
    }

    protected void populateAddressOnMap() {
        //reset fragment's databinding
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
                Coordinates coordinates;
                LatLng ltlg;
                // For dropping a marker at a point on the Map
                if (business.getCoordinates() != null && business.getName()!=null ) {
                    coordinates = business.getCoordinates();
                    ltlg = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(ltlg).title(business.getName()).snippet(getLocationAddress()));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(ltlg).zoom(11).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    coordinates = null;
                    ltlg = null;
                }

            }
        });
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
