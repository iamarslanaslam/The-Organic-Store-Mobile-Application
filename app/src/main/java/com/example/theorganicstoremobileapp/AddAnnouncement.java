package com.example.theorganicstoremobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.theorganicstoremobileapp.models.Announcement;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddAnnouncement extends AppCompatActivity {
    private EditText editTextTitle, editTextDate, editTextType, editTextDescription;
    private Button buttonAddAnnouncement;
    private FirebaseFirestore db;

    private Announcement announcement; // To hold the announcement being edited
    private boolean isEditing = false; // Flag to check if we are in edit mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        db = FirebaseFirestore.getInstance();

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDate = findViewById(R.id.editTextDate);
        editTextType = findViewById(R.id.editTextType);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonAddAnnouncement = findViewById(R.id.buttonAddAnnouncement);

        // Check if we are editing an announcement
        Intent intent = getIntent();
        if (intent.hasExtra("announcement")) {
            announcement = (Announcement) intent.getSerializableExtra("announcement");
            if (announcement != null) {
                isEditing = true;
                populateFields(announcement);
                buttonAddAnnouncement.setText("Update Announcement");
            } else {
                Toast.makeText(this, "Error loading announcement", Toast.LENGTH_SHORT).show();
            }
        }

        buttonAddAnnouncement.setOnClickListener(v -> {
            if (isEditing) {
                updateAnnouncement();
            } else {
                addAnnouncement();
            }
        });
    }

    private void populateFields(Announcement announcement) {
        editTextTitle.setText(announcement.getTitle());
        editTextDate.setText(announcement.getDate());
        editTextType.setText(announcement.getType());
        editTextDescription.setText(announcement.getDescription());
    }

    private void addAnnouncement() {
        String title = editTextTitle.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(type) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = db.collection("announcements").document().getId();
        Announcement newAnnouncement = new Announcement(id, title, date, type, description);

        db.collection("announcements")
                .document(id)
                .set(newAnnouncement)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddAnnouncement.this, "Announcement added successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddAnnouncement.this, "Error adding announcement", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateAnnouncement() {
        String title = editTextTitle.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String type = editTextType.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(type) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the announcement with the new values
        announcement.setTitle(title);
        announcement.setDate(date);
        announcement.setType(type);
        announcement.setDescription(description);

        db.collection("announcements")
                .document(announcement.getId())
                .set(announcement)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddAnnouncement.this, "Announcement updated successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddAnnouncement.this, "Error updating announcement", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        editTextTitle.setText("");
        editTextDate.setText("");
        editTextType.setText("");
        editTextDescription.setText("");
        isEditing = false;
        buttonAddAnnouncement.setText("Add Announcement");
        finish();
    }
}
