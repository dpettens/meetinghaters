package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * Created by ludovic on 22/04/16.
 */

public class RemoteDBHandler {
    private static String url = "http://92.222.83.75:3000";
    private static String apiMeeting = "/api/meetings";
    private static String apiUser = "/api/users";

    private RequestQueue requestQueue;
    private String TAG;

    public RemoteDBHandler(RequestQueue requestQueue, String TAG) {
        this.requestQueue = requestQueue;
        this.TAG = TAG;
    }

    private RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public <T> void add(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancel(){
        requestQueue.cancelAll(TAG);
    }

    public String apiMeetingURL(){
        return url + apiMeeting;
    }

    public String apiMeetingURL(String meetingID){
        return url + apiMeeting + "/" + meetingID;
    }

    public String apiMeetingURL(String meetingID, String userID){
        String path = url + meetingID + "/" + meetingID + "users";
        if (userID != null){
            path = path + "/" + userID;
        }
        return path;
    }

    public String apiUserURL(){
        return url + apiUser;
    }

    public String apiUserURL(String userID){
        return url + apiUser + "/" + userID;
    }

    /*
    // MEETINGS //////////////////////////////////////
    // no value required
    public void getMeeting(){
        JsonRequestHelper request = new JsonRequestHelper(
                Request.Method.GET,
                apiMeetingURL(),
                null,
                appInstance.myDBHandler.getToken(),
                appInstance.myDBHandler.getUser(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        returned_response = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO
                    }
                }
        );
    }
    // value required to create JSONObject
    public void postMeeting(){
        JSONObject json = new JSONObject();
        try {
            json.put("","");
            //json.put("", new Integer(200));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // no value required
    public void getMeetin(String meetingID, JSONObject ref_response){}
    // vluae required to create JSONObject
    public void putMeeting(String meetingID, JSONObject ref_response){}
    // no value required
    public void deleteMeeting(String meetingID, JSONObject ref_response){}

    // USERS /////////////////////////////////////////
    */
}
