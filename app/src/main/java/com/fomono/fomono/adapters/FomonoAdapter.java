package com.fomono.fomono.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.fomono.fomono.FomonoApplication;
import com.fomono.fomono.R;
import com.fomono.fomono.activities.FomonoActivity;
import com.fomono.fomono.activities.FomonoDetailActivity;
import com.fomono.fomono.activities.FomonoTrailerActivity;
import com.fomono.fomono.databinding.EventListItemBinding;
import com.fomono.fomono.models.FomonoEvent;
import com.fomono.fomono.models.eats.Business;
import com.fomono.fomono.models.events.events.Event;
import com.fomono.fomono.models.movies.Movie;
import com.fomono.fomono.utils.ConfigUtil;
import com.fomono.fomono.utils.FavoritesUtil;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Saranu on 4/6/17.
 */

public class FomonoAdapter extends RecyclerView.Adapter<FomonoAdapter.ViewHolderEventsItem> {
    private Context mContext;
    private ArrayList<FomonoEvent> mFomonoEvents;
    private static final String TAG = "Fomono Adapter";
    private int screenWidth;
    private FavoritesUtil favsUtil;
    private String fragmentName;

    public interface FomonoAdapterObjectListener {
        void onOpenLink(Intent i);
    }

    public interface FomonoEventUpdateListener {
        void onFomonoEventUpdated(FomonoEvent fEvent, String fragmentName);
    }

    FomonoAdapterObjectListener fomonoAdapterObjectListener;

    public FomonoAdapter(Context context, ArrayList<FomonoEvent> fomonoEvents, String fragmentName) {
        mContext = context;
        mFomonoEvents = fomonoEvents;
        this.fomonoAdapterObjectListener = null;
        this.fragmentName = fragmentName;
        this.favsUtil = FavoritesUtil.getInstance();
    }

    public void setCustomObjectListener(FomonoAdapterObjectListener listener) {
        this.fomonoAdapterObjectListener = listener;
    }
    @Override
    public ViewHolderEventsItem onCreateViewHolder(ViewGroup parent, int viewType) {

        ViewHolderEventsItem viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        EventListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.event_list_item, parent, false);
        viewHolder = new ViewHolderEventsItem(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderEventsItem viewHolder, int position) {
        ViewHolderEventsItem vhItem = viewHolder;
        configureViewHolderEventsItem(vhItem, position);
    }

    @Override
    public int getItemCount() {
        return mFomonoEvents.size();
    }

