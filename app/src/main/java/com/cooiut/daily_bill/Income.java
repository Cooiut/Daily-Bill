/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Income extends AppCompatActivity {

    private ArrayList<Bills> income;
    private String category = "";

    private DatabaseReference myRefIncome;

    private String[] cateList = {"Salary", "Investment", "Miscellaneous"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        setTitle("Income");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            income = extras.getParcelableArrayList("income");
        }

        String userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace('.', '_');
        myRefIncome = FirebaseDatabase.getInstance().getReference(userKey).child("income");

        Toolbar toolbarIncome = findViewById(R.id.toolbarIncome);
        setSupportActionBar(toolbarIncome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        FloatingActionButton fab = findViewById(R.id.floatingActionButton1);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                add();
//                finish();
//            }
//        });

        Spinner spinner = findViewById(R.id.spinnerCategory1);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = cateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "Miscellaneous";
            }
        });
    }

    public void add() {
        EditText editTextName = findViewById(R.id.editTextName1);
        EditText editTextQuantity = findViewById(R.id.editTextQuantity1);
        EditText editTextDescription = findViewById(R.id.editTextDescription1);

        String item, key, description;
        double quantity;
        int year, month, day;

        item = editTextName.getText().toString();
        description = editTextDescription.getText().toString();
        if (editTextQuantity.getText().toString().matches(""))
            quantity = 0;
        else
            quantity = Double.parseDouble(editTextQuantity.getText().toString());

        DatePicker datePicker = findViewById(R.id.datePicker1);
        year = datePicker.getYear();
        month = datePicker.getMonth() + 1;
        day = datePicker.getDayOfMonth();

        key = myRefIncome.push().getKey();
        Bills bills = new Bills(key, category, item, quantity, description, year, month, day);
        income.add(bills);
        if (key != null) {
            myRefIncome.child(key).setValue(bills);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    public void done(MenuItem item) {
        add();
        finish();
    }
}
