package com.example.sarprasfilkom;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

public class FragmentDetailComplaint extends Fragment {

    Context context;
    BaseApiService mApiService;
    String kodeKeluhan, ticket, pelapor, lokasi, barang, tanggal, keterangan, status, foto;
    EditText etTicketNumber, etPelapor, etLokasi, etBarang, etTanggal, etKeterangan, etStatus;
    Button btn_report;
    Bundle bundle;
    ImageView iv_compPhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_complaint, container, false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bn_main);
        bnv.setVisibility(View.INVISIBLE);
        mApiService = UtilsApi.getAPIService();
        context = getActivity();
        etTicketNumber = view.findViewById(R.id.et_ticketNumber);
        etPelapor = view.findViewById(R.id.et_reporterName);
        etLokasi = view.findViewById(R.id.et_locDetailComplaint);
        etBarang = view.findViewById(R.id.et_asset);
        etTanggal = view.findViewById(R.id.et_detailComplaintDate);
        etKeterangan = view.findViewById(R.id.et_descDetailComplaint);
        etStatus = view.findViewById(R.id.et_statusComplaint);
        iv_compPhoto = view.findViewById(R.id.iv_photoComplaint);
        btn_report = view.findViewById(R.id.btn_createReport);
        ticket = getArguments().getString("ticket");
        bundle = new Bundle();
        requestDetailComplaint();

        btn_report.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_form_repair, bundle));
        return view;
    }

    private void requestDetailComplaint() {
        Log.d(TAG, "requestDetailComplaint: "+ticket);
        mApiService.detailComplaintRequest(ticket)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.
//                                    Toast.makeText(context, "Berhasil mengambil detail", Toast.LENGTH_SHORT).show();
                                    kodeKeluhan = jsonRESULTS.getString("tiket");
                                    pelapor = jsonRESULTS.getJSONObject("complaint").getString("pelapor");
                                    lokasi = jsonRESULTS.getJSONObject("complaint").getString("lokasi");
                                    barang = jsonRESULTS.getJSONObject("complaint").getString("asset");
                                    tanggal = jsonRESULTS.getJSONObject("complaint").getString("tanggal");
                                    keterangan = jsonRESULTS.getJSONObject("complaint").getString("keterangan");
                                    status = jsonRESULTS.getJSONObject("complaint").getString("status");
                                    foto = jsonRESULTS.getJSONObject("complaint").getString("foto");
                                    etTicketNumber.setText(kodeKeluhan);
                                    etPelapor.setText(pelapor);
                                    etLokasi.setText(lokasi);
                                    etBarang.setText(barang);
                                    etTanggal.setText(tanggal);
                                    etKeterangan.setText(keterangan);
                                    etStatus.setText(status);
                                    Glide.with(context)
                                            .load("https://sarprasfilkomub.000webhostapp.com/images/"+foto)
                                            .placeholder(R.drawable.ic_baseline_panorama_24)
                                            .into(iv_compPhoto);
                                    bundle.putString("tiket", kodeKeluhan);
                                    bundle.putString("ruang", lokasi);
                                    bundle.putString("asset", barang);

                                } else {
                                    // Jika login gagal
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "periksa jaringan anda", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
