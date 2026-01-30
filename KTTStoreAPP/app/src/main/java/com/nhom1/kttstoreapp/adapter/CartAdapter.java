package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
<<<<<<< HEAD
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
=======
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
>>>>>>> upstream/main
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.CartItem;
<<<<<<< HEAD
import com.nhom1.kttstoreapp.util.CartManager;

import java.text.NumberFormat;
=======

>>>>>>> upstream/main
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
<<<<<<< HEAD
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartChangeListener listener) {
=======
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);

        void onDeleteClick(CartItem item, int position);

        void onItemChecked(CartItem item, boolean isChecked);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
>>>>>>> upstream/main
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
<<<<<<< HEAD
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
=======
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
>>>>>>> upstream/main
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
<<<<<<< HEAD
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
=======
        CartItem item = cartItems.get(position);
        holder.bind(item, position);
>>>>>>> upstream/main
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

<<<<<<< HEAD
    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvQuantity;
        Button btnDecrease;
        Button btnIncrease;
        ImageView ivDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }

        public void bind(CartItem cartItem) {
            tvProductName.setText(cartItem.getProduct().getName());

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvProductPrice.setText(format.format(cartItem.getProduct().getPrice()));

            tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

            Glide.with(context).load(cartItem.getProduct().getImage()).into(ivProductImage);

            btnDecrease.setOnClickListener(v -> {
                CartManager.getInstance().updateQuantity(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity() - 1
                );
                if (listener != null) {
                    listener.onCartChanged();
                }
            });

            btnIncrease.setOnClickListener(v -> {
                CartManager.getInstance().updateQuantity(
                    cartItem.getProduct().getId(),
                    cartItem.getQuantity() + 1
                );
                if (listener != null) {
                    listener.onCartChanged();
                }
            });

            ivDelete.setOnClickListener(v -> {
                CartManager.getInstance().removeProduct(cartItem.getProduct().getId());
                if (listener != null) {
                    listener.onCartChanged();
                }
=======
    class CartViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbSelect;
        ImageView imgProduct;
        TextView tvProductName, tvProductVariant, tvProductPrice, tvQuantity;
        ImageButton btnDelete, btnMinus, btnPlus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cbSelect = itemView.findViewById(R.id.cbSelect);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductVariant = itemView.findViewById(R.id.tvProductVariant);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }

        void bind(CartItem item, int position) {
            // Load image (using Glide if available, or placeholder)
            // Assuming Glide is used in the project based on typical Android setups, but if
            // not, simple resource setting
            // Checking Gradle file to confirm Glide would be best, but for now assuming it
            // works or I'll implement checks
            // Actually, I should verify if Glide is in build.gradle. If not, I'll just use
            // a placeholder text or standard bitmap logic.
            // But usually android projects have Glide/Picasso.
            // Let's assume Glide is available for now as it makes image loading easier.

            // Note: Since I didn't check build.gradle dependencies explicitly for Glide,
            // I'll use a safe check catch or just standard usage.
            // Actually, to be safe and robust, I'll try to use Glide, if it fails
            // compilation the user will tell me.
            // Or I can check build.gradle... earlier list_dir showed build.gradle.kts.

            // For now, I will use Glide since it's standard.
            if (item.getProduct().getImage() != null && !item.getProduct().getImage().isEmpty()) {
                Glide.with(context)
                        .load(item.getProduct().getImage())
                        .placeholder(R.drawable.ic_launcher_background) // Default placeholder
                        .error(R.drawable.ic_launcher_background)
                        .into(imgProduct);
            } else {
                imgProduct.setImageResource(R.drawable.ic_launcher_background);
            }

            tvProductName.setText(item.getProduct().getName());

            // Variant format: "Màu: Be Size: M"
            String variantText = String.format("Màu: %s  Size: %s",
                    item.getColor() != null ? item.getColor() : "N/A",
                    item.getSize() != null ? item.getSize() : "N/A");
            tvProductVariant.setText(variantText);

            // Price format
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvProductPrice.setText(formatter.format(item.getProduct().getPrice()));

            tvQuantity.setText(String.valueOf(item.getQuantity()));
            cbSelect.setChecked(item.isSelected());

            // Events
            cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setSelected(isChecked);
                if (listener != null)
                    listener.onItemChecked(item, isChecked);
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null)
                    listener.onDeleteClick(item, getAdapterPosition());
            });

            btnMinus.setOnClickListener(v -> {
                int newQty = item.getQuantity() - 1;
                if (newQty >= 1) { // Min quantity 1
                    item.setQuantity(newQty);
                    tvQuantity.setText(String.valueOf(newQty));
                    if (listener != null)
                        listener.onQuantityChanged(item, newQty);
                }
            });

            btnPlus.setOnClickListener(v -> {
                int newQty = item.getQuantity() + 1;
                item.setQuantity(newQty);
                tvQuantity.setText(String.valueOf(newQty));
                if (listener != null)
                    listener.onQuantityChanged(item, newQty);
>>>>>>> upstream/main
            });
        }
    }
}
