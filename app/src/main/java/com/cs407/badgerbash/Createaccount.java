package com.cs407.badgerbash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Createaccount extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        EditText newUsername = (EditText) findViewById(R.id.newusernme);
        EditText newPassword = (EditText) findViewById(R.id.newpassword);

        Button createbutton = findViewById(R.id.createbutton);
        String Username = newUsername.getText().toString();
        String Password = newPassword.getText().toString();

        createbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                sharedPreferences.edit().putString("Username", Username).apply();
                sharedPreferences.edit().putString("Password", Password).apply();
                Intent intent = new Intent(Createaccount.this, login.class);
                startActivity(intent);
            }
        });
    }
}
