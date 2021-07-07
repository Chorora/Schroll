package com.example.schroll;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class profileActivity extends AppCompatActivity {

    private static final String KEY_ADDRESS ="Address" ;
    private static final String KEY_PHONE = "Phone";

    private TextView Name, Surname,Age, Class, phone, address;
    private ImageView imageView;
    private AutoCompleteTextView Address;
    private EditText Phone;
    Button imageUpdate;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        Name = findViewById(R.id.nameView);
        Surname = findViewById(R.id.surnameView);
        Age = findViewById(R.id.specialtyView);
        Class = findViewById(R.id.classView);
        Address = findViewById(R.id.addressEditView);
        address = findViewById(R.id.addressView2);
        Phone = findViewById(R.id.phoneEditView);
        phone = findViewById(R.id.phoneView2);
        imageView = findViewById(R.id.imageView);
        imageUpdate= findViewById(R.id.imageUpdate);

        StorageReference profileImageRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });

        DocumentReference documentReference = fStore.collection("Students").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    Name.setText(documentSnapshot.getString("Name"));
                    Surname.setText(documentSnapshot.getString("Surname"));
                    Age.setText(documentSnapshot.get("Age").toString());
                    Class.setText(documentSnapshot.getString("Class"));
                    address.setText(documentSnapshot.getString("Address"));
                    phone.setText(documentSnapshot.getString("Phone").toString());
            }
        });

        imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

    }

    public void updateProfile(View v){
        String address = Address.getText().toString();
        String phone = Phone.getText().toString();
        if (address.equals("") || phone.equals("")) {
            Toast.makeText(this, "The Field(s) should not be empty", Toast.LENGTH_SHORT).show();
        }
        if (phone.length() < 8 || phone.length() >= 11){ Toast.makeText(this, "Incorrect phone number length", Toast.LENGTH_SHORT).show(); }
        else {
        fStore = FirebaseFirestore.getInstance();
        DocumentReference userRef = fStore.collection("Students").document(userID);

        Map<String, Object> Loc = new HashMap<>();
         Loc.put(KEY_ADDRESS, address);
         Loc.put(KEY_PHONE, phone);
        userRef.set(Loc, SetOptions.merge())
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(profileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageURI = data.getData();
                uploadImageToFirebase(imageURI);
            }
        }
    }

    public void uploadImageToFirebase(Uri imageURI){
        StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    public void editPhone(View v){
        phone.setVisibility(View.INVISIBLE);
        Phone.setVisibility(View.VISIBLE);
    }
    
    public void editAddress(View v){
        address.setVisibility(View.INVISIBLE);
        Address.setVisibility(View.VISIBLE);
    }

    }