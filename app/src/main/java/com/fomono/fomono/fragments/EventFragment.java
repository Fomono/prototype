package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.models.db.Filter;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.EventBriteResponse;
import com.fomono.fomono.supportclasses.EndlessRecyclerViewScrollListener;
import com.fomono.fomono.utils.FilterUtil;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Saranu on 4/6/17.
 */

public class EventFragment extends MainListFragment {
  //  private EventBriteClient client;
    private final static String TAG = "Event fragment";
    private int eventPage = 1;
    private String sortParameter = null;
    private String searchParameter = null;
    private int buttonSelected = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(internetAlertDialogue.checkForInternet()) {
            populateEvents(eventPage++, sortParameter, searchParameter);
        }

        rvList.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) rvList.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (internetAlertDialogue.checkForInternet()) {
                    populateEvents(eventPage++, sortParameter, searchParameter);
                }
            }
        });

        return view;
    }
    public void searchEventList(String query) {
        clear();
        searchParameter = query;
        eventPage = 1;
        populateEvents(eventPage++, sortParameter, searchParameter);
    }

    public void refreshEventList(String sortParam) {
        sortParameter = sortParam;
        clear();
        eventPage = 1;
        populateEvents(eventPage++, sortParameter, searchParameter);
    }

    public void populateEvents(int page, String sortParam, String searchQuery) {

        if(internetAlertDialogue.checkForInternet()) {
            smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
            try {
                //get user filters for events
                FilterUtil.getInstance().getFilters(FomonoApplication.API_NAME_EVENTS, (filters, e) -> {
                    String categoriesString = "";
                    if (filters != null) {
                        Filter.initializeFromList(filters);
                        categoriesString = FilterUtil.getInstance().buildCategoriesString(filters);
                    }
                    //default to our own set of filter categories
                    if (TextUtils.isEmpty(categoriesString)) {
                        categoriesString = FilterUtil.getInstance().getDefaultFilterString(FomonoApplication.API_NAME_EVENTS, mContext);
                    }
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String location = currentUser.getString("location");
                    int distance = currentUser.getInt("distance");
                    getEventList(page, searchQuery, location, categoriesString, distance, sortParam);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getEventList(int page, String strQuery, String location, String categories,
                             int distance, String sortParam) {

        Map<String, String> data = new HashMap<>();
        data.put("token", getResources().getString(R.string.event_brite_user_key));
        if(strQuery != null) {
            data.put("q", strQuery);
        }
        if (!TextUtils.isEmpty(location)) {
            data.put("location.address", location);

            if (distance > 0) {
                data.put("location.within", distance + "mi");
            }
        }
        if (!TextUtils.isEmpty(categories)) {
            data.put("categories", categories);
        }

        if((sortParam != null) && !(sortParam.equals(""))) {
            data.put("sort_by", sortParam);
        }

        data.put("page", String.valueOf(page));

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(cal.getTime());
        data.put("start_date.range_start", ""+dateString+"T00:00:00");



        Call<EventBriteResponse> call = eventBriteClientRetrofit.EBRetrofitClientFactory().
                getEventsFromServer(data);

            Log.d(TAG, "Events URL String is " + call.request().url());

        call.enqueue(new Callback<EventBriteResponse>() {
            @Override
            public void onResponse(Call<EventBriteResponse> call, Response<EventBriteResponse> response) {
                if (response == null || response.body() == null) {
                    Log.d(TAG, "Events response body is empty!");
                    return;
                }
                ArrayList<Event> events = response.body().getEvents();
                if (events == null || events.isEmpty()) {
                    Log.d(TAG, "No events fetched!!");
                } else {
                    Event.saveOrUpdateFromList(events);
                    fomonoEvents.addAll(events);
                    fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                }
                smoothProgressBar.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onFailure(Call<EventBriteResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());
                smoothProgressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }

    @Override
    public void clearSearch() {
        if (searchParameter == null) {
            return;
        }
        clear();
        searchParameter = null;
        eventPage = 1;
        populateEvents(eventPage++, sortParameter, searchParameter);
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }
}