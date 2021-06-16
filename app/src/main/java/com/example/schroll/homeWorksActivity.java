package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class homeWorksActivity extends AppCompatActivity {

    private static final String TAG = "homeWorksActivity";
    public String Year, Classroom;
    TextView hw01,hwdd01, hw02,hwdd02, hw03,hwdd03, hw04,hwdd04, hw05,hwdd05, hw06,hwdd06, hw07,hwdd07, courseName;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID4;
    public static final int EXTRA_NUMBER2 = 5;
    int courseNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_works);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID4 = fAuth.getCurrentUser().getUid();
        getSupportActionBar().setTitle("Home works");

        hw01 = findViewById(R.id.homeWorkCourse);
        hwdd01 = findViewById(R.id.hwView);
        hw02 = findViewById(R.id.homeWorkCourse2);
        hwdd02 = findViewById(R.id.hwView2);
        hw03 = findViewById(R.id.homeWorkCourse3);
        hwdd03 = findViewById(R.id.hwView3);
        hw04 = findViewById(R.id.homeWorkCourse4);
        hwdd04 = findViewById(R.id.hwView4);
        hw05 = findViewById(R.id.homeWorkCourse5);
        hwdd05 = findViewById(R.id.hwView5);
        hw06 = findViewById(R.id.homeWorkCourse6);
        hwdd06 = findViewById(R.id.hwView6);
        hw07 = findViewById(R.id.homeWorkCourse7);
        hwdd07 = findViewById(R.id.hwView7);
        courseName = findViewById(R.id.joko);

        Intent intent = getIntent();
        courseNumber = intent.getIntExtra(String.valueOf(Course_01_Activity.EXTRA_NUMBER), -1);

        DocumentReference userRef = fStore.collection("Students").document(userID4);
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(homeWorksActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }

                if (documentSnapshot.exists()){
                    classroomFinder classroom = documentSnapshot.toObject(classroomFinder.class);
                    assert classroom != null;
                    Year=classroom.getYear();
                    displayCourseName(Year);
                    Classroom = classroom.getClassroom();
                    displayDueDate(Year, Classroom);
                }
            }
        });

    }

    public void displayCourseName(String Year) {
        int i;
        for (i = 1; i <= 7; i++) {

            DocumentReference documentReference = fStore.collection("Year" + Year + " Courses").document("Matiere 0" + i);
            int finalI = i;
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (finalI == 1) { hw01.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 2) { hw02.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 3) {hw03.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 4) {hw04.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 5) {hw05.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 6) {hw06.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 7) {hw07.setText(documentSnapshot.getString("Name"));}
                }
            });
        }
    }

        public void displayDueDate(String Year, String classroom) {
            DocumentReference documentReference = fStore.collection("Home Works").document("Class " +Year +"_" +classroom);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    hwdd01.setText(documentSnapshot.getString("HW 01 END"));
                    hwdd02.setText(documentSnapshot.getString("HW 02 END"));
                    hwdd03.setText(documentSnapshot.getString("HW 03 END"));
                    hwdd04.setText(documentSnapshot.getString("HW 04 END"));
                    hwdd05.setText(documentSnapshot.getString("HW 05 END"));
                    hwdd06.setText(documentSnapshot.getString("HW 06 END"));
                    hwdd07.setText(documentSnapshot.getString("HW 07 END"));
                }
    });
    }

    public void onDetailsClick1(View v){
        int J = 1;
        Intent intent = new Intent(this, homeWorkDetailsActivity.class);
        intent.putExtra(String.valueOf(EXTRA_NUMBER2), J);
        startActivity(intent);
    }
    public void onDetailsClick2(View v){
        int J = 2;
        Intent intent = new Intent(this, homeWorkDetailsActivity.class);
        intent.putExtra(String.valueOf(EXTRA_NUMBER2), J);
        startActivity(intent);
    }
    public void onDetailsClick3(View v){
        int J = 3;
        Intent intent = new Intent(this, homeWorkDetailsActivity.class);
        intent.putExtra(String.valueOf(EXTRA_NUMBER2), J);
        startActivity(intent);
    }
    public void onDetailsClick4(View v){
        int J = 4;
        Intent intent = new Intent(this, homeWorkDetailsActivity.class);
        intent.putExtra(String.valueOf(EXTRA_NUMBER2), J);
        startActivity(intent);
    }
    public void onDetailsClick5(View v){
        int J = 5;
        Intent intent = new Intent(this, homeWorkDetailsActivity.class);
        intent.putExtra(String.valueOf(EXTRA_NUMBER2), J);
        startActivity(intent);
    }
    public void onDetailsClick6(View v){
        int J = 6;
        Intent intent = new Intent(this, homeWorkDetailsActivity.class);
        intent.putExtra(String.valueOf(EXTRA_NUMBER2), J);
        startActivity(intent);
    }
    public void onDetailsClick7(View v){
        int J = 7;
        Intent intent = new Intent(this, homeWorkDetailsActivity.class);
        intent.putExtra(String.valueOf(EXTRA_NUMBER2), J);
        startActivity(intent);
    }

}