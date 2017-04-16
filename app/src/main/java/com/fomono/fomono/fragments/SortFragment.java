package com.fomono.fomono.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentSortBinding;

/**
 * Created by jsaluja on 4/15/2017.
 */


public class SortFragment extends DialogFragment {

    private Spinner eventSortOrder;
    private ImageButton eventSortSaveButton;
    String sortingOrder;

    public OnFragmentInteractionListener mListener;

    public SortFragment() {
        // Required empty public constructor
    }

    public static SortFragment newInstance() {
        SortFragment fragment = new SortFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSortBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sort, container, false);
        View mView = binding.getRoot();
        dataBindFragmentValues(binding);

        getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        getDialog().getWindow().setAttributes(params);

        eventSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortingOrder = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        eventSortSaveButton.setOnClickListener(v -> {
            mListener = (OnFragmentInteractionListener) getActivity();
            mListener.onFinishEventSortDialog(sortingOrder);
            dismiss();
        });
        return mView;
    }

    public void dataBindFragmentValues(FragmentSortBinding binding) {

        eventSortOrder = binding.EventSortSpinnerId;
        eventSortSaveButton = binding.EventSortButtonId;
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
        void onFinishEventSortDialog(String sortingOrder);
    }
}