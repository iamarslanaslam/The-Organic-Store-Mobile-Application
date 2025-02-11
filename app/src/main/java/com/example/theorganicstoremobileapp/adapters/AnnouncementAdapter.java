package com.example.theorganicstoremobileapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theorganicstoremobileapp.AddAnnouncement;
import com.example.theorganicstoremobileapp.R;
import com.example.theorganicstoremobileapp.models.Announcement;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ProductViewHolder> {
    private List<Announcement> announcementList;

    public AnnouncementAdapter(List<Announcement> productList) {
        this.announcementList = productList;
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
        Announcement product = announcementList.get(position);
        holder.productName.setText("Title :"+product.getTitle());
        holder.productPrice.setText("Description: "+product.getDescription()
                +"\nDate: "+product.getDate()
                +"\nType: "+product.getType()
        );
        holder.btnEditProduct.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), AddAnnouncement.class);
            intent.putExtra("announcement", product);
//            intent.putExtra("announcementId", product.getId());
//            intent.putExtra("announcementTitle", product.getTitle());
//            intent.putExtra("announcementDate", product.getDate());
//            intent.putExtra("announcementType", product.getType());
//            intent.putExtra("announcementDescription", product.getDescription());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.btnDeleteProduct.setOnClickListener(v -> {
            // Delete product from the database
            FirebaseFirestore.getInstance().collection("announcements")
                    .document(product.getId())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the product from the list
                        announcementList.remove(product);
                        notifyDataSetChanged();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return announcementList.size();
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


    public void filterList(List<Announcement> filteredList) {
        announcementList = filteredList;
        notifyDataSetChanged();
    }

}
