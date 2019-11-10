
package com.example.lend;

import android.content.Context;
import android.location.Location;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class LendUser {
    public String email, username, password;
    public String latLocation;
    public String lngLocation;
    public String ID;
    public HashMap<String, String> mLendedItemList;
    public HashMap<String, String> mBorrowedItemList;

    public LendUser() {

    }

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

    public HashMap<String, String> getmBorrowedItemList() {
        return mBorrowedItemList;
    }

    public void setmBorrowedItemList(HashMap<String, String> mBorrowedItemList) {
        this.mBorrowedItemList = mBorrowedItemList;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}