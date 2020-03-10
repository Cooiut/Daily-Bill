/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SwipeToDelete extends ItemTouchHelper.SimpleCallback {
    private BillViewAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public SwipeToDelete(BillViewAdapter adapter, String bill) {
        super(0, ItemTouchHelper.LEFT);
        this.adapter = adapter;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(bill);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.deleteItem(position);
    }
}