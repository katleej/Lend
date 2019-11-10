package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    public TextView tvPrice;
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
        Log.d("XYZ", item.getItemName());
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        days = findViewById(R.id.viewDays);
        itemName = findViewById(R.id.borrow_item_name);
        itemImage = findViewById(R.id.borrow_item_image);
        itemDescription = findViewById(R.id.borrow_item_description);
        lenderImage = findViewById(R.id.user_image);
        lenderName = findViewById(R.id.user_name);
        book = findViewById(R.id.btnBook);
        tvPrice = findViewById(R.id.tvPrice);

        itemName.setText(item.getItemName());
        itemDescription.setText(item.getItemDescription());
        lenderName.setText(item.getLender());
        Glide.with(getApplicationContext()).load(item.getPhotoURL()).into(itemImage);
        tvPrice.setText("Current Price: " + item.getPrice());

        days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tvPrice.setText("Current Price: " + ((Integer) Integer.parseInt(item.getPrice()) * Integer.parseInt(days.getSelectedItem().toString())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> booking = new HashMap<>();
                booking.put("Item ID", item.getID());
                booking.put("Lender ID", item.getLender());
                booking.put("Borrower ID", auth.getCurrentUser().getUid());
                booking.put("Active", true);
                booking.put("Booking Days", days.getSelectedItem());
                booking.put("User Returned", false);
                booking.put("ID", booking.hashCode());
                db.collection("bookings").document(booking.get("ID").toString())
                        .set(booking)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplication(), "This item has been booked for you!", Toast.LENGTH_SHORT).show();
                                book.setText("Booked!");
                                book.setTextColor(getResources().getColor(R.color.quantum_googgreen500));
                                book.setBackgroundColor(getResources().getColor(R.color.quantum_googgreen200));
                                DocumentReference rf = db.collection("items").document(item.getID());
                                Log.d("item", item.getID());
                                Log.d("rf", rf.getId());
                                HashMap<String, Object> map = new HashMap();
                                map.put("Booked", true);
                                rf.update(map);
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
