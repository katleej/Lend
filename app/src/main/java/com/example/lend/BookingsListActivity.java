package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class BookingsListActivity extends AppCompatActivity {
    ArrayList<Booking> currBookings;
    ArrayList<Booking> pastBookings;
    RecyclerView currBook;
    RecyclerView pastBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currBookings = new ArrayList();
        pastBookings = new ArrayList();
        currBook = findViewById(R.id.rvCurr);
        pastBook = findViewById(R.id.rvPast);
        db.collection("bookings")
                .whereEqualTo("Borrower ID", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                Map<String, Object> bookingMap = document.getData();
                                Booking booking  = new Booking();
                                booking.setID(bookingMap.get("ID").toString());
                                booking.setLenderID(bookingMap.get("Lender ID").toString());
                                booking.setBorrower(bookingMap.get("Borrower ID").toString());
                                booking.setItem(bookingMap.get("Item ID").toString());
                                booking.setActive(bookingMap.get("Active").toString());
                                if (booking.getActive().equals("true")) {
                                    currBookings.add(booking);
                                    setUpRV(currBook, currBookings);
                                } else {
                                    pastBookings.add(booking);
                                    setUpRV(pastBook, pastBookings);
                                }
                            }
                        }
                    }
                });
    }

    public void setUpRV(RecyclerView recList, ArrayList<Booking> bookings) {
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(BookingsListActivity.this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        CurrBookingAdapter adapter = new CurrBookingAdapter(getApplicationContext(), bookings);
        recList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("XYZ", ((Integer) bookings.size()).toString());
    }






}
