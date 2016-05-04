package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapViewActivity extends FragmentActivity {
    private GoogleMap mMap;

    private MeetingApplication appInstance = MeetingApplication.getAppInstance();

    private ArrayList<String> name;
    private ArrayList<String> lat;
    private ArrayList<String> lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        name = null;
        lat = null;
        lon = null;
        setUpMapIfNeeded();
    }

    private void request(){
        //TODO
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
        double lat = 0;
        double lon = 0;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
        } catch  (NullPointerException npe){
            lat = -1;
            lon = -1;
        }
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap == null) {
            return;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
        //Marker marker = new Marker();
        //mMap.addMarker(marker);
        MarkerOptions markerOpt = new MarkerOptions();
        markerOpt.position(new LatLng(lat, lon));
        markerOpt.title("me");
        mMap.addMarker(markerOpt);

    }
}