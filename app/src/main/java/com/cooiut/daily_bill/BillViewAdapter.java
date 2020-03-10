/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BillViewAdapter extends RecyclerView.Adapter {
    private String bill;
    private ArrayList list;
    private Context context;
    private DatabaseReference myRef;

    public BillViewAdapter(Context context, ArrayList list, String bill) {
        this.context = context;
        this.bill = bill;
        this.list = list;
        myRef = FirebaseDatabase.getInstance().getReference(bill);
    }

    public void deleteItem(int position) {
        Bills b = (Bills) list.get(position);
        myRef.child(b.getKey()).removeValue();
        list.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_view, parent, false);
        return new BillViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BillViewHolder) holder).date.setText("" + ((Bills) list.get(position)).getMonth() + "/" + ((Bills) list.get(position)).getDay() + "/" + ((Bills) list.get(position)).getYear());
        ((BillViewHolder) holder).name.setText(((Bills) list.get(position)).getItem());
        ((BillViewHolder) holder).cost.setText("" + (((Bills) list.get(position)).getCost() * ((Bills) list.get(position)).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class BillViewHolder extends RecyclerView.ViewHolder {
        public TextView date, name, cost;
        public Button edit;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textViewNumber);
            name = itemView.findViewById(R.id.textViewItem);
            cost = itemView.findViewById(R.id.textViewQuantity);
            edit = itemView.findViewById(R.id.buttonEdit);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 3/10/2020
                }
            });
        }
    }
}
