package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.photojoints.multiplecontactpicker.Contact;
import com.photojoints.multiplecontactpicker.MultipleContactPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class CreateEventActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {



    private static final int CONTACT_PICKER_REQUEST = 200;
    private static final int PLACE_PICKER_REQUEST = 300;

    MeetingApp appInstance = MeetingApp.getAppInstance();

    private EditText textName;
    private EditText textDescription;

    private CardView cardDate;
    private TextView textDate;
    int year = -1;
    int month = -1;
    int day = -1;


    private CardView cardTime;
    private TextView textTimeFrom;
    private TextView textTimeTo;
    int hourStart = -1;
    int minuteStart = -1;
    int hourEnd = -1;
    int minuteEnd = -1;

    private CardView cardMember;
    private TextView textMember;
    ArrayList<String> mailMember = new ArrayList<String>();
    ArrayList<String> nameMember = new ArrayList<String>();


    private CardView cardLocation;
    private TextView textLocation;

    private FloatingActionButton fabAddMeeting;
    private FloatingActionButton fabCancelMeeting;

    private String eventName;
    private String eventDescription;
    private String eventDate;
    private String eventTimeFrom;
    private String eventTimeTo;

    private String eventLocation;
    private Place place;

    private GoogleApiClient mGoogleApiClient;

    private boolean eventAdded;

    private String formatDateTime(int year, int month, int day, int hour, int minute, int modifierMinute){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        long millis = cal.getTimeInMillis();
        millis = millis + modifierMinute*60*1000;
        cal.setTimeInMillis(millis);
        String YYYY = new Integer(cal.get(Calendar.YEAR)).toString();
        String MM = new Integer(cal.get(Calendar.MONTH)).toString();
        if (MM.length() == 1) MM="0"+MM;
        String DD = new Integer(cal.get(Calendar.DAY_OF_MONTH)).toString();
        if (DD.length()==1) DD="0"+DD;
        String hh = new Integer(cal.get(Calendar.HOUR_OF_DAY)).toString();
        if (hh.length()==1) hh="0"+hh;
        String mm = new Integer(cal.get(Calendar.MINUTE)).toString();
        if (mm.length()==1) mm="0"+mm;
        return YYYY+"-"+MM+"-"+DD+" "+hh+":"+mm+":"+"00";
    }

     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventAdded = false;
        //EditText Name
        textName = (EditText) findViewById(R.id.create_event_name);
        //EditText Description
        textDescription = (EditText) findViewById(R.id.create_event_description);
        //cardView date
        cardDate = (CardView) findViewById(R.id.create_card_date);
        textDate = (TextView) findViewById(R.id.create_event_date);
        datePickerDialog(cardDate); //DONE
        //cardview time dual from to
        cardTime = (CardView) findViewById(R.id.create_card_time);
        textTimeFrom = (TextView) findViewById(R.id.create_event_time_from);
        textTimeTo = (TextView) findViewById(R.id.create_event_time_to);
        doubleTimePickerDialog(cardTime);
        //cardview member 17 char max
        cardMember = (CardView) findViewById(R.id.create_card_people);
        textMember = (TextView) findViewById(R.id.create_event_member);
        contactPicker(cardMember);
        //cardview location
        cardLocation = (CardView) findViewById(R.id.create_card_location);
        textLocation = (TextView) findViewById(R.id.create_event_location);
        //locationPicker(cardLocation);
        cardLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationPickerInner();
            }
        });
        //button add /////////////////////////////////////////////////////////////////////
        fabAddMeeting = (FloatingActionButton) findViewById(R.id.fab_add_event);
        fabAddMeeting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //if (!validator()){return;}
                if (!eventAdded) {
                    addCalendarEvent();
                    eventAdded = true;
                }
                ///////////////////////////////////////////////////////////////////////////////////
                //
                //   VOLLEY START
                //
                ///////////////////////////////////////////////////////////////////////////////////
                //Remote db
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name",eventName);
                    jsonObject.put("id_owner",appInstance.myDBHandler.getUser());
                    jsonObject.put("description",""+eventDescription);
                    jsonObject.put("location",place.getLatLng().toString());
                    jsonObject.put("time_pre",formatDateTime(year, month, day, hourStart, minuteStart, -15));
                    jsonObject.put("time_post",formatDateTime(year, month ,day, hourStart, minuteStart, 15));
                    jsonObject.put("time_start",formatDateTime(year, month ,day, hourStart, minuteStart, 0));
                    if (hourEnd != -1)
                        jsonObject.put("time_end",formatDateTime(year, month ,day, hourStart, minuteStart, 15));
                    else jsonObject.put("time_end",formatDateTime(year, month ,day, hourEnd, minuteEnd, 0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonRequestHelper request = new JsonRequestHelper(
                        Request.Method.POST,
                        appInstance.remoteDBHandler.apiMeetingURL(),
                        jsonObject,
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
                                mailMember.add(appInstance.myDBHandler.getUser());
                                for (int i=0; i<mailMember.size(); i++){
                                    JSONObject mJson = new JSONObject();
                                    try {
                                        mJson.put("id_meeting", id);
                                        mJson.put("id_user", mailMember.get(i));
                                        mJson.put("id_owner", appInstance.myDBHandler.getUser());
                                    } catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    JsonRequestHelper innerRequest = new JsonRequestHelper(
                                            Request.Method.POST,
                                            appInstance.remoteDBHandler.apiMeetingURL(appInstance.myDBHandler.getUser(), new Integer(id).toString(), null),
                                            mJson,
                                            appInstance.myDBHandler.getToken(),
                                            appInstance.myDBHandler.getUser(),
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    //NOTHING TODO
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.e("2 error pre: ", "yop" /*new Integer(error.networkResponse.statusCode).toString()*/);
                                                    if (error instanceof TimeoutError || error instanceof NoConnectionError){
                                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Log.e("2 error post: ", String.valueOf(error.networkResponse.statusCode));
                                                        //*
                                                        switch (error.networkResponse.statusCode) {
                                                            case 400:
                                                                Log.e("2 message 400 : ", getResources().getString(R.string.server_error));
                                                                Log.e("2 token 400 : ", appInstance.myDBHandler.getToken());
                                                                Log.e("2 user 400 : ", appInstance.myDBHandler.getUser());
                                                                break;
                                                            case 500:
                                                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                                                break;
                                                        }/**/
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
                                Log.e("First error pre: ", "yop" /*new Integer(error.networkResponse.statusCode).toString()*/);
                                if (error instanceof TimeoutError || error instanceof NoConnectionError){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                                } else {
                                    Log.e("First error post: ", String.valueOf(error.networkResponse.statusCode));
                                    //*
                                    switch (error.networkResponse.statusCode) {
                                        case 400:
                                            Log.e("1 message 400 : ", getResources().getString(R.string.server_error));
                                            Log.e("1 token 400 : ", appInstance.myDBHandler.getToken());
                                            Log.e("1 user 400 : ", appInstance.myDBHandler.getUser());
                                            break;
                                        case 500:
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                            break;
                                    }/**/
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
            }
        });
        // calendar manager

    }

    public void addCalendarEvent(){

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hourStart, minuteStart);
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", false);
        if (hourEnd != -1 && minuteEnd != -1){
            cal.set(year, month, day, hourEnd, minuteEnd);
            intent.putExtra("endTime", cal.getTimeInMillis());
        } else {
            intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        }
        intent.putExtra("title", eventName);
        intent.putExtra("description", eventDescription);
        intent.putExtra("eventLocation", place.getAddress());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appInstance.tmpHourStart = -1;
        appInstance.tmpMinuteStart = -1;
        appInstance.tmpHourEnd = -1;
        appInstance.tmpMinuteEnd = -1;
    }

    private void registerMeeting() {
        if (!validator()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_register_meeting), Toast.LENGTH_LONG).show();
            return;
        }
        //TODO PROCESS THE SENDING WITH VOLLEY

    }

    public void datePickerDialog(CardView card) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogFragment picker = new DatePickerFragment();
                //picker.show(getSupportFragmentManager(), "datePicker");
                DatePickerFragment picker = new DatePickerFragment();
                picker.show(getSupportFragmentManager(), "datePicker");
                DatePicker pick = picker.getDatePicker();
                year = appInstance.tmpYear;
                appInstance.tmpYear = -1;
                month = appInstance.tmpMonth;
                appInstance.tmpMonth = -1;
                day = appInstance.tmpDay;
                appInstance.tmpDay = -1;
                DateObject pickedDate = new DateObject(0, 0, day, month, year);
                if (pickedDate.isPrevious(new DateObject(Calendar.getInstance()))) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.date_too_low), Toast.LENGTH_LONG).show();
                } else {
                    textDate.setText(new Integer(day).toString() + "/" + new Integer(month).toString() + "/" + new Integer(year).toString());
                }
            }
        });
    }

    public void doubleTimePickerDialog(CardView card) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment pickerStart = new TimePickerFragment();
                pickerStart.show(getSupportFragmentManager(), "timePickerStart");
                hourStart = appInstance.tmpHourStart;

                minuteStart = appInstance.tmpMinuteStart;

                appInstance.toggle = true;
                DialogFragment pickerEnd = new TimePickerFragment();
                pickerEnd.show(getSupportFragmentManager(), "timePickerEnd");
                hourEnd = appInstance.tmpHourEnd;

                minuteEnd = appInstance.tmpMinuteEnd;

                appInstance.toggle = false;
                Toast.makeText(getApplicationContext(), ""+hourStart, Toast.LENGTH_SHORT);
                Toast.makeText(getApplicationContext(), ""+minuteStart, Toast.LENGTH_SHORT);
                Toast.makeText(getApplicationContext(), ""+hourEnd, Toast.LENGTH_SHORT);
                Toast.makeText(getApplicationContext(), ""+minuteEnd, Toast.LENGTH_SHORT);
                //final check in the validator in order to be sure that the time and date selected is coherent with the current date and time
                textTimeFrom.setText(new Integer(hourStart).toString() + ":" + new Integer(minuteStart).toString());
                textTimeTo.setText(new Integer(hourEnd).toString() + ":" + new Integer(minuteEnd).toString());
            }
        });
    }

    public void contactPicker(CardView card) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(getApplicationContext(), MultipleContactPickerActivity.class);
                startActivityForResult(contactPickerIntent, CONTACT_PICKER_REQUEST);

            }
        });
    }

    public void locationPickerInner(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            Log.e("lol", "locationPickerInner");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void locationPicker(CardView card) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(getApplication()), PLACE_PICKER_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                ArrayList<Contact> contacts = new ArrayList<Contact>();
                contacts = data.getParcelableArrayListExtra("contacts");
                for (Contact c : contacts) {
                    mailMember.add(c.getEmail());
                    nameMember.add(c.getName());
                }
            }
            String textMemberNames = "";
            for (int i = 0; i < mailMember.size(); i++) {
                if (textMemberNames.length() + 6 < 17) {
                    textMemberNames = textMemberNames + mailMember.get(i) + " ";
                } else {
                    if (i == 0)
                        textMemberNames = textMemberNames + new Integer(mailMember.size() - i).toString() + "more";
                }
            }
            textMember.setText(textMemberNames);
        }else if (requestCode == PLACE_PICKER_REQUEST) {
            Log.e("lol", "" + resultCode);
            if (resultCode == RESULT_OK){
                place = PlacePicker.getPlace(data, this);
                Toast.makeText(getApplicationContext(), place.getLatLng().toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean validator() {

        eventName = textName.getText().toString();
        if (eventName.isEmpty()) {
            textName.setError(getString(R.string.meeting_name_missing));
            return false;
        } else {
            textName.setError(null);
        }

        eventDescription = textName.getText().toString();
        if (eventDescription.isEmpty()) eventDescription = null;

        eventDate = textDate.getText().toString();
        if (eventDate.toString().equals(getString(R.string.meeting_select_date))) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.meeting_date_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        eventTimeFrom = textTimeFrom.getText().toString();
        if (eventTimeFrom.toString().equals(getString(R.string.meeting_time_from))) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.meeting_time_from_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        eventTimeTo = textTimeTo.getText().toString();
        if (eventTimeTo.toString().equals(getString(R.string.meeting_time_to))) eventTimeTo = null;

        if (mailMember.size() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_member), Toast.LENGTH_LONG).show();
        }

        eventLocation = textLocation.getText().toString();
        if (eventDate.toString().equals(getString(R.string.meeting_location))) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_location), Toast.LENGTH_LONG).show();
            return false;
        }

        String dateAndTimeStart = eventTimeFrom.toString() + "-" + eventDate.toString();
        String dateAndTimeEnd = eventTimeTo.toString() + "-" + eventDate.toString();
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


    @Override
    public void onConnected( Bundle bundle ) {
        //NOPE NOT NEEDED
    }

    @Override
    public void onConnectionSuspended( int i ) {
        //NOPE NOT NEEDED
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult ) {
        //NOPE NOT NEEDED
    }





}
