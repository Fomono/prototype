package com.fomono.fomono.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fomono.fomono.R;
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
    public TextView eventUrl;

    public ViewHolderEventsItem(View itemView) {
        super(itemView);
        eventName = (TextView)itemView.findViewById(R.id.EventNameId);
        eventDistance = (TextView)itemView.findViewById(R.id.EventDistanceId);
        eventMediaImage = (com.makeramen.roundedimageview.RoundedImageView)itemView.findViewById(R.id.EventMediaImageId);
        eventPrice = (TextView)itemView.findViewById(R.id.EventPriceId);
        eventDesc = (TextView)itemView.findViewById(R.id.EventDescId);
        eventDateTime = (TextView)itemView.findViewById(R.id.EventDateTimeId);
        eventType = (TextView)itemView.findViewById(R.id.EventTypeId);
        eventUrl = (LinkifiedTextView)itemView.findViewById(R.id.EventUrlId);

    }
}
