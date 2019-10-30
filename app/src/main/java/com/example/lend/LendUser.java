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
    private FusedLocationProviderClient fusedLocationClient;
    private String email, username, password;
    private double lat, lng;
    private HashMap<Integer, Booking> mLendedItemList;

    public LendUser(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    lat = 37.8;
                    lng = 122.2;
                } else {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                }
            }
        });
    }

    public FusedLocationProviderClient getFusedLocationClient() {
        return fusedLocationClient;
    }

    public void setFusedLocationClient(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient;
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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public HashMap<Integer, Booking> getmLendedItemList() {
        return mLendedItemList;
    }

    public void setmLendedItemList(HashMap<Integer, Booking> mLendedItemList) {
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
