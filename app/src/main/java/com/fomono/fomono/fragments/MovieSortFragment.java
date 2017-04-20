package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fomono.fomono.R;
import com.fomono.fomono.fragments.BaseSortFragment;

public class MovieSortFragment extends BaseSortFragment {

    String sortingParam;
    int sortingParamPos=0;

    public static MovieSortFragment newInstance(int previousSortingParamPos) {
        MovieSortFragment fragment = new MovieSortFragment();
        Bundle args = new Bundle();
        args.putInt("previous_spinner_pos", previousSortingParamPos);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        sortButton1.setBackground(ContextCompat.getDrawable(getActivity(), R.color.colorGrey));

        sortButton1.setText(getResources().getString(R.string.movie_sort_val_one));
        sortButton2.setText(getResources().getString(R.string.movie_sort_val_two));
        sortButton3.setText(getResources().getString(R.string.movie_sort_val_three));

        return view;
    }
}