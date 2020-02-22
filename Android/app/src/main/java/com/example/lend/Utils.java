
package com.example.lend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public final class Utils {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static FirebaseAuth auth = FirebaseAuth.getInstance();
    final String TAG = "henlo";
    static ArrayList<Item> items;
    static ArrayList<Item> currentItems;

    public static void userWrite(String input, Place location) {
        Map<String, Object> username = new HashMap<>();
        username.put("username", input);
        username.put("lat", ((Double) location.getLatLng().latitude).toString());
        username.put("long", ((Double) location.getLatLng().longitude).toString());
        username.put("ID", auth.getCurrentUser().getUid());
        db.collection("users").document(auth.getCurrentUser().getUid())
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

    public static void itemWrite(String lenderIDToken, String lenderName, String itemName, String itemDescription, String itemPrice, String itemCategory, String photoURL, String murder) {
        Map<String, Object> item = new HashMap<>();
        item.put("Lender ID", lenderIDToken);
        item.put("Lender Name", lenderName);
        item.put("Item Name", itemName);
        item.put("Item Description", itemDescription);
        item.put("Item Price", itemPrice);
        item.put("Item Category", itemCategory);
        item.put("Photo URL", photoURL);
        item.put("ID", item.hashCode());
        item.put("Booked" , murder);
        db.collection("items").document(item.get("ID").toString())
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

    public static void bookingWrite(String itemID, String lenderID, String borrowerID, String active, String days)  {
        Map<String, Object> booking = new HashMap<>();
        booking.put("Item ID", itemID);
        booking.put("Lender ID", lenderID);
        booking.put("Borrower ID" , borrowerID);
        booking.put("Active", active);
        booking.put("Booking Days", days);
        booking.put("User Returned", false);
        booking.put("ID", booking.hashCode());
        db.collection("bookings").document(booking.get("ID").toString())
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

    public static void itemRead(String category) {
        items = new ArrayList<>();
        db.collection("items")
                .whereEqualTo("Item Category", category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("henlo", document.getId() + " => " + document.getData());
                                Map<String, Object> itemMap = document.getData();
                                Item temp = new Item();
                                temp.setid(itemMap.get("ID").toString());
                                temp.setCategory(itemMap.get("Item Category").toString());
                                temp.setItemDescription(itemMap.get("Item Description").toString());
                                temp.setItemName(itemMap.get("Item Name").toString());
                                temp.setPhotoURL(itemMap.get("Photo URL").toString());
                                temp.setLender(itemMap.get("Lender ID").toString());
                                temp.setPrice((itemMap.get("Item Price").toString()));
                                items.add(temp);
                                Log.d("EEEEEEEEEEE", items.toString());
                            }
                            helper(items);
                        } else {
                            Log.d("henlo", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void helper(ArrayList<Item> a) {
        currentItems = a;
        Log.d("helper", currentItems.toString());
    }

    public static ArrayList<Item> getCurrentItems() {
        Log.d("getCurrentItems", currentItems.toString());
        return currentItems;
    }
}
