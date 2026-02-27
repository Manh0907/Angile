package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.OrderDetailActivity;
import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.Order;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        TextView tvOrderDate;
        TextView tvCustomerName;
        TextView tvCustomerPhone;
        TextView tvShippingAddress;
        TextView tvTotalAmount;
        TextView tvOrderStatus;
        TextView tvDeliveryStatus;
        TextView tvPaymentStatus;
        CardView cardView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvCustomerPhone = itemView.findViewById(R.id.tvCustomerPhone);
            tvShippingAddress = itemView.findViewById(R.id.tvShippingAddress);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvDeliveryStatus = itemView.findViewById(R.id.tvDeliveryStatus);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            cardView = (CardView) itemView;
        }

        public void bind(Order order) {
            String shortId = order.getId() != null && order.getId().length() > 6
                    ? order.getId().substring(order.getId().length() - 6)
                    : order.getId();
            tvOrderId.setText("Đơn hàng #" + shortId);

            tvCustomerName.setText("Người nhận: " + order.getCustomerName());
            tvCustomerPhone.setText("SĐT: " + (order.getPhone() != null ? order.getPhone() : "N/A"));
            tvShippingAddress
                    .setText("Địa chỉ: " + (order.getShippingAddress() != null ? order.getShippingAddress() : "N/A"));

            // Format date
            if (order.getCreatedAt() != null && !order.getCreatedAt().isEmpty()) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                            Locale.getDefault());
                    Date date = inputFormat.parse(order.getCreatedAt());
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    tvOrderDate.setText(outputFormat.format(date));
                } catch (ParseException e) {
                    tvOrderDate.setText(order.getCreatedAt());
                }
            } else {
                tvOrderDate.setText("");
            }

            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvTotalAmount.setText("Tổng tiền: " + format.format(order.getTotalAmount()));

            tvOrderStatus.setText("Trạng thái đơn hàng: " + order.getStatusDisplay());

            // Dummy logic for delivery status based on order status
            String deliveryText = "Đang xử lý";
            if ("shipping".equals(order.getStatus()))
                deliveryText = "Đang giao hàng";
            else if ("delivered".equals(order.getStatus()))
                deliveryText = "Đã giao hàng thành công";
            else if ("preparing".equals(order.getStatus()))
                deliveryText = "Đang chuẩn bị";
            else if ("cancelled".equals(order.getStatus()))
                deliveryText = "Đã hủy giao";

            tvDeliveryStatus.setText("Trạng thái giao hàng: " + deliveryText);

            // Placeholder for payment status (assume COD or already paid if not pending)
            tvPaymentStatus.setText(
                    "Thanh toán: " + ("pending".equals(order.getStatus()) ? "Chưa thanh toán" : "Đã thanh toán"));

            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("order_id", order.getId());
                context.startActivity(intent);
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
}
