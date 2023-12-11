package com.cs407.badgerbash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class EventDescriptionPage extends AppCompatActivity {

    private DatabaseReference rootRef;
    EventInfo event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description_page);

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String lat=intent.getStringExtra("lat");
        String lon=intent.getStringExtra("lon");
        String createdBy=intent.getStringExtra("createdBy");
        String brief=intent.getStringExtra("brief");
        String full=intent.getStringExtra("full");
        String selectedTime=intent.getStringExtra("selectedTime");
        event=new EventInfo(name,lat,lon,createdBy,brief,full,selectedTime);

        Button homeButton=findViewById(R.id.HomeButton);
        setUpHomeButton(homeButton);
        Button signUpButton=findViewById(R.id.SignUpButton);
        setUpSignUpButton(signUpButton);
        Button locationButton=findViewById(R.id.LocationButton);
        setUpLocationButton(locationButton);

        TextView nameText=findViewById(R.id.NameText);
        nameText.setText("Event Name:\n"+name);

        TextView fullDescription=findViewById(R.id.FullDescrip);
        fullDescription.setText("Event Description:\n"+full);

        TextView timeTxt=findViewById(R.id.TimeTxt);
        timeTxt.setText("Event Time: \n"+selectedTime);
    }

    private void setUpLocationButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDescriptionPage.this, Map.class);
                intent.putExtra("name",event.getName());
                intent.putExtra("lat",event.getLat());
                intent.putExtra("lon", event.getLon());
                intent.putExtra("createdBy",event.getCreatedBy());
                intent.putExtra("brief",event.getBriefDescription());
                intent.putExtra("full",event.getFullDescription());
                intent.putExtra("selectedTime",event.getSelectedTime());
                startActivity(intent);
            }
        });
    }
    private void setUpHomeButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDescriptionPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpSignUpButton(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDescriptionPage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}