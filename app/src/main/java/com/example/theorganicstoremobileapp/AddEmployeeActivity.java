package com.example.theorganicstoremobileapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.theorganicstoremobileapp.models.Employee;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;

public class AddEmployeeActivity extends AppCompatActivity {


    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPhone,
            editTextPosition, editTextSalary, editTextDepartment;
    private Button buttonAddEmployee;

    private FirebaseFirestore db;
    private String employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPosition = findViewById(R.id.editTextPosition);
        editTextSalary = findViewById(R.id.editTextSalary);
        editTextDepartment = findViewById(R.id.editTextDepartment);
        buttonAddEmployee = findViewById(R.id.buttonAddEmployee);

        // Get the employee ID from intent
        employeeId = getIntent().getStringExtra("EMPLOYEE_ID");

        // Load employee data if ID is provided
        if (employeeId != null) {
            loadEmployeeData(employeeId);
            buttonAddEmployee.setText("Update Employee");
        }

        // Set up click listener for the add/update employee button
        buttonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employeeId != null) {
                    updateEmployee();
                } else {
                    addEmployee();
                }
            }
        });

    }

    private void loadEmployeeData(String employeeId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading employee data...");
        progressDialog.show();

        db.collection("employees").document(employeeId).get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {
                        Employee employee = documentSnapshot.toObject(Employee.class);
                        if (employee != null) {
                            editTextFirstName.setText(employee.getFirstName());
                            editTextLastName.setText(employee.getLastName());
                            editTextEmail.setText(employee.getEmail());
                            editTextPhone.setText(employee.getPhone());
                            editTextPosition.setText(employee.getPosition());
                            editTextSalary.setText(String.valueOf(employee.getSalary()));
                            editTextDepartment.setText(employee.getDepartment());
                        }
                    }
                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> Toast.makeText(AddEmployeeActivity.this, "Error loading employee data", Toast.LENGTH_SHORT).show());
    }

    private void addEmployee() {
        // Get data from input fields
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String position = editTextPosition.getText().toString().trim();
        String salaryString = editTextSalary.getText().toString().trim();
        String department = editTextDepartment.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(position) || TextUtils.isEmpty(salaryString) ||
                TextUtils.isEmpty(department)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double salary = Double.parseDouble(salaryString);

        // Create a new Employee object
        Employee employee = new Employee(
                String.valueOf(System.currentTimeMillis()), // Unique ID
                firstName,
                lastName,
                email,
                phone,
                position,
                salary,
                new Date(), // Current date as hire date
                department
        );

        // Add employee to Firestore
        db.collection("employees")
                .document(employee.getId())
                .set(employee)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddEmployeeActivity.this, "Employee added successfully", Toast.LENGTH_SHORT).show();
                    clearFields(); // Clear input fields
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddEmployeeActivity.this, "Error adding employee: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateEmployee() {
        // Get data from input fields (similar to addEmployee method)
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String position = editTextPosition.getText().toString().trim();
        String salaryString = editTextSalary.getText().toString().trim();
        String department = editTextDepartment.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(position) || TextUtils.isEmpty(salaryString) ||
                TextUtils.isEmpty(department)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double salary = Double.parseDouble(salaryString);

        // Create a new Employee object for update
        Employee employee = new Employee(
                employeeId, // Use the existing ID
                firstName,
                lastName,
                email,
                phone,
                position,
                salary,
                new Date(), // Current date as hire date
                department
        );

        // Update employee in Firestore
        db.collection("employees")
                .document(employee.getId())
                .set(employee)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddEmployeeActivity.this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddEmployeeActivity.this, "Error updating employee: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteEmployee() {
        if (employeeId != null) {
            db.collection("employees")
                    .document(employeeId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddEmployeeActivity.this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
                        clearFields(); // Clear input fields
                        finish(); // Close activity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddEmployeeActivity.this, "Error deleting employee: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void clearFields() {
        editTextFirstName.setText("");
        editTextLastName.setText("");
        editTextEmail.setText("");
        editTextPhone.setText("");
        editTextPosition.setText("");
        editTextSalary.setText("");
        editTextDepartment.setText("");
        finish();
    }
}
