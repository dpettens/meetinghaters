package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.Application;
import android.content.Intent;

/**
 * Created by ludovic on 23/03/16.
 */
public class MeetingApp extends Application {

    private String mail = null;
    private String location = null;

    //connection to mysql
    // TODO


    @Override
    public void onCreate() {
        super.onCreate();
        MyDBHandler dbHandler = new MyDBHandler(this, null);

        mail = dbHandler.isRegistered();

        Intent i;
        if (mail != null) {
            i=new Intent(MeetingApp.this,MainActivity.class);
            startActivity(i);
        } else {
            i=new Intent(MeetingApp.this,SignInActivity.class);
            startActivity(i);
        }

    }

    public String getMail(){
        return mail;
    }
}
