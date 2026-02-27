package com.nhom1.kttstoreapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView ivBack;
    private TextView tvToolbarTitle;
    private TextView tvOrderDate;
    private TextView tvOrderStatus;
    private TextView tvDeliveryStatus;
    private TextView tvPaymentStatus;
    private TextView tvCustomerName;
    private TextView tvCustomerPhone;
    private TextView tvShippingAddress;
    private RecyclerView rvOrderItems;
    private TextView tvSubtotal;
    private TextView tvDiscount;

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
        ivBack = findViewById(R.id.ivBack);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvDeliveryStatus = findViewById(R.id.tvDeliveryStatus);
        tvPaymentStatus = findViewById(R.id.tvPaymentStatus);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDiscount = findViewById(R.id.tvDiscount);

        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new OrderItemAdapter(this, new java.util.ArrayList<>());
        rvOrderItems.setAdapter(itemAdapter);

        ivBack.setOnClickListener(v -> finish());
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
        if (currentOrder == null)
            return;

        // Toolbar title
        String shortId = currentOrder.getId() != null && currentOrder.getId().length() > 6
                ? currentOrder.getId().substring(currentOrder.getId().length() - 6)
                : currentOrder.getId();
        tvToolbarTitle.setText("Chi tiết đơn hàng #" + shortId);

        // Date
        if (currentOrder.getCreatedAt() != null && !currentOrder.getCreatedAt().isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        Locale.getDefault());
                Date date = inputFormat.parse(currentOrder.getCreatedAt());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                tvOrderDate.setText("Ngày đặt: " + outputFormat.format(date));
            } catch (ParseException e) {
                tvOrderDate.setText("Ngày đặt: " + currentOrder.getCreatedAt());
            }
        } else {
            tvOrderDate.setText("Ngày đặt: N/A");
        }

        tvOrderStatus.setText("Trạng thái đơn hàng: " + currentOrder.getStatusDisplay());

        // Delivery logic
        String deliveryText = "Đang xử lý";
        if ("shipping".equals(currentOrder.getStatus()))
            deliveryText = "Đang giao hàng";
        else if ("delivered".equals(currentOrder.getStatus()))
            deliveryText = "Đã giao hàng thành công";
        else if ("preparing".equals(currentOrder.getStatus()))
            deliveryText = "Đang chuẩn bị";
        else if ("cancelled".equals(currentOrder.getStatus()))
            deliveryText = "Đã hủy giao";
        tvDeliveryStatus.setText("Trạng thái giao hàng: " + deliveryText);

        tvPaymentStatus.setText(
                "Thanh toán: " + ("pending".equals(currentOrder.getStatus()) ? "Chưa thanh toán" : "Đã thanh toán"));

        tvCustomerName.setText("Họ tên: " + currentOrder.getCustomerName());
        tvCustomerPhone
                .setText("Số điện thoại: " + (currentOrder.getPhone() != null ? currentOrder.getPhone() : "N/A"));
        tvShippingAddress.setText(
                "Địa chỉ: " + (currentOrder.getShippingAddress() != null ? currentOrder.getShippingAddress() : "N/A"));

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvSubtotal.setText(format.format(currentOrder.getTotalAmount()));

        // Placeholder for discount if any
        tvDiscount.setText("0đ");

        // Update items
        itemAdapter = new OrderItemAdapter(this, currentOrder.getItems());
        rvOrderItems.setAdapter(itemAdapter);
    }
}
