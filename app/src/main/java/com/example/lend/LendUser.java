package com.example.lend;

import android.content.Context;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class LendUser {
    public String id, username, profileImageURL, city;
    public String latLocation;
    public String longLocation;
    public int numberOfBookings, numReviews, rating, yearJoined;
    public HashMap<String, String> mLendedItemList, mBorrowedItemList;
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

    public void setYearJoined(int yearJoined) {
        this.yearJoined = yearJoined;
    }

    public LendUser() {

    }

    public LendUser(Context context) {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getNumReviews() {
        return numReviews;
    }

    public void setNumReviews(int numReviews) {
        this.numReviews = numReviews;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}