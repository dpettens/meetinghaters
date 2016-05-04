package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;

import com.ucl.epl.lfsab1509.groupe20.meetinghaters.R;

/**
 * Created by ludovic on 23/03/16.
 */
public class MeetingViewHolder extends RecyclerView.ViewHolder {

    protected TextView nameText;
    protected TextView descText;
    protected TextView startText;
    protected TextView endText;
    protected TextView locationText;

    protected CardView card;

    public MeetingViewHolder(View itemView){
        super(itemView);
        nameText = (TextView) itemView.findViewById(R.id.meeting_name);
        descText = (TextView) itemView.findViewById(R.id.meeting_description);
        startText = (TextView) itemView.findViewById(R.id.meeting_start);
        endText = (TextView) itemView.findViewById(R.id.meeting_end);
        locationText = (TextView) itemView.findViewById(R.id.meeting_location);

        card = (CardView) itemView;
    }

}
