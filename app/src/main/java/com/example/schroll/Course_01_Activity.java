package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.util.Map;

public class Course_01_Activity extends AppCompatActivity {

    private static final String TAG = "Course_01_Activity";
    public String Year, Classroom, classCode;
    private Map chapter01Map, chapter02Map, chapter03Map;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String in = "";
    String userID2, chapter01Desc = "", chapter02Desc = "", chapter03Desc = "", courseName;
    TextView Chapter01, Chapter01Description, Chapter02, Chapter02Description, Chapter03, Chapter03Description;
    TextView Name, Description;
    ImageView courseImage;

    public static final int EXTRA_NUMBER = 5;
    public static final String EXTRA_YEARS = "EXTRA_YEARS";
    public static final String EXTRA_COURSE = "EXTRA_COURSE";
    public static final String EXTRA_CLASSCODE = "EXTRA_CLASSCODE";

    int courseNumberX = -5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_01_);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID2 = fAuth.getCurrentUser().getUid();

        Name = findViewById(R.id.titleCourse);
        Description = findViewById(R.id.titleCourseView);
        Chapter01 = findViewById(R.id.textViewChapters);
        Chapter01Description = findViewById(R.id.chapterDescription);
        Chapter02 = findViewById(R.id.textViewChapters2);
        Chapter02Description = findViewById(R.id.chapterDescription2);
        Chapter03 = findViewById(R.id.textViewChapters3);
        Chapter03Description = findViewById(R.id.chapterDescription3);
        courseImage = findViewById(R.id.courseImage);

        Intent intent = getIntent();
        int courseNumber = intent.getIntExtra(MainActivity.EXTRA_TEXT, -1);

        if (courseNumber >= 0 && courseNumber <= 2) {
            courseNumber += 1;
            in = String.valueOf(courseNumber);
            courseNumberX = courseNumber;
            setCourseImage(courseNumber);
        }

        Intent intent1 = getIntent();
        int courseNumber2 = intent1.getIntExtra(MainActivity.EXTRA_TEXT2, 2);
        if (courseNumber2 >= 3) {
            courseNumber2 += 1;
            in = String.valueOf(courseNumber2);
            courseNumberX = courseNumber2;
            setCourseImage(courseNumber2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference userRef = fStore.collection("Students").document(userID2);
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(Course_01_Activity.this, "Error !", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
                if (documentSnapshot.exists()) {
                    classroomFinder classroom = documentSnapshot.toObject(classroomFinder.class);
                    assert classroom != null;
                    Year = classroom.getYear();
                    Classroom = classroom.getClassroom();
                    classCode = "Class " +Year +"_" +Classroom;
                    displayCourseInfo(Year);
                }
            }
        });

    }

    public void displayCourseInfo(String Year) {

        DocumentReference documentReference = fStore.collection("Year" + Year + " Courses").document("Matiere 0" + in);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Name.setText(documentSnapshot.getString("Name"));
                courseName = documentSnapshot.getString("Name");
                courseName = courseName.substring(0, courseName.length() - 4);
                Description.setText(documentSnapshot.getString("Description"));


                if (documentSnapshot.contains("Chapitre 01")) {
                    Chapter01.setText(documentSnapshot.getString("Chapitre 01"));
                }
                if (documentSnapshot.contains("Chapitre 02")) {
                    Chapter02.setText(documentSnapshot.getString("Chapitre 02"));
                }
                if (documentSnapshot.contains("Chapitre 03")) {
                    Chapter03.setText(documentSnapshot.getString("Chapitre 03"));
                }

                if(documentSnapshot.contains("Chapitre 01 Desc")) {
                    chapter01Map = (Map<String, String>) documentSnapshot.get("Chapitre 01 Desc");
                    Log.v("desc", String.valueOf(chapter01Map));
                    for (Object key : chapter01Map.keySet()) {
                        chapter01Desc += "- ";
                        chapter01Desc += " " + chapter01Map.get(key);
                        chapter01Desc += "\n";
                    }
                    Chapter01Description.setText(chapter01Desc);
                }

                if(documentSnapshot.contains("Chapitre 02 Desc")) {
                    chapter02Map = (Map<String, String>) documentSnapshot.get("Chapitre 02 Desc");
                    Log.v("desc", String.valueOf(chapter02Map));

                    for (Object key : chapter02Map.keySet()) {
                        chapter02Desc += "- ";
                        chapter02Desc += " " + chapter02Map.get(key);
                        chapter02Desc += "\n";
                    }
                    Chapter02Description.setText(chapter02Desc);
                } if(documentSnapshot.contains("Chapitre 03 Desc")) {
                    chapter03Map = (Map<String, String>) documentSnapshot.get("Chapitre 03 Desc");
                    Log.v("desc", String.valueOf(chapter03Map));

                    for (Object key : chapter03Map.keySet()) {
                        chapter03Desc += "- ";
                        chapter03Desc += " " + chapter03Map.get(key);
                        chapter03Desc += "\n";
                    }
                    Chapter03Description.setText(chapter03Desc);
                }
            }

        });
    }

    private void setCourseImage(int courseNumber){
        if (courseNumber == 1){
            courseImage.setImageDrawable(getDrawable(R.drawable.math_logo));
        }
        if (courseNumber == 2){
            courseImage.setImageDrawable(getDrawable(R.drawable.science_logo));
        }
        if (courseNumber == 3){
            courseImage.setImageDrawable(getDrawable(R.drawable.physics_logo));
        }
        if (courseNumber == 4){
            courseImage.setImageDrawable(getDrawable(R.drawable.art_logo));
        }
        if (courseNumber == 5){
            courseImage.setImageDrawable(getDrawable(R.drawable.islam_logo));
        }
        if (courseNumber == 6){
            courseImage.setImageDrawable(getDrawable(R.drawable.arabic_logo));
        }
        if (courseNumber == 7){
            courseImage.setImageDrawable(getDrawable(R.drawable.english_logo));
        }
    }

    public void goToHW (View v){
        int N = -1;
        if (courseNumberX > 0 && courseNumberX <= 3) {
             N = courseNumberX;
        }
        else if (courseNumberX >=4 && courseNumberX <= 7){
             N = courseNumberX;
        }
        Intent intent = new Intent(this, homeWorkDetailsActivity.class);
        intent.putExtra(String.valueOf(EXTRA_NUMBER), N);
        intent.putExtra(EXTRA_YEARS, Year);
        startActivity(intent);
    }

    public void viewLessons(View v){
        Intent intent = new Intent(this,coursesViewActivity.class);
        intent.putExtra(EXTRA_COURSE, courseName);
        intent.putExtra(EXTRA_CLASSCODE, classCode);
        startActivity(intent);
    }

}