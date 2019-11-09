package com.example.lend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public final class Utils {
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String TAG = "henlo";
    static ArrayList<Item> items;
    static ArrayList<Item> currentItems;
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

    public static void itemWrite(String lenderIDToken, String itemName, String itemDescription, String itemPrice, String itemCategory, String photoURL)  {
        Map<String, Object> item = new HashMap<>();
        item.put("Lender ID" , lenderIDToken);
        item.put("Item Name" , itemName);
        item.put("Item Description" , itemDescription);
        item.put("Item Price" , itemPrice);
        item.put("Item Category" , itemCategory);
        item.put("Photo URL" , photoURL);
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

    public static void itemRead(String category)   {
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
                                temp.setItemCategory(itemMap.get("Item Category").toString());
                                temp.setItemDescription(itemMap.get("Item Description").toString());
                                temp.setItemName(itemMap.get("Item Name").toString());
                                temp.setPhotoURL(itemMap.get("Photo URL").toString());
                                temp.setLender(itemMap.get("Lender ID").toString());
                                temp.setPrice(Integer.parseInt(itemMap.get("Item Price").toString()));
                                items.add(temp);
                                Log.d("EEEEEEEEEEE" , items.toString());
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
        Log.d("helper" , currentItems.toString());
    }

    public static ArrayList<Item> getCurrentItems() {
        Log.d("getCurrentItems" , currentItems.toString());
        return currentItems;
    }
}