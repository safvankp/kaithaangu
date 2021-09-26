package com.example.kaithaangu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    AppCompatButton login;
    SharedData sharedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.loginBtn);
        sharedData = new SharedData(this);

        if (DataClass.getInstance(sharedData).getLoggedStatus()){
            login.setVisibility(View.GONE);
            if (DataClass.getInstance(sharedData).getLastVisitedScreen().equals(DataClass.Admin)){
                screeNavigation(1);
            }else if(DataClass.getInstance(sharedData).getLastVisitedScreen().equals(DataClass.Profile)){
                screeNavigation(2);
            }else{
                login.setVisibility(View.VISIBLE);
            }
        }else{
            login.setVisibility(View.VISIBLE);
        }



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivityForResult(intent,1);
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
          screeNavigation(1);
        }else if(resultCode == 2){
            screeNavigation(2);
        }

    }

    private void screeNavigation(int i){
        switch (i){
            case 1:
                // adminPage
                Intent intent = new Intent(MainActivity.this,Admin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case 2:
                // user profile page
                Intent intent2 = new Intent(MainActivity.this,ProfileActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                break;
            default:
                finish();
        }
    }
}