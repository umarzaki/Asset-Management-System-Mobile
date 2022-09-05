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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentDetailRoom#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDetailRoom extends Fragment {
    BaseApiService mApiService;
    ProgressDialog progressDialog;
    List<Result> results;
    RecyclerView DetailRoomRecycler;
    SharedPrefManager sharedPrefManager;
    String id, role, args, foto;
    TextView nomorLaporan, namaKegiatan, staff, tanggal, keterangan, status;
    ImageView iv_reportPhoto;
    Button btnVerify;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentDetailRoom() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDetailRoom.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentDetailRoom newInstance(String param1, String param2) {
        FragmentDetailRoom fragment = new FragmentDetailRoom();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_detail_room, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("detail pemeriksaan ruang");
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        results = new ArrayList<>();
        sharedPrefManager = new SharedPrefManager(getActivity());
        DetailRoomRecycler  = view.findViewById(R.id.DetailRoomRecycler);
        nomorLaporan = view.findViewById(R.id.DetailRoomId);
        namaKegiatan = view.findViewById(R.id.DetailRoomName);
        staff = view.findViewById(R.id.DetailRoomStaff);
        tanggal = view.findViewById(R.id.DetailRoomDate);
        keterangan = view.findViewById(R.id.DetailRoomDesc);
        iv_reportPhoto = view.findViewById(R.id.iv_DetailRoom);
        status = view.findViewById(R.id.DetailRoomStatus);
        btnVerify = view.findViewById(R.id.btn_VerifyRoom);

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
        mApiService.detailRoomReport(reportId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.
//                                    Toast.makeText(context, "Berhasil mengambil detail", Toast.LENGTH_SHORT).show();
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
        mApiService.statusReportRoom(reportId).enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                progressDialog.dismiss();
                String value = response.body().getValue();
//                progressBar.setVisibility(View.GONE);
                try {
                    if (value.equals("1")) {
                        results = response.body().getResult();
                        DetailRoomRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        DetailRoomRecycler.setAdapter(new AdapterStatusDetailRoom(getActivity(), results));
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