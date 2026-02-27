package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.OrderItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private Context context;
    private List<OrderItem> items;

    public OrderItemAdapter(Context context, List<OrderItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvProductVariant;
        TextView tvProductPrice;
        TextView tvQuantity;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductVariant = itemView.findViewById(R.id.tvProductVariant);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }

        public void bind(OrderItem item) {
            tvProductName.setText(item.getProductName());
            tvProductVariant.setText("Màu: N/A - Size: N/A");
            tvQuantity.setText("x" + item.getQuantity());

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvProductPrice.setText(format.format(item.getPrice()));

            // Image is not in OrderItem model, leave as placeholder
            imgProduct.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
