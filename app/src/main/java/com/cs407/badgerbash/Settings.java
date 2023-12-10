package com.cs407.badgerbash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button backButton = findViewById(R.id.settingsBackButton);
        Button changePassword = findViewById(R.id.settingsChangePassword);
        TextView emailTextView = findViewById(R.id.settingsEmailTextView);
        sharedPreferences=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        String email=sharedPreferences.getString("Username","defaultUsername");
        emailTextView.setText("Email: " + email);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, ResetPassword.class);
                intent.putExtra("PREVIOUS_ACTIVITY", "Settings");
                startActivity(intent);
            }
        });

    }


}