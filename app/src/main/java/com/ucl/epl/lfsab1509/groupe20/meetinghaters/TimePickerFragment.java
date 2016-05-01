package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by ludovic on 1/05/16.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    MeetingApp mInstance = MeetingApp.getAppInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        if (!mInstance.toggle){
            mInstance.tmpHourStart = hourOfDay;
            mInstance.tmpMinuteStart = minute;
        } else {
            mInstance.tmpHourEnd = hourOfDay;
            mInstance.tmpMinuteEnd = minute;
        }
        //mInstance.toggle = !mInstance.toggle;
    }
}