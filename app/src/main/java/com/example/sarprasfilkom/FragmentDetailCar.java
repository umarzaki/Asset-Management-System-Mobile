package com.example.sarprasfilkom;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.Result;
import com.example.sarprasfilkom.model.SharedPrefManager;
import com.example.sarprasfilkom.model.Value;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class FragmentDetailCar extends Fragment {
    BaseApiService mApiService;
    ProgressDialog progressDialog;
    List<Result> results;
    RecyclerView DetailCarRecycler;
    SharedPrefManager sharedPrefManager;
    String id, role, args, foto;
    TextView nomorLaporan, namaKegiatan, staff, tanggal, keterangan, status;
    ImageView iv_reportPhoto;
    Button btnVerify;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_car, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("detail pemeriksaan ruang");
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        results = new ArrayList<>();
        sharedPrefManager = new SharedPrefManager(getActivity());
        DetailCarRecycler  = view.findViewById(R.id.DetailCarRecycler);
        nomorLaporan = view.findViewById(R.id.DetailCarId);
        namaKegiatan = view.findViewById(R.id.DetailCarName);
        staff = view.findViewById(R.id.DetailCarStaff);
        tanggal = view.findViewById(R.id.DetailCarDate);
        keterangan = view.findViewById(R.id.DetailCarDesc);
        iv_reportPhoto = view.findViewById(R.id.iv_DetailCar);
        status = view.findViewById(R.id.DetailCarStatus);
        btnVerify = view.findViewById(R.id.btn_verifyCar);

        args = getArguments().getString("reportId");
        id = sharedPrefManager.getSPId();
        role = sharedPrefManager.getSPRole();
        if (role.equals("1")){
            btnVerify.setVisibility(View.VISIBLE);
        } else {
            btnVerify.setVisibility(View.INVISIBLE);
        }
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                verifyReport(args, id);
            }
        });

        loadData(args);
        loadStatus(args);
        return view;
    }

    private void loadData(String reportId){
        mApiService.detailCarReport(reportId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    nomorLaporan.setText(jsonRESULTS.getString("reportId"));
                                    namaKegiatan.setText(jsonRESULTS.getJSONObject("detail").getString("namaKegiatan"));
                                    staff.setText(jsonRESULTS.getJSONObject("detail").getString("staff"));
                                    tanggal.setText(jsonRESULTS.getJSONObject("detail").getString("tanggal"));
                                    keterangan.setText(jsonRESULTS.getJSONObject("detail").getString("keterangan"));
                                    status.setText(jsonRESULTS.getString("verified"));
                                    foto = jsonRESULTS.getJSONObject("detail").getString("foto");
                                    Glide.with(getActivity())
                                            .load("https://sarprasfilkomub.000webhostapp.com/images/"+foto)
                                            .placeholder(R.drawable.ic_baseline_panorama_24)
                                            .centerCrop()
                                            .into(iv_reportPhoto);
                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(getActivity(), error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(getActivity(), "periksa jaringan anda", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadStatus(String reportId){
        mApiService.statusReportCar(reportId).enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                progressDialog.dismiss();
                String value = response.body().getValue();
                try {
                    if (value.equals("1")) {
                        results = response.body().getResult();
                        DetailCarRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        DetailCarRecycler.setAdapter(new AdapterStatusDetailCar(getActivity(), results));
                    }
                } catch (Exception e){
                    Log.e(TAG, "onResponse: ", e );
                }
            }
            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    public void verifyReport(String reportId, String pegawaiId){
        mApiService.verifyReport(reportId, pegawaiId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()){
                            Log.i("debug", "onResponse: BERHASIL");
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")){
                                    Toast.makeText(getActivity(), "laporan berhasil diverifikasi", Toast.LENGTH_SHORT).show();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(getActivity(), error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.i("debug", "onResponse: TIDAK BERHASIL");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(getActivity(), "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}