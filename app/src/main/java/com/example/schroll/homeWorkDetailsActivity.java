package com.example.schroll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class homeWorkDetailsActivity extends AppCompatActivity {

    EditText editText;
    TextView courseViewText,homeWorkGrade, deadLine, homeWorkDescription;
    Button button;
    StorageReference storageReference;
    String userID, Surname, pdfLocationName, Y, C;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference, reference, homeworkRef, studentRef;
    int courseNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_details);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        documentReference = fStore.collection("Pdf Uploads").document(userID);
        reference = fStore.collection("Grades").document(userID);

        editText = findViewById(R.id.selectPDF);
        button = findViewById(R.id.uploadPDF);
        homeWorkGrade = findViewById(R.id.noteView);
        deadLine = findViewById(R.id.delaiView);
        homeWorkDescription = findViewById(R.id.descriptionView);

        button.setEnabled(false);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });

        courseViewText = findViewById(R.id.courseNameView);
        Intent intent = getIntent();

        courseNumber = intent.getIntExtra(String.valueOf(homeWorksActivity.EXTRA_NUMBER2), -1);
        if (courseNumber == 1) {pdfLocationName = "Math"; courseViewText.setText(pdfLocationName);}
        if (courseNumber == 2) {pdfLocationName = "Science"; courseViewText.setText(pdfLocationName);}
        if (courseNumber == 3) {pdfLocationName = "Physics" ; courseViewText.setText(pdfLocationName);}
        if (courseNumber == 4) {pdfLocationName = "Art"; courseViewText.setText(pdfLocationName);}
        if (courseNumber == 5) {pdfLocationName = "Islamic" ;courseViewText.setText(pdfLocationName);}
        if (courseNumber == 6) {pdfLocationName = "Arabic" ;courseViewText.setText(pdfLocationName);}
        if (courseNumber == 7) {pdfLocationName = "English" ;courseViewText.setText(pdfLocationName);}

        setHomeWorkGrade();
        getStudentSurname();
        getClassStudent();
    }

    private void getClassStudent() {
        studentRef = fStore.collection("Students").document(userID);
        studentRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
             Y = documentSnapshot.getString("Year");
             C = documentSnapshot.getString("Classroom");
                sethomeWorkDetails(Y, C);
            }
        });
    }

    private void sethomeWorkDetails(String Y, String C) {
        homeworkRef = fStore.collection("Home Works").document("Class " +Y +"_" +C);

        homeworkRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable  DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                deadLine.setText(documentSnapshot.getString("HW 0" +courseNumber +" END"));
                homeWorkDescription.setText(documentSnapshot.getString("HW 0" +courseNumber));
            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null){
            button.setEnabled(true);
            editText.setText(data.getDataString()
                    .substring(data.getDataString().lastIndexOf("/") + 1));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPDF(data.getData());
                }
            });
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        StorageReference pdfRef = storageReference.child("Pdf Uploads/" +pdfLocationName +"/" +Surname +".pdf");
        pdfRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        putPDF putPDF = new putPDF(editText.getText().toString(), uri.toString());
                        UploadTask uploadTask = storageReference.child(documentReference.toString()).putFile(Uri.parse(userID));
                        Toast.makeText(homeWorkDetailsActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("File Uploading... " +(int) progress +"%");
            }
        });
    }

    public void setHomeWorkGrade() {

        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
             homeWorkGrade.setText(documentSnapshot.getString("Grade " +pdfLocationName));
            }
        });
    }

    public void getStudentSurname(){
        DocumentReference documentReference = fStore.collection("Students").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Surname = documentSnapshot.getString("Surname");
                }
            }
        });
    }
}