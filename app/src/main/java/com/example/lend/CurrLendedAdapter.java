package com.example.lend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Map;

public class CurrLendedAdapter extends RecyclerView.Adapter<CurrLendedAdapter.CustomViewHolder> {
    Context context;
    ArrayList<Booking> bookings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LendUser user;
    Item item;


    public CurrLendedAdapter(Context context, ArrayList<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
        item = new Item();
        Log.d("ABC", "lend" + bookings.toString());
    }

    @NonNull
    @Override
    public CurrLendedAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lended_booking_row_card, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CurrLendedAdapter.CustomViewHolder holder, int position) {
        final Booking booking = bookings.get(position);
        holder.btnReturn.setClickable(false);
        db.collection("items")
                .whereEqualTo("ID", Integer.parseInt(booking.getItem()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> itemMap = document.getData();
                                item.setItemName(itemMap.get("Item Name").toString());
                                item.setPrice(itemMap.get("Item Price").toString());
                                item.setid(itemMap.get("ID").toString());
                                holder.tvItemName.setText(item.getItemName());
                                holder.tvPrice.setText("$" + Integer.parseInt(item.getPrice()) * Integer.parseInt(booking.getDaysBooked()));
                            }
                        }
                    }
                });

        if (booking.getUserReturned().equals("true")) {
            holder.btnReturn.setText("Confirm Returned");
            holder.btnReturn.setClickable(true);
            holder.btnReturn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DocumentReference rf = db.collection("bookings").document(booking.getID());
                    DocumentReference rf2 = db.collection("items").document(item.getid());
                    rf.update("Active", false);
                    rf2.update("Booked", "false");
                    bookings.remove(booking);
                    notifyDataSetChanged();
                }
            });
        }
        db.collection("users").whereEqualTo("ID", booking.getBorrower()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        user = document.toObject(LendUser.class);
                        holder.tvLenderName.setText(user.getUsername());

                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvLenderName;
        TextView tvItemName;
        TextView tvPrice;
        Button btnReturn;

        public CustomViewHolder(View itemView) {
            super(itemView);
            tvLenderName = itemView.findViewById(R.id.tvLenderName);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnReturn = itemView.findViewById(R.id.btnReturn);

        }
    }

}
