package com.example.kaithaangu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminAmountFragment extends Fragment implements Clicklistner {

    TextView Username;
    AppCompatEditText AmountField,description;
    AppCompatButton submitbtn;
    RecyclerView listView;
    AppCompatToggleButton toggle;
    RelativeLayout AddMoneyLayout;
    Context context;
    ProgressBar progressBar;

    String userID = "";
    int AmountInvested = 0;
    int withdraw = 0;



    private boolean isWithdraw = true;
    private boolean isSmooth = false;
    private boolean isClicked = false;

    private FirebaseFirestore db;


    View v;


    public AdminAmountFragment() {
        // Required empty public constructor
    }

    public static AdminAmountFragment newInstance() {
        AdminAmountFragment fragment = new AdminAmountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isWithdraw = false;
                    Log.d(DataClass.LOG_TAG, String.valueOf(compoundButton.isChecked()));
                }else{
                    isWithdraw = true;
                    Log.d(DataClass.LOG_TAG, String.valueOf(compoundButton.isChecked()));

                }
            }
        });

        Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isClicked){
                    listView.setVisibility(View.VISIBLE);
                    isClicked = false;
                }else{
                    listView.setVisibility(View.GONE);
                    isClicked =true;
                }

            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()){
                    progressBar.setVisibility(View.VISIBLE);
                    String msg = "";
                    if (isWithdraw){
                        msg = "You are going to withdraw the Amount. Are u sure want to Continue?";
                        AlertDialogue.showDialogueDetailed(context,msg,Username.getText().toString(),
                                AmountField.getText().toString(),listener);
                    }else{
                        // statement add
                        // individual adding
                        // global adding
                        msg = "You are going to Add the Amount. Are u sure want to Continue?";
                        AlertDialogue.showDialogueDetailed(context,msg,Username.getText().toString(),
                                AmountField.getText().toString(),listener);

                    }
                }


            }
        });

    }

    AlertDialogue.DialogListener listener = new AlertDialogue.DialogListener() {
        @Override
        public void alertDialogAction(String action) {
            statementAdding();
        }
    };

    AlertDialogue.DialogListener listener2 = new AlertDialogue.DialogListener() {
        @Override
        public void alertDialogAction(String action) {

        }
    };

    private void individualAdding(){
        DocumentReference df2 = db.collection("users")
                .document(userID);
        int amount = 0;
        if (isWithdraw) {
            amount = withdraw + Integer.parseInt(AmountField.getText().toString());
            df2.update("TotalAmountWithdraw", amount)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            globalAdding();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    clearFields();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            amount = AmountInvested + Integer.parseInt(AmountField.getText().toString());
            df2.update("TotalAmountInvested", amount)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            globalAdding();

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
    }

    private void globalAdding(){
        DocumentReference df3 = db.collection("TotalAmount")
                .document("admin");
        int g_Amount = 0;

        if (isWithdraw)
            g_Amount = DataClass.CompanyAmount - Integer.parseInt(AmountField.getText().toString());
        else
            g_Amount = DataClass.CompanyAmount + Integer.parseInt(AmountField.getText().toString());

        df3.update("amount",g_Amount).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Registered SuccessFully", Toast.LENGTH_SHORT).show();
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


    private void statementAdding(){
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference df = db.collection("statements").document();
        Map<String,Object> map = new HashMap<>();
        map.put("UserName",Username.getText().toString());
        map.put("UserID",userID);
        map.put("Amount",AmountField.getText().toString());
        map.put("Desc",description.getText().toString());
        map.put("date",currentDate());
        if (isWithdraw)
            map.put("isWithdraw", true);
        else
            map.put("isWithdraw", false);
        df.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
//                clearFields();
//                progressBar.setVisibility(View.GONE);
                individualAdding();

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

    SimpleDateFormat cut_off_sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    public long currentTime(){
        try {
            Date currentTime = cut_off_sdf.parse(cut_off_sdf.format(Calendar.getInstance().getTime()));
            return currentTime.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    private String currentDate(){
        try {
            return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    private boolean isValid(){
        boolean isvalid = true;

        if (TextUtils.isEmpty(Username.getText())){
            isvalid = false;
            Toast.makeText(context, "Name is Empty", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(AmountField.getText())){
            isvalid = false;
            Toast.makeText(context, "Amount is Empty", Toast.LENGTH_SHORT).show();
        }
        return isvalid;
    }

    private void clearFields(){
        Username.setText("");
        AmountField.setText("");
        description.setText("");
        progressBar.setVisibility(View.GONE);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_admin_amount, container, false);

        Username = v.findViewById(R.id.Username);
        AmountField = v.findViewById(R.id.AmountField);
        description = v.findViewById(R.id.description);
        submitbtn = v.findViewById(R.id.submitbtn);
        listView = v.findViewById(R.id.listView);
        toggle = v.findViewById(R.id.toggle);
        AddMoneyLayout = v.findViewById(R.id.AddMoneyLayout);
        progressBar = v.findViewById(R.id.progress);

        return v;
    }

    public void refreshPage(){
        if (isAdded()) {
            if (DataClass.documentClassList.size() > 0) {
                // AddMoneyLayout.setVisibility(View.VISIBLE);
                isSmooth = true;

                MyListAdapter adapter = new MyListAdapter(DataClass.documentClassList,this);
                listView.setLayoutManager(new LinearLayoutManager(context));
                listView.setAdapter(adapter);
            } else {
                AddMoneyLayout.setVisibility(View.GONE);
                isSmooth = false;
            }

        }
    }

    @Override
    public void itemClicked(int pos, String from) {
        Username.setText(DataClass.documentClassList.get(pos).getUserName());
        userID = DataClass.documentClassList.get(pos).getId();
        AmountInvested = DataClass.documentClassList.get(pos).getTotalAMountIntested();
        withdraw = DataClass.documentClassList.get(pos).getTotalAMountWithdraw();
        listView.setVisibility(View.GONE);
        isClicked =true;
    }

    public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{

        private final List<DocumentClass> listdata;
        public final Clicklistner list;

        public MyListAdapter(List<DocumentClass> listdata,Clicklistner clicklistner) {
            this.listdata = listdata;
            list = clicklistner;
        }

        @NonNull
        @Override
        public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.itemlist, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                holder.name.setText(listdata.get(holder.getAdapterPosition()).getUserName());
                if (position == (listdata.size()-1))
                    holder.v.setVisibility(View.GONE);
                else
                    holder.v.setVisibility(View.VISIBLE);


            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.itemClicked(position,"");
                }
            });
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private View v;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.label);
                v = itemView.findViewById(R.id.view);
            }
        }
    }
}