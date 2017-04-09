package com.fomono.fomono.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fomono.fomono.R;
import com.fomono.fomono.activities.FomonoDetailActivity;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<Event> mEvents;
    private ArrayList<Business> mBusinesses;
    private ArrayList<Movie> mMovieResults;
    private static final String TAG = "Fomono Adapter";
    private int screenWidth, screenHeight;
    private boolean USE_BUSINESSES = false;
    private boolean USE_EVENTS = false;
    private boolean USE_MOVIES = true;

    public FomonoAdapter(Context context, ArrayList<Event> events, ArrayList<Business> businesses, ArrayList<Movie> movieResults, int width, int height) {
        mContext = context;
        mEvents = events;
        mBusinesses = businesses;
        mMovieResults = movieResults;
        screenWidth = width;
        screenHeight = height;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View ViewEvents = inflater.inflate(R.layout.event_list_item, parent, false);
        viewHolder = new ViewHolderEventsItem(ViewEvents);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolderEventsItem vhItem = (ViewHolderEventsItem) viewHolder;
        configureViewHolderEventsItem(vhItem, position);
    }

    @Override
    public int getItemCount() {
        if (USE_BUSINESSES) {
            return mBusinesses.size();
        } else if (USE_EVENTS) {
            return mEvents.size();
        } else if (USE_MOVIES) {
            return mMovieResults.size();
        } else {
            return mMovieResults.size();
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderEventsItem vhImage = (ViewHolderEventsItem) holder;
        Glide.clear(vhImage.eventMediaImage);
    }

    private void configureViewHolderEventsItem(ViewHolderEventsItem holder, int position) {
        if (USE_BUSINESSES) {
            populateWithBusinesses(holder, position);
        } else if (USE_MOVIES) {
            populateWithMovies(holder, position);
        } else {
            populateWithEvents(holder, position);

        }
    }

    private void populateWithEvents(ViewHolderEventsItem holder, int position) {
        Event event = mEvents.get(position);
        Log.d(TAG, "name is " + event.getName().getText());
        if (event != null) {
            if (event.getName() != null) {
                holder.eventName.setText(event.getName().getText());
                holder.eventName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mContext, FomonoDetailActivity.class);
                        i.putExtra("FOM_OBJ", event);
                        mContext.startActivity(i);

                    }
                });
            } else {
                holder.eventName.setVisibility(View.GONE);
            }

            if (event.getDescription() != null) {
                if (event.getDescription().getText() != null) {
                    holder.eventDesc.setText(event.getDescription().getText().toString());
                }
            } else {
                holder.eventDesc.setVisibility(View.GONE);
            }

            Log.d(TAG, "Screen width is " + screenWidth + " and screen height is " + screenHeight);
            int imageSet = 0;
            if (event.getLogo() != null) {
                if (event.getLogo().getOriginal() != null) {
                    if (!TextUtils.isEmpty(event.getLogo().getOriginal().getUrl())) {
                        Glide.with(mContext).load(event.getLogo().getOriginal().getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).override(screenWidth, screenHeight).into(holder.eventMediaImage);
                        // Picasso.with(mContext).load(event.getLogo().getOriginal().getUrl()).placeholder(R.drawable.ic_fomono).resize(screenWidth, 0).into(holder.eventMediaImage);
                        imageSet = 1;
                    }
                }
            }
            if (imageSet == 0) {
                holder.eventMediaImage.setVisibility(View.GONE);
            }

            if (!event.getIsFree()) {
                holder.eventPrice.setText(R.string.EventFeePaid);
            } else {
                holder.eventPrice.setText(R.string.EventFeeFree);
            }

            int DateViewSet = 0;
            if (event.getStart() != null) {
                if (!TextUtils.isEmpty(event.getStart().getLocal())) {
                    String localDateTime = event.getStart().getLocal();
                    localDateTime = localDateTime.replace('T', ' ');
                    SimpleDateFormat format = new SimpleDateFormat(mContext.getString(R.string.adapter_date_string_format));
                    try {
                        Date dateString = format.parse(localDateTime);
                        DateViewSet = 1;
                        holder.eventDateTime.setText(dateString.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (DateViewSet == 0) {
                holder.eventDateTime.setVisibility(View.GONE);
            }

            if (event.getUrl() != null) {
                holder.eventUrl.setText(event.getUrl());
            } else {
                holder.eventUrl.setVisibility(View.GONE);
            }

            holder.eventDistance.setVisibility(View.GONE);
            holder.eventType.setVisibility(View.GONE);
        }

    }

    private void populateWithBusinesses(ViewHolderEventsItem holder, int position) {
        Business business = mBusinesses.get(position);

        if (business != null) {
            if (business.getName() != null) {
                holder.eventName.setText(business.getName());
            } else {
                holder.eventName.setVisibility(View.GONE);
            }

            holder.eventDesc.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(business.getImageUrl())) {
                Glide.with(mContext).load(business.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).override(screenWidth, screenHeight / 2).into(holder.eventMediaImage);
                // Picasso.with(mContext).load(business.getImageUrl()).placeholder(R.drawable.ic_fomono).resize(screenWidth, 0).into(holder.eventMediaImage);
            } else {
                holder.eventMediaImage.setVisibility(View.GONE);
            }

            int DistSet = 0;
            //FIXME - is distance in miles here? or km?
            double distance = (business.getDistance() / 1000);
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            if (business.getDistance() != null)
                holder.eventDistance.setText("" + numberFormat.format(distance) + "m");
            if (business.getPrice() != null) holder.eventPrice.setText(business.getPrice());
            if (business.getCategories() != null) {
                if (business.getCategories().get(0).getAlias() != null) {
                    holder.eventType.setText(business.getCategories().get(0).getAlias());
                    DistSet = 1;
                }
            }
            if (DistSet == 0) {
                holder.eventPrice.setVisibility(View.GONE);
            }

            //FIXME - call this something else. We can reuse this field, or collapse it and show something else.
            holder.eventDateTime.setText("" + business.getRating() + "/5, " + business.getReviewCount() + " Reviews");

            if (business.getUrl() != null) {
                holder.eventUrl.setText(business.getUrl());
            } else {
                holder.eventUrl.setVisibility(View.GONE);
            }
        }

    }

    public void populateWithMovies(ViewHolderEventsItem holder, int position) {

    }
}
