package com.example.pat.mapsdemo;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    int location = -1;
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        provider = locationManager.getBestProvider(new Criteria(), false);

        Intent i = getIntent();
        //location = (i.getIntExtra("locationInfo", -1));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (location == -1 && location == 0) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(provider, 1000, 1, this);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (location != -1 && location != 0) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ListViewActivity.locations.get(location), 10));
            mMap.addMarker(new MarkerOptions().position(ListViewActivity.locations.get(location)).title(ListViewActivity.places.get(location)));
        } else {
            locationManager.requestLocationUpdates(provider, 1000, 1, this);
        }


        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }

        mMap.setOnMapLongClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.i("Info", "Permission was denied");

            return;
        }
        locationManager.requestLocationUpdates(provider, 1000, 1, this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
          //      ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
         //   return;
        //}
        // TODO: mal weg machen und funktion testen
        //locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        Double alt = location.getAltitude();
        Float bearing = location.getBearing();
        Float speed = location.getSpeed();
        Float accuracy = location.getAccuracy();

        //Debugging
        //Log.i("Latitude", lat.toString());
        //Log.i("Longitude", lng.toString());
        //Log.i("Altitude", alt.toString());
        //Log.i("Bearing", bearing.toString());
        //Log.i("Speed", speed.toString());
        //Log.i("Accuracy", accuracy.toString());

        //Sets the last known location into the center of the map
       /* if(mMap != null) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
        }*/
        //Provides Infos about the GeoLocation
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    //Button-function which clears the map of all markers
    public void clearMap(View view) {
        if (mMap != null) {
            mMap.clear();
        }
    }

    /*Called once the activity is no longer visible.
    Time or CPU intensive shut-down operations,
    such as writing information to a database should be down in the onStop() method.
     This method is guaranteed to be called as of API 11.*/
    @Override
    protected void onStop() {
        super.onStop();
    }


    //Back to the HomeMenu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return Boolean.parseBoolean(null);
                }
                locationManager.removeUpdates(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }


    @Override
    public void onMapLongClick (LatLng point){

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String label = new Date().toString();


        //reads the address on a specific point with lat and lng
        try {
            List<Address> addressList = geocoder.getFromLocation(point.latitude, point.longitude, 1);

            if(addressList != null && addressList.size()>0) {
                label = addressList.get(0).getAddressLine(0);
            }
        } catch (IOException exNoLocation) {
            Log.i("Exception exNoLocation", "Ist wohl keine Location oder addressList ist null oder 0");
            exNoLocation.printStackTrace();
        }

        ListViewActivity.places.add(label);
        ListViewActivity.arrayAdapter.notifyDataSetChanged();
        ListViewActivity.locations.add(point);


        try {
            ListViewActivity.locationsDB.execSQL("INSERT INTO locations (label, latitude, longitude) VALUES ('" + label + "' ," + point.latitude + "," + point.longitude + ")");
            //Debugging
            // ListViewActivity.locationsDB.execSQL("INSERT INTO locations (label, latitude, longitude) VALUES ('Hallo', 4.3, 1.2)");
        }catch(Exception exInsertLocationToDatabase){
            Log.i("Exception exInsert", "Fehler beim Eintragen in die Datenbank");
            exInsertLocationToDatabase.printStackTrace();
        }

        //Debugging
        //Debugging, prints all locations in the DB in the console
        try {
            Cursor c = ListViewActivity.locationsDB.rawQuery("SELECT * FROM locations", null);

            c.moveToLast();
            c.moveToPrevious();

            int labelIndex = c.getColumnIndex("label");
            int latIndex = c.getColumnIndex("latitude");
            int lngIndex = c.getColumnIndex("longitude");
            int idIndex = c.getColumnIndex("id");

            if(c.moveToNext()) {
                Log.i("Label", c.getString(labelIndex));
                Log.i("Latitude", Double.toString(c.getDouble(latIndex)));
                Log.i("Longitude", Double.toString(c.getDouble(lngIndex)));
                Log.i("ID", Integer.toString(c.getInt(idIndex)));
            }
            c.moveToNext();
        }catch (Exception exRead){
            Log.i("Exception exRead", "Fehler beim auslesen der Tabelle");
            exRead.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(label)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
}
