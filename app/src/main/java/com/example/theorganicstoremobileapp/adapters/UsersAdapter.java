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
import com.example.theorganicstoremobileapp.RegistrationActivity;
import com.example.theorganicstoremobileapp.models.Employee;
import com.example.theorganicstoremobileapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ProductViewHolder> {
    private List<User> userList;

    public UsersAdapter(List<User> productList) {
        this.userList = productList;
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
        User user = userList.get(position);
        holder.productName.setText("Name :" + user.getFirstName() + " " + user.getLastName());
        holder.productPrice.setText(
                "Email: " + user.getEmail()
                        + "\nGender: " + user.getGender()
                        + "\nPhone: " + user.getContactNo()
                        + "\nRole: " + user.getRole()
                        + "\nAddress: " + user.getAddress()
                        + "\nDate Of Registration : " + user.getDateOfRegistration()
        );

        holder.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), RegistrationActivity.class);
            intent.putExtra("userId", user.getId());
            holder.itemView.getContext().startActivity(intent);
        });
        holder.btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete product from the database
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(user.getId()).delete();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
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


    public void filterList(List<User> filteredList) {
        userList = filteredList;
        notifyDataSetChanged();
    }

}
