package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.Application;
import android.content.Intent;

/**
 * Created by ludovic on 23/03/16.
 */
public class MeetingApp extends Application {

    private String mail = null;
    private String location = null;
    public MyDBHandler myDBHandler;
    //connection to mysql
    // TODO
    private RemoteDBHandler remoteDBHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        myDBHandler = new MyDBHandler(this, null);
        remoteDBHandler = new RemoteDBHandler();

        Intent i;
        if (myDBHandler.isRegistered() != null) {
            i=new Intent(MeetingApp.this,MeetingListActivity.class);
        } else {
            i=new Intent(MeetingApp.this,SignUpActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }
}
