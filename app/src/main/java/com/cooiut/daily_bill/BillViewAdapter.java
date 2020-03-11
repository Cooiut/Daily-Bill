/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BillViewAdapter extends RecyclerView.Adapter {
    private ArrayList list;
    private Context context;
    private DatabaseReference myRef;
    private String bill;

    BillViewAdapter(Context context, ArrayList list, String bill) {
        this.context = context;
        this.list = list;
        this.bill = bill;
        System.out.println(bill + "bbbbbbbbbbbbbbbbbbbbbb");
        myRef = FirebaseDatabase.getInstance().getReference(bill);
    }

    void deleteItem(int position) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BillViewHolder) holder).date.setText("" + ((Bills) list.get(position)).getMonth() + "/" + ((Bills) list.get(position)).getDay() + "/" + ((Bills) list.get(position)).getYear());
        ((BillViewHolder) holder).name.setText(((Bills) list.get(position)).getItem());
        ((BillViewHolder) holder).cost.setText("$" + (((Bills) list.get(position)).getCost() * ((Bills) list.get(position)).getQuantity()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class BillViewHolder extends RecyclerView.ViewHolder {
        TextView date, name, cost;
        Button edit;
        int type;

        BillViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textViewNumber);
            name = itemView.findViewById(R.id.textViewItem);
            cost = itemView.findViewById(R.id.textViewQuantity);
            edit = itemView.findViewById(R.id.buttonEdit);


            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    System.out.println(position + "zzzzzzzzzzzzzzzzzzzz");
                    Bills b = (Bills) list.get(position);
                    System.out.println(b.getDay());
                    Intent intent = new Intent(context, Edit.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Bill", b);
                    bundle.putInt("int", position);
                    bundle.putString("type", bill);
                    bundle.putString("key", b.getKey());
                    bundle.putParcelableArrayList("arrayList", list);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    notifyDataSetChanged();
                    // TODO: 3/10/2020  
                }   
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Detail");
                    int position = getAdapterPosition();
                    Bills b = (Bills) list.get(position);

                    builder.setMessage(b.toString());
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }
}
