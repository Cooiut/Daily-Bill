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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Edit extends AppCompatActivity {

    private ArrayList<Bills> spend;
    private String category = "";
    Bills b;
    int position, spinnerIndex;
    String key;
    String type;
    private DatabaseReference myRefSpend;
    String[] cateList;
    ArrayAdapter<CharSequence> adapter;
    private String[] cateList1 = {"Food", "Transportation", "Housing", "Utilities", "Insurance",
            "Medical & Healthcare", "Saving, Investing, & Debt Payments", "Personal Spending",
            "Recreation & Entertainment", "Miscellaneous"};
    private String[] cateList2 = {"Salary", "Investment", "Miscellaneous"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setTitle("Edit Details");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            b = extras.getParcelable("Bill");
            position = extras.getInt("int");
            key = extras.getString("key");
            type = extras.getString("type");
            spend = extras.getParcelableArrayList("arrayList");
        }
        EditText editTextCost = findViewById(R.id.editTextCost1);
        EditText editTextName = findViewById(R.id.editTextName1);
        EditText editTextQuantity = findViewById(R.id.editTextQuantity1);
        EditText editTextDescription = findViewById(R.id.editTextDescription1);

        DatePicker datePicker = findViewById(R.id.datePicker1);
        datePicker.init(b.getYear(), b.getMonth() - 1, b.getDay(), null);

        editTextCost.setText(String.format("%s", b.getCost()));
        editTextName.setText(b.getItem());
        editTextDescription.setText(b.getDescription());
        editTextQuantity.setText(String.format("%s", b.getQuantity()));


        Button buttonEdit = findViewById(R.id.buttonUpdate);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                finish();
            }
        });


        myRefSpend = FirebaseDatabase.getInstance().getReference(type);

        Spinner spinner = findViewById(R.id.spinnerCategory1);
        List<String> list = new ArrayList<>();

        if (type.equals("spend")) {
            Collections.addAll(list, cateList1);
            spinnerIndex = list.indexOf(b.getCategory());
            cateList = cateList1;
            adapter = ArrayAdapter.createFromResource(this, R.array.spending, android.R.layout.simple_spinner_item);
        } else if (type.equals("income")) {
            Collections.addAll(list, cateList2);
            spinnerIndex = list.indexOf(b.getCategory());
            cateList = cateList2;
            adapter = ArrayAdapter.createFromResource(this, R.array.income, android.R.layout.simple_spinner_item);
        }
        spinner.setAdapter(adapter);
        spinner.setSelection(spinnerIndex);
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

    public void update() {
        EditText editTextCost = findViewById(R.id.editTextCost1);
        EditText editTextName = findViewById(R.id.editTextName1);
        EditText editTextQuantity = findViewById(R.id.editTextQuantity1);
        EditText editTextDescription = findViewById(R.id.editTextDescription1);

        String item, description;
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

        DatePicker datePicker = findViewById(R.id.datePicker1);
        year = datePicker.getYear();
        month = datePicker.getMonth() + 1;
        day = datePicker.getDayOfMonth();

//        key = myRefSpend.getKey();
        System.out.println(key);
        Bills bills = new Bills(key, category, item, quantity, cost, description, year, month, day);
        System.out.println(spend.get(position).toString() + "  123123\n\n");
        spend.set(position, bills);
        System.out.println(spend.get(position).toString() + "    asdasd\n\n");

        if (key != null) {
            myRefSpend.child(key).setValue(bills);
        }

    }
}
