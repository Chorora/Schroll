package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shockwave.pdfium.PdfDocument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class exercicePDFs extends AppCompatActivity implements OnLoadCompleteListener,OnPageChangeListener {
    private static final String KEY_GRADE ="Grade";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName, title, userID, course;
    int pdf_index;
    Handler mHandler,handler;
    EditText gradeHW;
    TextView textView;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercice_p_d_fs);
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdf_index = getIntent().getIntExtra("pos" , 0);
        mHandler = new Handler();
        handler = new Handler();
        title = pdf_list.documentArrayList.get(pdf_index).getData();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        gradeHW = findViewById(R.id.hwGrade);
        textView = findViewById(R.id.textView17);

        Intent intent = getIntent();
        course = intent.getStringExtra(pdf_list.EXTRA_COURSE);

        cutName(title);
        FirebaseStorage mFirebaseStorage=FirebaseStorage.getInstance();
        StorageReference mmFirebaseStorageRef = mFirebaseStorage.getReference().child("Pdf Uploads/" +course +"/");
        final long ONE_MEGABYTE = 1024 * 1024;

        mmFirebaseStorageRef.child(pdf_list.documentArrayList.get(pdf_index).getData()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                pdfView.fromBytes(bytes).load();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"download unsuccessful",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference student1 = db.document("Students/Az1tFXxmiJYDOmtslTuqXdnA8jM2/");
    private DocumentReference student2 = db.document("Students/1ANAoz9YFKa2sZZMSFabd3J2qUq1/");
    private DocumentReference student3 = db.document("Students/SRnLO3Px4GgK6EmAMvzEi8TtPha2/");
    private DocumentReference student4 = db.document("Students/eZuJ8S4oQtf372pDnvwmgIjdQlx2/");
    private DocumentReference student5 = db.document("Students/ezlwp3ubldTFNUpNqde1RYZ801n1/");
    private DocumentReference student6 = db.document("Students/feTUxVKRb5VXontwQITqnqM4Jhk2/");
    private DocumentReference student7 = db.document("Students/u1pYHOLazLSstHINna0mYt4yjo72/");
    private DocumentReference student8 = db.document("Students/vPMoWARINdMeNlIqGA6AY0NEiqf1/");

    public void cutName(String title){

        title = title.substring(0,title.length() - 4 );

        if (title.matches("Jaber"))
        {
            student1.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                               String documentID = documentSnapshot.getId();
                               // textView.setText(documentID);
                                userRef = fStore.collection("Grades").document(""+documentID);
                            }
                        }
                    });
        }
        if (title.matches("Bendjeddou"))
        {
            student2.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String documentID = documentSnapshot.getId();
                                userRef = fStore.collection("Grades").document(""+documentID);
                            }
                        }
                    });
        }
        if (title.matches("Wejdi"))
        {
            student3.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String documentID = documentSnapshot.getId();
                                userRef = fStore.collection("Grades").document(""+documentID);
                            }
                        }
                    });
        }
        if (title.matches("Makhlouf"))
        {
            student4.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String documentID = documentSnapshot.getId();
                                userRef = fStore.collection("Grades").document(""+documentID);
                            }
                        }
                    });
        }
        if (title.matches("Belkader"))
        {
            student5.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String documentID = documentSnapshot.getId();
                                userRef = fStore.collection("Grades").document(""+documentID);
                            }
                        }
                    });
        }
        if (title.matches("Badis"))
        {
            student6.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String documentID = documentSnapshot.getId();
                                userRef = fStore.collection("Grades").document(""+documentID);
                            }
                        }
                    });
        }
        if (title.matches("Faouzi"))
        {
            student7.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String documentID = documentSnapshot.getId();
                                userRef = fStore.collection("Grades").document(""+documentID);
                            }
                        }
                    });
        }
        if (title.matches("Riyad"))
        {
            student8.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String documentID = documentSnapshot.getId();
                                userRef = fStore.collection("Grades").document(""+documentID);
                            }
                        }
                    });
        }
    }


    public void setGradeHW(View v) {
        String grade = gradeHW.getText().toString();

        Map<String, Object> Loc = new HashMap<>();
        String S = KEY_GRADE +" "+ course;
        Loc.put(S, grade);
        userRef.set(Loc, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(exercicePDFs.this, "Grade Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}