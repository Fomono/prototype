package com.fomono.fomono.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.fomono.fomono.R;

public class EatsSortFragment extends BaseSortFragment {

    String sortingParam;
    int sortingParamPos=0;

    public static EatsSortFragment newInstance(int previousSortingParamPos) {
        EatsSortFragment fragment = new EatsSortFragment();
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

        sortButton1.setText(getResources().getString(R.string.eats_sort_val_one));
        sortButton2.setText(getResources().getString(R.string.eats_sort_val_two));
        sortButton3.setText(getResources().getString(R.string.eats_sort_val_three));

        return view;
    }
}
