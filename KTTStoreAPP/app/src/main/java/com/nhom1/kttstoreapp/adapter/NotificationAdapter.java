package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notifications;

    public NotificationAdapter(Context context, List<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void updateList(List<Notification> newList) {
        this.notifications = newList;
        notifyDataSetChanged();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        View viewUnreadIndicator;
        TextView tvTitle;
        TextView tvContent;
        TextView tvTimestamp;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            viewUnreadIndicator = itemView.findViewById(R.id.viewUnreadIndicator);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
        }

        public void bind(Notification notification) {
            tvTitle.setText(notification.getTitle());
            tvContent.setText(notification.getContent());
            tvTimestamp.setText(notification.getTimestamp());

            if (notification.isRead()) {
                viewUnreadIndicator.setVisibility(View.INVISIBLE);
                tvTitle.setTextColor(Color.parseColor("#333333"));
            } else {
                viewUnreadIndicator.setVisibility(View.VISIBLE);
                // Use colorPrimary. Assuming context has resources, but we can set hardcoded
                // blue.
                tvTitle.setTextColor(Color.parseColor("#2196F3"));
            }
        }
    }
}
