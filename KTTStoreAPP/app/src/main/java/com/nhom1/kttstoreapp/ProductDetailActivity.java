package com.nhom1.kttstoreapp;

<<<<<<< HEAD
import android.os.Bundle;
import android.widget.Button;
=======
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
>>>>>>> upstream/main
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

<<<<<<< HEAD
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.model.Product;
import com.nhom1.kttstoreapp.util.CartManager;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";
    public static final String EXTRA_PRODUCT_NAME = "extra_product_name";
    public static final String EXTRA_PRODUCT_PRICE = "extra_product_price";
    public static final String EXTRA_PRODUCT_IMAGE = "extra_product_image";
    public static final String EXTRA_PRODUCT_RATING = "extra_product_rating";
    public static final String EXTRA_PRODUCT_DESCRIPTION = "extra_product_description";
    public static final String EXTRA_PRODUCT_CATEGORY_ID = "extra_product_category_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageView ivProductImage = findViewById(R.id.ivProductImage);
        TextView tvProductName = findViewById(R.id.tvProductName);
        TextView tvProductPrice = findViewById(R.id.tvProductPrice);
        TextView tvProductRating = findViewById(R.id.tvProductRating);
        TextView tvProductDescription = findViewById(R.id.tvProductDescription);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);

        String productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        String name = getIntent().getStringExtra(EXTRA_PRODUCT_NAME);
        double price = getIntent().getDoubleExtra(EXTRA_PRODUCT_PRICE, 0);
        String image = getIntent().getStringExtra(EXTRA_PRODUCT_IMAGE);
        double rating = getIntent().getDoubleExtra(EXTRA_PRODUCT_RATING, 0);
        String description = getIntent().getStringExtra(EXTRA_PRODUCT_DESCRIPTION);
        String categoryId = getIntent().getStringExtra(EXTRA_PRODUCT_CATEGORY_ID);

        // Create Product object from intent data
        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        product.setPrice(price);
        product.setImage(image);
        product.setRating(rating);
        product.setDescription(description);
        product.setCategoryId(categoryId);

        tvProductName.setText(name);

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvProductPrice.setText(format.format(price));

        tvProductRating.setText(String.valueOf(rating));

        if (description != null && !description.isEmpty()) {
            tvProductDescription.setText(description);
        }

        Glide.with(this).load(image).into(ivProductImage);

        btnAddToCart.setOnClickListener(v -> {
            CartManager.getInstance().addProduct(product);
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }
}

=======
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.model.Product;

import java.text.DecimalFormat;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivProductImage, ivFavorite;
    private TextView tvProductName, tvProductPrice, tvOriginalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ivProductImage = findViewById(R.id.ivProductImage);
        ivFavorite = findViewById(R.id.ivFavorite);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvOriginalPrice = findViewById(R.id.tvOriginalPrice);

        Product product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            displayProductDetails(product);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }

        ivFavorite.setOnClickListener(v -> {
            boolean currentState = product != null && product.isFavorite();
            if (product != null)
                product.setFavorite(!currentState);
            updateFavoriteIcon(!currentState);
            Toast.makeText(this, !currentState ? "Đã thêm vào yêu thích" : "Đã bỏ yêu thích", Toast.LENGTH_SHORT)
                    .show();
        });

        findViewById(R.id.btnAddToCart).setOnClickListener(v -> {
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnBuyNow).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng mua ngay đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void displayProductDetails(Product product) {
        tvProductName.setText(product.getName());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,### đ");
        tvProductPrice.setText(decimalFormat.format(product.getPrice()));

        // Mock original price for demo (price * 1.5)
        double originalPrice = product.getPrice() * 1.5;
        tvOriginalPrice.setText(decimalFormat.format(originalPrice));
        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(this)
                .load(product.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(ivProductImage);

        updateFavoriteIcon(product.isFavorite());
    }

    private void updateFavoriteIcon(boolean isFavorite) {
        if (isFavorite) {
            ivFavorite.setImageResource(R.drawable.ic_favorite_red);
        } else {
            ivFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
>>>>>>> upstream/main
