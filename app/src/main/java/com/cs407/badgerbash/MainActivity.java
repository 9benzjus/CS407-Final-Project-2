package com.cs407.badgerbash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
        Button friendsButton = findViewById(R.id.FriendsButton);
        setUpFriendsButton(friendsButton);
        Button settingsButton = findViewById(R.id.SettingButton);
        setUpSettingsButton(settingsButton);
        Button signoutButton=findViewById(R.id.signout);
        setUpSignoutButton(signoutButton);
//        Button refreshButton=findViewById(R.id.Refresh);
//        setUpRefreshButton(refreshButton);

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

    private void setUpVertiOnClick(FrameLayout verti, EventInfo event){
        String name=event.getName();
        String lat=event.getLat();
        String lon=event.getLon();
        String createdBy=event.getCreatedBy();
        String brief=event.getBriefDescription();
        String full=event.getBriefDescription();
        verti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventDescriptionPage.class);
                intent.putExtra("name",name);
                intent.putExtra("lat",lat);
                intent.putExtra("lon",lon);
                intent.putExtra("createdBy",createdBy);
                intent.putExtra("brief",brief);
                intent.putExtra("full",full);
                startActivity(intent);
            }
        });
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
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    EventInfo event = userSnapshot.getValue(EventInfo.class);
                    // Add each event to the ScrollView
                    FrameLayout horiframe=addEventFrameLayout(event);
                    addViewToHori(horiframe);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }




    private void loadAllEvents(){
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot eventsSnapshot = userSnapshot.child("Created Events");
                    for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                        String eventName = dataSnapshot.child("name").getValue(String.class);
                        String briefDescription = dataSnapshot.child("briefDescription").getValue(String.class);
                        String createdBy = dataSnapshot.child("createdBy").getValue(String.class);
                        String fullDescription = dataSnapshot.child("fullDescription").getValue(String.class);
                        String lat = dataSnapshot.child("lat").getValue(String.class);
                        String lon = dataSnapshot.child("lon").getValue(String.class);
                        EventInfo event=new EventInfo(eventName,lat,lon,createdBy,briefDescription,fullDescription);
                        // Add each event to the ScrollView
                        VertiLayoutContainer.removeAllViews();
                        FrameLayout vertiframe=addEventFrameLayout(event);
                        addViewToVerti(vertiframe);
                        setUpVertiOnClick(vertiframe, event);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

//    private void setUpRefreshButton(Button button) {
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linearLayoutContainer.removeAllViews();
//                loadAllEvents();
//            }
//        });
//
//    }


    private FrameLayout addEventFrameLayout(EventInfo event) {
        // Create a new FrameLayout with layout parameters
        FrameLayout frameLayout = new FrameLayout(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, // Width
                FrameLayout.LayoutParams.WRAP_CONTENT  // Height
        );
        layoutParams.setMargins(16, 16, 16, 16); // Example margins
        frameLayout.setLayoutParams(layoutParams);

        // Set background, padding, etc.
        frameLayout.setBackgroundColor(Color.LTGRAY); // Example background color
        frameLayout.setPadding(10, 10, 10, 10);

        String name=event.getName();
        String lat=event.getLat();
        String lon=event.getLon();
        String createdBy=event.getCreatedBy();
        String brief=event.getBriefDescription();
        String full=event.getFullDescription();

        // Add child views to the FrameLayout
        TextView textView1 = new TextView(this);
        textView1.setText(name); // Set text from event data
        TextView textView2 = new TextView(this);
        textView2.setText(lat);
        TextView textView3 = new TextView(this);
        textView3.setText(lon);
        TextView textView4 = new TextView(this);
        textView4.setText(createdBy);
        TextView textView5 = new TextView(this);
        textView5.setText(brief);
        TextView textView6 = new TextView(this);
        textView6.setText(full);
        // Configure textView (style, layout params, etc.)

        frameLayout.addView(textView1);
        frameLayout.addView(textView2);
        frameLayout.addView(textView3);
        frameLayout.addView(textView4);
        frameLayout.addView(textView5);
        frameLayout.addView(textView6);

        // Add the FrameLayout to the LinearLayout
        return frameLayout;
    }

    private void addViewToVerti(FrameLayout frameLayout){
        VertiLayoutContainer.addView(frameLayout);
    }

    private void addViewToHori(FrameLayout frameLayout){
        HoriLayoutContainer.addView(frameLayout);
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

    private void setUpFriendsButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FriendsList.class);
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