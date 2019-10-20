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
    public String starting_date;

    public int getBooking_number() {
        return booking_number;
    }

    public void setBooking_number(int booking_number) {
        this.booking_number = booking_number;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(String starting_date) {
        this.starting_date = starting_date;
    }

    public String getEnding_date() {
        return ending_date;
    }

    public void setEnding_date(String ending_date) {
        this.ending_date = ending_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String ending_date;
    public int price;

    public Place location;
    public String photoURL;
    public String category;
    public HashMap<Integer, Booking> bookingList;

    public Item() {

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


/*    public CalendarView getItem_calendar() {
        return item_calendar;
    }

    public void setItem_calendar(CalendarView item_calendar) {
        this.item_calendar = item_calendar;
    }

 */

    public Place getLocation() {
        return location;
    }

    public void setLocation(Place location) {
        this.location = location;
    }

}
