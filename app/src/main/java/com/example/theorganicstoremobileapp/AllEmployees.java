package com.example.theorganicstoremobileapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.adapters.EmployeesAdapter;
import com.example.theorganicstoremobileapp.models.Employee;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllEmployees extends AppCompatActivity {
    private FloatingActionButton fabAddAnnouncement;
    private RecyclerView recyclerViewAnnouncements;
    private EmployeesAdapter employeesAdapter;
    private List<Employee> employeeList;
    private FirebaseFirestore db;
    private SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employees);

        recyclerViewAnnouncements = findViewById(R.id.recyclerViewEmployees);

        db = FirebaseFirestore.getInstance();
        employeeList = new ArrayList<>();

        searchView = findViewById(R.id.searchViewEmployees);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchEmployees(newText);
                return false;
            }
        });

        fabAddAnnouncement = findViewById(R.id.addEmployee);
        fabAddAnnouncement.setOnClickListener(v -> {
            Intent intent = new Intent(AllEmployees.this, AddEmployeeActivity.class);
            startActivity(intent);
        });

        loadEmployees();
    }

    private void searchEmployees(String newText) {
        ArrayList<Employee> searchList = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getFirstName().toLowerCase().contains(newText.toLowerCase())
                    || employee.getLastName().toLowerCase().contains(newText.toLowerCase())
                    || employee.getEmail().toLowerCase().contains(newText.toLowerCase())
            ) {
                searchList.add(employee);
            }
        }
        employeesAdapter.filterEmployees(searchList);
    }


    private void loadEmployees() {
        db.collection("employees").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                employeeList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Employee announcement = document.toObject(Employee.class);
                    announcement.setId(document.getId());
                    employeeList.add(announcement);
                }

                employeesAdapter = new EmployeesAdapter(employeeList);
                recyclerViewAnnouncements.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewAnnouncements.setAdapter(employeesAdapter);
            } else {
                Toast.makeText(AllEmployees.this, "Error getting announcements", Toast.LENGTH_SHORT).show();
            }
        });
    }
}