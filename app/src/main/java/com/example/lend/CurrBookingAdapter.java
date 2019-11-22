package com.example.lend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import java.util.HashMap;
import java.util.Map;

public class CurrBookingAdapter extends RecyclerView.Adapter<CurrBookingAdapter.CustomViewHolder> {
    Context context;
    ArrayList<Booking> bookings;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LendUser user;
    Item item;

    public CurrBookingAdapter(Context context, ArrayList<Booking> bookings) {
        this.context = context;
        this.bookings = bookings;
        item = new Item();
        Log.d("ABC", "book" + bookings.toString());
    }

    @NonNull
    @Override
    public CurrBookingAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.curr_booking_row_card, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CurrBookingAdapter.CustomViewHolder holder, int position) {
        final Booking booking = bookings.get(position);
        if (booking.getUserReturned().equals("true")) {
            holder.btnReturn.setText("Pending Confirmation");

        }

        db.collection("users")
                .whereEqualTo("ID", booking.getLenderID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Log.d("ABC", "user" + task.getResult().size());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        user = document.toObject(LendUser.class);
                        holder.tvLenderName.setText(user.getUsername());
                    }
                }
            }
        });


        db.collection("items")
                .whereEqualTo("ID", Integer.parseInt(booking.getItem()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d("ABC", "items" + task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                item = document.toObject(Item.class);
                                Map<String, Object> itemMap = document.getData();
                                item.setItemName(itemMap.get("Item Name").toString());
                                item.setPrice(itemMap.get("Item Price").toString());
                                holder.tvItemName.setText(item.getItemName());
                                holder.tvPrice.setText("$" + Integer.parseInt(item.getPrice()) * Integer.parseInt(booking.getDaysBooked()));
                            }
                        }
                    }
                });


        holder.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booking.setUserReturned(((Boolean) true).toString());
                holder.btnReturn.setText("Pending Confirmation");
                final Dialog rankDialog = new Dialog(context, R.style.Theme_AppCompat_Light_Dialog);
                rankDialog.setContentView(R.layout.rank_dialog);
                rankDialog.setCancelable(true);
                RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
                ratingBar.setRating(0);

                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rankDialog.dismiss();
                    }
                });
                //now that the dialog is set up, it's time to show it
                rankDialog.show();
                Log.d("henlo3" , "going into dialog");

                DocumentReference rf = db.collection("bookings").document(booking.getID());
                rf.update("User Returned", true);
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
        TextView tvItemCategory;
        ImageView tvPhoto;
        Button btnReturn;

        public CustomViewHolder(View itemView) {
            super(itemView);
            tvLenderName = itemView.findViewById(R.id.tvLenderName);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnReturn = itemView.findViewById(R.id.btnReturn);
            tvPhoto = itemView.findViewById(R.id.tvPhoto);
            tvItemCategory = itemView.findViewById(R.id.tvItemCategory);

        }
    }

}
