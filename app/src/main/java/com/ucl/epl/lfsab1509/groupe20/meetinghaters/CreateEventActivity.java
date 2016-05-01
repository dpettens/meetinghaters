package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class CreateEventActivity extends AppCompatActivity {

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

    private CardView cardMember;
    private TextView textMember;

    private CardView cardLocation;
    private TextView textLocation;

    private Button btnAdd;
    private Button btnCancel;

    private String eventName;
    private String eventDescription;
    private String eventDate;
    private String eventTimeFrom;
    private String eventTimeTo;
    private String[] eventListMember;
    private String eventLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //EditText Name
        textName = (EditText) findViewById(R.id.create_event_name);
        //EditText Description
        textDescription = (EditText) findViewById(R.id.create_event_description);
        //cardView date
        cardDate = (CardView) findViewById(R.id.create_card_date);
        textDate = (TextView) findViewById(R.id.create_event_date);
        datePickerDialog(cardDate);
        //cardview time dual from to
        cardTime = (CardView) findViewById(R.id.create_card_time);
        textTimeFrom = (TextView) findViewById(R.id.create_event_time_from);
        textTimeTo = (TextView) findViewById(R.id.create_event_time_to);
        //cardview member 17 char max
        cardMember = (CardView) findViewById(R.id.create_card_people);
        textMember = (TextView) findViewById(R.id.create_event_member);
        //cardview location
        cardLocation = (CardView) findViewById(R.id.create_card_location);
        textLocation = (TextView) findViewById(R.id.create_event_location);
        //button add
        btnAdd = (Button) findViewById(R.id.btn_add_event);
        //button cancel
        btnCancel = (Button) findViewById(R.id.btn_cancel_create);

/*        final Button button = (Button) findViewById(R.id.btn_create_event);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO add meeting
            }
        });
*/
    }

    @Override
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
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void registerMeeting(){
        if (!validator()){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_register_meeting), Toast.LENGTH_LONG).show();
            return;
        }
        //TODO PROCESS THE SENDING WITH VOLLEY

    }

    public void datePickerDialog(CardView card){
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getSupportFragmentManager(), "datePicker");
                year = appInstance.tmpYear; appInstance.tmpYear = -1;
                month = appInstance.tmpMonth; appInstance.tmpMonth = -1;
                day = appInstance.tmpDay; appInstance.tmpDay = -1;
                DateObject pickedDate = new DateObject(0,0,day,month,year);
                if (pickedDate.isPrevious(new DateObject(Calendar.getInstance()))){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.date_too_low), Toast.LENGTH_LONG).show();
                } else {
                    textDate.setText(new Integer(day).toString()+"/"+new Integer(month).toString()+"/"+new Integer(year).toString());
                }
            }
        });
    }

    private boolean validator(){

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
        if (eventDate.toString().equals(getString(R.string.meeting_select_date))){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.meeting_date_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        eventTimeFrom = textTimeFrom.getText().toString();
        if (eventDate.toString().equals(getString(R.string.meeting_time_from))){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.meeting_time_from_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        eventTimeTo = textTimeTo.getText().toString();
        if (eventTimeTo.toString().equals(getString(R.string.meeting_time_to))) eventTimeTo = null;

        if (eventListMember.length == 0){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_member), Toast.LENGTH_LONG).show();
        }

        eventLocation = textLocation.getText().toString();
        if (eventDate.toString().equals(getString(R.string.meeting_location))){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_location), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


}
