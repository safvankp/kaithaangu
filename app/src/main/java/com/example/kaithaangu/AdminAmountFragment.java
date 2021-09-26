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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class AdminAmountFragment extends Fragment implements Clicklistner {

    TextView Username;
    AppCompatEditText AmountField,description;
    AppCompatButton submitbtn;
    RecyclerView listView;
    AppCompatToggleButton toggle;
    RelativeLayout AddMoneyLayout;
    Context context;



    private boolean isWithdraw = false;
    private boolean isSmooth = false;
    private boolean isClicked = false;


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

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    isWithdraw = true;
                    Log.d(DataClass.LOG_TAG, String.valueOf(compoundButton.isChecked()));
                }else{
                    isWithdraw = false;
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
                if (isWithdraw){

                }else{

                }
            }
        });

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
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.label);
            }
        }
    }
}