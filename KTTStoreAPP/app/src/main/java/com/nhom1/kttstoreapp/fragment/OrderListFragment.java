package com.nhom1.kttstoreapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.adapter.OrderAdapter;
import com.nhom1.kttstoreapp.api.ApiClient;
import com.nhom1.kttstoreapp.api.ApiService;
import com.nhom1.kttstoreapp.model.Order;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListFragment extends Fragment {

    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        rvOrders = view.findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        orderAdapter = new OrderAdapter(getContext(), new ArrayList<>());
        rvOrders.setAdapter(orderAdapter);

        loadOrders();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orderAdapter.updateOrders(response.body());
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi tải danh sách đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
