package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Adapter.MeetingItem;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Contact.Contact;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Contact.ContactsPickerActivity;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonArrayRequestHelper;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonRequestHelper;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Fragment.DateObject;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Fragment.DatePickerFragment;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.Fragment.TimePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class ViewEventActivity extends AppCompatActivity {

    private static final String TAG = "ViewEventActivity";

    //FIXME private boolean eventAdded;

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
        /*meetingPeopleRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                people();
            }
        });

        meetingLocationRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location();
            }
        });*/

        /*
        JsonArrayRequestHelper request = new JsonArrayRequestHelper(
                Request.Method.GET,
                appInstance.remoteDBHandler.apiMeetingURL(String id_owner, String id_meeting),
                null, //GET REQUEST so no JSONObject to pass
                appInstance.myDBHandler.getToken(),
                appInstance.myDBHandler.getUser(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e(TAG, "success length response :: " + response.length());
                        for (int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Log.e(TAG, "my json : " + jsonObject.toString());
                                MeetingItem meetingItem = new MeetingItem(
                                        jsonObject.getString("_id"),
                                        jsonObject.getString("name"),
                                        //jsonObject.getString("description"),
                                        "new desc",
                                        jsonObject.getString("time_start"),
                                        jsonObject.getString("time_end"));
                                //meetings.add(meetingItem);
                                recyclerAdapter.addItem(meetingItem);
                                Log.e(TAG, "size equal " + meetings.size());
                            } catch (JSONException jsonex) {
                                jsonex.printStackTrace();
                            }
                        }
                        Log.e(TAG, "success :: " + meetings.toString());
                        //recyclerAdapter.swap(meetings);
                        //recyclerAdapter.dataSetChanged();

                        recyclerView.setAdapter(recyclerAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "error volley " + error.networkResponse.statusCode);
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
        appInstance.remoteDBHandler.add(request);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
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

    private String formatDateTime(int year, int month, int day, int hour, int minute, int modifierMinute) {
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
}
