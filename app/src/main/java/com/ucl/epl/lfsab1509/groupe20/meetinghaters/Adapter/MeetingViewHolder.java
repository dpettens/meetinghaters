package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucl.epl.lfsab1509.groupe20.meetinghaters.MapViewActivity;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.MeetingApplication;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.R;

/**
 * Created by ludovic on 23/03/16.
 */
public class MeetingViewHolder extends RecyclerView.ViewHolder {
    protected RelativeLayout meeting;
    protected TextView idText;
    protected TextView nameText;
    protected TextView descriptionText;
    protected TextView timeText;

    protected MeetingApplication appInstance = MeetingApplication.getAppInstance();

    public MeetingViewHolder(View itemView) {
        super(itemView);

        idText = (TextView) itemView.findViewById(R.id.meeting_id);
        nameText = (TextView) itemView.findViewById(R.id.meeting_name);
        descriptionText = (TextView) itemView.findViewById(R.id.meeting_description);
        timeText = (TextView) itemView.findViewById(R.id.meeting_time);

        meeting = (RelativeLayout) itemView.findViewById(R.id.meeting);
        meeting.setOnClickListener(new View.OnClickListener() {
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
