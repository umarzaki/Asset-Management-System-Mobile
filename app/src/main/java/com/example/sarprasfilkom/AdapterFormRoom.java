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

public class AdapterFormRoom extends RecyclerView.Adapter<AdapterFormRoom.FrViewHolder> {

    Context context;
    List<Result> results;
    RadioButton selectedRadioButton;

    public AdapterFormRoom(Context con, List<Result> results){

        context = con;
        this.results = results;
    }

    @NonNull
    @Override
    public FrViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_form_room, parent, false);
        return new FrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FrViewHolder holder, int position) {
        Result result = results.get(position);
        String namaBarang = result.getNamaBarang();
        holder.roomAsset.setText(namaBarang);
        SharedPrefManager sp = new SharedPrefManager(context);
//        int selectedButton = holder.rgStatus.getCheckedRadioButtonId();
//        if (selectedButton==R.id.rb_statusOk){
//            sp.saveSPBoolean(namaBarang, true);
//        } else if (selectedButton==R.id.rb_statusBroken){
//            sp.saveSPBoolean(namaBarang, false);
//        }

        holder.rbOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.saveSPString(namaBarang, "OK");
                Log.d(TAG, "onClick: "+namaBarang+" true");
            }
        });
        holder.rbBroken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.saveSPString(namaBarang, "RUSAK");
                Log.d(TAG, "onClick: "+namaBarang+" false");

            }
        });
    }
    @Override
    public int getItemCount() {
        return results.size();
    }

    public class FrViewHolder extends RecyclerView.ViewHolder {
        TextView roomAsset;
        RadioButton rbOk, rbBroken;
        public FrViewHolder(@NonNull View itemView) {
            super(itemView);
            roomAsset = itemView.findViewById(R.id.tv_roomCondition);
            rbOk = itemView.findViewById(R.id.rb_statusOk);
            rbBroken = itemView.findViewById(R.id.rb_statusBroken);
        }
    }
}
