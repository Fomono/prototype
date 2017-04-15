package com.fomono.fomono.adapters;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fomono.fomono.R;
import com.fomono.fomono.databinding.EventListItemBinding;
import com.fomono.fomono.supportclasses.LinkifiedTextView;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by jsaluja on 4/7/2017.
 */

public class ViewHolderEventsItem extends RecyclerView.ViewHolder {

    public TextView eventName;
    public TextView eventDistance;
    public com.makeramen.roundedimageview.RoundedImageView eventMediaImage;
    public TextView eventPrice;
    public TextView eventDesc;
    public TextView eventDateTime;
    public TextView eventType;
    public ImageButton eventUrl;

    public ViewHolderEventsItem(EventListItemBinding binding) {
        super(binding.getRoot());
        eventName = binding.EventNameId;
        eventDistance = binding.EventDistanceId;
        eventMediaImage = binding.EventMediaImageId;
        eventPrice = binding.EventPriceId;
        eventDesc = binding.EventDescId;
        eventDateTime = binding.EventDateTimeId;
        eventType = binding.EventTypeId;
        eventUrl = binding.ImageLogoButtonId;
    }
}
