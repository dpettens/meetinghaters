package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ludovic on 23/03/16.
 */
public class MeetingApp extends Application {

    private String mail = null;
    private String location = null;
    private MyDBHandler dbHandler;
    //connection to mysql
    // TODO


    @Override
    public void onCreate() {
        super.onCreate();
        dbHandler = new MyDBHandler(this, null);

        mail = dbHandler.isRegistered();
        //mail = "test";
        Log.e("APP LUDOVIC DEBUG : ", "" + mail);

        Intent i;
        if (mail != null) {
            i=new Intent(MeetingApp.this,MeetingListActivity.class);
        } else {
            i=new Intent(MeetingApp.this,SignUpActivity.class);
        }
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        /*final Class<? extends Activity> activityClass;
        if(mail != null)
            activityClass = MainActivity.class;
        else
            activityClass = SignInActivity.class;

        Intent newActivity = new Intent(MeetingApp.this, activityClass);
        newActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MeetingApp.this.startActivity(newActivity);*/

    }

    public String getMail(){
        return mail;
    }

    public void setMail(String mail){
        dbHandler.addUser(mail);
        this.mail = mail;
    }
}
