package com.cs407.badgerbash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateEventPage extends AppCompatActivity {

    private DatabaseReference rootRef;
    private Uri selectedImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_page);

        Button home = findViewById(R.id.CRHomeButton);
        setUpHomeButton(home);

        Button insertEventImgButton = findViewById(R.id.CRIEIButton);
        insertEventImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        EditText brief=findViewById(R.id.Brief);
        EditText full=findViewById(R.id.Full);
        EditText name=findViewById(R.id.EvtName);


        rootRef= FirebaseDatabase.getInstance().getReference();
        Button createEvtButton=findViewById(R.id.CreateEvtButton);
        createEvtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName=name.getText().toString();
                String briefDescription=brief.getText().toString();
                String fullDescription=full.getText().toString();
                EventInfo event=new EventInfo(eventName,)


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