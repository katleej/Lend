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
    ArrayList<Booking> lendBookings;
    ArrayList<Booking> borrowBookings;
    RecyclerView lendBook;
    RecyclerView borrowBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        lendBookings = new ArrayList();
        borrowBookings = new ArrayList();
        lendBook = findViewById(R.id.rvLend);
        borrowBook = findViewById(R.id.rvBorrow);
        db.collection("bookings")
                .whereEqualTo("Lender ID", user.getUid())
                .whereEqualTo("Active", true).get()
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
                                booking.setDaysBooked(bookingMap.get("Booking Days").toString());
                                booking.setUserReturned(bookingMap.get("User Returned").toString());
                                lendBookings.add(booking);
                                Log.d("ABC", "lending here");
                                CurrLendedAdapter adapter1 = new CurrLendedAdapter(getApplicationContext(), lendBookings);
                                setUpRV(lendBook, lendBookings, adapter1);
                            }
                        }
                    }
                });

        db.collection("bookings")
                .whereEqualTo("Borrower ID", user.getUid())
                .whereEqualTo("Active", true).get()
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
                                booking.setDaysBooked(bookingMap.get("Booking Days").toString());
                                booking.setUserReturned(bookingMap.get("User Returned").toString());
                                borrowBookings.add(booking);
                                Log.d("ABC", "booking here");
                                CurrBookingAdapter adapter2 = new CurrBookingAdapter(getApplicationContext(), borrowBookings);
                                setUpRV(borrowBook, borrowBookings, adapter2);
                            }
                        }
                    }
                });

    }

    public void setUpRV(RecyclerView recList, ArrayList<Booking> bookings, RecyclerView.Adapter adapter) {
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(BookingsListActivity.this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("ABC", ((Integer) bookings.size()).toString());
    }






}
