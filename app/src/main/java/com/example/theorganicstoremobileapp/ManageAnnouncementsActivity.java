package com.example.theorganicstoremobileapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.adapters.AnnouncementAdapter;
import com.example.theorganicstoremobileapp.models.Announcement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageAnnouncementsActivity extends AppCompatActivity {

    private FloatingActionButton fabAddAnnouncement;
    private RecyclerView recyclerViewAnnouncements;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcementList;
    private FirebaseFirestore db;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_announcements);

        recyclerViewAnnouncements = findViewById(R.id.recyclerViewAnnouncements);

        db = FirebaseFirestore.getInstance();
        announcementList = new ArrayList<>();
        searchView = findViewById(R.id.searchViewAnoucement);
        loadAnnouncements();
        fabAddAnnouncement = findViewById(R.id.addAnnouncement);
        fabAddAnnouncement.setOnClickListener(v -> {
            Intent intent = new Intent(ManageAnnouncementsActivity.this, AddAnnouncement.class);
            startActivity(intent);
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAnnouncements(newText);
                return false;
            }
        });
    }

    private void searchAnnouncements(String newText) {
        //search inn the list onlu
        List<Announcement> searchList = new ArrayList<>();
        for (Announcement announcement : announcementList) {
            if (announcement.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || announcement.getDate().toLowerCase().contains(newText.toLowerCase())
                    || announcement.getType().toLowerCase().contains(newText.toLowerCase())) {
                searchList.add(announcement);
            }
        }
        announcementAdapter.filterList(searchList);

    }

    private void loadAnnouncements() {
        db.collection("announcements").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                announcementList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Announcement announcement = document.toObject(Announcement.class);
                    announcement.setId(document.getId());
                    announcementList.add(announcement);
                }

                announcementAdapter = new AnnouncementAdapter(announcementList);
                recyclerViewAnnouncements.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewAnnouncements.setAdapter(announcementAdapter);
            } else {
                Toast.makeText(ManageAnnouncementsActivity.this, "Error getting announcements", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
