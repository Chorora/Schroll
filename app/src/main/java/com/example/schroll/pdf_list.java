package com.example.schroll;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class pdf_list extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.Application.schroll.EXTRA_TITLE";
    public static final String EXTRA_COURSE = "EXTRA_COURSE";
    public static ArrayList<pdfmodel > documentArrayList;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    DocumentReference teacherRef, classXRef2;
    RecyclerView recyclerView;
    FirebaseStorage documents;
    String userID, course, j_but_on_string;
    ProgressBar progress;
    int j, limited = 5;
    String[] classRoomXD = new String[limited];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        teacherRef = fStore.collection("Users").document(userID);

        setContentView(R.layout.activity_pdf_list);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        course = intent.getStringExtra(MainActivity2.EXTRA_SPECIALTY1);

        classXRef2 = fStore.collection("Users").document(userID);

        classXRef2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                for (int j = 1; j < limited; j++) {
                    int finalJ = j;
                    j_but_on_string = Integer.toString(finalJ);
                    classRoomXD[finalJ] = documentSnapshot.getString("Class 0" + j_but_on_string);

                    if(classRoomXD[finalJ].equals("")){
                        classRoomXD[finalJ] = null;
                    }
                    PDFList(classRoomXD[finalJ]);
                }
            }
        });
    }

    public void PDFList(String classRoomXD) {
        documents = FirebaseStorage.getInstance();
        StorageReference listRef = documents.getReference().child("Pdf Uploads/" +classRoomXD +"/"  +course +"/");
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        System.out.println(listResult.getItems());
                        String[] array = new String[listResult.getItems().size()];
                        for(j = 0; j < listResult.getItems().size(); j++)
                        {
                            String title;
                            title = listResult.getItems().get(j).getName();
                            array[j] = title;

                            pdfmodel Model = new pdfmodel();
                            documentArrayList = new ArrayList<>();

                            String dt= listResult.getItems().get(j).getName();
                            listResult.getItems()
                                    .get(j).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Model.setName(title);
                                                Model.setData(dt);
                                                documentArrayList.add(Model);

                                                recyclerView = findViewById(R.id.recyclerview1);
                                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                PdfAdapter  adapter = new PdfAdapter (getApplicationContext(), documentArrayList);
                                                recyclerView.setAdapter(adapter);

                                                adapter.setOnItemClickListener(new PdfAdapter.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(int pos, View v) {
                                                        Intent intent = new Intent(getApplicationContext(), exercicePDFs.class);
                                                        intent.putExtra("pos", pos);
                                                        intent.putExtra(EXTRA_COURSE, course);
                                                        String titl = array[pos];

                                                        intent.putExtra(EXTRA_TITLE, titl);
                                                        startActivity(intent);
                                                    }
                                                });

                                                progress.setVisibility(View.GONE);
                                            }
                                    }
                            );
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"download unsuccessful",Toast.LENGTH_LONG).show();
            }
        });

    }
}