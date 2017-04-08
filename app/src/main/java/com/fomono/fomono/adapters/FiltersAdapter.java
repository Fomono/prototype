package com.fomono.fomono.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.ItemCategoryBinding;
import com.fomono.fomono.interfaces.ICategory;

import java.util.List;

/**
 * Created by David on 4/8/2017.
 */

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ToggleButton tbCategory;
        private ItemCategoryBinding binding;

        public ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            this.tbCategory = binding.tbCategory;
            this.tbCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    //TODO: update user's selected models
                }
            });
        }
    }

    private List<ICategory> categories;
    private Context context;

    public FiltersAdapter(@NonNull Context context, @NonNull List<ICategory> categories) {
        this.context = context;
        this.categories = categories;
    }

    private Context getContext() {
        return this.context;
    }

    @Override
    public FiltersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        ItemCategoryBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FiltersAdapter.ViewHolder viewHolder, int position) {
        ICategory category = this.categories.get(position);
        viewHolder.tbCategory.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }
}
