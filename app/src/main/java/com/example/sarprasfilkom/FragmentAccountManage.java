package com.example.sarprasfilkom;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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

public class FragmentAccountManage extends Fragment {
    Context context;
    BaseApiService mApiService;
    List<Result> results;
    RecyclerView accountManageRecycler;
    AdapterAccountManage adapterAccountManage;
    Button btnAddAccount;
    ProgressDialog progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_manage, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Kelola Akun");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        context = getActivity();
        btnAddAccount = view.findViewById(R.id.btn_addaccount);
        progressBar = new ProgressDialog(context);
        progressBar.show();

        results = new ArrayList<>();
        accountManageRecycler = view.findViewById(R.id.AccountManageList);

        btnAddAccount.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_add_account, null));
        mApiService = UtilsApi.getAPIService();
        loadData();
        return view;
    }

    private void loadData(){
        mApiService.staffManager().enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                progressBar.dismiss();
                try {
                    if (value.equals("1")) {
                        results = response.body().getResult();
                        adapterAccountManage = new AdapterAccountManage(getActivity(), results);
                        accountManageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                        accountManageRecycler.setAdapter(adapterAccountManage);
                        adapterAccountManage.notifyDataSetChanged();
                    }
                } catch (Exception e){
                    progressBar.dismiss();
                    Log.e(TAG, "onResponse: ", e );
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressBar.dismiss();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });

    }
}
