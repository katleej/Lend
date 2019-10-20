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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseUser;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText view_price, view_name, view_description;
    private Spinner view_category;
    private int price;
    private Item item;
    private String name, description, category;
    public static final int PICK_IMAGE = 1;
    private final String TAG = "Hello";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        EditText view_price = (EditText) findViewById(R.id.price);
        EditText view_name = (EditText) findViewById(R.id.name);
        EditText view_description = (EditText) findViewById(R.id.description);
        Spinner view_category = (Spinner) findViewById(R.id.category);

        int price = Integer.parseInt(view_price.getText().toString());
        String name = view_name.getText().toString();
        String description = view_description.getText().toString();
        String category = view_category.getSelectedItem().toString();

        Places.initialize(getApplicationContext(), "AIzaSyB7PN4NZcXwmlTvJ1K_NV6g4md9nGoKV30");
        PlacesClient placesCLient = Places.createClient(this);


        item.setItemName(name);
        item.setItemDescription(description);
        item.setItemCategory(category);

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                item.setLocation(place);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        item.setItemName(name);
        item.setItemDescription(description);
        item.setItemCategory(category);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.add_image:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                break;
            case R.id.save_item:
                Map<String, Object> lendData = new HashMap<>();
                lendData.put(item.getItemName() , item);
                db.collection("users").document(item.getLender().getDisplayName()).collection("Lender").document("item")
                        .set(lendData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

        }
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
