package com.example.citycycle_rentals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MembershipsAdapter extends RecyclerView.Adapter<MembershipsAdapter.MembershipViewHolder> {

    private List<Memberships> membershipsList;

    public MembershipsAdapter(List<Memberships> membershipsList) {
        this.membershipsList = membershipsList;
    }

    @NonNull
    @Override
    public MembershipsAdapter.MembershipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.membership_item, parent,false);

        return new MembershipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembershipsAdapter.MembershipViewHolder holder, int position) {
        Memberships memberships = membershipsList.get(position);
        holder.tvPrice.setText("LKR " + memberships.getPrice());
        holder.tvMembershipDuration.setText(memberships.getDuration());
        holder.tvMembershipType.setText(memberships.getType());
        holder.tvMembershipDetail1.setText("➜ " + memberships.getDetail1());
        holder.tvMembershipDetail2.setText("➜ " + memberships.getDetail2());
        holder.tvMembershipDetail3.setText("➜ " +memberships.getDetail3());
        holder.tvMembershipDetail4.setText("➜ " + memberships.getDetail4());
    }

    @Override
    public int getItemCount() {
        return membershipsList.size();
    }

    public class MembershipViewHolder extends RecyclerView.ViewHolder {

        TextView tvPrice, tvMembershipDuration, tvMembershipType, tvMembershipDetail1, tvMembershipDetail2, tvMembershipDetail3, tvMembershipDetail4;

        public MembershipViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvMembershipType = itemView.findViewById(R.id.tvMembershipType);
            tvMembershipDuration = itemView.findViewById(R.id.tvMembershipDuration);
            tvMembershipDetail1 = itemView.findViewById(R.id.tvMembershipDetail1);
            tvMembershipDetail2 = itemView.findViewById(R.id.tvMembershipDetail2);
            tvMembershipDetail3 = itemView.findViewById(R.id.tvMembershipDetail3);
            tvMembershipDetail4 = itemView.findViewById(R.id.tvMembershipDetail4);
        }
    }
}
