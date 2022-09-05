package com.example.sarprasfilkom.ui.complaint;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sarprasfilkom.R;
import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.Result;
import com.example.sarprasfilkom.model.Value;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class FragmentComplaint extends Fragment {
    BaseApiService mApiService;
    List<Result> results;
    RecyclerView complaintRecycler;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Daftar Keluhan");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        complaintRecycler = view.findViewById(R.id.complaintlist);
        progressDialog = new ProgressDialog(getActivity());
        results = new ArrayList<>();
        mApiService = UtilsApi.getAPIService();
        progressDialog.show();
        loadData();

        return view;
    }

    private void loadData(){
        mApiService.complaintList().enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
//                progressBar.setVisibility(View.GONE);
                try {
                    if (value.equals("1")) {
                        progressDialog.dismiss();
                        results = response.body().getResult();
                        complaintRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        complaintRecycler.setAdapter(new complaintAdapter(getActivity(), results));
                        Log.d(TAG, "masuk");
                    } else {
                        progressDialog.dismiss();
                    }
                } catch (Exception e){
                    Log.e(TAG, "onResponse: ", e );
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });

    }
}