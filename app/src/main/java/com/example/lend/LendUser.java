package com.example.lend;

import android.content.Context;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class LendUser {
    public String email, username, password;
    public String latLocation;
    public String longLocation;
    public String id;
    public HashMap<String, String> mLendedItemList;
    public HashMap<String, String> mBorrowedItemList;
    public int num_reviews = 0;
    public int rating = 0;
    public int yearJoined;
    public String description = "No Description";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYearJoined() {
        return yearJoined;
    }

    public void setYearJoined(int year_joined) {
        this.yearJoined = yearJoined;
    }

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
        return longLocation;
    }

    public void setLng(String lng) {
        this.longLocation = lng;
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

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public int getNum_reviews() {
        return num_reviews;
    }

    public void setNum_reviews(int num_reviews) {
        this.num_reviews = num_reviews;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}