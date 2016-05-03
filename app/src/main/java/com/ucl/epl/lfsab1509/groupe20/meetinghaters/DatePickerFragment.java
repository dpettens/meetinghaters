package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by ludovic on 1/05/16.
 */



public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    MeetingApp mInstance = MeetingApp.getAppInstance();
    DatePicker mDatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        mDatePicker = dialog.getDatePicker();
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        mInstance.tmpYear = year;
        mInstance.tmpMonth = month;
        mInstance.tmpDay = day;
    }

    public DatePicker getDatePicker(){
        return mDatePicker;
    }

}