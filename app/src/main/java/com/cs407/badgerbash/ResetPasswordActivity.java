package com.cs407.badgerbash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        EditText editTextUsername = (EditText) findViewById(R.id.newusernme);
        EditText editTextNewPassword = (EditText) findViewById(R.id.newpassword);
        EditText editTextConfirmNewPassword = (EditText) findViewById(R.id.confirmpasswordtext);
        Button buttonResetPassword = findViewById(R.id.createbutton);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String confirmNewPassword = editTextConfirmNewPassword.getText().toString();

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String storedUsername = sharedPreferences.getString("Username", "");

                if (!username.equals(storedUsername)) {
                    return;
                }
                if (!newPassword.equals(confirmNewPassword)) {
                    return;
                }
                sharedPreferences.edit().putString("Password", newPassword).apply();
                Intent intent = new Intent(ResetPasswordActivity.this, login.class);
                startActivity(intent);
            }
        });
    }
}