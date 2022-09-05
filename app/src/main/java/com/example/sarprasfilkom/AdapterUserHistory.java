package com.example.sarprasfilkom;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarprasfilkom.model.Result;

import java.util.List;

public class AdapterUserHistory extends RecyclerView.Adapter<AdapterUserHistory.UhViewHolder> {

    Context context;
    List<Result> results;

    public AdapterUserHistory(Context con, List<Result> results){
        context = con;
        this.results = results;
    }

    @NonNull
    @Override
    public UhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_user_history, parent, false);
        return new UhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UhViewHolder holder, int position) {
        Result result = results.get(position);
        String kodeKegiatan = result.getKodeKegiatan();
        holder.eventName.setText(result.getNamaPemeriksaan());
        holder.userHistoryDate.setText(result.getTanggalPemeriksaan());
        holder.reportNumber.setText(result.getNomorLaporan());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle;
                bundle = new Bundle();
                if (result.getNamaPemeriksaan().contains("pemeriksaan")){
                    switch (kodeKegiatan){
                        case "1" :  bundle.putString("reportId", result.getNomorLaporan());
                                    Navigation.findNavController(view).navigate(R.id.nav_detail_room, bundle);
                                    break;
                        case "2" :  bundle.putString("reportId", result.getNomorLaporan());
                                    Navigation.findNavController(view).navigate(R.id.nav_detail_car, bundle);
                                    break;
                    }
                } if (result.getNamaPemeriksaan().contains("pemeliharaan")) {
                    bundle.putString("reportId", result.getNomorLaporan());
                    Navigation.findNavController(view).navigate(R.id.nav_detail_repair, bundle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class UhViewHolder extends RecyclerView.ViewHolder {
        TextView reportNumber, eventName, userHistoryDate;
        CardView card;
        public UhViewHolder(@NonNull View itemView) {
            super(itemView);
            reportNumber = itemView.findViewById(R.id.tv_reportNumber);
            eventName = itemView.findViewById(R.id.tv_EventName);
            userHistoryDate = itemView.findViewById(R.id.tv_UserHistoryDate);
            card = itemView.findViewById(R.id.card_user_history);
        }
    }
}
