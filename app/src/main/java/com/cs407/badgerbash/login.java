package com.cs407.badgerbash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        Button loginbutton = findViewById(R.id.button);
        Button forgotbutton = findViewById(R.id.button2);
        Button createbutton = findViewById(R.id.CreateButton);
        EditText usernameEditText = (EditText) findViewById(R.id.newusernme);
        EditText passwordEditText = (EditText) findViewById(R.id.newpassword);

        forgotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, Createaccount.class);
                startActivity(intent);
            }
        });

        forgotbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputUsername = usernameEditText.getText().toString();
                String inputPassword = passwordEditText.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String storedUsername = sharedPreferences.getString("Username", "");
                String storedPassword = sharedPreferences.getString("Password", "");
                if(inputUsername.equals(storedUsername) && inputPassword.equals(storedPassword)) {
                    // Passwords match, navigate to HomePageActivity
                    Intent intent = new Intent(login.this, Homepage.class);
                    startActivity(intent);
                }
            }
        });
    }
}