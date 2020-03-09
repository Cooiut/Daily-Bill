package com.cooiut.daily_bill;

import android.os.Parcel;
import android.os.Parcelable;

public class Bills implements Parcelable {
    private String item, category, key, description;
    private double quantity, cost;
    private int year, month, day;

    public Bills() {
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

    protected Bills(Parcel in) {
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStore() {
        return category;
    }

    public void setStore(String store) {
        this.category = store;
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
}
