package com.example.schroll;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class pdf_list extends AppCompatActivity {
    public static ArrayList<pdfmodel > documentArrayList;
    RecyclerView recyclerView;
    FirebaseStorage documents;
    String uri, course;
    ProgressBar progress;
    int j;


    public static final String EXTRA_TITLE = "com.example.Application.schroll.EXTRA_TITLE";
    public static final String EXTRA_COURSE = "EXTRA_COURSE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_list);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        course = intent.getStringExtra(MainActivity2.EXTRA_SPECIALTY1);
        PDFList();
    }

    public void PDFList() {
        documents = FirebaseStorage.getInstance();
        StorageReference listRef = documents.getReference().child("Pdf Uploads/" +course +"/");
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
                            System.out.println(j);
                            pdfmodel Model = new pdfmodel ();
                            documentArrayList = new ArrayList<>();
                            System.out.println(j);
                            String dt= listResult.getItems().get(j).getName();
                            listResult.getItems()
                                    .get(j).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Uri data = Uri.parse(uri.toString());
                                                Model.setName(title);
                                                Model.setData(dt);
                                                documentArrayList.add(Model);
                                                System.out.println("done");
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