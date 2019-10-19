package com.example.lend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText view_price, view_name, view_description;
    private Spinner view_category;
    private int price;
    private Item item;
    private String name, description, category;
    public static final int PICK_IMAGE = 1;

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

                progressBar.setVisibility(View.VISIBLE);
                new CloudStorage().upload(imageUri, new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        image.setImageBitmap(selectedImage);
                        uploadedImageURL = s;
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddNewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
