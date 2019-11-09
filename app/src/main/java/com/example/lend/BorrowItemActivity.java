package com.example.lend;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

public class BorrowItemActivity extends AppCompatActivity {

    public Spinner days;
    public TextView itemName;
    public ImageView itemImage;
    public TextView itemDescription;
    public ImageView lenderImage;
    public TextView lenderName;
    public Button book;
    public Item item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_item);
        item = (Item) Parcels.unwrap(getIntent().getParcelableExtra("item"));

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
                bookMethod();
            }
        });
    }

    public void bookMethod() {
        Booking newBooking = new Booking();
        newBooking.setBorrower(FirebaseAuth.getInstance().getCurrentUser().getUid());
        newBooking.setItem(((Integer) item.hashCode()).toString());
        newBooking.setLenderID(item.getLender());
        Utils.bookingWrite(newBooking.getItem(), newBooking.getLenderID(), newBooking.getBorrower());

    }




}
