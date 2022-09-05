package com.example.sarprasfilkom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.Result;
import com.example.sarprasfilkom.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterAccountManage extends RecyclerView.Adapter<AdapterAccountManage.AmViewHolder> {

    Context context;
    List<Result> results;
    BaseApiService mApiService;


    public AdapterAccountManage(Context con, List<Result> results){
        context = con;
        this.results = results;
        mApiService = UtilsApi.getAPIService();
    }

    @NonNull
    @Override
    public AmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_account_manage, parent, false);
        return new AmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmViewHolder holder, int position) {
        Result result = results.get(position);
        holder.staffName.setText(result.getNama());
        holder.staffRole.setText(result.getJabatan()+" "+ result.getUnit());
        Bundle bundle = new Bundle();
        bundle.putString("nama", result.getNama());
        holder.btnEdit.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_edit_account, bundle));
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("apakah anda yakin untuk menghapus user ini?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                mApiService.deleteUser(result.getNama()).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if(response.isSuccessful()){
                                            try{
                                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                                if (jsonRESULTS.getString("error").equals("false")) {
                                                    String message = jsonRESULTS.getString("msg");
                                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                                } else {
                                                    String error_message = jsonRESULTS.getString("error_msg");
                                                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                                        Toast.makeText(context, "periksa jaringan anda", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Navigation.createNavigateOnClickListener(R.id.nav_account_manage, null);
                             }
                        });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class AmViewHolder extends RecyclerView.ViewHolder {
        TextView staffName, staffRole;
        ImageView btnEdit, btnDelete;

        public AmViewHolder(@NonNull View itemView) {
            super(itemView);
            staffName = itemView.findViewById(R.id.tv_StaffName);
            staffRole = itemView.findViewById(R.id.tv_StaffRole);
            btnEdit = itemView.findViewById(R.id.btn_editaccount);
            btnDelete = itemView.findViewById(R.id.btn_deleteaccount);
        }
    }




}
