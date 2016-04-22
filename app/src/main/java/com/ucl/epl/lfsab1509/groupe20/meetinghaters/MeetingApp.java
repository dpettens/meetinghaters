package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ludovic on 23/03/16.
 */
public class MeetingApp extends Application {

    private String mail = null;
    private String location = null;

    private static MeetingApp appInstance;

    public MyDBHandler myDBHandler;
    //connection to mysql
    // TODO

    private RequestQueue volleyRequestQueue;
    public RemoteDBHandler remoteDBHandler;



    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        myDBHandler = new MyDBHandler(this, null);
        volleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        remoteDBHandler = new RemoteDBHandler(volleyRequestQueue);

        Intent i;
        Log.e("My name from db is :", ""+myDBHandler.isRegistered());
        if (myDBHandler.isRegistered() != null) {
            i=new Intent(MeetingApp.this,MeetingListActivity.class);
        } else {
            i=new Intent(MeetingApp.this,SignUpActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public static synchronized MeetingApp getAppInstance(){
        return appInstance;
    }

}
