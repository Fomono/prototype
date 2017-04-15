package com.fomono.fomono.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.adapters.FomonoAdapter;
import com.fomono.fomono.models.db.Filter;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.EventBriteResponse;
import com.fomono.fomono.network.client.EventBriteClientRetrofit;
import com.fomono.fomono.supportclasses.EndlessRecyclerViewScrollListener;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;
import com.fomono.fomono.utils.FilterUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private EventBriteClientRetrofit eventBriteClientRetrofit;
    int eventPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if(internetAlertDialogue.checkForInternet() && !initialEventsLoaded) {
            populateEvents(eventPage++);
            Log.d(TAG, "data loaded = "+initialEventsLoaded);
        }

        rvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(internetAlertDialogue.checkForInternet()) {
                    populateEvents(eventPage++);
                }
            }
        });

        return view;
    }

    public void populateEvents(int page) {
        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        try {
            //get user filters for events
            FilterUtil.getFilters(FomonoApplication.API_NAME_EVENTS, new FindCallback<Filter>() {
                @Override
                public void done(List<Filter> filters, ParseException e) {
                    String categoriesString = "";
                    if (filters != null) {
                        Filter.initializeFromList(filters);
                        categoriesString = FilterUtil.buildCategoriesString(filters);
                    }
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    String location = currentUser.getString("location");
                    int distance = currentUser.getInt("distance");
                    getEventList(page, null, location, categoriesString, distance);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }

    public void getEventList(int page, String strQuery, String location, String categories, int distance) {

        Log.d(TAG, "data loaded inside getEvent = "+initialEventsLoaded);
        eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();
        initialEventsLoaded = true;
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

        data.put("page", String.valueOf(page));
        Call<EventBriteResponse> call = eventBriteClientRetrofit.EBRetrofitClientFactory().
                getEventsFromServer(data);

            Log.d(TAG, "Events URL String is " + call.request().url());

        call.enqueue(new Callback<EventBriteResponse>() {
            @Override
            public void onResponse(Call<EventBriteResponse> call, Response<EventBriteResponse> response) {
                    ArrayList<Event> events = response.body().getEvents();
                    if (events == null || events.isEmpty()) {
                        Log.d(TAG, "No events fetched!!");
                    } else {
                        fomonoEvents.addAll(events);
                        fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                    }
            }

            @Override
            public void onFailure(Call<EventBriteResponse> call, Throwable t) {
                Log.d(TAG, "REQUEST Failed " + t.getMessage());

            }
        });
    }

/*
    public void getLocalEventBriteEventList(String query, String sortBy, String locationAddress, String locationRadius, String locationLat,
                                                   String locationLon, String categories, String subCategories, String price, String startDateRangeStart,
                                                   String startDateRangeEnd, String startDateKeyword, String dateModifiedRangeStart, String dateModifiedRangeEnd,
                                                   String dateModifiedDateKeyword) {
        String Url = "https://www.eventbriteapi.com/v3/events";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("token", USER_KEY);

        if (query != null) {
            params.put("q", query);
        }
        if (sortBy != null) {
            params.put("sort_by", sortBy);
        }
        if (locationAddress != null) {
            params.put("location.address", sortBy);
        }
        if (locationRadius != null) {
            params.put("location.within", locationRadius);
        }
        if (locationLat != null) {
            params.put("location.latitude", locationLat);
        }
        if (locationLon != null) {
            params.put("location.longitude", locationLon);
        }
        if (categories != null) {
            params.put("categories", categories);
        }
        if (subCategories != null) {
            params.put("subcategories", subCategories);
        }
        if (price != null) {
            params.put("price", price);
        }
        if (startDateRangeStart != null) {
            params.put("start_date.range_start", startDateRangeStart);
        }
        if (startDateRangeEnd != null) {
            params.put("start_date.range_end", startDateRangeEnd);
        }
        if (startDateKeyword != null) {
            params.put("start_date.keyword", startDateKeyword);
        }
        if (dateModifiedRangeStart != null) {
            params.put("date_modified.range_start", startDateRangeStart);
        }
        if (dateModifiedRangeEnd != null) {
            params.put("date_modified.range_end", startDateRangeEnd);
        }
        if (dateModifiedDateKeyword != null) {
            params.put("date_modified.keyword", startDateKeyword);
        }

        client.get(Url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                EventBriteResponse eventBriteResponse;
                Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                Log.d(TAG, "response is " + response);
                eventBriteResponse = gson.fromJson(response.toString(), EventBriteResponse.class);

                fomonoEvents.addAll(eventBriteResponse.getEvents());
                fomonoAdapter.notifyItemRangeInserted(fomonoAdapter.getItemCount(), fomonoEvents.size());
                eventsLoaded = 1;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "There is an error, status_code " + statusCode);
            }
        });
    }
*/
}