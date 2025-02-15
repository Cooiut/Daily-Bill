/*
 * Copyright (c) 2020. Cooiut & Jason
 * All right reserved.
 * This code is for UCI I&CS 45J project use only,
 * Please do not copy or duplicate.
 */

package com.cooiut.daily_bill;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Bills implements Parcelable {
    public static final Creator<Bills> CREATOR = new Creator<Bills>() {
        @Override
        public Bills createFromParcel(Parcel in) {
            return new Bills(in);
        }

        @Override
        public Bills[] newArray(int size) {
            return new Bills[size];
        }
    };
    private String item, category, key, description;
    private double quantity, cost;
    private int year, month, day;

    public Bills() {
    }

    public Bills(String key, String category, String item, double cost,
                 String description, int year, int month, int day) {
        this.key = key;
        this.category = category;
        this.item = item;
        this.quantity = 1;
        this.cost = cost;
        this.description = description;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Bills(String key, String category, String item, double quantity, double cost,
                 String description, int year, int month, int day) {
        this.key = key;
        this.category = category;
        this.item = item;
        this.quantity = quantity;
        this.cost = cost;
        this.description = description;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private Bills(Parcel in) {
        item = in.readString();
        category = in.readString();
        key = in.readString();
        description = in.readString();
        quantity = in.readDouble();
        cost = in.readDouble();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(item);
        dest.writeString(category);
        dest.writeString(key);
        dest.writeString(description);
        dest.writeDouble(quantity);
        dest.writeDouble(cost);
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item: " + item + "\n\n" +
                "Category: " + category + "\n\n" +
                "Cost: $" + cost + ", Quantity: " + quantity + "\n\n" +
                "Description: " + description + "\n\n" +
                "Date: " + day + "/" + month + "/" + year;
    }
}
