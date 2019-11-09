package com.example.lend;

import android.widget.CalendarView;

import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class Item {
    //keeps track of number of bookings made on this item

    public String ID;
    public String lender;
    public String itemName;
    public String itemDescription;
    public String price;
    public String booked;

    public String lat;
    public String lng;
    public String photoURL;
    public String category;
    public HashMap<String, String> bookingList;

    public Item() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBooked() {
        return booked;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public HashMap<String, String> getBookingList() {
        return bookingList;
    }

    public void setBookingList(HashMap<String, String> bookingList) {
        this.bookingList = bookingList;
    }
}
