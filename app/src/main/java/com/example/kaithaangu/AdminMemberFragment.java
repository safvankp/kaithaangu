package com.example.kaithaangu;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class AdminMemberFragment extends Fragment {


    AppCompatEditText UserName,UserEmail, UserPass,UserNumber;
    AppCompatButton submitBtn;
    View v;
    private boolean isValid  = false;
    private Context context;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    ProgressBar progressBar;


    public AdminMemberFragment() {
        // Required empty public constructor
    }

    public static AdminMemberFragment newInstance() {
        AdminMemberFragment fragment = new AdminMemberFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidText())
                    addUserDetails();
            }
        });
    }

    private void addUserDetails(){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(UserEmail.getText().toString(),UserPass.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        DocumentReference df = db.collection("users")
                                .document(user.getUid());
                        Map<String,Object> map = new HashMap<>();
                        map.put("UserName",UserName.getText().toString());
                        map.put("UserEmail",UserEmail.getText().toString());
                        map.put("UserPassCode",UserPass.getText().toString());
                        map.put("UserID",user.getUid().toString());
                        map.put("UserNumber",UserNumber.getText().toString());
                        map.put("TotalAmountInvested",0);
                        map.put("TotalAmountWithdraw",0);
                        map.put("IsUser","1");
                         df.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 Toast.makeText(context, "Registered", Toast.LENGTH_SHORT).show();
                                 clearFields();
                                 progressBar.setVisibility(View.GONE);
                             }
                         }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                                 Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                 clearFields();
                                 progressBar.setVisibility(View.GONE);
                             }
                         });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                clearFields();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context  =context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_admin_member, container, false);
        UserName = (AppCompatEditText) v.findViewById(R.id.UserName);
        UserEmail =(AppCompatEditText)  v.findViewById(R.id.UserEmail);
        UserPass = (AppCompatEditText)  v.findViewById(R.id.UserPass);
        UserNumber =(AppCompatEditText)  v.findViewById(R.id.UserNumber);
        submitBtn =(AppCompatButton)  v.findViewById(R.id.submitBtn);

        progressBar = v.findViewById(R.id.progressBar);
        return v;
    }

    private boolean isValidText(){
        if (TextUtils.isEmpty(UserName.getText())) {
            isValid = false;
            Toast.makeText(context, "Name is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            isValid = true;
        }
        if (TextUtils.isEmpty(UserEmail.getText())) {
            isValid = false;
            Toast.makeText(context, "Email is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

            Pattern pat = Pattern.compile(emailRegex);
            if (UserEmail.getText() == null)
                return false;
            isValid =  pat.matcher(UserEmail.getText()).matches();
            if (!isValid)
                Toast.makeText(context, "Email Format is not Correct", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(UserPass.getText())) {
            isValid = false;
            Toast.makeText(context, "User Code is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            isValid = true;
        }
        if (TextUtils.isEmpty(UserNumber.getText())) {
            isValid = false;
            Toast.makeText(context, "Number is Empty", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            isValid = !TextUtils.isEmpty(UserNumber.getText()) &&
                    Patterns.PHONE.matcher(Objects.requireNonNull(UserNumber.getText())).matches() && UserNumber.getText().length() == 10 ;

            if (!isValid)
                Toast.makeText(context, "Number Format is not Correct", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }

    private void clearFields(){
        UserName.setText("");
        UserEmail.setText("");
        UserPass.setText("");
        UserNumber.setText("");
    }
}