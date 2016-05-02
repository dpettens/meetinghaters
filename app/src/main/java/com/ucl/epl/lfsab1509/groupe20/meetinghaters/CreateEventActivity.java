package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.photojoints.multiplecontactpicker.Contact;
import com.photojoints.multiplecontactpicker.MultipleContactPickerActivity;

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

    private Button btnAdd;
    private Button btnCancel;

    private String eventName;
    private String eventDescription;
    private String eventDate;
    private String eventTimeFrom;
    private String eventTimeTo;

    private String eventLocation;

    private GoogleApiClient mGoogleApiClient;
//    private AutoCompleteAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

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
        //button add

        /*btnAdd = (Button) findViewById(R.id.btn_add_event);
        //button cancel
        btnCancel = (Button) findViewById(R.id.btn_cancel_create);*/

/*        final Button button = (Button) findViewById(R.id.btn_create_event);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
          ApiClient.Builder(this).addApi(AppIndex.API).build();*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
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
                DialogFragment picker = new DatePickerFragment();
                picker.show(getSupportFragmentManager(), "datePicker");
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
                appInstance.tmpHourStart = -1;
                minuteStart = appInstance.tmpMinuteStart;
                appInstance.tmpMinuteStart = -1;
                appInstance.toggle = true;
                DialogFragment pickerEnd = new TimePickerFragment();
                pickerEnd.show(getSupportFragmentManager(), "timePickerEnd");
                hourEnd = appInstance.tmpHourEnd;
                appInstance.tmpHourEnd = -1;
                minuteEnd = appInstance.tmpMinuteEnd;
                appInstance.tmpMinuteEnd = -1;
                appInstance.toggle = false;
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
                Place place = PlacePicker.getPlace(data, this);
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

    }

    @Override
    public void onConnectionSuspended( int i ) {

    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult ) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
