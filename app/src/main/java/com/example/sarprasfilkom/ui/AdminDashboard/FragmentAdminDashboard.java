package com.example.sarprasfilkom.ui.AdminDashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.sarprasfilkom.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentAdminDashboard extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Admin Dashboard");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bn_main);
        bottomNavigationView.setVisibility(View.INVISIBLE);

        Button btndataMtRoom = view.findViewById(R.id.btnDataMtRoom);
        Button btndataMtCar = view.findViewById(R.id.btnDataMtCar);
        Button btndataRepair = view.findViewById(R.id.btnDataRepair);
        Button btnAllStaffHistory = view.findViewById(R.id.btnStaffHistory);
        Button btnAccountManage = view.findViewById(R.id.btnAccountManage);

        btndataMtRoom.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_data_report_room, null));
        btndataMtCar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_data_report_car, null));
        btndataRepair.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_data_report_repair, null));
        btnAllStaffHistory.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_all_staff_history, null));
        btnAccountManage.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_account_manage, null));
        return view;
    }
}