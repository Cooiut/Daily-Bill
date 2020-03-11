/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BillHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Bills> spend, income;
    private DatabaseReference myRefSpend, myRefIncome;
    private Spinner spinner_sort, spinner_sequence;
    private TextView textView;

    private int selectedButton, selectedSec, selectedSort;

    private ItemTouchHelper itemTouchHelper;

    private double totalSpend, totalIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);
        setTitle("Bill History");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            income = extras.getParcelableArrayList("income");
            spend = extras.getParcelableArrayList("spend");
        }
        myRefSpend = FirebaseDatabase.getInstance().getReference("spend");
        myRefIncome = FirebaseDatabase.getInstance().getReference("income");

        myRefSpend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                spend.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Bills s = ds.getValue(Bills.class);
                    spend.add(s);
                    sort();
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
                    sort();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerView = findViewById(R.id.recyclerViewList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        textView = findViewById(R.id.textView);

        adapter = new BillViewAdapter(BillHistory.this, spend, "spend");
        recyclerView.setAdapter(adapter);
        itemTouchHelper = new ItemTouchHelper(new SwipeToDelete((BillViewAdapter) adapter, "spend"));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        spinner_sort = findViewById(R.id.spinner_sort);
        spinner_sequence = findViewById(R.id.spinner_sequence);
        spinner_sort.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        selectedSort = 0;
                        break;
                    case 1:
                        selectedSort = 1;
                        break;
                }
                sort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_sequence.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                    case 2:
                        selectedSec = 2;
                        break;
                    case 1:
                        selectedSec = 1;
                        break;
                    case 3:
                        selectedSec = 3;
                        break;
                }
                sort();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sort();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.buttonSpend:
                if (checked)
                    selectedButton = 0;
                break;
            case R.id.buttonIncome:
                if (checked)
                    selectedButton = 1;
                break;
        }
        sort();
    }

    public void sort() {
        Comparator<Bills> dateComparator = new Comparator<Bills>() {
            @Override
            public int compare(Bills o1, Bills o2) {
                if (o1.getYear() < o2.getYear())
                    return -1;
                else if (o1.getYear() > o2.getYear())
                    return 1;
                else {
                    if (o1.getMonth() < o2.getMonth())
                        return -1;
                    else if (o1.getMonth() > o2.getMonth())
                        return 1;
                    else {
                        return Integer.compare(o1.getDay(), o2.getDay());
                    }
                }
            }
        };

        Comparator<Bills> amountComparator = new Comparator<Bills>() {
            @Override
            public int compare(Bills o1, Bills o2) {
                double amount1 = o1.getCost() * o1.getQuantity();
                double amount2 = o2.getCost() * o2.getQuantity();
                if (amount1 < amount2)
                    return -1;
                else if (amount1 > amount2)
                    return 1;
                return 0;
            }
        };

        Comparator<Bills> nameComparator = new Comparator<Bills>() {
            @Override
            public int compare(Bills o1, Bills o2) {
                return o1.getItem().compareToIgnoreCase(o2.getItem());
            }
        };

        totalSpend = 0;
        for (Bills b : spend) {
            totalSpend += b.getQuantity() * b.getCost();
        }
        totalIncome = 0;
        for (Bills b : income) {
            totalIncome += b.getQuantity() * b.getCost();
        }


        switch (selectedButton) {
            case 0:
                switch (selectedSec) {
                    case 1:
                        Collections.sort(spend, dateComparator);
                        break;
                    case 2:
                        Collections.sort(spend, amountComparator);
                        break;
                    case 3:
                        Collections.sort(spend, nameComparator);
                        break;
                }
                if (selectedSort == 0)
                    Collections.reverse(spend);
                itemTouchHelper.attachToRecyclerView(null);
                adapter = new BillViewAdapter(BillHistory.this, spend, "spend");
                recyclerView.setAdapter(adapter);
                itemTouchHelper = new ItemTouchHelper(new SwipeToDelete((BillViewAdapter) adapter, "spend"));
                itemTouchHelper.attachToRecyclerView(recyclerView);

                textView.setText(String.format("%s%s", getString(R.string.totalSpend), totalSpend));

                break;
            case 1:
                switch (selectedSec) {
                    case 1:
                        Collections.sort(income, dateComparator);
                        break;
                    case 2:
                        Collections.sort(income, amountComparator);
                        break;
                    case 3:
                        Collections.sort(income, nameComparator);
                        break;
                }
                if (selectedSort == 0)
                    Collections.reverse(income);
                itemTouchHelper.attachToRecyclerView(null);
                adapter = new BillViewAdapter(BillHistory.this, income, "income");
                recyclerView.setAdapter(adapter);
                itemTouchHelper = new ItemTouchHelper(new SwipeToDelete((BillViewAdapter) adapter, "income"));
                itemTouchHelper.attachToRecyclerView(recyclerView);

                textView.setText(String.format("%s%s", getString(R.string.totalIncome), totalIncome));

                break;
        }

        // TODO: 3/10/2020
    }
}
