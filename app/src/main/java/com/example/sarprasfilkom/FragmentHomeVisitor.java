package com.example.sarprasfilkom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class FragmentHomeVisitor extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_visitor, container, false);

        Button btnCreateComplaint = view.findViewById(R.id.btnCreateComplaint);
        Button btnTrackComplaint = view.findViewById(R.id.btnTrackComplaint);

        btnCreateComplaint.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_form_complaint, null));
        btnTrackComplaint.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.nav_track_complaint, null));
        return view;
    }
}
