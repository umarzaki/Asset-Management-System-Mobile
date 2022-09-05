package com.example.sarprasfilkom;

import static android.content.ContentValues.TAG;

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
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentNotification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNotification extends Fragment {

    BaseApiService mApiService;
    ProgressDialog progressDialog;
    List<Result> results;
    RecyclerView todayRecycler, earlierRecycler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNotification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNotification.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNotification newInstance(String param1, String param2) {
        FragmentNotification fragment = new FragmentNotification();
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("notifikasi");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bn_main);
        bottomNavigationView.setVisibility(View.INVISIBLE);
        String s1[], s2[], s3[];
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        results = new ArrayList<>();
        todayRecycler = view.findViewById(R.id.todayNotificationRecyler);

        s1 = getResources().getStringArray(R.array.staffName);
        s2 = getResources().getStringArray(R.array.descNotif);
        s3 = getResources().getStringArray(R.array.timeNotif);

        loadData();



        return view;
    }

    private void loadData() {
        mApiService.dataNotification().enqueue(new Callback<Value>() {
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
                            todayRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                            todayRecycler.setAdapter(new AdapterNotificationRecycler(getActivity(),results));
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