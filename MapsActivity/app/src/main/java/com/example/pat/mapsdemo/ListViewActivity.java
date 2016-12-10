package com.example.pat.mapsdemo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ListViewActivity extends AppCompatActivity {

    static ArrayList<String> places;
    static ArrayAdapter arrayAdapter;
    static ArrayList<LatLng> locations;
    static SQLiteDatabase locationsDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        ListView listView = (ListView)findViewById(R.id.listView);
        places = new ArrayList<>();

        places.add("Add a new place...");
        locations = new ArrayList<>();
        locations.add(new LatLng(0,0));
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);


        listView.setAdapter(arrayAdapter);

        //Creating the Database
        try{
            locationsDB = this.openOrCreateDatabase("Locations", MODE_PRIVATE, null);
            locationsDB.execSQL("CREATE TABLE IF NOT EXISTS locations (label VARCHAR, latitude DOUBLE, longitude DOUBLE, id INTEGER PRIMARY KEY)");
        }catch (Exception exCreate){
            Log.i("Exception exCreate", "Fehler beim anlegen der Datenbank/Table");
            exCreate.printStackTrace();
        }


        //Read from the Database
        try {
            //Debugging, prints all locations in the DB in the console
            Cursor c = locationsDB.rawQuery("SELECT * FROM locations", null);

            int labelIndex = c.getColumnIndex("label");
            int latIndex = c.getColumnIndex("latitude");
            int lngIndex = c.getColumnIndex("longitude");
            int idIndex = c.getColumnIndex("id");

            c.moveToFirst();

             do {
                //Debugging
                //Log.i("Fuck","Fuck this shit");


                Log.i("Label", c.getString(labelIndex));
                Log.i("Latitude", Double.toString(c.getDouble(latIndex)));
                Log.i("Longitude", Double.toString(c.getDouble(lngIndex)));
                Log.i("ID", Integer.toString(c.getInt(idIndex)));


                places.add(c.getString(labelIndex));
                locations.add(new LatLng(c.getDouble(latIndex),c.getDouble(lngIndex)));
            }while (c.moveToNext());
        }catch (Exception exRead){
            Log.i("Exception exRead", "Fehler beim auslesen der Tabelle");
            exRead.printStackTrace();
        }


        //Starts Map_Activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent (getApplicationContext(), MapsActivity.class);
                i.putExtra("locationInfo", position);
                startActivity(i);

            }
        });


    }


}
