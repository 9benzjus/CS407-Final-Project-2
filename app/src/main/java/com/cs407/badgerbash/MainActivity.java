package com.cs407.badgerbash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private LinearLayout VertiLayoutContainer;
    private LinearLayout HoriLayoutContainer;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VertiLayoutContainer = findViewById(R.id.VertiLayout);
        HoriLayoutContainer = findViewById(R.id.HoriLayout);

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);

        loadSignedUpEvents();
        loadAllEvents();

        Button createEventButton = findViewById(R.id.CreateEventButton);
        setUpCreatEventButton(createEventButton);
        Button settingsButton = findViewById(R.id.SettingButton);
        setUpSettingsButton(settingsButton);
        Button signoutButton=findViewById(R.id.signout);
        setUpSignoutButton(signoutButton);
        Button refreshButton=findViewById(R.id.Refresh);
        setUpRefreshButton(refreshButton);

    }

    private void setUpRefreshButton(Button refreshButton) {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllEvents();
            }
        });
    }

    private void setUpSignoutButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {

        FirebaseAuth.getInstance().signOut();
        // Clear the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username"); // Use the same key that was used to store the username
        editor.apply();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }




    private void loadSignedUpEvents(){
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        String email=sharedPreferences.getString("Username","defaultUsername");
        int dotIndex = email.indexOf('.');
        String username = email.substring(0, dotIndex);

        usersRef.child(username).child("SignedUp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String eventName = eventSnapshot.child("name").getValue(String.class);
                    String briefDescription = eventSnapshot.child("briefDescription").getValue(String.class);
                    String createdBy = eventSnapshot.child("createdBy").getValue(String.class);
                    String fullDescription = eventSnapshot.child("fullDescription").getValue(String.class);
                    String lat = eventSnapshot.child("lat").getValue(String.class);
                    String lon = eventSnapshot.child("lon").getValue(String.class);
                    String selectedTime=eventSnapshot.child("selectedTime").getValue(String.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    try {
                        Date eventDate = dateFormat.parse(selectedTime);
                        Date currentDate = new Date();

                        if (eventDate != null && eventDate.before(currentDate)) {
                            // The event is in the past, delete it
                            eventSnapshot.getRef().removeValue();
                            continue;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    EventInfo event=new EventInfo(eventName,lat,lon,createdBy,briefDescription,fullDescription,selectedTime);
                    // Add each event to the ScrollView
                    addEventToHori(event);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }




    private void loadAllEvents(){
        VertiLayoutContainer.removeAllViews();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot eventsSnapshot = userSnapshot.child("Created Events");
                    for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                        String eventName = eventSnapshot.child("name").getValue(String.class);
                        String briefDescription = eventSnapshot.child("briefDescription").getValue(String.class);
                        String createdBy = eventSnapshot.child("createdBy").getValue(String.class);
                        String fullDescription = eventSnapshot.child("fullDescription").getValue(String.class);
                        String lat = eventSnapshot.child("lat").getValue(String.class);
                        String lon = eventSnapshot.child("lon").getValue(String.class);
                        String selectedTime=eventSnapshot.child("selectedTime").getValue(String.class);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        try {
                            Date eventDate = dateFormat.parse(selectedTime);
                            Date currentDate = new Date();

                            if (eventDate != null && eventDate.before(currentDate)) {
                                // The event is in the past, delete it
                                eventSnapshot.getRef().removeValue();
                                continue;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        EventInfo event=new EventInfo(eventName,lat,lon,createdBy,briefDescription,fullDescription,selectedTime);
                        // Add each event to the ScrollView
                        addEventToLayout(event);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void addEventToHori(EventInfo event){
        LinearLayout horiLayout = findViewById(R.id.HoriLayout);
        String name=event.getName();
        String lat=event.getLat();
        String lon=event.getLon();
        String createdBy=event.getCreatedBy();
        String brief=event.getBriefDescription();
        String full=event.getFullDescription();
        String selectedTime= event.getSelectedTime();

        View eventView = LayoutInflater.from(this).inflate(R.layout.loaded_event_box, horiLayout, false);

        TextView eventName = eventView.findViewById(R.id.EventName);
        TextView eventDescription = eventView.findViewById(R.id.briefDesc);

        eventName.setText(event.getName());
        eventDescription.setText(event.getBriefDescription());
        horiLayout.addView(eventView);
        eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,EventDescriptionPage.class);
                intent.putExtra("name",name);
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                intent.putExtra("createdBy",createdBy);
                intent.putExtra("brief",brief);
                intent.putExtra("full",full);
                intent.putExtra("selectedTime",selectedTime);
                startActivity(intent);
            }
        });
    }

    private void addEventToLayout(EventInfo event){
        LinearLayout VertiLayout = findViewById(R.id.VertiLayout);
        String name=event.getName();
        String lat=event.getLat();
        String lon=event.getLon();
        String createdBy=event.getCreatedBy();
        String brief=event.getBriefDescription();
        String full=event.getFullDescription();
        String selectedTime= event.getSelectedTime();

        View eventView = LayoutInflater.from(this).inflate(R.layout.loaded_event_box, VertiLayout, false);

        TextView eventName = eventView.findViewById(R.id.EventName);
        TextView eventDescription = eventView.findViewById(R.id.briefDesc);

        eventName.setText(event.getName());
        eventDescription.setText(event.getBriefDescription());
        VertiLayout.addView(eventView);
        eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,EventDescriptionPage.class);
                intent.putExtra("name",name);
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                intent.putExtra("createdBy",createdBy);
                intent.putExtra("brief",brief);
                intent.putExtra("full",full);
                intent.putExtra("selectedTime",selectedTime);
                startActivity(intent);
            }
        });
    }


    private void setUpCreatEventButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateEventPage.class);
                startActivity(intent);
            }
        });

    }


    private void setUpSettingsButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

    }
}