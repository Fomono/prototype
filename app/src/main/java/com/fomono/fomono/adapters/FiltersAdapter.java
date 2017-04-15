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
import com.fomono.fomono.models.ICategory;
import com.fomono.fomono.models.db.Filter;
import com.fomono.fomono.utils.FilterUtil;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;
import java.util.Map;

/**
 * Created by David on 4/8/2017.
 */

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ToggleButton tbCategory;

        public ViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());

            this.tbCategory = binding.tbCategory;
        }
    }

    List<ICategory> categories;
    Context context;
    ParseUser user;
    Map<String, Filter> filters;

    public FiltersAdapter(@NonNull Context context, String apiName, @NonNull List<ICategory> categories, @NonNull ParseUser user, @NonNull Map<String, Filter> filters) {
        this.context = context;
        this.categories = categories;
        this.user = user;
        this.filters = filters;
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
        viewHolder.tbCategory.setTextOn(category.getName());
        viewHolder.tbCategory.setTextOff(category.getName());

        //set whether button is checked or not
        if (filters.containsKey(category.getId())) {
            viewHolder.tbCategory.setChecked(true);
        } else {
            viewHolder.tbCategory.setChecked(false);
        }

        //attach listener
        viewHolder.tbCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //update user's selected filters
                if (b) {
                    //store new user filter, but first check for dupe
                    try {
                        FilterUtil.getFilter(category.getParamName(), category.getId(), category.getApiName(), new GetCallback<Filter>() {
                            @Override
                            public void done(Filter object, ParseException e) {
                                if (object == null) {
                                    Filter f = new Filter(category.getParamName(), category.getId(), category.getApiName());
                                    filters.put(category.getId(), f);
                                    f.pinInBackground();    //save locally on device for easy access
                                    f.saveInBackground();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //delete existing user filter
                    filters.remove(category.getId());
                    try {
                        FilterUtil.getFilter(category.getParamName(), category.getId(), category.getApiName(), new GetCallback<Filter>() {
                            @Override
                            public void done(Filter object, ParseException e) {
                                if (object != null) {
                                    object.deleteEventually();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }
}
