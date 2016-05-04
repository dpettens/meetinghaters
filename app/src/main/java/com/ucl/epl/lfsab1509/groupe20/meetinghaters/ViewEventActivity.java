package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Contact.Contact;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Contact.ContactsPickerActivity;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonRequestHelper;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Fragment.DateObject;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Fragment.DatePickerFragment;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Fragment.TimePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewEventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "MeetingListActivity";
    private static final int CONTACT_PICKER_REQUEST = 100;
    private static final int PLACE_PICKER_REQUEST = 200;

    //FIXME private boolean eventAdded;

    private int year = -1;
    private int month = -1;
    private int day = -1;

    private int hourStart = -1;
    private int minuteStart = -1;
    private int hourEnd = -1;
    private int minuteEnd = -1;

    private static final int TIMEDEFAULT = 0;
    private static final int TIMESTART = 1;
    private static final int TIMEEND = 2;
    private int type = TIMEDEFAULT;

    private EditText meetingNameEditText;
    private EditText meetingDescriptionEditText;

    private TextView meetingDateSubtitleText;
    private TextView meetingTimeStartSubtitleText;
    private TextView meetingTimeEndSubtitleText;
    private TextView meetingPeopleSubtitleText;
    private TextView meetingLocationSubtitleText;

    private String name;
    private String description;
    private Place place;
    private ArrayList<String> mailMember;

    private MeetingApplication appInstance = MeetingApplication.getAppInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //FIXME eventAdded = false;

        meetingNameEditText = (EditText) findViewById(R.id.meeting_name);
        meetingDescriptionEditText = (EditText) findViewById(R.id.meeting_description);

        RelativeLayout meetingDateRelativeLayout = (RelativeLayout) findViewById(R.id.meeting_date);
        RelativeLayout meetingTimeStartRelativeLayout = (RelativeLayout) findViewById(R.id.meeting_time_start);
        RelativeLayout meetingTimeEndRelativeLayout = (RelativeLayout) findViewById(R.id.meeting_time_end);
        RelativeLayout meetingPeopleRelativeLayout = (RelativeLayout) findViewById(R.id.meeting_people);
        RelativeLayout meetingLocationRelativeLayout = (RelativeLayout) findViewById(R.id.meeting_location);

        meetingDateSubtitleText = (TextView) findViewById(R.id.meeting_date_subtitle);
        meetingTimeStartSubtitleText = (TextView) findViewById(R.id.meeting_time_start_subtitle);
        meetingTimeEndSubtitleText = (TextView) findViewById(R.id.meeting_time_end_subtitle);
        meetingPeopleSubtitleText = (TextView) findViewById(R.id.meeting_people_subtitle);
        meetingLocationSubtitleText = (TextView) findViewById(R.id.meeting_location_subtitle);

        // Listener
        /*meetingDateRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });

        meetingTimeStartRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = TIMESTART;
                timePickerDialog();
            }
        });

        meetingTimeEndRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = TIMEEND;
                timePickerDialog();
            }
        });

        meetingPeopleRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactPicker();
            }
        });

        meetingLocationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationPicker();
            }
        });*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                ArrayList<Contact> contacts = data.getParcelableArrayListExtra("SelectedContacts");
                mailMember = new ArrayList<String>();

                for (Contact c : contacts) {
                    mailMember.add(c.getMail());
                }

                if (mailMember.size() != 0)
                    meetingPeopleSubtitleText.setText(getString(R.string.meeting_people_changed_subtitle, mailMember.size()));
            }
        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                meetingLocationSubtitleText.setText(R.string.meeting_location_changed_subtitle);
            }
        }
    }

    private void datePickerDialog() {
        DatePickerFragment picker = new DatePickerFragment();

        if(year != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt("YEAR", year);
            bundle.putInt("MONTH", month);
            bundle.putInt("DAY", day);
            picker.setArguments(bundle);
        }

        picker.show(getSupportFragmentManager(), "datePicker");
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        DateObject pickedDate = new DateObject(0, 0, day, month, year);
        if (pickedDate.isPrevious(new DateObject(Calendar.getInstance()))) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.date_too_low), Toast.LENGTH_LONG).show();
        } else {
            meetingDateSubtitleText.setText(getString(R.string.meeting_date, day, month, year));
        }
    }

    private void timePickerDialog() {
        DialogFragment picker = new TimePickerFragment();

        if(type == TIMESTART && hourStart != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt("HOUR", hourStart);
            bundle.putInt("MINUTE", minuteStart);
            picker.setArguments(bundle);
        } else if(type == TIMEEND && hourEnd != -1) {
            Bundle bundle = new Bundle();
            bundle.putInt("HOUR", hourEnd);
            bundle.putInt("MINUTE", minuteEnd);
            picker.setArguments(bundle);
        }

        picker.show(getSupportFragmentManager(), "timePicker");
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (type == TIMESTART) {
            this.hourStart = hourOfDay;
            this.minuteStart = minute;
            meetingTimeStartSubtitleText.setText(getString(R.string.meeting_time, hourStart, minuteStart));
        } else if(type == TIMEEND) {
            this.hourEnd = hourOfDay;
            this.minuteEnd = minute;
            meetingTimeEndSubtitleText.setText(getString(R.string.meeting_time, hourEnd, minuteEnd));
        }

        type = TIMEDEFAULT;
    }

    private void contactPicker() {
        Intent contactPickerIntent = new Intent(getApplicationContext(), ContactsPickerActivity.class);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_REQUEST);
    }

    private void locationPicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                registerMeeting();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void registerMeeting() {
        if (!validator()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.validate_error_message), Toast.LENGTH_LONG).show();
            return;
        }

        /* FIXME if (!eventAdded) {
            addCalendarEvent();
            eventAdded = true;
        }*/

        // add the owner to the mailMember for send mail and add in m2m table db
        mailMember.add(appInstance.myDBHandler.getUser());

        ///////////////////////////////////////////////////////////////////////////////////
        //
        //   VOLLEY START
        //
        ///////////////////////////////////////////////////////////////////////////////////
        JSONObject meetingjson = new JSONObject();
        try {
            meetingjson.put("name", name);
            meetingjson.put("id_owner", appInstance.myDBHandler.getUser());
            meetingjson.put("description", description);
            meetingjson.put("location", place.getLatLng().toString());
            meetingjson.put("time_pre", formatDateTime(year, month, day, hourStart, minuteStart, -15));
            meetingjson.put("time_post", formatDateTime(year, month ,day, hourStart, minuteStart, 15));
            meetingjson.put("time_start", formatDateTime(year, month ,day, hourStart, minuteStart, 0));
            if (hourEnd != -1)
                meetingjson.put("time_end", formatDateTime(year, month ,day, hourStart, minuteStart, 15));
            else
                meetingjson.put("time_end", formatDateTime(year, month ,day, hourEnd, minuteEnd, 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonRequestHelper request = new JsonRequestHelper(
                Request.Method.POST,
                appInstance.remoteDBHandler.apiMeetingURL(),
                meetingjson,
                appInstance.myDBHandler.getToken(),
                appInstance.myDBHandler.getUser(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int id = -1;
                        try {
                            id = response.getInt("id");
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        for (int i = 0; i < mailMember.size(); i++) {
                            JSONObject m2mjson = new JSONObject();
                            try {
                                m2mjson.put("id_meeting", id);
                                m2mjson.put("id_user", mailMember.get(i));
                                m2mjson.put("id_owner", appInstance.myDBHandler.getUser());
                            } catch (JSONException e){
                                e.printStackTrace();
                            }

                            JsonRequestHelper innerRequest = new JsonRequestHelper(
                                    Request.Method.POST,
                                    appInstance.remoteDBHandler.apiMeetingURL(appInstance.myDBHandler.getUser(), Integer.toString(id), null),
                                    m2mjson,
                                    appInstance.myDBHandler.getToken(),
                                    appInstance.myDBHandler.getUser(),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            // do nothing
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            if (error instanceof TimeoutError || error instanceof NoConnectionError){
                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                                            } else {
                                                switch (error.networkResponse.statusCode) {
                                                    case 500:
                                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                                        break;
                                                }
                                            }
                                        }
                                    }
                            );

                            innerRequest.setPriority(Request.Priority.HIGH);
                            appInstance.remoteDBHandler.add(innerRequest);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                        } else {
                            switch (error.networkResponse.statusCode) {
                                case 500:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                }
        );

        request.setPriority(Request.Priority.HIGH);
        appInstance.remoteDBHandler.add(request);
        ///////////////////////////////////////////////////////////////////////////////////
        //
        //   VOLLEY END
        //
        ///////////////////////////////////////////////////////////////////////////////////

        Intent email = new Intent(Intent.ACTION_SEND);
        // need this to prompts email client only
        email.setData(Uri.parse("mailto:"));
        email.setType("message/rfc822");
        email.setType("text/plain");
        email.putExtra(Intent.EXTRA_EMAIL, mailMember.toArray(new String[mailMember.size()]));
        email.putExtra(Intent.EXTRA_SUBJECT, "MeetingHaters : You have a new meeting");
        email.putExtra(Intent.EXTRA_TEXT, "Ceci est un test de notre application de merde");

        try {
            startActivity(Intent.createChooser(email, "Choose an Email client"));
            finish();
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ViewEventActivity.this, getResources().getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
        }
    }

    /* FIXME
    private void addCalendarEvent() {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hourStart, minuteStart);

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("title", name);
        intent.putExtra("description", description);
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        if (hourEnd != -1 && minuteEnd != -1) {
            cal.set(year, month, day, hourEnd, minuteEnd);
            intent.putExtra("endTime", cal.getTimeInMillis());
        } else {
            intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        }

        intent.putExtra("eventLocation", place.getAddress());
        startActivity(intent);
    }*/

    private String formatDateTime(int year, int month, int day, int hour, int minute, int modifierMinute){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        long millis = cal.getTimeInMillis();
        millis = millis + modifierMinute*60*1000;
        cal.setTimeInMillis(millis);
        String YYYY = Integer.toString(cal.get(Calendar.YEAR));
        String MM = Integer.toString(cal.get(Calendar.MONTH));
        if (MM.length() == 1) MM="0"+MM;
        String DD = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        if (DD.length()==1) DD="0"+DD;
        String hh = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        if (hh.length()==1) hh="0"+hh;
        String mm = Integer.toString(cal.get(Calendar.MINUTE));
        if (mm.length()==1) mm="0"+mm;
        return YYYY+"-"+MM+"-"+DD+" "+hh+":"+mm+":"+"00";
    }

    private boolean validator() {
        name = meetingNameEditText.getText().toString();
        if (name.isEmpty()) {
            meetingNameEditText.setError(getString(R.string.meeting_name_missing));
            return false;
        } else {
            meetingNameEditText.setError(null);
        }

        description = meetingDescriptionEditText.getText().toString();
        if (description.isEmpty())
            description = null;

        String date = meetingDateSubtitleText.getText().toString();
        if (date.equals(getString(R.string.meeting_date_subtitle))) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.meeting_date_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        String timeStart = meetingTimeStartSubtitleText.getText().toString();
        if (timeStart.equals(getString(R.string.meeting_time_start_subtitle))) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.meeting_time_from_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        String timeEnd = meetingTimeEndSubtitleText.getText().toString();
        if (timeEnd.equals(getString(R.string.meeting_time_end_subtitle)))
            timeEnd = null;

        if (mailMember.size() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_member), Toast.LENGTH_LONG).show();
        }

        String location = meetingLocationSubtitleText.getText().toString();
        if (location.equals(getString(R.string.meeting_location_subtitle))) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_location), Toast.LENGTH_LONG).show();
            return false;
        }

        String dateAndTimeStart = timeStart + "-" + date;
        String dateAndTimeEnd = timeEnd + "-" + date;
        DateObject dateSelectedStart = new DateObject(dateAndTimeStart);
        DateObject dateSelectedEnd = new DateObject(dateAndTimeEnd);
        DateObject dateCurrent = new DateObject(Calendar.getInstance());
        if (dateSelectedEnd.isPrevious(dateSelectedStart)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.end_before_start), Toast.LENGTH_LONG).show();
            return false;
        }
        if (dateSelectedStart.isPrevious(dateCurrent)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.start_before_current), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
