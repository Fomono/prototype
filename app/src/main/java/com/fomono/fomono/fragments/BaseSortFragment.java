package com.fomono.fomono.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.FragmentSortBinding;

public class BaseSortFragment extends DialogFragment {

    public RadioButton sortButton1, sortButton2, sortButton3;
    public RadioGroup sortRadioGroup;
    TextView tvSortTitle;

    public OnFragmentInteractionListener mListener;

    public BaseSortFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSortBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sort, container, false);
        View view = binding.getRoot();
        dataBindFragmentValues(binding);

        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setButtonChecked(compoundButton);
                } else {
                    setButtonUnchecked(compoundButton);
                }
            }
        };

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        };

        sortButton1.setOnCheckedChangeListener(changeListener);
        sortButton2.setOnCheckedChangeListener(changeListener);
        sortButton3.setOnCheckedChangeListener(changeListener);

        sortButton1.setOnClickListener(clickListener);
        sortButton2.setOnClickListener(clickListener);
        sortButton3.setOnClickListener(clickListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int screenWidthPx = displayMetrics.widthPixels;
        int dialogWidth = screenWidthPx * 2 / 3;        //set dialog width to 2/3rds of the screen size

        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = dialogWidth;
        window.setAttributes(layoutParams);
    }

    public void dataBindFragmentValues(FragmentSortBinding binding) {
        sortButton1 = binding.SortButton1Id;
        sortButton2 = binding.SortButton2Id;
        sortButton3 = binding.SortButton3Id;
        sortRadioGroup = binding.SortRadioGrp;
        tvSortTitle = binding.tvSortTitle;
    }

    protected void setButtonChecked(CompoundButton button) {
        button.setTextColor(ContextCompat.getColor(getContext(), R.color.colorFomono));
        button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    }

    protected void setButtonUnchecked(CompoundButton button) {
        button.setTextColor(ContextCompat.getColor(getContext(), R.color.colorBlack));
        button.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
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
