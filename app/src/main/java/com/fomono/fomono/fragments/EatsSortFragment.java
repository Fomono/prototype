package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.fomono.fomono.R;

public class EatsSortFragment extends BaseSortFragment {

    String sortingParam;
    int sortingParamPos=0;
    int prevSortingParamId=0;

    public static EatsSortFragment newInstance(int selectedRadioButton) {
        EatsSortFragment fragment = new EatsSortFragment();
        Bundle args = new Bundle();
        args.putInt("previous_pos", selectedRadioButton);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        sortButton1.setText(getResources().getString(R.string.eats_sort_val_one));
        sortButton2.setText(getResources().getString(R.string.eats_sort_val_two));
        sortButton3.setText(getResources().getString(R.string.eats_sort_val_three));

        Bundle bundle = getArguments();
        if (bundle != null) {
            prevSortingParamId = bundle.getInt("previous_pos", 0);
        }
        if (prevSortingParamId <= 0) {
            //default to "best_match"
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
                return "best_match";
            case R.id.SortButton2Id:
                return "distance";
            case R.id.SortButton3Id:
                return "review_count";
            default:
                return "";
        }
    }
}
