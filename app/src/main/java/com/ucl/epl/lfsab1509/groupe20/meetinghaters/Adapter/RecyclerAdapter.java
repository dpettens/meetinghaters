package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ucl.epl.lfsab1509.groupe20.meetinghaters.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ludovic on 23/03/16.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<MeetingViewHolder> {

    private List<MeetingItem> meetings;

    public RecyclerAdapter(List<MeetingItem> meetings) {
        this.meetings = new ArrayList<>();
        this.meetings.addAll(meetings);
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.cv_meeting_list_element, viewGroup, false);
        return new MeetingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder meetingViewHolder, int i){
        MeetingItem meetingItem = meetings.get(i);
        meetingViewHolder.nameText.setText(meetingItem.getName());
        meetingViewHolder.descText.setText(meetingItem.getDescription());
        meetingViewHolder.startText.setText(meetingItem.getStart());
        meetingViewHolder.endText.setText(meetingItem.getEnd());
        meetingViewHolder.locationText.setText(meetingItem.getLocation());
        //meetingViewHolder.card.setCardBackgroundColor(/*int color*/);
    }

    @Override
    public int getItemCount(){
        return meetings.size();
    }
}
