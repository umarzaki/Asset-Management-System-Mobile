package com.example.sarprasfilkom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarprasfilkom.model.Result;
import com.example.sarprasfilkom.model.SharedPrefManager;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AdapterFormCar extends RecyclerView.Adapter<AdapterFormCar.fcViewHolder> {

    Context context;
    List<Result> results;

    public AdapterFormCar(Context con, List<Result> results){

        context = con;
        this.results = results;
    }

    @NonNull
    @Override
    public fcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_form_car, parent, false);
        return new fcViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull fcViewHolder holder, int position) {
        Result result = results.get(position);
        String namaPart = result.getNamaPart();
        holder.carPart.setText(namaPart);
        SharedPrefManager sp = new SharedPrefManager(context);

        holder.rbOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.saveSPString(namaPart, "OK");
                Log.d(TAG, "onClick: "+namaPart+" true");
            }
        });
        holder.rbBroken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.saveSPString(namaPart, "KURANG BAIK");
                Log.d(TAG, "onClick: "+namaPart+" false");

            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class fcViewHolder extends RecyclerView.ViewHolder {
        TextView carPart;
        RadioButton rbOk, rbBroken;
        public fcViewHolder(@NonNull View itemView) {
            super(itemView);
            carPart = itemView.findViewById(R.id.tv_DetailReportItem);
            rbOk = itemView.findViewById(R.id.rb_statusOk);
            rbBroken = itemView.findViewById(R.id.rb_statusBroken);
        }
    }
}
