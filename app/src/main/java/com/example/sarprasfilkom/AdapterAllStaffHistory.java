package com.example.sarprasfilkom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarprasfilkom.model.Result;

import java.util.List;

public class AdapterAllStaffHistory extends RecyclerView.Adapter<AdapterAllStaffHistory.AsViewHolder> {

    Context context;
    List<Result> results;

    public AdapterAllStaffHistory(Context con, List<Result> results){
        context = con;
        this.results = results;
    }

    @NonNull
    @Override
    public AsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_all_staff_history, parent, false);
        return new AsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsViewHolder holder, int position) {
        Result result = results.get(position);
        holder.noIndukStaff.setText(result.getNoInduk());
        holder.staffName.setText(result.getNama());
        Bundle bundle = new Bundle();
        bundle.putString("id", result.getID());
        holder.card.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_history_user, bundle));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class AsViewHolder extends RecyclerView.ViewHolder {
        TextView noIndukStaff, staffName ;
        CardView card;
        public AsViewHolder(@NonNull View itemView) {
            super(itemView);
            noIndukStaff = itemView.findViewById(R.id.tv_noIndukStaff);
            staffName = itemView.findViewById(R.id.tv_StaffName);
            card = itemView.findViewById(R.id.card_all_staff);
        }
    }
}
