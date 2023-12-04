package com.cs407.badgerbash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateEventPage extends AppCompatActivity {

    private DatabaseReference rootRef;
    private Uri selectedImg;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_page);

        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);

        Button home = findViewById(R.id.CRHomeButton);
        setUpHomeButton(home);

        Button insertEventImgButton = findViewById(R.id.CRIEIButton);
//        insertEventImgButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 1);
//            }
//        });

        EditText brief=findViewById(R.id.Brief);
        EditText full=findViewById(R.id.Full);
        EditText name=findViewById(R.id.EvtName);

        Button addLocationButton=findViewById(R.id.AddLocationButton);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateEventPage.this, MapSelect.class);
                startActivity(intent);
            }
        });

        rootRef= FirebaseDatabase.getInstance().getReference().child("Users");
        Button createEvtButton=findViewById(R.id.CreateEvtButton);
        createEvtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName=name.getText().toString();
                String email=sharedPreferences.getString("Username","defaultUsername");
                int dotIndex = email.indexOf('.');
                String username = email.substring(0, dotIndex);
                String createdBy=username;
                String briefDescription=brief.getText().toString();
                String fullDescription=full.getText().toString();
                double latitude = getIntent().getDoubleExtra("lat", 0); // Default value as 0
                double longitude = getIntent().getDoubleExtra("lon", 0); // Default value as 0

                EventInfo event=new EventInfo(eventName,latitude,longitude,createdBy,
                        briefDescription,fullDescription);
                rootRef.child(createdBy).child("Created Events").setValue(event);

                Intent intent = new Intent(CreateEventPage.this, MainActivity.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1 && data != null) {
            selectedImg = data.getData();
            ImageView imageView = findViewById(R.id.EventImage);
            imageView.setImageURI(selectedImg);
        }
    }

}