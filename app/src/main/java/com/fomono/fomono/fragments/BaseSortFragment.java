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
import com.fomono.fomono.databinding.FragmentSortBinding;

public class BaseSortFragment extends DialogFragment {

    public Button sortButton1, sortButton2, sortButton3;
    String sortingParam;
    int sortingParamPos=0;

    public OnFragmentInteractionListener mListener;

    public BaseSortFragment() {
        // Required empty public constructor
    }

    public static BaseSortFragment newInstance(int previousSortingParamPos) {
        BaseSortFragment fragment = new BaseSortFragment();
        Bundle args = new Bundle();
        args.putInt("previous_spinner_pos", previousSortingParamPos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentSortBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sort, container, false);
        View mView = binding.getRoot();
        dataBindFragmentValues(binding);


        sortButton1.setBackground(ContextCompat.getDrawable(getActivity(), R.color.colorGrey));

        getDialog().getWindow().setGravity(Gravity.RIGHT | Gravity.TOP);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        getDialog().getWindow().setAttributes(params);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int previousSortingParamPos = bundle.getInt("previous_spinner_pos", 0);
        }

        sortButton1.setOnClickListener(v -> {
            mListener = (OnFragmentInteractionListener) getActivity();
            mListener.onFinishSortDialog(sortButton1.getText().toString(), sortingParamPos);
            dismiss();
        });
        sortButton2.setOnClickListener(v -> {
            mListener = (OnFragmentInteractionListener) getActivity();
            mListener.onFinishSortDialog(sortButton2.getText().toString(), sortingParamPos);
            dismiss();
        });
        sortButton3.setOnClickListener(v -> {
            mListener = (OnFragmentInteractionListener) getActivity();
            mListener.onFinishSortDialog(sortButton3.getText().toString(), sortingParamPos);
            dismiss();
        });
        return mView;
    }

    public void dataBindFragmentValues(FragmentSortBinding binding) {
        sortButton1 = binding.SortButton1Id;
        sortButton2 = binding.SortButton2Id;
        sortButton3 = binding.SortButton3Id;
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
