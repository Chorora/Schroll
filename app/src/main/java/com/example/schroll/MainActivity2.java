package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    String specialty;
    public static final String EXTRA_SPECIALTY1 = "EXTRA_SPECIALTY1";
    public static final String EXTRA_SPECIALTY2 = "EXTRA_SPECIALTY2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent = getIntent();
        specialty = intent.getStringExtra(loginActivity.EXTRA_SPECIALTY);
    }

    public void onViewClick(View v){
        Intent intent = new Intent(getApplicationContext(), pdf_list.class);
        intent.putExtra(EXTRA_SPECIALTY1, specialty);
        startActivity(intent);
    }

    public void onUpdateClick(View v){
        Intent intent = new Intent(getApplicationContext(), chooseClassroomActivity.class);
        intent.putExtra(EXTRA_SPECIALTY2, specialty);
        startActivity(intent);
    }

    public void onUploadClick(View v){
        Intent intent = new Intent(getApplicationContext(), chooseClassroomActivity2.class);
        intent.putExtra(EXTRA_SPECIALTY2, specialty);
        startActivity(intent);
    }

    public void onAddGradesClick(View v){
        Intent intent = new Intent(getApplicationContext(), addGradesActivity.class);
        intent.putExtra(EXTRA_SPECIALTY2, specialty);
        startActivity(intent);
    }

    public void onViewProfileClick(View v){
        Intent intent = new Intent(getApplicationContext(), teachProfileActivity.class);
        startActivity(intent);
    }

    public void signOut(View v){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), loginActivity.class));
        finish();
    }

}