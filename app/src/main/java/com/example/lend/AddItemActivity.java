package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item2);

        view_price = (EditText) findViewById(R.id.price);
        view_name = (EditText) findViewById(R.id.name);
        view_description = (EditText) findViewById(R.id.description);
        view_category = (Spinner) findViewById(R.id.category);
        Places.initialize(getApplicationContext(), "AIzaSyB7PN4NZcXwmlTvJ1K_NV6g4md9nGoKV30");
        PlacesClient placesClient = Places.createClient(this);
        image = findViewById(R.id.add_image);
        save = findViewById(R.id.add_item_post);
        image.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.writing_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
            switch(view.getId()) {
                case R.id.add_image:
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                        startActivityForResult(intent, 1046);
                    }
                    break;
                case R.id.add_item_post:
                    Item item = new Item();
                    int price = Integer.parseInt(view_price.getText().toString().trim());
                    String name = view_name.getText().toString();
                    String description = view_description.getText().toString();
                    String category = view_category.getSelectedItem().toString();
                    String photoURL = imageUri.toString();
                    try {
                        item.setItemName(name);
                        Log.d("henlo" , name);
                        item.setItemDescription(description);
                        Log.d("henlo" , description);
                        item.setItemCategory(category);
                        Log.d("henlo" , category);
                        item.setPrice(price);
                        Log.d("henlo" , Integer.toString(price));
                        item.setPhotoURL(photoURL);
                        itemWrite(user.getUid() , item.getItemName() , item.getItemDescription(), Integer.toString(item.getPrice()), item.getCategory(), item.getPhotoURL());
                        Log.d("henlo" , user.getUid());
                    }
                    catch (NullPointerException e)  {
                        Log.d("henlo" , "some nullpointerexception");
                    }


            }
        }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radio_delivery:
                if (checked)
                    break;
            case R.id.radio_pickup:
                if (checked)
                    break;
            case R.id.radio_both:
                if (checked)
                    break;

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
