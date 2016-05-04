package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;

import com.ucl.epl.lfsab1509.groupe20.meetinghaters.CreateMeetingActivity;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour, minute;

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            hour = bundle.getInt("HOUR");
            minute = bundle.getInt("MINUTE");
        } else {
            final Calendar cal = Calendar.getInstance();
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);
        }

        return new TimePickerDialog(getActivity(), (CreateMeetingActivity) getActivity(),
                hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}