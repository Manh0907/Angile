package com.nhom1.kttstoreapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.adapter.CartAdapter;
import com.nhom1.kttstoreapp.model.CartItem;
import com.nhom1.kttstoreapp.util.CartManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.nhom1.kttstoreapp.model.Product;

import java.util.ArrayList;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView rvCartItems;
    private CartAdapter adapter;
    private List<CartItem> cartList;
    private TextView tvAddress;
    private TextView tvUserName, tvUserPhone, tvUserEmail;
    private CartManager cartManager;

    private TextView tvTotalPrice;
    private Button btnCheckout;
    private com.google.android.material.chip.Chip chipVoucher;
    private Button btnApplyVoucher;
    private EditText etVoucherCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView(view);
        cartManager = CartManager.getInstance();
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartItems();
        updateTotalPrice();
    }

    private void updateCartItems() {
        if (adapter != null) {
            cartList.clear();
            cartList.addAll(cartManager.getCartItems());
            adapter.notifyDataSetChanged();
        }
    }

    private void updateTotalPrice() {
        if (tvTotalPrice == null)
            return;
        double total = 0;
        for (CartItem item : cartList) {
            if (item.isSelected()) {
                total += item.getTotalPrice();
            }
        }

        if (chipVoucher != null && chipVoucher.getVisibility() == View.VISIBLE) {
            total = total * 0.9; // Giảm 10%
        }

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText("Tổng cộng: " + format.format(total));
    }

    private void initView(View view) {
        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        // Setup RecyclerView
        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCartItems.setNestedScrollingEnabled(false); // Disable scrolling to work well inside NestedScrollView

        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        chipVoucher = view.findViewById(R.id.chipVoucher);
        btnApplyVoucher = view.findViewById(R.id.btnApplyVoucher);
        etVoucherCode = view.findViewById(R.id.etVoucherCode);

        btnCheckout.setOnClickListener(v -> {
            boolean hasSelected = false;
            for (CartItem item : cartList) {
                if (item.isSelected()) {
                    hasSelected = true;
                    break;
                }
            }
            if (!hasSelected) {
                Toast.makeText(getContext(), "Vui lòng chọn sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Chức năng thanh toán đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });

        btnApplyVoucher.setOnClickListener(v -> {
            String code = etVoucherCode.getText().toString().trim();
            if (!code.isEmpty()) {
                chipVoucher.setText(code + " - Đã áp dụng 10%");
                chipVoucher.setVisibility(View.VISIBLE);
                updateTotalPrice();
            } else {
                Toast.makeText(getContext(), "Vui lòng nhập mã giảm giá", Toast.LENGTH_SHORT).show();
            }
        });

        chipVoucher.setOnCloseIconClickListener(v -> {
            chipVoucher.setVisibility(View.GONE);
            etVoucherCode.setText("");
            updateTotalPrice();
        });
    }

    private void initData() {
        cartList = new ArrayList<>(cartManager.getCartItems());
        adapter = new CartAdapter(getContext(), cartList, this);
        rvCartItems.setAdapter(adapter);
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        cartManager.updateQuantity(item.getProduct().getId(), newQuantity);
        updateTotalPrice();
    }

    @Override
    public void onDeleteClick(CartItem item, int position) {
        cartManager.removeProduct(item.getProduct().getId());
        updateCartItems();
        updateTotalPrice();
        Toast.makeText(getContext(), "Đã xóa " + item.getProduct().getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemChecked(CartItem item, boolean isChecked) {
        item.setSelected(isChecked);
        updateTotalPrice();
    }
}
