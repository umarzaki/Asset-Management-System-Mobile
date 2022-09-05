package com.example.sarprasfilkom;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.Result;
import com.example.sarprasfilkom.model.Value;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class FragmentDataCar extends Fragment {
    BaseApiService mApiService;
    ProgressDialog progressDialog;
    List<Result> results;
    RecyclerView dataCarRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_car, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("data pemeriksaan kendaraan");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bn_main);
        bnv.setVisibility(View.INVISIBLE);
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        results = new ArrayList<>();
        dataCarRecycler  = view.findViewById(R.id.DataCarRecycler);

        loadData();

        return view;
    }

    private void loadData(){

        mApiService.dataCarReport().enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                progressDialog.dismiss();
                try {
                    if (value.equals("1")) {
                        String res;
                        results = response.body().getResult();
                        if (results.size() != 0){
                            res = results.get(0).toString();
                            Log.d(TAG, "results: " + res);
                            dataCarRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                            dataCarRecycler.setAdapter(new AdapterDataCar(getActivity(), results));
                        } else {
                            Toast.makeText(getActivity(), "tidak ada data tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e){
                    progressDialog.dismiss();
                    Log.e(TAG, "onResponse: ", e );
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                progressDialog.dismiss();
            }
        });

    }
}
