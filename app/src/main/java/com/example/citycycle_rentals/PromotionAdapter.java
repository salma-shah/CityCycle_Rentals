package com.example.citycycle_rentals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder> {
    private List<Promotion> promotionList;
    public PromotionAdapter(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    @NonNull
    @Override
    public PromotionAdapter.PromotionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promotion_item, parent,false);

        return new PromotionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionAdapter.PromotionViewHolder holder, int position) {
        // initializing
        Promotion promotion = promotionList.get(position);
        holder.imgPromotion.setImageResource(promotion.getPromoImage());
        holder.tvPromotionDesc.setText(promotion.getDescription());
        holder.btnPromo.setText(promotion.getPromoBtn());
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    public class PromotionViewHolder extends RecyclerView.ViewHolder {
        Button btnPromo;
        ImageView imgPromotion;
        TextView tvPromotionDesc;

        public PromotionViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPromo = itemView.findViewById(R.id.btnPromo);
            tvPromotionDesc = itemView.findViewById(R.id.tvPromotionDesc);
            imgPromotion = itemView.findViewById(R.id.imgPromotion);
        }
    }
}
