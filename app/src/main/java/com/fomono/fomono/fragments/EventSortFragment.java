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
import android.widget.ImageButton;
import android.widget.Spinner;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentSortEventsBinding;

/**
 * Created by jsaluja on 4/15/2017.
 */


public class EventSortFragment extends DialogFragment {

    private Spinner eventSortParam;
    private ImageButton eventSortSaveButton;
    private String sortingParam;
    private int sortingParamPos=0;

    public OnFragmentInteractionListener mListener;

    public EventSortFragment() {
        // Required empty public constructor
    }

    public static EventSortFragment newInstance(int previousSortingParamPos) {
        EventSortFragment fragment = new EventSortFragment();
        Bundle args = new Bundle();
        args.putInt("previous_spinner_pos", previousSortingParamPos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSortEventsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sort_events, container, false);
        View mView = binding.getRoot();
        dataBindFragmentValues(binding);

        getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        getDialog().getWindow().setAttributes(params);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int previousSortingParamPos = bundle.getInt("previous_spinner_pos", 0);
            eventSortParam.setSelection(previousSortingParamPos);
        }
        eventSortParam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortingParamPos = position;
                sortingParam = parent.getItemAtPosition(position).toString();
                if(sortingParam.equals("best_match")) {
                    sortingParam = "best";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        eventSortSaveButton.setOnClickListener(v -> {
            mListener = (OnFragmentInteractionListener) getActivity();
            mListener.onFinishSortDialog(sortingParam, sortingParamPos);
            dismiss();
        });
        return mView;
    }

    public void dataBindFragmentValues(FragmentSortEventsBinding binding) {

        eventSortParam = binding.EventSortSpinnerId;
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
        void onFinishSortDialog(String sortingParam, int sortingParamPos);
    }
}