package com.example.lend;

import android.widget.CalendarView;

import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class Item {
    //keeps track of number of bookings made on this item
    private int booking_number = 0;

    public FirebaseUser lender;
    public String itemName;
    public String itemDescription;
    public int price;
    public CalendarView item_calendar;
    public Place location;
    public String photoURL;
    public String category;
    public HashMap<Integer, Booking> bookingList;

    public void addBooking(Booking booking) {
        booking_number++;
        bookingList.put(booking_number, booking);
    }

    public Booking getBooking(int index) {
        return bookingList.get(index);
    }

    public HashMap<Integer, Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(HashMap<Integer, Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public String getItemCategory() {
        return category;
    }

    public void setItemCategory(String category) {
        this.category = category;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public FirebaseUser getLender() {
        return lender;
    }

    public void setLender(FirebaseUser lender) {
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public CalendarView getItem_calendar() {
        return item_calendar;
    }

    public void setItem_calendar(CalendarView item_calendar) {
        this.item_calendar = item_calendar;
    }

    public Place getLocation() {
        return location;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

}
