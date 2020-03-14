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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Bills> spend, income;
    private double balance, totalSpend, totalIncome;

    private TextView textViewBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        String userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.','_');

        // Setup Database
        DatabaseReference myRefSpend = FirebaseDatabase.getInstance().getReference(userKey).child("spend");
        DatabaseReference myRefIncome = FirebaseDatabase.getInstance().getReference(userKey).child("income");
        spend = new ArrayList<>();
        income = new ArrayList<>();

        textViewBalance = findViewById(R.id.textViewBalance);

        // Database Listener for spending
        myRefSpend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spend.clear();
                totalSpend = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Bills b = ds.getValue(Bills.class);
                    spend.add(b);
                    if (b != null) {
                        totalSpend += b.getQuantity() * b.getCost();
                    }
                }
                balance = totalIncome - totalSpend;
                textViewBalance.setText(String.format("%s%s", getString(R.string.balance), balance));
                updateChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Database Listener for income
        myRefIncome.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                income.clear();
                totalIncome = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Bills b = ds.getValue(Bills.class);
                    income.add(b);
                    if (b != null) {
                        totalIncome += b.getQuantity() * b.getCost();
                    }
                }
                balance = totalIncome - totalSpend;
                textViewBalance.setText(String.format("%s%s", getString(R.string.balance), balance));
                updateChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Fab for add new bill
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

        // Fab for changing to Bill history activity
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

    public void updateChart() {
        // Chart 1: Spending
        PieChart chartSpending = findViewById(R.id.chartSpending);
        List<PieEntry> entrySpending = new ArrayList<>();
        for (Bills b : spend) {
            entrySpending.add(new PieEntry((float) (b.getCost() * b.getQuantity()), b.getItem()));
        }
        PieDataSet setSpending = new PieDataSet(entrySpending, "");
        setSpending.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData dataSpending = new PieData(setSpending);
        chartSpending.setCenterText("Total Spending\n$" + totalSpend);
//        Description descriptionSpending = new Description();
//        descriptionSpending.setText("Total Spending: " + totalSpend);
//        descriptionSpending.setTextSize(13f);
//        descriptionSpending.setPosition(370f, 70f);
        chartSpending.setDescription(null);
        chartSpending.setHoleRadius(40f);
        chartSpending.setTransparentCircleRadius(45f);
        chartSpending.setData(dataSpending);
        chartSpending.invalidate();


        //Chart 2: Income
        PieChart chartIncome = findViewById(R.id.chartIncome);
        List<PieEntry> entryIncome = new ArrayList<>();
        for (Bills b : income) {
            entryIncome.add(new PieEntry((float) (b.getCost() * b.getQuantity()), b.getItem()));
        }
        PieDataSet setIncome = new PieDataSet(entryIncome, "");
        setIncome.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData dataIncome = new PieData(setIncome);
        chartIncome.setCenterText("Total Income\n$" + totalIncome);
//        Description descriptionIncome = new Description();
//        descriptionIncome.setText("Total Income: " + totalIncome);
//        descriptionIncome.setTextSize(13f);
//        descriptionIncome.setPosition(370f, 800f);
        chartIncome.setDescription(null);
        chartIncome.setHoleRadius(40f);
        chartIncome.setTransparentCircleRadius(45f);
        chartIncome.setData(dataIncome);
        chartIncome.invalidate();
    }

    public void analysis(View view) {
        Intent intent = new Intent(MainActivity.this, Analysis.class);
        Bundle b = new Bundle();
        b.putParcelableArrayList("spend", spend);
        b.putParcelableArrayList("income", income);
        intent.putExtras(b);
        startActivity(intent);
    }
}
