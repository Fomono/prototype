package com.fomono.fomono.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.events.events.EventBriteResponse;
import com.fomono.fomono.network.client.EventBriteClientRetrofit;
import com.fomono.fomono.supportclasses.InternetAlertDialogue;

import java.util.ArrayList;
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
    public static final String USER_KEY = "IMWD66EDBK2PQIUKRK4K";
    private final static String TAG = "Event fragment";
    private EventBriteClientRetrofit eventBriteClientRetrofit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
     //   client = new EventBriteClient(getActivity());
        InternetAlertDialogue internetAlertDialogue = new InternetAlertDialogue(mContext);
        if(internetAlertDialogue.checkForInternet()) {
            populateEvents();
        }

        return view;
    }

    public void populateEvents() {
        smoothProgressBar.setVisibility(ProgressBar.VISIBLE);
        getEventList(0,null);
        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(() -> {//Just to show the progress bar
            smoothProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }, 500);
    }

    public void getEventList(int page, String strQuery) {

        eventBriteClientRetrofit = EventBriteClientRetrofit.getNewInstance();
        Map<String, String> data = new HashMap<>();
        data.put("token", USER_KEY);
        if(strQuery != null) {
            data.put("q", strQuery);
        }
        Call<EventBriteResponse> call = eventBriteClientRetrofit.EBRetrofitClientFactory().
                getEventsFromServer(data);

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