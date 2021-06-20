package com.example.schroll;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class uploadCourseActivity extends AppCompatActivity {
    StorageReference storageReference;
    EditText courseNameInput;
    TextView classNameView;
    ImageView fileIcon;
    Button button;
    String classLocationName, courseName, courseNameInputX = null;
    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_course);
        storageReference = FirebaseStorage.getInstance().getReference();
        fileIcon = (ImageView) findViewById(R.id.imageView2);
        courseNameInput = findViewById(R.id.courseName);
        button = findViewById(R.id.uploadPDF2);
        classNameView = findViewById(R.id.classNameView);
        button.setEnabled(false);

        Intent intent = getIntent();
        classLocationName = intent.getStringExtra(chooseClassroomActivity2.EXTRA_CLASSROOM);
        courseName = intent.getStringExtra(chooseClassroomActivity2.EXTRA_COURSE);
        classNameView.setText(classLocationName);

        fileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 15);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 15 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            button.setEnabled(true);
            Toast.makeText(this, " PDF file selected", Toast.LENGTH_SHORT).show();
            fileIcon.setImageResource(R.drawable.file_selected_icon_1);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    courseNameInputX = courseNameInput.getText().toString();
                    if (courseNameInputX.matches("")) {
                        Toast.makeText(uploadCourseActivity.this, "Please name your file", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadPDF(data.getData());
                    }
                }
            });
        }
    }

    private void uploadPDF(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        StorageReference courseRef = storageReference.child("Course Uploads/" + classLocationName + "/" + courseName + "/" + courseNameInputX + ".pdf");

        courseRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        Uri uri = uriTask.getResult();
                        Toast.makeText(uploadCourseActivity.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Lesson file Uploading... " + (int) progress + "%");
            }
        });
    }

}