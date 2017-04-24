package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.fomono.fomono.R;

public class MovieSortFragment extends BaseSortFragment {

    String sortingParam;
    int sortingParamPos=0;
    int prevSortingParamId=0;

    public static MovieSortFragment newInstance(int selectedRadioButton) {
        MovieSortFragment fragment = new MovieSortFragment();
        Bundle args = new Bundle();
        args.putInt("previous_pos", selectedRadioButton);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        tvSortTitle.setText(getString(R.string.MovieSortFilterText));
        sortButton1.setText(getResources().getString(R.string.movie_sort_val_one));
        sortButton2.setText(getResources().getString(R.string.movie_sort_val_two));
        sortButton3.setText(getResources().getString(R.string.movie_sort_val_three));
        Bundle bundle = getArguments();
        if (bundle != null) {
            prevSortingParamId = bundle.getInt("previous_pos", 0);
        }
        if (prevSortingParamId <= 0) {
            //default to "playing_now"
            prevSortingParamId = R.id.SortButton1Id;
        }

        if(prevSortingParamId != -1) {
            RadioButton prevChecked = (RadioButton) view.findViewById(prevSortingParamId);
            prevChecked.setChecked(true);
        }

        sortRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId != prevSortingParamId) {
                String sortString = getSortString(checkedId);
                mListener.onFinishSortDialog(sortString, checkedId);
            }
        });
        return view;
    }

    private String getSortString(int buttonId) {
        switch (buttonId) {
            case R.id.SortButton1Id:
                return "playing_now";
            case R.id.SortButton2Id:
                return "popular";
            case R.id.SortButton3Id:
                return "top_rated";
            default:
                return "";
        }
    }
}