package com.fomono.fomono.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Criteria;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.x;
import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoDetailYelpFragment extends android.support.v4.app.Fragment {
    FragmentYelpDetailBinding fragmentBinding;

    private GoogleMap googleMap;
    MapView mMapView;
    YelpClientRetrofit yelpClientRetrofit;
    Business business;
    ProgressDialog pd;
    ImageButton ibFavorite;
    FavoritesUtil favsUtil;
    public int screenWidthDetail;
    LinearLayout llGallery;

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

        fragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_yelp_detail, parent, false);

        View view = fragmentBinding.getRoot();
        ButterKnife.bind(this, view);

        fragmentBinding.tvSiteLink.setClickable(true);
        fragmentBinding.tvSiteLink.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href=" + business.getUrl() + ">" + "CLICK HERE" + "</a>";
        fragmentBinding.tvSiteLink.setText(Html.fromHtml(text));

        fragmentBinding.tvEventDate.setText("");
        fragmentBinding.tvLocation.setText(getLocationAddress());
        fragmentBinding.rbMovierating.setRating(Double.valueOf(business.getRating()).floatValue());
        fragmentBinding.tvRatingText.setText(Double.valueOf(business.getRating()).toString() + "/5");

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        screenWidthDetail = (int) (pxWidth / displayMetrics.density);


        Log.d(TAG, "width is " + screenWidthDetail);

        setImageUrl(fragmentBinding.ivEventImage, business.getImageUrl(), screenWidthDetail);

        populateDetail();
        getDetailsFromAPI();

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        populateAddressMap();


        fragmentBinding.ivMessageShareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                sendIntent.putExtra(business.getUrl(), x);
                startActivity(sendIntent);

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
                    intent.putExtra(Intent.EXTRA_TEXT, business.getUrl());
                    startActivity(intent);

                } catch (Exception e) {
                    String url = "http://www.twitter.com/intent/tweet?url=YOURURL&text=YOURTEXT";
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
                if (business.getName() != null) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, business.getName().toString());
                }
                intent.putExtra(Intent.EXTRA_TEXT, business.getUrl());
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, ""));
                }
            }
        });

        ibFavorite = fragmentBinding.ivFavoriteIcon;
        if (favsUtil.isFavorited(business)) {
            ibFavorite.setImageResource(R.drawable.ic_favorite);
        }
        ibFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favsUtil.isFavorited(business)) {
                    ibFavorite.setImageResource(R.drawable.ic_favorite_grey);
                    favsUtil.removeFromFavorites(business);
                } else {
                    ibFavorite.setImageResource(R.drawable.ic_favorite);
                    favsUtil.addToFavorites(business);
                }
            }
        });

        return fragmentBinding.getRoot();

    }

    private View insertPhoto(String url) {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(400, 300));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(400, 300));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        setImageUrl(imageView,url,screenWidthDetail);

        layout.addView(imageView);
        return layout;
    }



    private void getDetailsFromAPI() {

        yelpClientRetrofit = YelpClientRetrofit.getInstance();
        Call<BusinessDetail> call = yelpClientRetrofit.YelpRetrofitClientFactory().getYelpBusinessDetailById
                (business.getId());

        pd = new ProgressDialog(getActivity());
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();

        call.enqueue(new Callback<BusinessDetail>() {
            @Override
            public void onResponse(Call<BusinessDetail> call, Response<BusinessDetail> response) {
                pd.dismiss();
                BusinessDetail bDetail = response.body();
                if (bDetail == null) {
                    Log.d(TAG, "NO MATCH ");
                } else {
                    business.setBusinessDetail(bDetail);
                    populateDetail();
                }
            }

            @Override
            public void onFailure(Call<BusinessDetail> call, Throwable t) {
                pd.dismiss();
                Log.d(TAG, "REQUEST Failed " + t.getMessage());
            }
        });
    }

    private void populateDetail() {
        fragmentBinding.setBusiness(business);
        if(business.getBusinessDetail() !=null) {
            fragmentBinding.tvHours.setText(createHoursOpenString());
        }
        if(business.getBusinessDetail() !=null && business.getBusinessDetail().getPhotos() !=null) {
            for (String url : business.getBusinessDetail().getPhotos()) {
                fragmentBinding.llGallery.addView(insertPhoto(url));
            }
        }else{
           // fragmentBinding.hsvGallery.setVisibility(View.GONE);
        }

    }


    private String getLocationAddress() {
        String address = "";
        Location l = business.getLocation();
        address = l.getAddress1() + ", " + l.getCity() + ", " + l.getLocationState()
                + ", " + l.getCountry() + ", " + l.getZipCode();
        return address;
    }

    private String createHoursOpenString(){
       String hoursOpen="";
       List<Open> openList = business.getBusinessDetail().getHours().get(0).getOpen();
       for(Open open : openList ) {
           String startDate = DateUtils.convertMilitarytoStandard(open.getStart());
           String endDate  =DateUtils.convertMilitarytoStandard(open.getEnd());
           if (open.getDay() == 0) {
               hoursOpen += "Monday: " + startDate + " - " + endDate + "\n";
           } else if (open.getDay() == 1) {
               hoursOpen += "Tuesday: " + startDate + " - " + endDate + "\n";;
           } else if (open.getDay() == 2) {
               hoursOpen += "Wednesday: " + startDate + " - " + endDate + "\n";;
           } else if (open.getDay() == 3) {
               hoursOpen += "Thursday: " + startDate + " - " + endDate + "\n";;
           } else if (open.getDay() == 4) {
               hoursOpen += "Friday: " + startDate + " - " + endDate+ "\n";;
           } else if (open.getDay() == 5) {
               hoursOpen += "Saturday: " + startDate + " - " + endDate+ "\n";;
           } else if (open.getDay() == 6) {
               hoursOpen += "Sunday: " + startDate + " - " + endDate+ "\n";;
           }
       }
       return hoursOpen;
    }

    protected void populateAddressMap() {
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
                if (business.getCoordinates() != null) {
                    coordinates = business.getCoordinates();
                    ltlg = new LatLng(coordinates.getLatitude(), coordinates.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(ltlg).title(getLocationAddress()));

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
