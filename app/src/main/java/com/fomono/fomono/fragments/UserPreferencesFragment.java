package com.fomono.fomono.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentUserPreferencesBinding;
import com.parse.ParseUser;

/**
 * Created by David on 4/7/2017.
 */

public class UserPreferencesFragment extends Fragment {

    public static final int CODE_FILTERS = 1;

    FragmentUserPreferencesBinding binding;

    Switch swLocationCurr;
    EditText etLocation;
    Spinner spDistance;
    TextView tvFiltersSelected;
    LinearLayout llFilters;

    ParseUser user;

    final int[] ALLOWED_DISTANCES = {1, 2, 5, 10};

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
        swLocationCurr = binding.swLocationCurr;
        etLocation = binding.etLocation;
        spDistance = binding.spDistance;
        tvFiltersSelected = binding.tvFiltersSelected;
        llFilters = binding.llFilters;

        //set switch
        //TODO: think about this

        //set location listener

        String location = user.getString("location");
        if (!TextUtils.isEmpty(location)) {
            etLocation.setText(location);
        }

        //initialize spinner
        String[] distanceStrs = new String[ALLOWED_DISTANCES.length];
        for (int i = 0; i < ALLOWED_DISTANCES.length; i++) {
            distanceStrs[i] = getString(R.string.pref_distance_value, ALLOWED_DISTANCES[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, distanceStrs);
        spDistance.setAdapter(adapter);
        //get selection
        int distanceIndex = user.getInt("distance");
        spDistance.setSelection(distanceIndex);
        spDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user.put("distance", ALLOWED_DISTANCES[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //set on click listener for filters
        llFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserPreferencesListener listener = (UserPreferencesListener) getActivity();
                listener.onComplete(CODE_FILTERS);
            }
        });

        //set number of categories selected
        int numCategories = 0;  //TODO: get num categories from user
        tvFiltersSelected.setText(getString(R.string.pref_filters_selected, numCategories));
    }
}
