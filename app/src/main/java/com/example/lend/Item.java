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

    public String lenderID;
    public String itemName;
    public String itemDescription;
    public String price;
    public String booked;

    public String latLocation;
    public String longLocation;
    public String photoURL;
    public String category;
    public HashMap<String, String> bookingList;

    public Item() {
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

//    public void addBooking(Booking booking) {
//        booking_number++;
//        bookingList.put(booking_number, booking);
//    }
//
//    public Booking getBooking(int index) {
//        return bookingList.get(index);
//    }

    public HashMap<String, String> getBookingList() {
        return bookingList;
    }

    public void setBookingList(HashMap<String, String> bookingList) {
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

    public String getLender() {
        return lenderID;
    }

    public void setLender(String lender) {
        this.lenderID = lender;
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

    public String getLatLocation() {
        return latLocation;
    }

    public void setLatLocation(String latLocation) {
        this.latLocation = latLocation;
    }

    public String getLongLocation() {
        return longLocation;
    }

    public void setLongLocation(String longLocation) {
        this.longLocation = longLocation;
    }
}
