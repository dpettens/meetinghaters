package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonArrayRequestHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapViewActivity extends FragmentActivity {
    private GoogleMap mMap;

    private MeetingApplication appInstance = MeetingApplication.getAppInstance();

    private ArrayList<String> name;
    private ArrayList<Long> lat;
    private ArrayList<Long> lon;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        name = new ArrayList<String>();
        lat = new ArrayList<Long>();
        lon = new ArrayList<Long>();
        setUpMapIfNeeded();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void request() {
        JsonArrayRequestHelper mRequest = new JsonArrayRequestHelper(
                Request.Method.GET,
                appInstance.remoteDBHandler.apiMeetingURL(appInstance.myDBHandler.getUser(),appInstance.currentMeeting, null),
                null,
                appInstance.myDBHandler.getToken(),
                appInstance.myDBHandler.getUser(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String emailUser = "";
                        for (int i=0; i<response.length(); i++) {
                            JSONObject jsonRequest = new JSONObject();
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                emailUser = jsonObject.getString("email");
                                try {
                                    String loc = jsonObject.getString("location");
                                    if (loc.equals("none")) return;
                                    name.add(jsonObject.getString("firstname")+" " + jsonObject.getString("lastname"));
                                    loc = loc.split(":")[1];
                                    loc = loc.replace("(","");
                                    String sLat = loc.split(",")[0];
                                    String sLon = loc.split(",")[1];
                                    lat.add(Long.valueOf(sLat));
                                    lon.add(Long.valueOf(sLon));
                                } catch (JSONException jse){
                                    jse.printStackTrace();
                                }
                                // TODO EVENTUALLY ADD THE MISSING ELEMENT FOR THE JSONOBJECT
                            } catch (JSONException jse){
                                jse.printStackTrace();
                            }

                        }
                        setUpMapIfNeeded();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                        } else {
                            switch (error.networkResponse.statusCode) {
                                case 500:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // WE ASSUME THE APPLICATION ALREADY HAVE ALL THIS PERMISSION
            // BUT ANDROID FORCE TO PUT THIS TEST IN
            return;
        }
        Location location = locationManager.getLastKnownLocation(bestProvider);
        double dLat = 0;
        double dLon = 0;
        try {
            dLat = location.getLatitude();
            dLon = location.getLongitude();
        } catch (NullPointerException npe) {
            dLat = -1;
            dLon = -1;
        }
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap == null) {
            return;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(dLat, dLon), 15));
        //Marker marker = new Marker();
        //mMap.addMarker(marker);
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(new LatLng(dLat, dLon));
        markerOpt.title("me");
        mMap.addMarker(markerOpt);
        for (int i=0; i<name.size(); i++){
            MarkerOptions m = new MarkerOptions();
            m.position(new LatLng(lat.get(i), lon.get(i)));
            m.title(name.get(i));
            mMap.addMarker(m);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MapView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ucl.epl.lfsab1509.groupe20.meetinghaters/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MapView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ucl.epl.lfsab1509.groupe20.meetinghaters/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}