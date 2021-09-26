package com.example.kaithaangu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Admin extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    AppCompatButton logout;
    SharedData sharedData;

    AdminMemberFragment memberFragment;
    AdminAmountFragment amountFragment;

    private FirebaseFirestore db;
    ProgressBar progressBar;
    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        viewPager = findViewById(R.id.viewpager);
        progressBar = findViewById(R.id.progressBar);
        nodata = findViewById(R.id.nodata);

        memberFragment = AdminMemberFragment.newInstance();
        amountFragment = AdminAmountFragment.newInstance();
        db = FirebaseFirestore.getInstance();

        // setting up the adapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),1);

        // add the fragments
        viewPagerAdapter.add(memberFragment, "Add Member");
        viewPagerAdapter.add(amountFragment, "Add Payment");

        // Set the adapter
        viewPager.setAdapter(viewPagerAdapter);

        // The Page (fragment) titles will be displayed in the
        // tabLayout hence we need to  set the page viewer
        // we use the setupWithViewPager().
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        logout = findViewById(R.id.Logout);
        sharedData  =new SharedData(this);
        getData();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataClass.getInstance(sharedData).saveLoggedState(false);
                Toast.makeText(Admin.this,"Logout Successful",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admin.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(DataClass.LOG_TAG, "onPageScrolled" +String.valueOf(position));
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(DataClass.LOG_TAG, "onPageSelected" +String.valueOf(position));
                if (position == 1)
                    amountFragment.refreshPage();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getData() {

        db.collection("users")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        List<DocumentSnapshot> documentSnapshots =  queryDocumentSnapshots.getDocuments();
                        if (DataClass.documentClassList.size() != 0){
                            DataClass.documentClassList.clear();
                        }
                        for (int i=0;i<documentSnapshots.size();i++){
                            DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                            if (documentSnapshot.get("UserID") != null){
                                DocumentClass documentClass = new DocumentClass();
                                documentClass.setId(documentSnapshot.get("UserID").toString());
                                documentClass.setUserName(documentSnapshot.get("UserName").toString());
                                documentClass.setMail(documentSnapshot.get("UserEmail").toString());
                                documentClass.setPhone(documentSnapshot.get("UserNumber").toString());
                                documentClass.setIsUser(documentSnapshot.get("IsUser").toString());
                                DataClass.documentClassList.add(documentClass);
                            }
                        }
                        Log.d(DataClass.LOG_TAG, String.valueOf(DataClass.documentClassList.size()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DataClass.LOG_TAG, "failed");
                        progressBar.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        nodata.setText(e.getMessage());
                    }
                });

//        db.collection("TotalAmount").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                //Log.d(DataClass.LOG_TAG, document.getId() + " => " + document.getData());
//                                Log.d(DataClass.LOG_TAG,  document.getString("amount"));
//                            }
//                        } else {
//                            Log.w(DataClass.LOG_TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });

    }
}