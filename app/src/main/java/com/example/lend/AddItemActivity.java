package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

import static com.example.lend.Utils.itemWrite;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText view_price, view_name, view_description;
    private Spinner view_category;
    private int price;
    private Item item;
    private String name, description, category;
    private Button image;
    private Button save;
    public static final int PICK_IMAGE = 1;
    private final String TAG = "Hello";
    LendUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        view_price = (EditText) findViewById(R.id.price);
        view_name = (EditText) findViewById(R.id.name);
        view_description = (EditText) findViewById(R.id.description);
        view_category = (Spinner) findViewById(R.id.category);
        image = findViewById(R.id.add_image);
        save = findViewById(R.id.save_item);
        image.setOnClickListener(this);
        save.setOnClickListener(this);

        queryLender();
    }

    @Override
    public void onClick(View view) {
//        switch(view.getId()) {
//            case R.id.add_image:
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//                break;
//            case R.id.save_item:
//                int price = Integer.parseInt(view_price.getText().toString());
//                String name = view_name.getText().toString();
//                String description = view_description.getText().toString();
//                String category = view_category.getSelectedItem().toString();
//                Map<String, Object> lendData = new HashMap<>();
//                lendData.put(item.getItemName() , item);
//                db.collection("users").document(item.getLender().getDisplayName()).collection("Lender").document("item")
//                        .set(lendData)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d(TAG, "DocumentSnapshot successfully written!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error writing document", e);
//                            }
//                        });
//
//                try {
//                    item.setItemName(name);
//                    item.setItemDescription(description);
//                    item.setItemCategory(category);
//                    item.setPrice(((Integer) price).toString());
//                    itemWrite(item.getLender() , item.getItemName() , item.getItemDescription() , item.getStarting_date());
//                }
//                catch (NullPointerException e)  {
//                    Log.d("henlo" , "some nullpointerexception");
//                }


//        }
    }

    public void queryLender() {
        db.collection("users").whereEqualTo("uid", item.getLender()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        user = document.toObject(LendUser.class);
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                new CloudStorage().upload(imageUri, new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
//                        image.setImageBitmap(selectedImage);
//                        uploadedImageURL = s;
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Incorrect requestcode", Toast.LENGTH_SHORT).show();
        }
    }
}
