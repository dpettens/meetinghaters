package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonRequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
public class BgServiceIntent extends IntentService {
    MeetingApplication appInstance = MeetingApplication.getAppInstance();
    public BgServiceIntent() {
        super("BackgroundCommunicationService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String username = intent.getStringExtra("username");
        String token = intent.getStringExtra("token");
        while (true) {
            try {
                Thread.sleep(1000 * 60 * 5);
                Log.e("BG SERVICE :", "stil working");
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, false);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // WE ASSUME THE APPLICATION ALREADY HAVE ALL THIS PERMISSION
                            // BUT ANDROID FORCE TO PUT THIS TEST IN
                    return;
                }
                Location location = locationManager.getLastKnownLocation(bestProvider);
                double lat = 0;
                double lon = 0;
                try {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                } catch  (NullPointerException npe){
                    lat = -1;
                    lon = -1;
                }
                JSONObject jsonLocation = new JSONObject();
                try {
                    String loc = "lat/lng: " + "(" + Double.toString(lat) + "," + Double.toString(lon) + ")";
                    loc.replaceAll(",", ".");
                    jsonLocation.put("test", loc); //TODO FIELD NAME TO CHANGE
                    Calendar cal = Calendar.getInstance();
                    String dateString = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
                    jsonLocation.put("text", dateString); //TODO FIELD NAME TO CHANGE
                } catch (JSONException jse){
                    jse.printStackTrace();
                }
                JsonRequestHelper requestLocation = new JsonRequestHelper(
                        Request.Method.PUT,
                        appInstance.remoteDBHandler.apiUserURL(appInstance.myDBHandler.getUser()),
                        jsonLocation,
                        appInstance.myDBHandler.getToken(),
                        appInstance.myDBHandler.getUser(),
                        new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response) {
                                //NOTHING TO DO
                            }
                        },
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof TimeoutError || error instanceof NoConnectionError){
                                    //Log.e("on error: ", getResources().getString(R.string.connection_error));
                                    Log.e("on error: ", "nope, timeout or no connection");
                                } else if (error.networkResponse.statusCode == 500){
                                    //Log.e("on error 500: ", getResources().getString(R.string.connection_error));
                                    Log.e("on error 500: ", ",nope 500 error");
                                }
                            }
                        }
                );
                requestLocation.setPriority(Request.Priority.HIGH);
                appInstance.remoteDBHandler.add(requestLocation);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}