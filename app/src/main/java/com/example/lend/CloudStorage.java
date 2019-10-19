package com.example.lend;

import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class CloudStorage {
    private FirebaseStorage storage;
    public CloudStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public void upload(Uri path, final OnSuccessListener<String> successListener, final OnFailureListener onFailureListener) {
        final StorageReference storageReference = storage.getReference("/pictures/" + UUID.randomUUID().toString() + ".jpeg");
        storageReference.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        successListener.onSuccess(uri.toString());
                    }
                }).addOnFailureListener(onFailureListener);
            }
        }).addOnFailureListener(onFailureListener);
    }
}
