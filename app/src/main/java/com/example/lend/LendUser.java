package com.example.lend;

import android.content.Context;
import android.location.Location;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

public class LendUser {
    private String email, username, password;
    private String latLocation;
    private String lngLocation;
    private HashMap<String, String> mLendedItemList;

    public LendUser(Context context) {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLat() {
        return latLocation;
    }

    public void setLat(String lat) {
        this.latLocation = lat;
    }

    public String getLng() {
        return lngLocation;
    }

    public void setLng(String lng) {
        this.lngLocation = lng;
    }

    public HashMap<String, String> getmLendedItemList() {
        return mLendedItemList;
    }

    public void setmLendedItemList(HashMap<String, String> mLendedItemList) {
        this.mLendedItemList = mLendedItemList;
    }

    public HashMap<Integer, Booking> getmBorrowedItemList() {
        return mBorrowedItemList;
    }

    public void setmBorrowedItemList(HashMap<Integer, Booking> mBorrowedItemList) {
        this.mBorrowedItemList = mBorrowedItemList;
    }

    public int getNum_borrowed() {
        return num_borrowed;
    }

    public void setNum_borrowed(int num_borrowed) {
        this.num_borrowed = num_borrowed;
    }

    public int getNum_lended() {
        return num_lended;
    }

    public void setNum_lended(int num_lended) {
        this.num_lended = num_lended;
    }

    private HashMap<Integer, Booking> mBorrowedItemList;

    private int num_borrowed, num_lended;


}
