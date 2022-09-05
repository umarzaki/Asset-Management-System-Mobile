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

public class AdapterStatusDetailCar extends RecyclerView.Adapter<AdapterStatusDetailCar.drViewHolder> {

    Context context;
    List<Result> results;

    public AdapterStatusDetailCar(Context con, List<Result> results){

        context = con;
        this.results = results;
    }

    @NonNull
    @Override
    public drViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_detail_report, parent, false);
        return new drViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull drViewHolder holder, int position) {
        Result result = results.get(position);

        holder.item.setText(result.getItem());
        holder.status.setText(result.getStatus());

    }
    @Override
    public int getItemCount() {
        return results.size();
    }

    public class drViewHolder extends RecyclerView.ViewHolder {
        TextView item, status;
        public drViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.tv_DetailReportItem);
            status = itemView.findViewById(R.id.tv_DetailReportStatus);
        }
    }
}
