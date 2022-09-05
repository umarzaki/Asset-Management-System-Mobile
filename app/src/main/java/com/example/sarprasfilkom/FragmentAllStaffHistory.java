package com.example.sarprasfilkom;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class FragmentAllStaffHistory extends Fragment {

    Context context;
    BaseApiService mApiService;
    List<Result> results;
    RecyclerView AllStaffHistoryRecycler;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_staff_history, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Riwayat Kegiatan staff");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        context = getActivity();
        mApiService = UtilsApi.getAPIService();
        results = new ArrayList<>();
        AllStaffHistoryRecycler = view.findViewById(R.id.AllStaffHistoryList);
        progressDialog = new ProgressDialog(context);

        progressDialog.show();
        loadData();

        return view;
    }

    private void loadData(){
        mApiService.allStaffHistory().enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                progressDialog.dismiss();
                try {
                    if (value.equals("1")) {
                        results = response.body().getResult();
                        AllStaffHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        AllStaffHistoryRecycler.setAdapter(new AdapterAllStaffHistory(getActivity(), results));
                    }
                } catch (Exception e){
                    progressDialog.dismiss();
                    Log.e(TAG, "onResponse: ", e );
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressDialog.dismiss();

            }
        });

    }
}
