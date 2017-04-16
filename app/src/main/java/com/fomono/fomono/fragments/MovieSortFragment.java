package com.fomono.fomono.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentSortMovieBinding;

public class MovieSortFragment extends DialogFragment {

    private Spinner movieSortParam;
    private ImageButton movieSortSaveButton;
    String sortingParam;

    public OnFragmentInteractionListener mListener;

    public MovieSortFragment() {
        // Required empty public constructor
    }

    public static MovieSortFragment newInstance() {
        MovieSortFragment fragment = new MovieSortFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSortMovieBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sort_movie, container, false);
        View mView = binding.getRoot();
        dataBindFragmentValues(binding);

        getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        getDialog().getWindow().setAttributes(params);

        movieSortParam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortingParam = parent.getItemAtPosition(position).toString();
                if(sortingParam.equals("rating")) {
                    sortingParam = "vote_average";
                }
                sortingParam = sortingParam + ".desc";
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        movieSortSaveButton.setOnClickListener(v -> {
            mListener = (OnFragmentInteractionListener) getActivity();
            mListener.onFinishEventSortDialog(sortingParam);
            dismiss();
        });
        return mView;
    }

    public void dataBindFragmentValues(FragmentSortMovieBinding binding) {

        movieSortParam = binding.MovieSortSpinnerId;
        movieSortSaveButton = binding.MovieSortButtonId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void onFinishEventSortDialog(String sortingParam);
    }
}
