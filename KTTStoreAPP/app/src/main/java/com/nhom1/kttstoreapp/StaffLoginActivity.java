package com.nhom1.kttstoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhom1.kttstoreapp.api.ApiClient;
import com.nhom1.kttstoreapp.api.ApiService;
import com.nhom1.kttstoreapp.model.AuthResponse;
import com.nhom1.kttstoreapp.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ImageView ivShowPassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check Keep Login
        android.content.SharedPreferences prefs = getSharedPreferences("KTTStoreStaffPrefs", MODE_PRIVATE);
        if (prefs.getBoolean("isStaffLoggedIn", false)) {
            startActivity(new Intent(StaffLoginActivity.this, StaffMainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_staff_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        ivShowPassword = findViewById(R.id.ivShowPassword);

        btnLogin.setOnClickListener(v -> loginStaff());

        ivShowPassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivShowPassword.setImageResource(android.R.drawable.ic_menu_view);
                isPasswordVisible = false;
            } else {
                etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
                ivShowPassword.setImageResource(android.R.drawable.ic_menu_revert);
                isPasswordVisible = true;
            }
            etPassword.setSelection(etPassword.getText().length());
        });
    }

    private void loginStaff() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(email, password);

        Call<AuthResponse> call = apiService.staffLogin(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Check if user is staff
                    if (response.body().getUser() != null && response.body().getUser().isStaff()) {
                        // Save Login State
                        android.content.SharedPreferences prefs = getSharedPreferences("KTTStoreStaffPrefs", MODE_PRIVATE);
                        android.content.SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isStaffLoggedIn", true);
                        editor.putString("staffEmail", email);
                        editor.putString("staffRole", response.body().getUser().getRole());
                        editor.putString("staffName", response.body().getUser().getName());
                        editor.apply();

                        Toast.makeText(StaffLoginActivity.this, "Đăng nhập thành công: " + response.body().getUser().getName(),
                                Toast.LENGTH_SHORT).show();
                        // Navigate to Staff Main Activity
                        Intent intent = new Intent(StaffLoginActivity.this, StaffMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(StaffLoginActivity.this, "Bạn không có quyền truy cập hệ thống nhân viên", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMsg = "Đăng nhập thất bại";
                    if (response.code() == 403) {
                        errorMsg = "Bạn không có quyền truy cập hệ thống nhân viên";
                    } else if (response.code() == 400) {
                        errorMsg = "Thông tin đăng nhập không đúng";
                    }
                    Toast.makeText(StaffLoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(StaffLoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
