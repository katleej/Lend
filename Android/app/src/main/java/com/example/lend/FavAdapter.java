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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.lend.Utils.auth;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.CustomViewHolder> {
    Context context;
    ArrayList<LendUser> users;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    String id = FirebaseAuth.getInstance().getUid();

    public FavAdapter(Context context, ArrayList<LendUser> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public FavAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavAdapter.CustomViewHolder holder, int position) {
        final LendUser user = users.get(position);
        db.collection("users").whereEqualTo("id", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        LendUser user = document.toObject(LendUser.class);
                        holder.usCity.setText(user.getCity());
                        holder.usName.setText(user.getUsername());
                        holder.numReviews.setText(Integer.toString(user.getNumReviews()) + " Reviews");
                        double userRating = user.getRating();
                        DecimalFormat decimalFormat = new DecimalFormat("0.#");
                        String rounded = decimalFormat.format(userRating);
                        holder.avgRating.setText("Average Rating:  " + rounded);
                        Glide.with(holder.usPhoto.getContext())
                                .load(user.getPhotoURL())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.usPhoto);
                    }
                }
            }
        });
    }

    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<LendUser> users) {
        users.addAll(users);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView usPhoto;
        TextView usCity;
        TextView usName;
        TextView numReviews;
        TextView avgRating;

        public CustomViewHolder(View itemView) {
            super(itemView);
            usPhoto = itemView.findViewById(R.id.usPhoto);
            usCity = itemView.findViewById(R.id.usCity);
            usName = itemView.findViewById(R.id.usName);
            numReviews = itemView.findViewById(R.id.numReviews);
            avgRating = itemView.findViewById(R.id.avgRating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        LendUser user = users.get(position);
                        if (user == null) {
                            Log.d("XYZ", "null item");
                        } else if (user.getId() == null) {
                            Log.d("XYZ", "Print null");
                        }
                        Intent intent = new Intent(context, ViewProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("user", Parcels.wrap(user));
                        context.startActivity(intent);
                    }
                }
            });

        }
    }
}
