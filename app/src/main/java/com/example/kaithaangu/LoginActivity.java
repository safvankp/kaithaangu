package com.example.kaithaangu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    AppCompatEditText email,pass;
    AppCompatButton signIn;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private SharedData sharedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailField);
        pass = findViewById(R.id.passField);
        signIn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progress);

        sharedData = new SharedData(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                sign(email.getText().toString(),pass.getText().toString());
                //getData();
            }
        });
    }

    private void getData() {

        db.collection("TotalAmount").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(DataClass.LOG_TAG, document.getId() + " => " + document.getData());
                                Log.d(DataClass.LOG_TAG,  document.getString("amount"));
                            }
                        } else {
                        Log.w(DataClass.LOG_TAG, "Error getting documents.", task.getException());
                    }
                    }
                });

    }


    private void sign(String email,String passcode){

        mAuth.signInWithEmailAndPassword(email, passcode)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);
                            Log.d(DataClass.LOG_TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            DocumentReference df = db.collection("users").document(user.getUid());
                            isAdminFlow(df);
                            //updateUI(user);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.d(DataClass.LOG_TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void isAdminFlow(DocumentReference df) {
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                DataClass.getInstance(sharedData).saveLoggedState(true);
                if (documentSnapshot.get("isAdmin") != null){
                    Log.d(DataClass.LOG_TAG,"isadmin");
                    DataClass.getInstance(sharedData).saveLastVisitedScreen(DataClass.Admin);
                    setResult(1);
                    finish();
                }else{
                    DataClass.getInstance(sharedData).saveLastVisitedScreen(DataClass.Profile);
                    setResult(2);
                    finish();
                }
            }
        });
    }
}