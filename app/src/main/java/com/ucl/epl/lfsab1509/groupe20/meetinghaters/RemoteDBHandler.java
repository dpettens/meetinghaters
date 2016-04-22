package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import com.android.volley.RequestQueue;

/**
 * Created by ludovic on 22/04/16.
 */

public class RemoteDBHandler {
    private static String url = "http://92.222.83.75:3000";

    private RequestQueue requestQueue;

    public RemoteDBHandler(RequestQueue requestQueue){
        this.requestQueue = requestQueue;
    }

    //public

}
