/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Analysis extends AppCompatActivity {

    private ArrayList<Bills> spend, income;

    private class Date {
        private int month;
        private int year;

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

        ArrayList<String> monthYearList = getMonthYearList();

        Log.d("MonthYearList", monthYearList.toString());
    }

    private ArrayList<String> getMonthYearList() {
        ArrayList<Bills> allTransactions = new ArrayList<>(spend);
        allTransactions.addAll(income);
        Collections.sort(allTransactions, new Comparator<Bills>() {
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
        });

        ArrayList<Date> uniqueMonthYear = new ArrayList<>();
        for (Bills b : allTransactions) {
            if (!uniqueMonthYear.contains(new Date(b))) {
                uniqueMonthYear.add(new Date(b));
            }
        }

        ArrayList<String> res = new ArrayList<>();
        for (Date d : uniqueMonthYear) {
            res.add(d.toString());
        }

        return res;
    }
}
