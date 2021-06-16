package com.example.schroll;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTableActivity extends AppCompatActivity {
    private static final String TAG = "TimeTableActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID3;

    TextView dayNameView, hour1, hour2, hour3, hour4, hour5, hour6, hour7;
    public String Year , Classroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID3 = fAuth.getCurrentUser().getUid();

        dayNameView = findViewById(R.id.dayView);
        hour1 = findViewById(R.id.hour1);
        hour2 = findViewById(R.id.hour2);
        hour3 = findViewById(R.id.hour3);
        hour4 = findViewById(R.id.hour4);
        hour5 = findViewById(R.id.hour5);
        hour6 = findViewById(R.id.hour6);
        hour7 = findViewById(R.id.hour7);
    }

    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference userRef = fStore.collection("Students").document(userID3);
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(TimeTableActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }

                if (documentSnapshot.exists()){
                    classroomFinder classroom = documentSnapshot.toObject(classroomFinder.class);
                    assert classroom != null;
                    Year=classroom.getYear();
                    Classroom = classroom.getClassroom();
                    displayHours(Year, Classroom);
                }
            }
        });
    }


    protected void displayHours(String Year, String Classroom) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        if (dayOfTheWeek.equalsIgnoreCase("Friday") || dayOfTheWeek.equalsIgnoreCase("Saturday")){ dayOfTheWeek = "Monday"; }

        DocumentReference userRef = fStore.collection(dayOfTheWeek).document("Class "+Year +"_" +Classroom);
        String finalDayOfTheWeek = dayOfTheWeek;
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null){
                    Toast.makeText(TimeTableActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }

                if (documentSnapshot.exists()){
                    dayNameView.setText(finalDayOfTheWeek);
                    hour1.setText(documentSnapshot.getString("Hour 01"));
                    hour2.setText(documentSnapshot.getString("Hour 02"));
                    hour3.setText(documentSnapshot.getString("Hour 03"));
                    hour4.setText(documentSnapshot.getString("Hour 04"));
                    hour5.setText(documentSnapshot.getString("Hour 05"));
                    hour6.setText(documentSnapshot.getString("Hour 06"));
                    hour7.setText(documentSnapshot.getString("Hour 07"));
                }
            }
        });
    }

}