package com.example.theorganicstoremobileapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.adapters.UsersAdapter;
import com.example.theorganicstoremobileapp.models.Role;
import com.example.theorganicstoremobileapp.models.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AllUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAnnouncements;
    private UsersAdapter eployeesAdapter;
    private List<User> userList;
    private FirebaseFirestore db;
    private TextView tvTitle;

    private String userType;
    private SearchView etSearch;

    private FloatingActionButton floatingActionButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        getDataFromIntent();
        recyclerViewAnnouncements = findViewById(R.id.recyclerViewUsers);
        tvTitle = findViewById(R.id.tvTitle);
        db = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();
        loadEmployees();
        tvTitle.setText("All " + userType + "S");
        etSearch = findViewById(R.id.searchUser);
        etSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEmployees(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchEmployees(newText);
                return false;
            }
        });

        floatingActionButton = findViewById(R.id.fabAddUser);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AllUsersActivity.this, RegistrationActivity.class);
                intent.putExtra("DoAdd",true);
                startActivity(intent);
            }
        });


    }

    private void searchEmployees(String query) {
        //seach in local list
        List<User> searchList = new ArrayList<>();
        for (User user : userList) {
            if (user.getFirstName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
                    || user.getLastName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
                    || user.getEmail().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
                    || user.getContactNo().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
                    || user.getRole().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
                    || user.getDateOfRegistration().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                searchList.add(user);
            }

        }
        eployeesAdapter.filterList(searchList);

    }

    private void getDataFromIntent() {
        String type = getIntent().getStringExtra("userType");
        switch (type) {
            case "moderator":
                userType = Role.MODERATOR;
                break;
            case "user":
                userType = Role.USER;
                break;
            case "admin":
                userType = Role.ADMIN;
                break;
        }

    }


    private void loadEmployees() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User User = document.toObject(User.class);
                    if (User.getRole().toLowerCase(Locale.getDefault()).equals(userType.toLowerCase(Locale.getDefault()))) {
                        userList.add(User);
                    }
                }

                eployeesAdapter = new UsersAdapter(userList);
                recyclerViewAnnouncements.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewAnnouncements.setAdapter(eployeesAdapter);
            } else {
                Toast.makeText(AllUsersActivity.this, "Error getting announcements", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        });
    }
}