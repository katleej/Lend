package com.example.lend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {
    Context context;
    ArrayList<Item> items;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    public ItemAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public ItemAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row2, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemAdapter.CustomViewHolder holder, int position) {
        Item item = items.get(position);
        db.collection("users").whereEqualTo("ID", item.getLender()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        LendUser user = document.toObject(LendUser.class);
                        holder.tvLenderName.setText(user.getUsername());
                    }
                }
            }
        });
        holder.tvItemName.setText(item.getItemName());
        holder.tvItemPrice.setText(item.getPrice() +"");
        Log.d("ITEM URL" , item.getPhotoURL());
        Glide.with(holder.photo.getContext())
                .load(item.getPhotoURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.photo);
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
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("item", Parcels.wrap(item));
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
