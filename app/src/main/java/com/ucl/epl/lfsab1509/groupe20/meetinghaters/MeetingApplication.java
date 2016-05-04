package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.Application;
import android.content.Intent;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.MyDBHandler;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.RemoteDBHandler;

public class MeetingApplication extends Application {
    private static final String TAG = "MeetingApp";


    public String currentMeeting;
    private static MeetingApplication appInstance;

    // sqlite db
    public MyDBHandler myDBHandler;

    // remote mariadb
    private RequestQueue volleyRequestQueue;
    public RemoteDBHandler remoteDBHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;

        // sqlite db
        myDBHandler = new MyDBHandler(this, null);

        // remote mariadb
        volleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        remoteDBHandler = new RemoteDBHandler(volleyRequestQueue, TAG);

        // launch SignUpActivity if the user is not registered in sqlite else meetingListActivity
        Intent i;
        if (myDBHandler.isRegistered() != null) {
            i = new Intent(MeetingApplication.this, MeetingListActivity.class);
        } else {
            i = new Intent(MeetingApplication.this, SignUpActivity.class);
        }

        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent bckService = new Intent(this, BgServiceIntent.class);
        bckService.putExtra("username", appInstance.myDBHandler.getUser());
        bckService.putExtra("token", appInstance.myDBHandler.getToken());
        startService(bckService);
        startActivity(i);
    }

    public static synchronized MeetingApplication getAppInstance() {
        return appInstance;
    }
}
