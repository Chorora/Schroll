package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class chooseClassroomActivity2 extends AppCompatActivity {

    TextView classRoom1, classRoom2, classRoom3, classRoom4;
    FirebaseFirestore fStore;
    DocumentReference class1Ref, class2Ref, class3Ref, class4Ref;
    String classRoomX1, classRoomX2, classRoomX3, classRoomX4, course;
    public static final String EXTRA_CLASSROOM = "EXTRA_CLASSROOM";
    public static final String EXTRA_COURSE = "EXTRA_COURSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_classroom2);
        fStore = FirebaseFirestore.getInstance();

        classRoom1 = findViewById(R.id.ClassRoom1);
        classRoom2 = findViewById(R.id.ClassRoom2);
        classRoom3 = findViewById(R.id.ClassRoom3);
        classRoom4 = findViewById(R.id.ClassRoom4);
        class1Ref = fStore.collection("Classrooms").document("Class 01");
        class2Ref = fStore.collection("Classrooms").document("Class 02");
        class3Ref = fStore.collection("Classrooms").document("Class 03");
        class4Ref = fStore.collection("Classrooms").document("Class 04");

        Intent intent = getIntent();
        course = intent.getStringExtra(MainActivity2.EXTRA_SPECIALTY2);

        class1Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                classRoom1.setText(documentSnapshot.getString("Name"));
                classRoomX1 = documentSnapshot.getString("Code");
            }
        });

        class2Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                classRoom2.setText(documentSnapshot.getString("Name"));
                classRoomX2 = documentSnapshot.getString("Code");
            }
        });

        class3Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                classRoom3.setText(documentSnapshot.getString("Name"));
                classRoomX3 = documentSnapshot.getString("Code");
            }
        });

        class4Ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                classRoom4.setText(documentSnapshot.getString("Name"));
                classRoomX4 = documentSnapshot.getString("Code");
            }
        });
    }

    public void onClass1Click(View v){
        Intent intent = new Intent(this, uploadCourseActivity.class);
        intent.putExtra(EXTRA_CLASSROOM, classRoomX1);
        intent.putExtra(EXTRA_COURSE, course);
        startActivity(intent);
    }

    public void onClass2Click (View v){
        Intent intent = new Intent(this, uploadCourseActivity.class);
        intent.putExtra(EXTRA_CLASSROOM, classRoomX2);
        intent.putExtra(EXTRA_COURSE, course);
        startActivity(intent);
    }

    public void onClass3Click (View v){
        Intent intent = new Intent(this, uploadCourseActivity.class);
        intent.putExtra(EXTRA_CLASSROOM, classRoomX3);
        intent.putExtra(EXTRA_COURSE, course);
        startActivity(intent);
    }

    public void onClass4Click (View v){
        Intent intent = new Intent(this, uploadCourseActivity.class);
        intent.putExtra(EXTRA_CLASSROOM, classRoomX4);
        intent.putExtra(EXTRA_COURSE, course);
        startActivity(intent);
    }

}