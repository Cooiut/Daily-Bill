/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Analysis extends AppCompatActivity {

    private ArrayList<Bills> spend, income;


    private Spinner spinner;
    private ArrayList<String> monthYearList;
    private ArrayList<Date> uniqueDate;
    private Date spinnerChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        Toolbar toolbarAnalysis = findViewById(R.id.toolbarAnalysis);
        setSupportActionBar(toolbarAnalysis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            income = extras.getParcelableArrayList("income");
            spend = extras.getParcelableArrayList("spend");
        }

        monthYearList = getMonthYearList();

    }

    private ArrayList<String> getMonthYearList() {
        ArrayList<Bills> allTransactions = new ArrayList<>(spend);
        allTransactions.addAll(income);
        Collections.sort(allTransactions, new Comparator<Bills>() {
            @Override
            public int compare(Bills o1, Bills o2) {
                if (o1.getYear() < o2.getYear())
                    return 1;
                else if (o1.getYear() > o2.getYear())
                    return -1;
                else {
                    if (o1.getMonth() < o2.getMonth())
                        return 1;
                    else if (o1.getMonth() > o2.getMonth())
                        return -1;
                    else {
                        return Integer.compare(o1.getDay(), o2.getDay());
                    }
                }
            }
        });

        ArrayList<Date> uniqueMonthYear = new ArrayList<>();
        for (Bills b : allTransactions) {
            if (!uniqueMonthYear.contains(new Date(b))) {
                uniqueMonthYear.add(new Date(b));
            }
        }
        uniqueDate = uniqueMonthYear;

        ArrayList<String> res = new ArrayList<>();
        for (Date d : uniqueMonthYear) {
            res.add(d.toString());
        }

        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_analysis, menu);
        MenuItem item = menu.findItem(R.id.spinner_analysis);
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monthYearList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerChoice = uniqueDate.get(position);
                drawTransaction();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerChoice = uniqueDate.get(0);
                drawTransaction();
            }
        });
        return true;
    }

    public void drawTransaction() {
        int[] monthDay = {0, 31, (spinnerChoice.year % 4 == 0 && spinnerChoice.year % 100 != 0 || spinnerChoice.year % 400 == 0) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        double[] incomeAmount = new double[monthDay[spinnerChoice.month] + 1];
        for (Bills b : income) {
            if (b.getYear() == spinnerChoice.year && b.getMonth() == spinnerChoice.month) {
                incomeAmount[b.getDay()] += b.getQuantity() * b.getCost();
            }
        }

        ArrayList<Entry> entryListIncome = new ArrayList<>();
        for (int i = 1; i < incomeAmount.length; i++) {
            entryListIncome.add(new Entry(i, (float) incomeAmount[i]));
        }

        LineDataSet dataSetIncome = new LineDataSet(entryListIncome, "Income");
        dataSetIncome.setColor(Color.BLUE);

        LineData dataIncome = new LineData(dataSetIncome);

        double[] spendAmount = new double[monthDay[spinnerChoice.month] + 1];
        for (Bills b : spend) {
            if (b.getYear() == spinnerChoice.year && b.getMonth() == spinnerChoice.month) {
                spendAmount[b.getDay()] += b.getQuantity() * b.getCost();
            }
        }


        ArrayList<Entry> entryListSpend = new ArrayList<>();
        for (int i = 1; i < incomeAmount.length; i++) {
            entryListSpend.add(new Entry(i, (float) spendAmount[i]));
        }

        LineDataSet dataSetSpend = new LineDataSet(entryListSpend, "Spend");
        dataSetSpend.setColor(Color.RED);

        LineData ldata = new LineData();
        ldata.addDataSet(dataSetIncome);
        ldata.addDataSet(dataSetSpend);

        ((LineChart) findViewById(R.id.chartOverall)).setData(ldata);
        findViewById(R.id.chartOverall).invalidate();
    }

    class Date {
        public int month;
        public int year;

        public Date(Bills b) {
            this.month = b.getMonth();
            this.year = b.getYear();
        }

        @Override
        public String toString() {
            return "" + month + "-" + year;
        }

        @Override
        public boolean equals(Object d) {
            return getClass() == d.getClass() && month == ((Date) d).month && year == ((Date) d).year;
        }
    }
}
