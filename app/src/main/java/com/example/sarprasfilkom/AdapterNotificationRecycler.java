package com.example.sarprasfilkom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarprasfilkom.model.Result;

import java.util.List;

public class AdapterNotificationRecycler extends RecyclerView.Adapter<AdapterNotificationRecycler.nrViewHolder> {

    Context context;
    List<Result> results;

    public AdapterNotificationRecycler(Context con, List<Result> results){

        context = con;
        this.results = results;
    }

    @NonNull
    @Override
    public nrViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_notification, parent, false);
        return new nrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull nrViewHolder holder, int position) {
        Result result = results.get(position);
        holder.staffName.setText(result.getNama());
        holder.notifDesc.setText(result.getNamaKegiatan());
        holder.notifTime.setText(result.getTanggalKegiatan());


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class nrViewHolder extends RecyclerView.ViewHolder {
        TextView staffName, notifDesc, notifTime;
        public nrViewHolder(@NonNull View itemView) {
            super(itemView);
            staffName = itemView.findViewById(R.id.staffNotification);
            notifDesc = itemView.findViewById(R.id.descNotification);
            notifTime = itemView.findViewById(R.id.timeNotification);
        }
    }
}
