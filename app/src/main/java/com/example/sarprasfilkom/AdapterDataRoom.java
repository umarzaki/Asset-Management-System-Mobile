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

public class AdapterDataRoom extends RecyclerView.Adapter<AdapterDataRoom.drViewHolder> {

    Context context;
    List<Result> results;

    public AdapterDataRoom(Context con, List<Result> results){
        context = con;
        this.results = results;
    }

    @NonNull
    @Override
    public drViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_data_report, parent, false);
        return new drViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull drViewHolder holder, int position) {
        Result result = results.get(position);
        holder.eventName.setText(result.getNamaPemeriksaan());
        holder.Date.setText(result.getTanggalPemeriksaan());
        holder.staffName.setText(result.getNama());
        holder.reportNumber.setText(result.getNomorLaporan());
        Bundle bundle = new Bundle();
        bundle.putString("reportId", result.getNomorLaporan());
        holder.card.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_detail_room, bundle));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class drViewHolder extends RecyclerView.ViewHolder {
        TextView reportNumber, eventName, staffName, Date;
        CardView card;

        public drViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.CardDataReport);
            reportNumber = itemView.findViewById(R.id.tv_reportNumber);
            eventName = itemView.findViewById(R.id.tv_EventName);
            staffName = itemView.findViewById(R.id.tv_StaffNameData);
            Date = itemView.findViewById(R.id.tv_UserHistoryDate);
        }
    }
}
