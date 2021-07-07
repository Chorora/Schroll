package com.example.schroll;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class teachProfileActivity extends AppCompatActivity {

    TextView Name, Surname, Specialty, Class1, Class2, Class3, Class4, phone, address;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference teacherRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teach_profile);

        Name = findViewById(R.id.nameView2);
        Surname = findViewById(R.id.surnameView2);
        Specialty = findViewById(R.id.specialtyView);
        Class1 = findViewById(R.id.classView1);
        Class2 = findViewById(R.id.classView2);
        Class3 = findViewById(R.id.classView3);
        Class4 = findViewById(R.id.classView4);
        address = findViewById(R.id.addressView3);
        phone = findViewById(R.id.phoneView3);

        teacherRef = fStore.collection("Users").document(userID);

        teacherRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Name.setText(documentSnapshot.getString("Name"));
                Surname.setText(documentSnapshot.getString("Surname"));
                Specialty.setText(documentSnapshot.getString("Specialty"));

                String class1 = documentSnapshot.getString("Class 01");
                if (!class1.equals("")) {
                    Class1.setText("Class " + documentSnapshot.getString("Class 01").substring(6, 7) + " A " + documentSnapshot.getString("Class 01").substring(8, 9));
                } else if (class1.equals("")) {
                    Class1.setText("");
                }

                String class2 = documentSnapshot.getString("Class 02");
                if (!class2.equals("")) {
                    Class2.setText("Class " + documentSnapshot.getString("Class 02").substring(6, 7) + " A " + documentSnapshot.getString("Class 02").substring(8, 9));
                } else if (class2.equals("")) {
                    Class2.setText("");
                }

                String class3 = documentSnapshot.getString("Class 03");
                if (!class3.equals("")) {
                    Class3.setText("Class " + documentSnapshot.getString("Class 03").substring(6, 7) + " A " + documentSnapshot.getString("Class 03").substring(8, 9));
                } else if (class3.equals("")) {
                    Class3.setText("");
                }

                String class4 = documentSnapshot.getString("Class 04");
                if (!class4.equals("")) {
                    Class4.setText("Class " + documentSnapshot.getString("Class 04").substring(6, 7) + " A " + documentSnapshot.getString("Class 04").substring(8, 9));
                } else if (class4.equals("")) {
                    Class4.setText("");
                }

                address.setText(documentSnapshot.getString("Address"));
                phone.setText(documentSnapshot.getString("Phone"));
            }
        });
    }
}