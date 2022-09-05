package com.example.sarprasfilkom;

import android.app.ProgressDialog;
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
import com.example.sarprasfilkom.model.SharedPrefManager;
import com.example.sarprasfilkom.model.Value;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class FragmentHistoryUser extends Fragment {

    BaseApiService mApiService;
    List<Result> results;
    RecyclerView userHistoryRecycler;
    ProgressDialog progressDialog;
    String args_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_user, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Riwayat Kegiatan");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bn_main);
        bnv.setVisibility(View.INVISIBLE);
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(getActivity());

        results = new ArrayList<>();
        userHistoryRecycler  = view.findViewById(R.id.UserHistoryList);

        if (getArguments() != null){
            args_id = getArguments().getString("id");
            progressDialog.show();
            loadData(args_id);
        } else {
            SharedPrefManager sp = new SharedPrefManager(getActivity());
            args_id = sp.getSPId();
            progressDialog.show();
            loadData(args_id);
        }

//        String s1[], s2[], s3[];
//        s1 = getResources().getStringArray(R.array.eventName);
//        s2 = getResources().getStringArray(R.array.userHistoryDate);
//        s3 = getResources().getStringArray(R.array.reportNumber);

//        userHistoryRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        userHistoryRecycler.setAdapter(new AdapterUserHistory(s1, s2, s3));

        return view;
    }

    private void loadData(String id){

        mApiService.userHistory(id).enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                progressDialog.dismiss();
//                progressBar.setVisibility(View.GONE);
                try {
                    if (value.equals("1")) {
                        String res;
                        results = response.body().getResult();
                        if (results != null){
                            res = results.get(0).toString();
                            Log.d(TAG, "results: " + res);
                            userHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                            userHistoryRecycler.setAdapter(new AdapterUserHistory(getActivity(), results));
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
