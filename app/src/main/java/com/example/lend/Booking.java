package com.example.lend;

import com.google.firebase.auth.FirebaseUser;

public class Booking {
    public String ID;
    public String itemID;
    public String borrowerID;
    public String lenderID;
    public String active;
    public String daysBooked;
    public String userReturned;

    public Booking() {

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getItem() {
        return itemID;
    }

    public void setItem(String item) {
        this.itemID = item;
    }

    public String getBorrower() {
        return borrowerID;
    }

    public void setBorrower(String borrower) {
        this.borrowerID = borrower;
    }

    public String getLenderID() {
        return lenderID;
    }

    public void setLenderID(String lenderID) {
        this.lenderID = lenderID;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDaysBooked() {
        return daysBooked;
    }

    public void setDaysBooked(String daysBooked) {
        this.daysBooked = daysBooked;
    }

    public String getUserReturned() {
        return userReturned;
    }

    public void setUserReturned(String userReturned) {
        this.userReturned = userReturned;
    }
}
