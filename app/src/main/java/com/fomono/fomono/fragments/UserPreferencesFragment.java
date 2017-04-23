package com.fomono.fomono.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentUserPreferencesBinding;
import com.fomono.fomono.models.db.Filter;
import com.fomono.fomono.utils.FilterUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by David on 4/7/2017.
 */

public class UserPreferencesFragment extends Fragment {

    public static final int CODE_FILTERS = 1;

    FragmentUserPreferencesBinding binding;

    ImageButton ibUseMyLocation;
    EditText etLocation;
    Spinner spDistance;
    TextView tvFiltersSelected;
    RelativeLayout rlFilters;

    ParseUser user;

    final int[] ALLOWED_DISTANCES = {1, 2, 5, 10, 25};

    public static UserPreferencesFragment newInstance() {
        UserPreferencesFragment fragment = new UserPreferencesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public interface UserPreferencesListener {
        void onComplete(int resultCode);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setup();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_preferences, container, false);

        setupViews();

        return binding.getRoot();
    }

    private void setup() {
        user = ParseUser.getCurrentUser();
    }

    private void setupViews() {
        ibUseMyLocation = binding.ibUseMyLocation;
        etLocation = binding.etLocation;
        spDistance = binding.spDistance;
        tvFiltersSelected = binding.tvFiltersSelected;
        rlFilters = binding.rlFilters;

        //set use my location
        ibUseMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fineLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                int coarseLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
                if (fineLocationPerm != PackageManager.PERMISSION_GRANTED && coarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    //don't check user for flag here since user is taking an action to use current location
                    Toast.makeText(getContext(), getString(R.string.pref_turn_on_location), Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, FomonoApplication.PERM_LOC_FILTER_REQ_CODE);
                } else {
                    useCurrentLocation();
                }
            }
        });

        String location = user.getString("location");
        if (!TextUtils.isEmpty(location)) {
            etLocation.setText(location);
        }

        //set location listener
        etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO: this is saving after every key stroke, we might wanna change this
                String location = editable.toString();
                user.put("location", location);
                user.saveInBackground();
                FilterUtil.getInstance().setDirty();
            }
        });

        //initialize spinner
        String[] distanceStrs = new String[ALLOWED_DISTANCES.length];
        for (int i = 0; i < ALLOWED_DISTANCES.length; i++) {
            distanceStrs[i] = getString(R.string.pref_distance_value, ALLOWED_DISTANCES[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, distanceStrs);
        spDistance.setAdapter(adapter);
        //get selection
        int distance = user.getInt("distance");
        int distanceIndex = getDistanceIndex(distance);
        spDistance.setSelection(distanceIndex);
        spDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user.put("distance", ALLOWED_DISTANCES[i]);
                user.saveInBackground();
                if (i != distanceIndex) {
                    FilterUtil.getInstance().setDirty();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //set on click listener for filters
        rlFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserPreferencesListener listener = (UserPreferencesListener) getActivity();
                listener.onComplete(CODE_FILTERS);
            }
        });

        //set number of categories selected
        try {
            FilterUtil.getInstance().getAllFilters(true, new FindCallback<Filter>() {
                @Override
                public void done(List<Filter> objects, ParseException e) {
                    int numCategories = 0;
                    if (objects != null) {
                        numCategories = objects.size();
                    }
                    tvFiltersSelected.setText(getString(R.string.pref_filters_selected, numCategories));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDistanceIndex(int distance) {
        for (int i = 0; i < ALLOWED_DISTANCES.length; i++) {
            if (ALLOWED_DISTANCES[i] == distance) {
                return i;
            }
        }
        return 0;
    }

    public void useCurrentLocation() {
        int fineLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPerm = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (fineLocationPerm == PackageManager.PERMISSION_GRANTED || coarseLocationPerm == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
                double latitude = loc.getLatitude();
                double longitude = loc.getLongitude();
                etLocation.setText(latitude + ", " + longitude);    //the on text changed handler will save the location on the user
            }
        }
    }
}
