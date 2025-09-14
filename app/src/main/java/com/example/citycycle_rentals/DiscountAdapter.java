package com.example.citycycle_rentals;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {
    private List<Discount> discountList;

    public DiscountAdapter(List<Discount> discountList) {
        this.discountList = discountList;
    }

    @NonNull
    @Override
    public DiscountAdapter.DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discount_item, parent,false);

        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountAdapter.DiscountViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        holder.tvDiscountName.setText(discount.getDiscountName());
        holder.tvDiscount.setText(discount.getPercentage() + "%");
        holder.tvDiscountInfo.setText(discount.getDiscountInfo());
        holder.tvValidity.setText("Valid until: " + discount.getValidity());
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView tvValidity, tvDiscountInfo, tvDiscount, tvDiscountName;
        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiscount= itemView.findViewById(R.id.tvDiscount);
            tvDiscountInfo= itemView.findViewById(R.id.tvDiscountInfo);
            tvValidity= itemView.findViewById(R.id.tvValidity);
            tvDiscountName = itemView.findViewById(R.id.tvDiscountName);
        }
    }
}
