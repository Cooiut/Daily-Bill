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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Spend extends AppCompatActivity {

    private ArrayList<Bills> spend;
    private Spinner spinner;
    private List<String> stringList;
    private ArrayAdapter<String> arrayAdapter;
    private String category = "";
    private DatePicker datePicker;

    private EditText editTextCost;
    private EditText editTextName;
    private EditText editTextQuantity;
    private EditText editTextDescription;

    private DatabaseReference myRefSpend;

    private String[] cateList = {"Food", "Transportation", "Housing", "Utilities", "Insurance",
            "Medical & Healthcare", "Saving, Investing, & Debt Payments", "Personal Spending",
            "Recreation & Entertainment", "Miscellaneous"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spend);
        setTitle("Income");

        Bundle extras = getIntent().getExtras();
        spend = extras.getParcelableArrayList("spend");

        myRefSpend = FirebaseDatabase.getInstance().getReference("spend");

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
//                myRefIncome.child("abc").setValue("abc");
                finish();
            }
        });



        spinner = findViewById(R.id.spinnerCategory);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = cateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void add() {
        editTextCost = findViewById(R.id.editTextCost);
        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextDescription = findViewById(R.id.editTextDescription);

        String item, key, description;
        double quantity, cost;
        int year, month, day;

        item = editTextName.getText().toString();
        description = editTextDescription.getText().toString();
        if (editTextQuantity.getText().toString().matches(""))
            quantity = 0;
        else
            quantity = Double.parseDouble(editTextQuantity.getText().toString());
        if (editTextCost.getText().toString().matches(""))
            cost = 0;
        else
            cost = Double.parseDouble(editTextCost.getText().toString());

        datePicker = findViewById(R.id.datePicker1);
        year = datePicker.getYear();
        month = datePicker.getMonth();
        day = datePicker.getDayOfMonth();

        key = myRefSpend.push().getKey();
        Bills bills = new Bills(key, category, item, quantity, cost, description, year, month, day);
        spend.add(bills);
        myRefSpend.child(key).setValue(bills);
    }
}
