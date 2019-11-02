package com.example.lend;

import com.google.firebase.auth.FirebaseUser;

public class Booking {
    public String itemID;
    public String borrowerID;
    public String lenderID;

    public Booking() {

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
}
