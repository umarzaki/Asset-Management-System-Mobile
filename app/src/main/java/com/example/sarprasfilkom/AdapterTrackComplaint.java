package com.example.sarprasfilkom;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterTrackComplaint extends RecyclerView.Adapter<AdapterTrackComplaint.CaViewHolder> {

    String data1[];
    String data2[];

    public AdapterTrackComplaint(String s1[], String s2[]){
        data1 = s1;
        data2 = s2;
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
        holder.complaintNumber.setText(data1[position]);
        holder.complaintStatus.setText(data2[position]);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class CaViewHolder extends RecyclerView.ViewHolder {
        TextView complaintNumber, complaintStatus;
        public CaViewHolder(@NonNull View itemView) {
            super(itemView);
            complaintNumber = itemView.findViewById(R.id.tv_complaintnum);
            complaintStatus = itemView.findViewById(R.id.tv_complaintstatus);
        }
    }
}
