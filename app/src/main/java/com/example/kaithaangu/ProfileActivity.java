package com.example.kaithaangu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    AppCompatButton logout;
    SharedData sharedData;
    private FirebaseFirestore db;

    ProgressBar progressBar;
    TextView nodata;
    RelativeLayout mainLayout;
    TextView name;
    TextView companyAmount;
    TextView iAmount;
    TextView wAmount;
    TextView netAmount;
    TextView statementText;
    TextView noStatements;
    RecyclerView statmentRecycler;

    DocumentClass documentClass;
    List<Statement> statements = new ArrayList<>();
    List<Statement> yourStatements = new ArrayList<>();
    boolean isselfStatement = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.Logout);
        sharedData  =new SharedData(this);
        db = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);
        nodata = findViewById(R.id.nodata);
        mainLayout = findViewById(R.id.mailLayout);
        name = findViewById(R.id.name);
        companyAmount = findViewById(R.id.companyAmount);
        iAmount = findViewById(R.id.iAmount);
        wAmount = findViewById(R.id.wAmount);
        netAmount = findViewById(R.id.netAmount);
        statmentRecycler = findViewById(R.id.statmentRecycler);
        statementText = findViewById(R.id.statementText);
        noStatements = findViewById(R.id.noStatements);

        documentClass = new DocumentClass();

        getData();
        progressBar.setVisibility(View.VISIBLE);

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

        statementText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isselfStatement){
                    isselfStatement = false;
                    statementText.setText("All Statements");
                    // all case
                    if (statements.size() == 0){
                        statmentRecycler.setVisibility(View.GONE);
                        noStatements.setVisibility(View.VISIBLE);
                    }else{
                        statmentRecycler.setVisibility(View.VISIBLE);
                        noStatements.setVisibility(View.GONE);
                        MyListAdapter adapter = new MyListAdapter(statements);
                        statmentRecycler.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                        statmentRecycler.setAdapter(adapter);
                    }


                }else{
                    isselfStatement = true;
                    statementText.setText("Your Statements");
                    // individual case
                    if (yourStatements.size() == 0){
                        statmentRecycler.setVisibility(View.GONE);
                        noStatements.setVisibility(View.VISIBLE);
                    }else{
                        statmentRecycler.setVisibility(View.VISIBLE);
                        noStatements.setVisibility(View.GONE);
                        MyListAdapter adapter = new MyListAdapter(yourStatements);
                        statmentRecycler.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                        statmentRecycler.setAdapter(adapter);
                    }
                }
            }
        });
    }

    public void getData() {
        mainLayout.setVisibility(View.VISIBLE);
        db.collection("users").document(DataClass.getInstance(sharedData).getUserId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                nodata.setVisibility(View.GONE);
                if (documentSnapshot.get("UserID") != null){
                    documentClass.setId(documentSnapshot.get("UserID").toString());
                    documentClass.setUserName(documentSnapshot.get("UserName").toString());
                    documentClass.setMail(documentSnapshot.get("UserEmail").toString());
                    documentClass.setPhone(documentSnapshot.get("UserNumber").toString());
                    documentClass.setIsUser(documentSnapshot.get("IsUser").toString());
                    documentClass.setTotalAMountInvested(Integer.parseInt(documentSnapshot.get("TotalAmountInvested").toString()));
                    documentClass.setTotalAMountWithdraw(Integer.parseInt(documentSnapshot.get("TotalAmountWithdraw").toString()));
                }else {
                    progressBar.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                    mainLayout.setVisibility(View.GONE);
                }

                name.setText(userNameCreation(documentClass.getUserName()));
                iAmount.setText(amountTextmMake(String.valueOf(documentClass.getTotalAMountIntested())));
                wAmount.setText(amountTextmMake(String.valueOf(documentClass.getTotalAMountWithdraw())));
                netAmount.setText(amountTextmMake(String.valueOf((documentClass.getTotalAMountIntested() - documentClass.getTotalAMountWithdraw()))));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);
                nodata.setText(e.getMessage());
            }
        });

        db.collection("TotalAmount")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documentSnapshots =  queryDocumentSnapshots.getDocuments();
                for (int i=0;i<documentSnapshots.size();i++){
                    DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                    DataClass.CompanyAmount = Integer.parseInt(documentSnapshot.get("amount").toString());
                }
                companyAmount.setText(amountTextmMake(String.valueOf(DataClass.CompanyAmount)));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(DataClass.LOG_TAG, "failed");
                progressBar.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                mainLayout.setVisibility(View.GONE);
                nodata.setText(e.getMessage());
            }
        });
        db.collection("statements").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> documentSnapshots =  queryDocumentSnapshots.getDocuments();
                        for (int i=0;i<documentSnapshots.size();i++){
                            DocumentSnapshot documentSnapshot = documentSnapshots.get(i);
                            Statement statement = new Statement();
                            statement.setAmount(documentSnapshot.get("Amount").toString());
                            statement.setDate(documentSnapshot.get("date").toString());
                            statement.setDesc(documentSnapshot.get("Desc").toString());
                            statement.setWithdraw(Boolean.parseBoolean(documentSnapshot.get("isWithdraw").toString()));
                            statement.setName(documentSnapshot.get("UserName").toString());
                            statement.setUserID(documentSnapshot.get("UserID").toString());
                            statements.add(statement);
                            if (statement.getUserID().equals(DataClass.getInstance(sharedData).getUserId())){
                                yourStatements.add(statement);
                            }
                        }
                        if (isselfStatement){
                            if (yourStatements.size() == 0){
                                statmentRecycler.setVisibility(View.GONE);
                                noStatements.setVisibility(View.VISIBLE);
                            }else{
                                statmentRecycler.setVisibility(View.VISIBLE);
                                noStatements.setVisibility(View.GONE);
                                MyListAdapter adapter = new MyListAdapter(yourStatements);
                                statmentRecycler.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                                statmentRecycler.setAdapter(adapter);
                            }
                        }else{
                            if (statements.size() == 0){
                                statmentRecycler.setVisibility(View.GONE);
                                noStatements.setVisibility(View.VISIBLE);
                            }else{
                                statmentRecycler.setVisibility(View.VISIBLE);
                                noStatements.setVisibility(View.GONE);
                                MyListAdapter adapter = new MyListAdapter(statements);
                                statmentRecycler.setLayoutManager(new LinearLayoutManager(ProfileActivity.this));
                                statmentRecycler.setAdapter(adapter);
                            }
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                statmentRecycler.setVisibility(View.GONE);
            }
        });

    }

    private String amountTextmMake(String amount){
        String finalString = "Rs ";
        finalString = finalString + amount +"/-";
        return finalString;
    }

    private String userNameCreation(String name){
        String finalString = "Hii.!, ";
        finalString = finalString + name;
        return finalString;
    }

    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

        private final List<Statement> listdata;

        public MyListAdapter(List<Statement> listdata) {
            this.listdata = listdata;
        }

        @NonNull
        @Override
        public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.list_statement_item, parent, false);
            MyListAdapter.ViewHolder viewHolder = new MyListAdapter.ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            if (position == (listdata.size()-1))
                holder.v.setVisibility(View.GONE);
            else
                holder.v.setVisibility(View.VISIBLE);
            String finalString = "";
            if (listdata.get(position).getUserID().equals(DataClass.getInstance(sharedData).getUserId())){
                finalString = finalString + "You";
            }else{
                finalString = finalString + listdata.get(position).getName();
            }
            if (listdata.get(position).isWithdraw()){
                finalString = finalString +  " withdrew Rs "+listdata.get(position).getAmount()+"/- on "+listdata.get(position).getDate()+".";
                holder.desc.setTextColor(getResources().getColor(R.color.red));
            }else{
                finalString = finalString +  " paid Rs "+listdata.get(position).getAmount()+"/- on "+listdata.get(position).getDate()+".";
                holder.desc.setTextColor(getResources().getColor(R.color.green));
            }
            holder.desc.setText(finalString);
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView desc;
            private View v;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                desc = itemView.findViewById(R.id.desc);
                v = itemView.findViewById(R.id.view);
            }
        }
    }
}