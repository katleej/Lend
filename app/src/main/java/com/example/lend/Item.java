package com.example.lend;

import android.widget.CalendarView;

import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class Item {
    //keeps track of number of bookings made on this item
    public int booking_number = 0;

    public FirebaseUser lender;
    public String itemName;
    public String itemDescription;
    public int price;
    public boolean booked;

    public Place location;
    public String photoURL;
    public String category;
    public HashMap<Integer, Booking> bookingList;

    public Item() {
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    public int getBooking_number() {
        return booking_number;
    }

    public void setBooking_number(int booking_number) {
        this.booking_number = booking_number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public Place getLocation() {
        return location;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

}
