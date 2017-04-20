package com.fomono.fomono.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.fomono.fomono.R;

/**
 * Created by jsaluja on 4/15/2017.
 */


public class EventSortFragment extends BaseSortFragment {

    String sortingParam;
    int sortingParamPos=0;
    int prevSortingParamId=0;

    public static EventSortFragment newInstance(int selectedRadioButton) {
        EventSortFragment fragment = new EventSortFragment();
        Bundle args = new Bundle();
        args.putInt("previous_pos", selectedRadioButton);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);


        sortButton1.setText(getResources().getString(R.string.event_sort_val_one));
        sortButton2.setText(getResources().getString(R.string.event_sort_val_two));
        sortButton3.setText(getResources().getString(R.string.event_sort_val_three));

        Bundle bundle = getArguments();
        if (bundle != null) {
            prevSortingParamId = bundle.getInt("previous_pos", 0);
        }

        if(prevSortingParamId != -1) {
            RadioButton prevChecked = (RadioButton) view.findViewById(prevSortingParamId);
            prevChecked.setChecked(true);
        }

        sortRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId != prevSortingParamId) {
                RadioButton r = (RadioButton) view.findViewById(checkedId);
                mListener.onFinishSortDialog(r.getText().toString(), checkedId);
                dismiss();
            }
        });
        return view;
    }
}