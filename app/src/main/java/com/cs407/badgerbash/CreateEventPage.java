package com.cs407.badgerbash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventPage extends AppCompatActivity {

    private DatabaseReference rootRef;
    private EditText brief;
    private EditText full;
    private EditText name;

    private String username;
    private int eventCount;
    private String selectedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_page);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        Button home = findViewById(R.id.CRHomeButton);
        setUpHomeButton(home);
        Button btnPickTime = findViewById(R.id.TimeSetButton);

        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the time picker dialog
                showDatePickerDialog();
            }
        });


        Intent intent=getIntent();
        brief=findViewById(R.id.Brief);
        full=findViewById(R.id.Full);
        name=findViewById(R.id.EvtName);
        if (intent.hasExtra("selectedTime")){
            selectedTime=intent.getStringExtra("selectedTime");
        }
        if (intent.hasExtra("eventName")) {
            String eventName = intent.getStringExtra("eventName");
            name.setText(eventName);
        }
        if (intent.hasExtra("fullDescription")) {
            String fullDescription = intent.getStringExtra("fullDescription");
            full.setText(fullDescription);
        }
        if (intent.hasExtra("briefDescription")) {
            String briefDescription = intent.getStringExtra("briefDescription");
            brief.setText(briefDescription);
        }

        String email = sharedPreferences.getString("Username", "defaultUsername");
        int dotIndex = email.indexOf('.');
        username = email.substring(0, dotIndex);

        Button addLocationButton=findViewById(R.id.AddLocationButton);
        setAddLocationButton(addLocationButton);


        rootRef= FirebaseDatabase.getInstance().getReference().child("Users");
        Button createEvtButton=findViewById(R.id.CreateEvtButton);

        getNumEvents();
        createEvtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName=name.getText().toString();
                String createdBy=username;
                String briefDescription=brief.getText().toString();
                String fullDescription=full.getText().toString();
                String lat = getIntent().getStringExtra("lat");
                String lon = getIntent().getStringExtra("lon");

                EventInfo event=new EventInfo(eventName,lat,lon,createdBy,
                        briefDescription,fullDescription,selectedTime);

                rootRef.child(createdBy).child("Created Events").child("Event"+eventCount).setValue(event);

                Intent intent = new Intent(CreateEventPage.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(CreateEventPage.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Now show the time picker
                showTimePickerDialog(calendar);
            }
        }, year, month, day).show();
    }

    private void showTimePickerDialog(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(CreateEventPage.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                // Format the datetime and use it as needed
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                selectedTime = dateFormat.format(calendar.getTime());
                // You can display or store the selectedDateTime
            }
        }, hour, minute, DateFormat.is24HourFormat(this)).show();
    }

    private void getNumEvents(){
        DatabaseReference events = rootRef.child(username).child("Created Events");
        events.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventCount = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAddLocationButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventPage.this, MapSelect.class);
                String eventName=name.getText().toString();
                String briefDescription=brief.getText().toString();
                String fullDescription=full.getText().toString();

                intent.putExtra("eventName",eventName);
                intent.putExtra("briefDescription",briefDescription);
                intent.putExtra("fullDescription",fullDescription);
                intent.putExtra("selectedTime",selectedTime);

                startActivity(intent);
            }
        });
    }

    private void setUpHomeButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}