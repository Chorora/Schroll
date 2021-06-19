package com.example.schroll;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class announcementsActivity extends AppCompatActivity {

    private static final String TAG = "announcementsActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore fStore;
    TextView title01, desc01, title02, desc02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);
        fStore = FirebaseFirestore.getInstance();

        title01 = findViewById(R.id.titleView1);
        title02 = findViewById(R.id.titleView2);
        desc01 = findViewById(R.id.descView1);
        desc02 = findViewById(R.id.descView2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference documentReference = fStore.collection("Announcements").document("Announcement 01");

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                title01.setText(documentSnapshot.getString("Title"));
                desc01.setText(documentSnapshot.getString("Description"));
            }
        });

        DocumentReference documentReference2 = fStore.collection("Announcements").document("Announcement 02");

        documentReference2.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                title02.setText(documentSnapshot.getString("Title"));
                desc02.setText(documentSnapshot.getString("Description"));
            }
        });
    }
}