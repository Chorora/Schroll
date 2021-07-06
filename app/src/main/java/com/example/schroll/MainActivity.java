package com.example.schroll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FeaturedAdapter.onCourseListener, OthersAdapter.onCoursesListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    MenuItem nav_out;
    RecyclerView featuredRecycler, othersRecycler;
    RecyclerView.Adapter featuredAdapter, othersAdapter;

    public static final String EXTRA_TEXT = "EXTRA_POSITION";
    public static final String EXTRA_TEXT2 = "EXTRA_POSITION2";
    private static final String TAG = "MainActivity";
    public String Year;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID5 = fAuth.getCurrentUser().getUid();
        featuredRecycler = findViewById(R.id.featured_recycler);
        othersRecycler = findViewById(R.id.othersRecycler);

        // ------------- Related to action bar ----------------
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        nav_out = findViewById(R.id.nav_log_out);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_log_out).setOnMenuItemClickListener(menuItem -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), loginActivity.class));
            finish();
            return true;
        });


        DocumentReference userRef = fStore.collection("Students").document(userID5);
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error !", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }

                if (documentSnapshot.exists()) {
                    classroomFinder classroom = documentSnapshot.toObject(classroomFinder.class);
                    assert classroom != null;
                    Year = classroom.getYear();
                    displayCourseName(Year);

                }
            }
        });
    }

    public void displayCourseName(String Year) {
        int i;
        final String[] c1 = {null, null, null, null, null, null};
        final String[] c2 = {null, null, null, null, null, null, null, null};


        for (i = 1; i <= 7; i++) {
            DocumentReference documentReference = fStore.collection("Year" + Year + " Courses").document("Matiere 0" + i);
            int finalI = i;
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (finalI == 1) {
                        c1[0] = documentSnapshot.getString("Name");
                        c1[1] = documentSnapshot.getString("Description");
                    }

                    if (finalI == 2) {
                        c1[2] = documentSnapshot.getString("Name");
                        c1[3] = documentSnapshot.getString("Description");
                    }

                    if (finalI == 3) {
                        c1[4] = documentSnapshot.getString("Name");
                        c1[5] = documentSnapshot.getString("Description");
                    }
                    featuredRecycler(c1[0], c1[1], c1[2], c1[3], c1[4], c1[5]);

                    if (finalI == 4) {
                        c2[0] = documentSnapshot.getString("Name");
                        c2[1] = documentSnapshot.getString("Description");
                    }

                    if (finalI == 5) {
                        c2[2] = documentSnapshot.getString("Name");
                        c2[3] = documentSnapshot.getString("Description");
                    }

                    if (finalI == 6) {
                        c2[4] = documentSnapshot.getString("Name");
                        c2[5] = documentSnapshot.getString("Description");
                    }

                    if (finalI == 7) {
                        c2[6] = documentSnapshot.getString("Name");
                        c2[7] = documentSnapshot.getString("Description");
                    }

                    othersRecycler(c2[0], c2[1], c2[2], c2[3], c2[4], c2[5], c2[6], c2[7]);
                }
            });
        }
    }


    private void featuredRecycler(String c1, String c11, String c2, String c22, String c3, String c33) {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.math_logo, c1, c11));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.science_logo, c2, c22));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.physics_logo, c3, c33));

        featuredAdapter = new FeaturedAdapter(featuredLocations, this);
        featuredRecycler.setAdapter(featuredAdapter);
    }


    private void othersRecycler(String c4, String c44, String c5, String c55, String c6, String c66, String c7, String c77) {

        othersRecycler.setHasFixedSize(true);
        othersRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<OthersHelperClass> othersLocations = new ArrayList<>();

        othersLocations.add(new OthersHelperClass(R.drawable.art_logo, c4, c44));
        othersLocations.add(new OthersHelperClass(R.drawable.islam_logo, c5, c55));
        othersLocations.add(new OthersHelperClass(R.drawable.arabic_logo, c6, c66));
        othersLocations.add(new OthersHelperClass(R.drawable.english_logo, c7, c77));

        othersAdapter = new OthersAdapter(othersLocations, this);
        othersRecycler.setAdapter(othersAdapter);
    }


    public void Course_01(View v) {
        startActivity(new Intent(this, Course_01_Activity.class));
    }

    //To avoid closing the application on pressing Back button using navigation bar
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;

            case R.id.nav_grades:
                startActivity(new Intent(this, GradesActivity.class));
                break;

            case R.id.nav_homeworks:
                startActivity(new Intent(this, homeWorksActivity.class));
                break;

            case R.id.nav_announcements:
                startActivity(new Intent(this, announcementsActivity.class));
                break;

            case R.id.nav_time_table:
                startActivity(new Intent(this, TimeTableActivity.class));
                break;

            case R.id.nav_profile:
                startActivity(new Intent(this, profileActivity.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCourseClick(int position) {
        Log.d(TAG, "onCourseClick: Clicked ." + position);
        int g = position;
        Intent intent = new Intent(this, Course_01_Activity.class);
        intent.putExtra(EXTRA_TEXT, g);
        startActivity(intent);
    }

    @Override
    public void onCoursesClick(int position) {
        Log.d(TAG, "onCoursesClick: Clicked ." + position);
        int k = position + 3;
        Intent intent = new Intent(this, Course_01_Activity.class);
        intent.putExtra(EXTRA_TEXT2, k);
        startActivity(intent);
    }
}