package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

public class BorrowItemActivity extends AppCompatActivity {

    public Spinner days;
    public TextView itemName;
    public ImageView itemImage;
    public TextView itemDescription;
    public ImageView lenderImage;
    public TextView lenderName;
    public Button book;
    public Item item;
    FirebaseAuth auth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_item);
        item = (Item) Parcels.unwrap(getIntent().getParcelableExtra("item"));
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        days = findViewById(R.id.viewDays);
        itemName = findViewById(R.id.borrow_item_name);
        itemImage = findViewById(R.id.borrow_item_image);
        itemDescription = findViewById(R.id.borrow_item_description);
        lenderImage = findViewById(R.id.user_image);
        lenderName = findViewById(R.id.user_name);
        book = findViewById(R.id.btnBook);

        itemName.setText(item.getItemName());
        itemDescription.setText(item.getItemDescription());
        lenderName.setText(item.getLender());
        Glide.with(getApplicationContext()).load(item.getPhotoURL()).into(itemImage);


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> booking = new HashMap<>();
                booking.put("ID", booking.hashCode());
                booking.put("Item ID", item.getID());
                booking.put("Lender ID", item.getLender());
                booking.put("Borrower ID", auth.getCurrentUser().getUid());
                booking.put("Active", true);
                booking.put("Booking Days", days.getSelectedItem());
                booking.put("User Returned", false);
                db.collection("bookings").document(((Integer) booking.hashCode()).toString())
                        .set(booking)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplication(), "This item has been booked for you!", Toast.LENGTH_SHORT).show();
                                book.setText("Booked!");
                                book.setTextColor(getResources().getColor(R.color.quantum_googgreen500));
                                book.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen200));
                                DocumentReference rf = db.collection("items").document(item.getID());
                                rf.update("Booked", false);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Henlo", "Error writing document", e);
                            }
                        });
            }
        });
    }
}
