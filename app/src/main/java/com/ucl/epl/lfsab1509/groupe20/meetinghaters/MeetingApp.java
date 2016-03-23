package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.Application;

/**
 * Created by ludovic on 23/03/16.
 */
public class MeetingApp extends Application{

    private String mGlobalUserName;

    public String getmGlobalUserName(){
        return mGlobalUserName;
    }

    public void setmGlobalUserName(String mGlobalUserName){
        this.mGlobalUserName = mGlobalUserName;
    }

}
