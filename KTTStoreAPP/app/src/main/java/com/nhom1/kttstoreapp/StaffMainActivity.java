package com.nhom1.kttstoreapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.adapter.StaffFeatureAdapter;

import java.util.ArrayList;
import java.util.List;

public class StaffMainActivity extends AppCompatActivity {

    private TextView tvStaffName;
    private Button btnLogout;
    private RecyclerView rvStaffFeatures;
    private StaffFeatureAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Check if staff is logged in
        SharedPreferences prefs = getSharedPreferences("KTTStoreStaffPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("isStaffLoggedIn", false)) {
            startActivity(new Intent(this, StaffLoginActivity.class));
            finish();
            return;
        }

        tvStaffName = findViewById(R.id.tvStaffName);
        btnLogout = findViewById(R.id.btnLogout);
        rvStaffFeatures = findViewById(R.id.rvStaffFeatures);

        String staffName = prefs.getString("staffName", "Nhân viên");
        String staffRole = prefs.getString("staffRole", "staff");

        tvStaffName.setText(staffName + " (" + (staffRole.equals("admin") ? "Quản trị viên" : "Nhân viên") + ")");

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, StaffLoginActivity.class));
            finish();
        });

        setupFeatures();
    }

    private void setupFeatures() {
        List<StaffFeature> features = new ArrayList<>();
        features.add(new StaffFeature("Quản lý sản phẩm", "Quản lý danh sách sản phẩm"));
        features.add(new StaffFeature("Quản lý đơn hàng", "Xem và xử lý đơn hàng"));
        features.add(new StaffFeature("Quản lý khách hàng", "Xem thông tin khách hàng"));
        features.add(new StaffFeature("Thống kê", "Xem báo cáo và thống kê"));

        adapter = new StaffFeatureAdapter(this, features);
        rvStaffFeatures.setLayoutManager(new GridLayoutManager(this, 2));
        rvStaffFeatures.setAdapter(adapter);
    }

    public static class StaffFeature {
        private String title;
        private String description;

        public StaffFeature(String title, String description) {
            this.title = title;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
}
