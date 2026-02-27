package com.nhom1.kttstoreapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.model.Product;
import com.nhom1.kttstoreapp.util.CartManager;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage, ivFavorite;
    private TextView tvProductName, tvProductPrice, tvOriginalPrice;
    private Button btnAddToCart, btnBuyNow;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product = (Product) getIntent().getSerializableExtra("product");

        initViews();

        if (product != null) {
            bindData();
        }

        setupListeners();
    }

    private void initViews() {
        ivProductImage = findViewById(R.id.ivProductImage);
        ivFavorite = findViewById(R.id.ivFavorite);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        // Setup Toolbar back button
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void bindData() {
        tvProductName.setText(product.getName());

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvProductPrice.setText(format.format(product.getPrice()));

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(this)
                    .load(product.getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(ivProductImage);
        }
    }

    private void setupListeners() {
        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                CartManager.getInstance().addProduct(product);
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        btnBuyNow.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng mua ngay đang phát triển", Toast.LENGTH_SHORT).show();
        });

        ivFavorite.setOnClickListener(v -> {
            Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
        });
    }
}
