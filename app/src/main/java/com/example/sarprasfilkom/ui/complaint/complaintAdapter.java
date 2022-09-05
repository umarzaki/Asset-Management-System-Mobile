package com.example.sarprasfilkom.ui.complaint;

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

import com.example.sarprasfilkom.R;
import com.example.sarprasfilkom.model.Result;

import java.util.ArrayList;
import java.util.List;

public class complaintAdapter extends RecyclerView.Adapter<complaintAdapter.CaViewHolder> {

    Context context;
    List<Result> results;

    public complaintAdapter(Context con, List<Result> results){
        context = con;
        this.results = results;
    }

    @NonNull
    @Override
    public CaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_complaint, parent, false);

        return new CaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CaViewHolder holder, int position) {
        Result result = results.get(position);
        holder.complaintNumber.setText(result.getComplaintNumber());
        holder.complaintStatus.setText(result.getComplaintStatus());
        String ticket = result.getComplaintNumber();
        Bundle bundle = new Bundle();
        bundle.putString("ticket", ticket);
        holder.complaintRow.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_detailComplaint, bundle));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class CaViewHolder extends RecyclerView.ViewHolder {
        TextView complaintNumber, complaintStatus;
        CardView complaintRow;
        public CaViewHolder(@NonNull View itemView) {
            super(itemView);
            complaintNumber = itemView.findViewById(R.id.tv_complaintnum);
            complaintStatus = itemView.findViewById(R.id.tv_complaintstatus);
            complaintRow = itemView.findViewById(R.id.cardComplaintRow);
        }
    }

    public void setComplaintList(ArrayList<Result> cl){
        results = cl;
        notifyDataSetChanged();
    }
}
