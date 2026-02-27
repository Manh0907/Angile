package com.nhom1.kttstoreapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.adapter.NotificationAdapter;
import com.nhom1.kttstoreapp.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private TextView tvBadge;
    private TextView tvMarkAllRead;

    private List<Notification> notificationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        rvNotifications = view.findViewById(R.id.rvNotifications);
        tvBadge = view.findViewById(R.id.tvBadge);
        tvMarkAllRead = view.findViewById(R.id.tvMarkAllRead);

        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        loadMockData();
        adapter = new NotificationAdapter(getContext(), notificationList);
        rvNotifications.setAdapter(adapter);

        updateBadge();

        tvMarkAllRead.setOnClickListener(v -> {
            for (Notification n : notificationList) {
                n.setRead(true);
            }
            adapter.notifyDataSetChanged();
            updateBadge();
            Toast.makeText(getContext(), "Đã đánh dấu tất cả là đã đọc", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void loadMockData() {
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("Bảo trì lần 2!", "Xin lỗi vì đã làm phiền trải nghiệm của quý khách!",
                "05/02/2025 13:52", false));
        notificationList.add(new Notification("Bảo trì hệ thống !", "Hệ thống gặp lỗi, cần bảo trì để cập nhật!",
                "05/02/2025 13:36", false));
        notificationList.add(new Notification("Bộ sưu tập mới - Xuân 2025",
                "🌸 Bộ sưu tập Xuân 2025 đã ra mắt! Khám phá ngay những thiết kế độc quyền mới nh...",
                "17/01/2025 11:00", false));
        notificationList.add(new Notification("Ưu đãi Cuối Tuần",
                "🎉 Cuối tuần vui vẻ với giảm giá 30%! Sử dụng mã WEEKEND30 cho đơn hàng từ 1.0...", "17/01/2025 14:00",
                false));
        notificationList.add(new Notification("Bảo trì hệ thống",
                "Hệ thống sẽ được bảo trì từ 23:00 - 01:00 ngày 20/01/2025. Mong quý khách thông c...",
                "15/01/2025 09:00", true));
    }

    private void updateBadge() {
        int unreadCount = 0;
        for (Notification n : notificationList) {
            if (!n.isRead())
                unreadCount++;
        }

        if (unreadCount > 0) {
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText(String.valueOf(unreadCount));
        } else {
            tvBadge.setVisibility(View.GONE);
        }
    }
}
