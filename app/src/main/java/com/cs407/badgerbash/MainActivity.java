package com.cs407.badgerbash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VertiLayoutContainer = findViewById(R.id.VertiLayout);
        HoriLayoutContainer = findViewById(R.id.HoriLayout);

        //loadSignedUpEvents();
        loadAllEvents();

        Button createEventButton = findViewById(R.id.CreateEventButton);
        setUpCreatEventButton(createEventButton);
        Button friendsButton = findViewById(R.id.FriendsButton);
        setUpFriendsButton(friendsButton);
        Button settingsButton = findViewById(R.id.SettingButton);
        setUpSettingsButton(settingsButton);
        Button Signoutbutton = findViewById(R.id.signout);
//        Button refreshButton=findViewById(R.id.Refresh);
//        setUpRefreshButton(refreshButton);

        Signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setUpVertiOnClick(FrameLayout verti, EventInfo event){
        String name=event.getName();
        double lat=event.getLat();
        double lon=event.getLon();
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


    /*private void loadSignedUpEvents(){
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        usersRef.child(Username).child("SignedUp").addListenerForSingleValueEvent(new ValueEventListener() {
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
    }*/




    private void loadAllEvents(){
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot eventsSnapshot = userSnapshot.child("Created Events");
                    for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                        EventInfo event = eventSnapshot.getValue(EventInfo.class);
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
        double lat=event.getLat();
        double lon=event.getLon();
        String createdBy=event.getCreatedBy();
        String brief=event.getBriefDescription();
        String full=event.getFullDescription();

        // Add child views to the FrameLayout
        TextView textView1 = new TextView(this);
        textView1.setText(name); // Set text from event data
        TextView textView2 = new TextView(this);
        textView2.setText(String.valueOf(lat));
        TextView textView3 = new TextView(this);
        textView3.setText(String.valueOf(lon));
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