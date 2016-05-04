package com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class RemoteDBHandler {
    private String TAG;
    private RequestQueue requestQueue;

    /*
     * URL routes
     */
    private static String url = "http://92.222.83.75:8080";
    private static String api = "api";
    private static String apiAuth = "authenticate";
    private static String apiUser = "users";
    private static String apiMeeting = "meetings";

    public RemoteDBHandler(RequestQueue requestQueue, String TAG) {
        this.requestQueue = requestQueue;
        this.TAG = TAG;
    }

    private RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public <T> void add(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancel() {
        requestQueue.cancelAll(TAG);
    }

    /*
     * URL getter
     */
    private String api() {
        return url + "/" + api;
    }

    public String apiAuth() {
        return api() + "/" + apiAuth;
    }

    public String apiUserURL() {
        return api() + "/" + apiUser;
    }

    public String apiUserURL(String id_user) {
        return apiUserURL() + "/" + id_user;
    }

    public String apiMeetingURL() {
        return api() + "/" + apiMeeting;
    }

    public String apiMeetingURL(String id_owner, String id_meeting) {
        return apiMeetingURL() + "/" + id_owner + "/" + id_meeting;
    }

    public String apiMeetingURL(String id_owner, String id_meeting, String id_user) {
        String path = apiMeetingURL(id_owner, id_meeting) + "/" +  apiUser;
        if (id_user != null)
            path += "/" + id_user;

        return path;
    }
}
