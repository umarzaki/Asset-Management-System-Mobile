package com.example.sarprasfilkom.ui.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sarprasfilkom.R;
import com.example.sarprasfilkom.model.SharedPrefManager;
import com.google.android.material.appbar.AppBarLayout;

import org.w3c.dom.Text;

public class FragmentProfile extends Fragment {
    SharedPrefManager sharedPrefManager;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Profil Akun");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        context = getActivity();

        TextView etEmail = view.findViewById(R.id.et_emailProfile);
        TextView etNama = view.findViewById(R.id.et_nameProfile);
        TextView etJabatan = view.findViewById(R.id.et_jabatanProfile);
        TextView etId = view.findViewById(R.id.et_idProfile);
        TextView etPhone = view.findViewById(R.id.et_phoneProfile);
        ImageView iv_profilePhoto = view.findViewById(R.id.iv_photoProfile);

        Glide.with(context)
                .load("https://sarprasfilkomub.000webhostapp.com/images/profile/IMG_"+sharedPrefManager.getSPNoInduk()+".jpg")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(iv_profilePhoto);

        etId.setText(sharedPrefManager.getSPNoInduk());
        etNama.setText(sharedPrefManager.getSPNama());
        etEmail.setText(sharedPrefManager.getSPEmail());
        etPhone.setText(sharedPrefManager.getSPPhone());
        etJabatan.setText(sharedPrefManager.getSPJabatan());
        return view;
    }
}