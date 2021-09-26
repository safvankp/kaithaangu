package com.example.kaithaangu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    AppCompatButton logout;
    SharedData sharedData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.Logout);
        sharedData  =new SharedData(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataClass.getInstance(sharedData).saveLoggedState(false);
                Toast.makeText(ProfileActivity.this,"Logout Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}