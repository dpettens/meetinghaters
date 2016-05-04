package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ucl.epl.lfsab1509.groupe20.meetinghaters.MapViewActivity;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.MeetingApplication;
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
    protected TextView idText;

    protected CardView card;

    protected MeetingApplication appInstance = MeetingApplication.getAppInstance();

    public MeetingViewHolder(View itemView){
        super(itemView);

        idText = (TextView) itemView.findViewById(R.id.id);
        nameText = (TextView) itemView.findViewById(R.id.meeting_name);
        descText = (TextView) itemView.findViewById(R.id.meeting_description);
        startText = (TextView) itemView.findViewById(R.id.meeting_start);
        endText = (TextView) itemView.findViewById(R.id.meeting_end);
        locationText = (TextView) itemView.findViewById(R.id.meeting_location);

        card = (CardView) itemView;
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO MODIFY TO LET THE DESCRIPTION
                appInstance.currentMeeting = idText.getText().toString();
                Intent i = new Intent(v.getContext(), MapViewActivity.class);
                v.getContext().startActivity(i);
            }
        });
    }

}
