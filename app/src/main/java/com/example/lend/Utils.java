package com.example.lend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public final class Utils {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "henlo";
    public static void userWrite(String input, Place location)  {
        Map<String, Object> username = new HashMap<>();
        username.put("username" , input);
        username.put("lat", ((Double) location.getLatLng().latitude).toString());
        username.put("long", ((Double) location.getLatLng().longitude).toString());
        db.collection("users").document()
                .set(username)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Henlo", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Henlo", "Error writing document", e);
                    }
                });
    }


    public static void itemWrite(String lenderIDToken, String itemName, String itemDescription, String starting_date)  {
        Map<String, Object> item = new HashMap<>();
        item.put("Lender ID" , lenderIDToken);
        item.put("Item Name" , itemName);
        item.put("Item Description" , itemDescription);
        item.put("Starting Date" , starting_date);
        db.collection("items").document()
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Henlo", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Henlo", "Error writing document", e);
                    }
                });
    }

    public static void bookingWrite(String itemID, String lenderID, String borrowerID)  {
        Map<String, Object> booking = new HashMap<>();
        booking.put("Item ID" , itemID);
        booking.put("Lender ID" , lenderID);
        booking.put("Borrower ID" , borrowerID);
        db.collection("bookings").document()
                .set(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Henlo", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Henlo", "Error writing document", e);
                    }
                });

    }
}