    @Override
    public void onViewRecycled(ViewHolderEventsItem holder) {
        super.onViewRecycled(holder);
        ViewHolderEventsItem vhImage = holder;
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

            int imageSet = 0;
            if (event.getLogo() != null) {
                if (event.getLogo().getOriginal() != null) {
                    if (!TextUtils.isEmpty(event.getLogo().getOriginal().getUrl())) {
//                        Picasso.with(mContext).load(event.getLogo().getOriginal().getUrl()).placeholder(R.drawable.ic_fomono_big).resize(screenWidth, 0).into(holder.eventMediaImage);
                        Glide.with(mContext).load(event.getLogo().getOriginal().getUrl())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .fitCenter()
                                .override(screenWidth, Target.SIZE_ORIGINAL)
                                .into(holder.eventMediaImage);
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
            holder.eventRatingBar.setVisibility(View.GONE);
            int DateViewSet = 0;
            if (event.getStart() != null) {
                if (!TextUtils.isEmpty(event.getStart().getLocal())) {
                    String localDateTime = event.getStart().getLocal();
                    localDateTime = localDateTime.replace('T', ' ');
                    String[] dateTimeString = localDateTime.trim().split("\\s+");
                    String[] dateParams = dateTimeString[0].trim().split("-");
                    String[] timeParams = dateTimeString[1].trim().split(":");

                    String TimeFollower = "AM";
                    if(Integer.parseInt(timeParams[0]) > 12) {TimeFollower = "PM";}
                    String monthName = new DateFormatSymbols().getMonths()[Integer.parseInt((dateParams[1])) - 1];
                    holder.eventDateTime.setText(""+monthName+ " " +dateParams[2]+"th, " +timeParams[0]+":"+timeParams[1]+TimeFollower);
                  //  holder.eventDateTime.setText(""+timeParams[0]+":"+timeParams[1]+TimeFollower+", "+dateParams[2]+"th "+monthName+" "+dateParams[0]);
                    DateViewSet = 1;
                }
            }
            if (DateViewSet == 0) {
                holder.eventDateTime.setVisibility(View.GONE);
            }

            if (event.getUrl() != null) {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_link);
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
                    holder.eventType.setText("#"+category);
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

            if (!TextUtils.isEmpty(business.getImageUrl())) {
                // Glide.with(mContext).load(business.getImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).override(screenWidth, screenHeight / 2).into(holder.eventMediaImage);
                Picasso.with(mContext).load(business.getImageUrl()).placeholder(R.drawable.ic_fomono_big).resize(screenWidth, 0).into(holder.eventMediaImage);
            } else {
                holder.eventMediaImage.setVisibility(View.GONE);
            }

            if (business.getDistance() != null) {
                //have to convert meters to miles
                distance = (business.getDistance() * 0.000621);
                DecimalFormat numberFormat = new DecimalFormat("#.0");
                holder.eventDistance.setText("" + numberFormat.format(distance) + " mi");
            }

            if (business.getPrice() != null) holder.eventPrice.setText(business.getPrice());
            else holder.eventPrice.setVisibility(View.GONE);

            holder.eventType.setVisibility(View.GONE);
            setBusinessCategory(business, holder);


            holder.eventRatingBar.setRating(Double.valueOf(business.getRating()).floatValue());

            if(business.getReviewCount() != null) holder.eventDateTime.setText(""+business.getReviewCount() + " Reviews");
            else holder.eventDateTime.setVisibility(View.GONE);

            if (business.getUrl() != null) {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_link);
                holder.eventUrl.setOnClickListener(v -> {
                    Uri uri = Uri.parse(business.getUrl());
                    Intent openLink = new Intent(Intent.ACTION_VIEW, uri);
                    fomonoAdapterObjectListener.onOpenLink(openLink);
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
                        holder.eventType.setText("#"+category);
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

            if(movie.getOrigBackdropPath() != null) {
                Picasso.with(mContext).load(movie.getBackdropPath()).placeholder(R.drawable.ic_fomono_big).resize(screenWidth, 0).into(holder.eventMediaImage);
            } else if(movie.getOrigPosterPath() != null) {
                Picasso.with(mContext).load(movie.getPosterPath()).placeholder(R.drawable.ic_fomono_big).resize(screenWidth, 0).into(holder.eventMediaImage);
            } else {
                holder.eventMediaImage.setVisibility(View.GONE);
            }

            double ratingString = movie.getVoteAverage()/2;
            holder.eventRatingBar.setRating((float)(ratingString));

            holder.eventDistance.setVisibility(View.GONE);
            holder.eventPrice.setVisibility(View.GONE);

            holder.eventDateTime.setVisibility(View.GONE);
          //  if(movie.getReleaseDate() != null) {holder.eventDateTime.setText(mContext.getString(R.string.movie_release_date_string)+movie.getReleaseDate());}
            holder.eventType.setVisibility(View.GONE);
            setMovieCategories(movie, holder);

            if(movie.getId() != -1) {
                holder.eventUrl.setBackgroundResource(R.drawable.ic_link);
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

    private void setMovieCategories(Movie movie, ViewHolderEventsItem holder) {
        List<Integer> genreIds = movie.getGenreIds();
        StringBuilder sb = new StringBuilder();
        if (genreIds != null) {
            Properties genres = ConfigUtil.getCategoriesMap(FomonoApplication.API_NAME_MOVIE_GENRE, mContext);
            for (int i = 0; i < genreIds.size(); i++) {
                int genreId = genreIds.get(i);
                String genre = (String) genres.get(String.valueOf(genreId));
                if (!TextUtils.isEmpty(genre)) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append("#");
                    sb.append(genre);
                }
            }
            holder.eventType.setText(sb.toString());
            holder.eventType.setVisibility(View.VISIBLE);
        }
    }


    public void setEventFavorited(ViewHolderEventsItem holder, FomonoEvent fEvent) {
        favsUtil.isFavorited(fEvent, isFavorited -> {
            if (isFavorited) {
                holder.eventFavorited.setImageResource(R.drawable.ic_favorite);
            } else {
                holder.eventFavorited.setImageResource(R.drawable.ic_favorite_grey);
            }
        });

        holder.eventFavorited.setOnClickListener(view -> {
            favsUtil.isFavorited(fEvent, isFavorited -> {
                if (isFavorited) {
                    holder.eventFavorited.setImageResource(R.drawable.ic_favorite_grey);
                    favsUtil.removeFromFavorites(fEvent);
                } else {
                    holder.eventFavorited.setImageResource(R.drawable.ic_favorite);
                    favsUtil.addToFavorites(fEvent);
                }
            });

            if (mContext instanceof FomonoEventUpdateListener) {
                ((FomonoEventUpdateListener) mContext).onFomonoEventUpdated(fEvent, fragmentName);
            }
        });
    }

    public final class ViewHolderEventsItem extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView eventDistance;
        ImageView eventMediaImage;
        TextView eventPrice;
        TextView eventDateTime;
        TextView eventType;
        ImageButton eventUrl;
        ImageButton eventFavorited;
        MaterialRatingBar eventRatingBar;

        public ViewHolderEventsItem(EventListItemBinding binding) {
            super(binding.getRoot());
            eventName = binding.EventNameId;
            eventDistance = binding.EventDistanceId;
            eventMediaImage = binding.EventMediaImageId;
            eventPrice = binding.EventPriceId;
            eventDateTime = binding.EventDateTimeId;
            eventType = binding.EventTypeId;
            eventUrl = binding.ImageLogoButtonId;
            eventFavorited = binding.ImageFavoriteButtonId;
            eventRatingBar = binding.EventRatingBarId;

            binding.EventlistCardWrapperId.setOnClickListener(view -> {
                Intent showDetails = new Intent(mContext, FomonoDetailActivity.class);
                int position = getAdapterPosition();
                showDetails.putExtra("position", position);
                showDetails.putExtra("FOM_OBJ", mFomonoEvents.get(position));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, eventMediaImage, "main_image");
                ((AppCompatActivity) mContext).startActivityForResult(showDetails, FomonoActivity.REQUEST_CODE_DETAILS, options.toBundle());
            });
        }
    }
}
