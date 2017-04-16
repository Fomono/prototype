package com.fomono.fomono.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.activities.FomonoTrailerActivity;
import com.fomono.fomono.databinding.EventListItemBinding;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.utils.ConfigUtil;
import com.fomono.fomono.utils.FavoritesUtil;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<FomonoEvent> mFomonoEvents;
    private static final String TAG = "Fomono Adapter";
    private int screenWidth;
    private FavoritesUtil favsUtil;

    public interface FomonoAdapterObjectListener {
        void onOpenLink(Intent i);
    }

    FomonoAdapterObjectListener fomonoAdapterObjectListener;

    public FomonoAdapter(Context context, ArrayList<FomonoEvent> fomonoEvents) {
        mContext = context;
        mFomonoEvents = fomonoEvents;
        this.fomonoAdapterObjectListener = null;
        this.favsUtil = FavoritesUtil.getInstance();
    }

    public void setCustomObjectListener(FomonoAdapterObjectListener listener) {
        this.fomonoAdapterObjectListener = listener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        EventListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.event_list_item, parent, false);
        viewHolder = new ViewHolderEventsItem(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolderEventsItem vhItem = (ViewHolderEventsItem) viewHolder;
        configureViewHolderEventsItem(vhItem, position);
    }

    @Override
    public int getItemCount() {
        return mFomonoEvents.size();
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ViewHolderEventsItem vhImage = (ViewHolderEventsItem) holder;
        Glide.clear(vhImage.eventMediaImage);
    }

    private void configureViewHolderEventsItem(ViewHolderEventsItem holder, int position) {

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int pxWidth = displayMetrics.widthPixels;
        screenWidth = (int)(pxWidth / displayMetrics.density);

        if (mFomonoEvents.get(position) instanceof Business) {
            populateWithBusinesses(holder, position);
        } else if (mFomonoEvents.get(position) instanceof Movie) {
            populateWithMovies(holder, position);
        } else {
            populateWithEvents(holder, position);
        }
    }

    private void populateWithEvents(ViewHolderEventsItem holder, int position) {
        Event event = (Event)mFomonoEvents.get(position);
        if (event != null) {
            if (event.getName() != null) {
                holder.eventName.setText(event.getName().getText());
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

            int imageSet = 0;
            if (event.getLogo() != null) {
                if (event.getLogo().getOriginal() != null) {
                    if (!TextUtils.isEmpty(event.getLogo().getOriginal().getUrl())) {
                     //   Glide.with(mContext).load(event.getLogo().getOriginal().getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).override(200, 350).into(holder.eventMediaImage);
                     //   Glide.with(mContext).load(event.getLogo().getOriginal().getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).override(screenWidth, screenHeight).into(holder.eventMediaImage);
                         Picasso.with(mContext).load(event.getLogo().getOriginal().getUrl()).placeholder(R.drawable.ic_fomono_big).resize(screenWidth, 0).into(holder.eventMediaImage);
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
                    String[] dateString = localDateTime.trim().split("\\s+");

                    //FIXME - This works. But now, format it to ex of : 8:30PM, 18th April 2017
                    holder.eventDateTime.setText(dateString[0]);
                    DateViewSet = 1;
                }
            }
            if (DateViewSet == 0) {
                holder.eventDateTime.setVisibility(View.GONE);
            }

            if (event.getUrl() != null) {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_fomono_red);
                holder.eventUrl.setOnClickListener(v -> {
                    Uri uri = Uri.parse(event.getUrl());
                    Intent openLink = new Intent(Intent.ACTION_VIEW, uri);
                    fomonoAdapterObjectListener.onOpenLink(openLink);
           //         mContext.startActivity(openLink);
                });
            } else {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_fomono_grey);
             //   holder.eventUrl.setVisibility(View.GONE);
            }

            holder.eventDistance.setVisibility(View.GONE);
            holder.eventType.setVisibility(View.GONE);

            String catId = event.getCategoryId();
            if (catId != null) {
                String category = ConfigUtil.getCategoryName(catId, FomonoApplication.API_NAME_EVENTS, mContext);
                if (!TextUtils.isEmpty(category)) {
                    holder.eventType.setText(category);
                    holder.eventType.setVisibility(View.VISIBLE);
                }
            }

            setEventFavorited(holder, event);
        }

    }

    private void populateWithBusinesses(ViewHolderEventsItem holder, int position) {
        Business business = (Business) mFomonoEvents.get(position);
        double distance = 0;

        if (business != null) {
            if (business.getName() != null) {
                holder.eventName.setText(business.getName());
            } else {
                holder.eventName.setVisibility(View.GONE);
            }

            holder.eventDesc.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(business.getImageUrl())) {
                // Glide.with(mContext).load(business.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).override(screenWidth, screenHeight / 2).into(holder.eventMediaImage);
                Picasso.with(mContext).load(business.getImageUrl()).placeholder(R.drawable.ic_fomono_big).resize(screenWidth, 0).into(holder.eventMediaImage);
            } else {
                holder.eventMediaImage.setVisibility(View.GONE);
            }

            if (business.getDistance() != null) {
                //have to convert meters to miles
                distance = (business.getDistance() * 0.000621);
                DecimalFormat numberFormat = new DecimalFormat("#.000");
                holder.eventDistance.setText("" + numberFormat.format(distance) + " mi");
            }

            if (business.getPrice() != null) holder.eventPrice.setText(business.getPrice());
            else holder.eventPrice.setVisibility(View.GONE);

            holder.eventType.setVisibility(View.GONE);
            setBusinessCategory(business, holder);

            //FIXME - call this something else. We can reuse this field, or collapse it and show something else.
            holder.eventDateTime.setText("" + business.getRating() + "/5, " + business.getReviewCount() + " Reviews");

            if (business.getUrl() != null) {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_fomono_red);
                holder.eventUrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(business.getUrl());
                        Intent openLink = new Intent(Intent.ACTION_VIEW, uri);
                        fomonoAdapterObjectListener.onOpenLink(openLink);
                    }
                });
            } else {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_fomono_grey);
            }
        }

        setEventFavorited(holder, business);
    }

    private void setBusinessCategory(Business business, ViewHolderEventsItem holder) {
        if (business.getCategories() != null) {
            for (int i = 0; i < business.getCategories().size(); i++) {
                String alias = business.getCategories().get(i).getAlias();
                if (alias != null) {
                    String category = ConfigUtil.getCategoryName(alias, FomonoApplication.API_NAME_EATS, mContext);
                    if (!TextUtils.isEmpty(category)) {
                        holder.eventType.setText(category);
                        holder.eventType.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        }
    }

    private void populateWithMovies(ViewHolderEventsItem holder, int position) {
        Movie movie = (Movie)mFomonoEvents.get(position);
        if(movie != null) {
            if(movie.getOriginalTitle() != null) {
                holder.eventName.setText(movie.getOriginalTitle());
            } else {holder.eventName.setVisibility(View.GONE);}

            if(movie.getOverview() != null) { holder.eventDesc.setText(movie.getOverview());}
            else {holder.eventDesc.setVisibility(View.GONE);}

            if(movie.getPosterPath() != null) {
              //  Glide.with(mContext).load(movie.getBackdropPath()).diskCacheStrategy(DiskCacheStrategy.ALL).override(screenWidth, screenHeight).into(holder.eventMediaImage);
                Picasso.with(mContext).load(movie.getBackdropPath()).placeholder(R.drawable.ic_fomono_big).resize(screenWidth, 0).into(holder.eventMediaImage);
            } else {
                holder.eventMediaImage.setVisibility(View.GONE);
            }
            //FIXME - Using distance for rating
            double ratingString = movie.getVoteAverage()/2;
            DecimalFormat numberFormat = new DecimalFormat("#.0");
            holder.eventDistance.setText("" + numberFormat.format(ratingString) + "/5");
            holder.eventPrice.setVisibility(View.GONE);

            if(movie.getReleaseDate() != null) {holder.eventDateTime.setText(mContext.getString(R.string.movie_release_date_string)+movie.getReleaseDate());}
            holder.eventType.setVisibility(View.GONE);

            if(movie.getId() != -1) {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_fomono_red);
                holder.eventUrl.setOnClickListener(v -> {
                    Intent movTrail = new Intent(mContext, FomonoTrailerActivity.class);
                    movTrail.putExtra(mContext.getResources().getString(R.string.MovieId), movie.getId());
                    fomonoAdapterObjectListener.onOpenLink(movTrail);
                });
            } else {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_fomono_grey);
            }
        }

        setEventFavorited(holder, movie);
    }


    public void setEventFavorited(ViewHolderEventsItem holder, FomonoEvent fEvent) {
        if (favsUtil.isFavorited(fEvent)) {
            holder.eventFavorited.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.eventFavorited.setImageResource(R.drawable.ic_favorite_grey);
        }
        holder.eventFavorited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favsUtil.isFavorited(fEvent)) {
                    holder.eventFavorited.setImageResource(R.drawable.ic_favorite_grey);
                    favsUtil.removeFromFavorites(fEvent);
                } else {
                    holder.eventFavorited.setImageResource(R.drawable.ic_favorite);
                    favsUtil.addToFavorites(fEvent);
                }
            }
        });
    }
}
