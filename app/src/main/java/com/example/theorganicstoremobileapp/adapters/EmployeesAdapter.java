package com.example.theorganicstoremobileapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.AddEmployeeActivity;
import com.example.theorganicstoremobileapp.R;
import com.example.theorganicstoremobileapp.models.Employee;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ProductViewHolder> {
    private List<Employee> employeeList;

    public EmployeesAdapter(List<Employee> productList) {
        this.employeeList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.productName.setText("Name :"+employee.getFirstName() + " " + employee.getLastName());
        holder.productPrice.setText(
                "Email: "+employee.getEmail()
                +"\nPhone: "+employee.getPhone()
                +"\nPosition: "+employee.getPosition()
                +"\nSalary: "+employee.getSalary()
                +"\nDepartment: "+employee.getDepartment()
        );
        holder.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AddEmployeeActivity.class);
            intent.putExtra("EMPLOYEE_ID", employee.getId());
//            intent.putExtra("announcementId", product.getId());
//            intent.putExtra("announcementTitle", product.getTitle());
//            intent.putExtra("announcementDate", product.getDate());
//            intent.putExtra("announcementType", product.getType());
//            intent.putExtra("announcementDescription", product.getDescription());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.btnDeleteProduct.setOnClickListener(v -> {
            // Delete product from the database
            FirebaseFirestore.getInstance().collection("products")
                    .document(employee.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the product from the list
                        employeeList.remove(employee);
                        notifyDataSetChanged();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public void filterEmployees(ArrayList<Employee> searchList) {
        employeeList = searchList;

        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        Button btnEditProduct;
        Button btnDeleteProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            btnEditProduct = itemView.findViewById(R.id.edit_product);
            btnDeleteProduct = itemView.findViewById(R.id.delete_product);
        }
    }
}
