package com.example.schroll;

import android.os.Bundle;
import android.util.Log;
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

public class GradesActivity extends AppCompatActivity {
    private static final String TAG = "GradesActivity";
    public String Year;
    TextView courseNameView1, courseNameView2, courseNameView3, courseNameView4, courseNameView5, courseNameView6, courseNameView7;
    TextView gradeView1, gradeView2, gradeView3, gradeView4, gradeView5, gradeView6, gradeView7;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID3 = fAuth.getCurrentUser().getUid();
        getSupportActionBar().setTitle("Grades");

        courseNameView1 = findViewById(R.id.courseView1);
        courseNameView2 = findViewById(R.id.courseView2);
        courseNameView3 = findViewById(R.id.courseView3);
        courseNameView4 = findViewById(R.id.courseView4);
        courseNameView5 = findViewById(R.id.courseView5);
        courseNameView6 = findViewById(R.id.courseView6);
        courseNameView7 = findViewById(R.id.courseView7);
        gradeView1 = findViewById(R.id.gradeView1);
        gradeView2 = findViewById(R.id.gradeView2);
        gradeView3 = findViewById(R.id.gradeView3);
        gradeView4 = findViewById(R.id.gradeView4);
        gradeView5 = findViewById(R.id.gradeView5);
        gradeView6 = findViewById(R.id.gradeView6);
        gradeView7 = findViewById(R.id.gradeView7);

        DocumentReference userRef = fStore.collection("Students").document(userID3);
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(GradesActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }

                if (documentSnapshot.exists()){
                    classroomFinder classroom = documentSnapshot.toObject(classroomFinder.class);
                    assert classroom != null;
                    Year=classroom.getYear();
                    displayCourseName(Year);
                    displayGrades();
                }
            }
        });

    }


    public void displayCourseName(String Year) {
        int i;
        for (i = 1; i <= 8; i++) {

            DocumentReference documentReference = fStore.collection("Year" + Year + " Courses").document("Matiere 0" + i);
            int finalI = i;
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (finalI == 1) { courseNameView1.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 2) { courseNameView2.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 3) {courseNameView3.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 4) {courseNameView4.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 5) {courseNameView5.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 6) {courseNameView6.setText(documentSnapshot.getString("Name"));}

                    if (finalI == 7) {courseNameView7.setText(documentSnapshot.getString("Name"));}

                }

            });
        }
    }

    public void displayGrades(){

        DocumentReference documentReference = fStore.collection("Grades").document(userID3);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                gradeView1.setText(documentSnapshot.getString("Grade 01"));

                gradeView2.setText(documentSnapshot.getString("Grade 02"));

                gradeView3.setText(documentSnapshot.getString("Grade 03"));

                gradeView4.setText(documentSnapshot.getString("Grade 04"));

                gradeView5.setText(documentSnapshot.getString("Grade 05"));

                gradeView6.setText(documentSnapshot.getString("Grade 06"));

                gradeView7.setText(documentSnapshot.getString("Grade 07"));

            }
        });
    }

}