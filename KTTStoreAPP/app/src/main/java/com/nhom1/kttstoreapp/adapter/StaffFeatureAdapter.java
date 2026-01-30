package com.nhom1.kttstoreapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.StaffMainActivity;

import java.util.List;

public class StaffFeatureAdapter extends RecyclerView.Adapter<StaffFeatureAdapter.FeatureViewHolder> {

    private Context context;
    private List<StaffMainActivity.StaffFeature> features;

    public StaffFeatureAdapter(Context context, List<StaffMainActivity.StaffFeature> features) {
        this.context = context;
        this.features = features;
    }

    @NonNull
    @Override
    public FeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_staff_feature, parent, false);
        return new FeatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeatureViewHolder holder, int position) {
        StaffMainActivity.StaffFeature feature = features.get(position);
        holder.tvTitle.setText(feature.getTitle());
        holder.tvDescription.setText(feature.getDescription());

        holder.cardView.setOnClickListener(v -> {
            android.widget.Toast.makeText(context, feature.getTitle() + " - Chức năng sẽ được triển khai sau", 
                    android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return features.size();
    }

    static class FeatureViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitle;
        TextView tvDescription;

        public FeatureViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardFeature);
            tvTitle = itemView.findViewById(R.id.tvFeatureTitle);
            tvDescription = itemView.findViewById(R.id.tvFeatureDescription);
        }
    }
}
