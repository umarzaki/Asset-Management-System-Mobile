package com.example.sarprasfilkom.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.sarprasfilkom.R;
import com.example.sarprasfilkom.model.SharedPrefManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentHome extends Fragment {
    SharedPrefManager sharedPrefManager;
    String noInduk;

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Selamat Datang");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bn_main);
        bottomNavigationView.setVisibility(View.VISIBLE);
        sharedPrefManager = new SharedPrefManager(getActivity());

        Button btnMtRoom = view.findViewById(R.id.btnMtRoom);
        Button btnMtCar = view.findViewById(R.id.btnMtCar);
        Button btnRepair = view.findViewById(R.id.btnRepair);
        Button btnHistory = view.findViewById(R.id.btnHistoryUser);

        btnMtRoom.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_form_room, null));
        btnMtCar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_form_car, null));
        btnRepair.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_form_repair, null));
        Bundle bundle = new Bundle();
        bundle.putString("id", sharedPrefManager.getSPId());
        btnHistory.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_history_user, bundle));
        return view;
    }
}