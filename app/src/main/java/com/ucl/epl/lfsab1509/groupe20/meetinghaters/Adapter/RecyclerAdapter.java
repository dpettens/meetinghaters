package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Adapter;

import android.support.annotation.UiThread;
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
    //private LayoutInflater inflater;


    public RecyclerAdapter(/*Context context,*/ List<MeetingItem> meetings) {
        //inflater = LayoutInflater.from(context);
        this.meetings = new ArrayList<>();
        this.meetings.addAll(meetings);
    }


    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                                      .inflate(R.layout.element_meeting_list, viewGroup, false);
        //View itemView = inflater.inflate(R.layout.cv_meeting_list_element, viewGroup, false);
        return new MeetingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder meetingViewHolder, int i) {
        MeetingItem meetingItem = meetings.get(i);
        meetingViewHolder.idText.setText(meetingItem.getId());
        meetingViewHolder.nameText.setText(meetingItem.getName());
        meetingViewHolder.descriptionText.setText(meetingItem.getDescription());
        meetingViewHolder.timeText.setText(meetingItem.getStart() + " - " + meetingItem.getEnd());
    }

    @Override
    public int getItemCount(){
        if (meetings == null) return 0;
        return meetings.size();
    }

    @UiThread
    public void swap(ArrayList<MeetingItem> meetings){
        this.meetings.clear();
        meetings.addAll(meetings);
        notifyDataSetChanged();
        //dataSetChanged();
        //runOnUiThread(new Runnable(){...})
    }

    /*@UiThread
    public void dataSetChanged(){
        notifyDataSetChanged();
    }*/
    public void addItem(MeetingItem item){
        meetings.add(item);
        notifyDataSetChanged();
    }

    public void clear(){
        meetings.clear();
        notifyDataSetChanged();
    }

}