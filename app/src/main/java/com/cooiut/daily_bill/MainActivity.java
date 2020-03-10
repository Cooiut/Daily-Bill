/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Bills> spend, income;
    private DatabaseReference myRefSpend, myRefIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRefSpend = FirebaseDatabase.getInstance().getReference("spend");
        myRefIncome = FirebaseDatabase.getInstance().getReference("Income");
        spend = new ArrayList<Bills>();
        income = new ArrayList<Bills>();

        myRefSpend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spend.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Bills s = ds.getValue(Bills.class);
                    spend.add(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefIncome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                income.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Bills s = ds.getValue(Bills.class);
                    income.add(s);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose a category to add a bill");
                String[] animals = {"Income", "Spend"};
                builder.setItems(animals, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent;
                        Bundle bundle;
                        switch (which) {
                            case 0:
                                intent = new Intent(MainActivity.this, Income.class);
                                bundle = new Bundle();
                                bundle.putParcelableArrayList("income", income);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(MainActivity.this, Spend.class);
                                bundle = new Bundle();
                                bundle.putParcelableArrayList("spend", spend);
                                intent.putExtras(bundle);
                                startActivity(intent);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        FloatingActionButton fab2 = findViewById(R.id.bill_history);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BillHistory.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("income", income);
                bundle.putParcelableArrayList("spend", spend);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}
