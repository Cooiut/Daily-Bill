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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BillHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Bills> spend, income;
    private DatabaseReference myRefSpend, myRefIncome;
    private Spinner spinner_sort, spinner_sequence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);
        setTitle("Bill History");

        Bundle extras = getIntent().getExtras();
        income = extras.getParcelableArrayList("income");
        spend = extras.getParcelableArrayList("spend");
        myRefSpend = FirebaseDatabase.getInstance().getReference("spend");
        myRefIncome = FirebaseDatabase.getInstance().getReference("Income");

        spinner_sort = findViewById(R.id.spinner_sort);
        spinner_sequence = findViewById(R.id.spinner_sequence);
        spinner_sort.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // TODO: 3/10/2020
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_sequence.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    // TODO: 3/10/2020
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.buttonSpend:
                if (checked)
                    // TODO: 3/10/2020 Change recyclerView to spend
                    break;
            case R.id.buttonIncome:
                if (checked)
                    // TODO: 3/10/2020 Change recyclerView to income
                    break;
        }
    }

    // TODO: 3/10/2020 Create comparator for Bills
}
