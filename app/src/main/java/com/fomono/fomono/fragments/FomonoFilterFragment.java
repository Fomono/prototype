package com.fomono.fomono.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fomono.fomono.R;
import com.fomono.fomono.adapters.FiltersAdapter;
import com.fomono.fomono.databinding.FragmentCategoryFilterBinding;
import com.fomono.fomono.models.ICategory;
import com.fomono.fomono.models.db.Filter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoFilterFragment extends Fragment {

    public static final int CODE_NEXT = 1;
    public static final int CODE_CANCEL = 2;
    public static final int CODE_DONE = 3;

    FragmentCategoryFilterBinding binding;
    RecyclerView rvFilters;
    TextView tvTitle;
    Button btnNext;
    Button btnCancel;

    List<ICategory> categories;
    FiltersAdapter adptFilters;
    String title;
    String apiName;
    boolean lastPage;
    ParseUser user;

    public static FomonoFilterFragment newInstance(String title, String apiName, List<ICategory> categories, boolean lastPage, List<Filter> filters) {
        FomonoFilterFragment fragment = new FomonoFilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("apiName", apiName);
        args.putParcelableArrayList("categories", (ArrayList<ICategory>)categories);
        args.putBoolean("lastPage", lastPage);
        args.putParcelableArrayList("filters", (ArrayList<Filter>)filters);
        fragment.setArguments(args);
        return fragment;
    }

    public interface FilterFragmentListener {
        void onSubmit(int resultCode);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setup();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_filter, container, false);

        setupViews();

        return binding.getRoot();
    }

    private void setup() {
        title = getArguments().getString("title");
        categories = getArguments().getParcelableArrayList("categories");
        lastPage = getArguments().getBoolean("lastPage");
        apiName = getArguments().getString("apiName");
        List<Filter> filters = getArguments().getParcelableArrayList("filters");
        //build map for filters
        Map<String, Filter> filtersMap = new HashMap<>();
        for (Filter f : filters) {
            filtersMap.put(f.getValue(), f);
        }
        adptFilters = new FiltersAdapter(getActivity(), apiName, categories, user, filtersMap);
        user = ParseUser.getCurrentUser();
    }

    private void setupViews() {
        rvFilters = binding.rvFilters;
        tvTitle = binding.tvTitle;
        btnNext = binding.btnNext;
        btnCancel = binding.btnCancel;

        if (lastPage) {
            btnNext.setText(getString(R.string.btn_done));
        }

        // Set layout manager to position the items
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        rvFilters.setLayoutManager(layoutManager);
        rvFilters.setAdapter(adptFilters);

        tvTitle.setText(title);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterFragmentListener listener = (FilterFragmentListener) getActivity();
                if (lastPage) {
                    listener.onSubmit(CODE_DONE);
                } else {
                    listener.onSubmit(CODE_NEXT);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterFragmentListener listener = (FilterFragmentListener) getActivity();
                listener.onSubmit(CODE_CANCEL);
            }
        });
    }
}
