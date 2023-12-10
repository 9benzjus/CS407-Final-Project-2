package com.cs407.badgerbash;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapSelect extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SearchView mapSearchView;

    private LatLng latLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_select);
        mapSearchView=findViewById(R.id.mapSearch);
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        Intent intent = getIntent();
        // Extract the data from the Intent
        String selectedTime=intent.getStringExtra("selectedTime");
        String eventName = intent.getStringExtra("eventName");
        String briefDescription = intent.getStringExtra("briefDescription");
        String fullDescription = intent.getStringExtra("fullDescription");

        Button confirmButton=findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapSelect.this, CreateEventPage.class);
                intent.putExtra("lat",String.valueOf(latLng.latitude));
                intent.putExtra("lon",String.valueOf(latLng.longitude));
                intent.putExtra("eventName",eventName);
                intent.putExtra("briefDescription",briefDescription);
                intent.putExtra("fullDescription",fullDescription);
                intent.putExtra("selectedTime",selectedTime);
                startActivity(intent);
            }
        });

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location=mapSearchView.getQuery().toString();
                List<Address> addressList=null;

                if(location!=null){
                    Geocoder geocoder=new Geocoder(MapSelect.this);
                    try{
                        addressList=geocoder.getFromLocationName(location,1 );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address=addressList.get(0);
                    latLng=new LatLng(address.getLatitude(),address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(MapSelect.this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
