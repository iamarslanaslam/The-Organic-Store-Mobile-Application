package com.example.theorganicstoremobileapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theorganicstoremobileapp.adapters.CartAdapter;
import com.example.theorganicstoremobileapp.models.Order;
import com.example.theorganicstoremobileapp.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<Product> cartProducts;
    private FirebaseFirestore db;
    private String userId;

    private Button buttonPlaceOrder;

    private String publishableKey = "pk_test_51OMAzMJ9Tuk0GVYZIe2F6f0czHBtu7tO3FOOjzQHSwtmH0xPyk3MHxsHCZwu4fGYBY7ywO180FXXgiCxplcqFCIj006OhbYxNs";
    private String secretKey = "sk_test_51OMAzMJ9Tuk0GVYZtGVe5Vtf0a5QLsZ2fAnWE7gCFt8FgRYNsDv1lhbNBwQmdDfWFs5xotxwil8AX6qIMihl5LcN00FeIAONQH";
    private PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        cartProducts = new ArrayList<>();
        cartAdapter = new CartAdapter(cartProducts);
        recyclerViewCart.setAdapter(cartAdapter);
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
        PaymentConfiguration.init(
                getApplicationContext(), // Application context
                publishableKey
        );

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadCartProducts();

        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateStripePayment();
            }
        });
    }

    private void loadCartProducts() {
        db.collection("users").document(userId).collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cartProducts.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        cartProducts.add(product);
                    }
                    cartAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(CartActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show());
    }

    private void initiateStripePayment() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest createCustomerRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers",
                response -> {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        String customerId = responseObject.getString("id");
                        createEphemeralKey(requestQueue, customerId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Failed to create customer", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
                return headers;
            }
        };
        requestQueue.add(createCustomerRequest);
    }

    private void createEphemeralKey(RequestQueue requestQueue, String customerId) {
        StringRequest createEphemeralKeyRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                response -> {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        String ephemeralKey = responseObject.getString("secret");
                        createPaymentIntent(requestQueue, customerId, ephemeralKey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Failed to create ephemeral key", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
                headers.put("Stripe-Version", "2022-08-01");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerId);
                return params;
            }
        };
        requestQueue.add(createEphemeralKeyRequest);
    }

    private void createPaymentIntent(RequestQueue requestQueue, String customerId, String ephemeralKey) {
        StringRequest createPaymentIntentRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                response -> {
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        String clientSecret = responseObject.getString("client_secret");
                        Toast.makeText(this, "Client Secret: " + clientSecret, Toast.LENGTH_SHORT).show();
                        presentPaymentSheet(clientSecret, customerId, ephemeralKey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Failed to create payment intent", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("amount", "1099"); // Amount in smallest currency unit (e.g., cents for EUR)
                params.put("currency", "eur");
                params.put("customer", customerId);
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };
        requestQueue.add(createPaymentIntentRequest);
    }

    private void presentPaymentSheet(String clientSecret, String customerId, String ephemeralKey) {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration(
                "Your Store Name",
                new PaymentSheet.CustomerConfiguration(customerId, ephemeralKey)
        );

        paymentSheet.presentWithPaymentIntent(clientSecret, configuration);
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
            // Optional: Call placeOrder() on successful payment
            placeOrder();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "Payment failed: " + ((PaymentSheetResult.Failed) paymentSheetResult).getError(), Toast.LENGTH_LONG).show();
        }
    }
    private void placeOrder() {
        String id = db.collection("orders").document().getId();
        Order order = new Order(id,userId, cartProducts, "Pending");
        db.collection("orders").add(order);
        db.collection("users").document(userId).collection("cart").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete();
                    }
                    loadCartProducts();
                });

        Toast.makeText(CartActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();

    }
}

