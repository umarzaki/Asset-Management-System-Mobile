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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.google.android.material.appbar.AppBarLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;

public class FragmentTrackComplaint extends Fragment {
    Context context;
    BaseApiService mApiService;
    String kodeKeluhan, ticket, pelapor, lokasi, barang, tanggal, keterangan, status;
    EditText etTicketNumber;
    TextView etPelapor, etLokasi, etBarang, etTanggal, etKeterangan, etStatus;
    ConstraintLayout dataLayout;
    ProgressDialog progressDialog;
    Button btnSend;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_track_complaint, container, false);
        dataLayout = view.findViewById(R.id.dataLayout);
        context = getActivity();
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(getActivity());
        btnSend = view.findViewById(R.id.btnSendTicket);
        etTicketNumber = view.findViewById(R.id.et_ticketNumber);
        etPelapor = view.findViewById(R.id.tv_pelaporTrack);
        etLokasi = view.findViewById(R.id.tv_locTrack);
        etBarang = view.findViewById(R.id.tv_assetTrack);
        etTanggal = view.findViewById(R.id.tv_dateTrack);
        etKeterangan = view.findViewById(R.id.tv_descTrack);
        etStatus = view.findViewById(R.id.tv_statusTrack);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticket = etTicketNumber.getText().toString();
                requestDetailComplaint();
                progressDialog.show();

            }
        });

        return view;
    }

    private void requestDetailComplaint() {
//        dataLayout.setVisibility(View.VISIBLE);
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
                                    progressDialog.dismiss();
                                    kodeKeluhan = jsonRESULTS.getString("tiket");
                                    pelapor = jsonRESULTS.getJSONObject("complaint").getString("pelapor");
                                    lokasi = jsonRESULTS.getJSONObject("complaint").getString("lokasi");
                                    barang = jsonRESULTS.getJSONObject("complaint").getString("asset");
                                    tanggal = jsonRESULTS.getJSONObject("complaint").getString("tanggal");
                                    keterangan = jsonRESULTS.getJSONObject("complaint").getString("keterangan");
                                    status = jsonRESULTS.getJSONObject("complaint").getString("status");
                                    etTicketNumber.setText(kodeKeluhan);
                                    etPelapor.setText(pelapor);
                                    etLokasi.setText(lokasi);
                                    etBarang.setText(barang);
                                    etTanggal.setText(tanggal);
                                    etKeterangan.setText(keterangan);
                                    etStatus.setText(status);

                                } else {
                                    // Jika gagal
                                    progressDialog.dismiss();
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(context, "periksa jaringan anda", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
