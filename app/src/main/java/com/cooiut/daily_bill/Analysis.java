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

public class Analysis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        Toolbar toolbarAnalysis = findViewById(R.id.toolbarAnalysis);
        setSupportActionBar(toolbarAnalysis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
