package com.nhom1.kttstoreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.adapter.OrderItemAdapter;
import com.nhom1.kttstoreapp.api.ApiClient;
import com.nhom1.kttstoreapp.api.ApiService;
import com.nhom1.kttstoreapp.model.Order;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderStatus;
    private TextView tvCustomerName;
    private TextView tvPhone;
    private TextView tvShippingAddress;
    private TextView tvTotalAmount;
    private RecyclerView rvOrderItems;
    private Button btnConfirmOrder;
    private Button btnPrepareOrder;

    private Order currentOrder;
    private OrderItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        String orderId = getIntent().getStringExtra("order_id");
        if (orderId == null) {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        loadOrderDetail(orderId);
    }

    private void initViews() {
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvPhone = findViewById(R.id.tvPhone);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnPrepareOrder = findViewById(R.id.btnPrepareOrder);

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new OrderItemAdapter(this, new java.util.ArrayList<>());
        rvOrderItems.setAdapter(itemAdapter);

        btnConfirmOrder.setOnClickListener(v -> confirmOrder());
        btnPrepareOrder.setOnClickListener(v -> prepareOrder());
    }

    private void loadOrderDetail(String orderId) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentOrder = response.body();
                    displayOrder();
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayOrder() {
        if (currentOrder == null) return;

        tvOrderStatus.setText(currentOrder.getStatusDisplay());
        tvCustomerName.setText("Tên: " + currentOrder.getCustomerName());
        tvPhone.setText("SĐT: " + currentOrder.getPhone());
        tvShippingAddress.setText("Địa chỉ: " + currentOrder.getShippingAddress());

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalAmount.setText(format.format(currentOrder.getTotalAmount()));

        // Set status background color
        int bgColor = getStatusColor(currentOrder.getStatus());
        tvOrderStatus.setBackgroundColor(bgColor);

        // Update order items
        itemAdapter = new OrderItemAdapter(this, currentOrder.getItems());
        rvOrderItems.setAdapter(itemAdapter);

        // Show/hide buttons based on status
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        String status = currentOrder.getStatus();
        btnConfirmOrder.setVisibility(View.GONE);
        btnPrepareOrder.setVisibility(View.GONE);

        if ("pending".equals(status)) {
            btnConfirmOrder.setVisibility(View.VISIBLE);
        } else if ("confirmed".equals(status)) {
            btnPrepareOrder.setVisibility(View.VISIBLE);
        }
    }

    private void confirmOrder() {
        updateOrderStatus("confirmed", "Đã xác nhận đơn hàng");
    }

    private void prepareOrder() {
        updateOrderStatus("preparing", "Đã chuẩn bị đơn hàng");
    }

    private void updateOrderStatus(String newStatus, String successMessage) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        ApiService.UpdateOrderStatusRequest request = new ApiService.UpdateOrderStatusRequest(newStatus);

        apiService.updateOrderStatus(currentOrder.getId(), request).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentOrder = response.body();
                    displayOrder();
                    Toast.makeText(OrderDetailActivity.this, successMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi cập nhật trạng thái đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getStatusColor(String status) {
        switch (status) {
            case "pending":
                return 0xFFFF9800; // Orange
            case "confirmed":
                return 0xFF2196F3; // Blue
            case "preparing":
                return 0xFF9C27B0; // Purple
            case "shipping":
                return 0xFF00BCD4; // Cyan
            case "delivered":
                return 0xFF4CAF50; // Green
            case "cancelled":
                return 0xFFF44336; // Red
            default:
                return 0xFF757575; // Grey
        }
    }
}
