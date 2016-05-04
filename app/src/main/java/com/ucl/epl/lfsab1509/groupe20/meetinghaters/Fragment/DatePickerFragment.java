package com.ucl.epl.lfsab1509.groupe20.meetinghaters.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.ucl.epl.lfsab1509.groupe20.meetinghaters.CreateMeetingActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year, month, day;

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            year = bundle.getInt("YEAR");
            month = bundle.getInt("MONTH");
            day = bundle.getInt("DAY");
        } else {
            final Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
        }

        return new DatePickerDialog(getActivity(), (CreateMeetingActivity) getActivity(), year, month, day);
    }
}