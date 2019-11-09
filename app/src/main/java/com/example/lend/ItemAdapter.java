package com.example.lend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {
    Context context;
    ArrayList<Item> items;

    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public ItemAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.CustomViewHolder holder, int position) {
        Item item = items.get(position);
//        holder.tvLenderName.setText(item.getLender2().getDisplayName());
        holder.tvItemName.setText(item.getItemName());
        holder.tvItemPrice.setText(item.getPrice() +"");
        Glide.with(context).load(item.getPhotoURL()).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView tvLenderName;
        TextView tvItemName;
        TextView tvItemPrice;

        public CustomViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.ivPhoto);
            tvLenderName = itemView.findViewById(R.id.tvLenderName);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Item item = items.get(position);
                        Intent intent = new Intent(context, BorrowItemActivity.class);
                        intent.putExtra("item", Parcels.wrap(item));
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
