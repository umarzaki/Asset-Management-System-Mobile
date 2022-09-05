package com.example.sarprasfilkom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAddAccount extends Fragment {
    BaseApiService mApiService;
    Context context;
    ProgressDialog loading;
    EditText etNoInduk;
    EditText etNama;
    EditText etEmail;
    EditText etPassword;
    EditText etPhone;
    Spinner spinnerJabatan;
    Spinner spinnerUnit;
    Button btnRegist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_account, container, false);
        context = getActivity();
        mApiService = UtilsApi.getAPIService();

        etNoInduk = view.findViewById(R.id.et_addNoInduk);
        etNama = view.findViewById(R.id.et_addNama);
        etEmail = view.findViewById(R.id.et_addEmail);
        etPassword = view.findViewById(R.id.et_addPassword);
        etPhone = view.findViewById(R.id.et_addPhone);
        spinnerJabatan = view.findViewById(R.id.spinnerJabatan);
        spinnerUnit = view.findViewById(R.id.spinnerUnit);
        btnRegist = view.findViewById(R.id.btn_regist);

        ArrayAdapter<CharSequence> jabatanAdapter = ArrayAdapter.createFromResource(context, R.array.jabatan, android.R.layout.simple_spinner_item);
        jabatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJabatan.setAdapter(jabatanAdapter);
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(context, R.array.unit, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNoInduk.getText().toString())
                        || TextUtils.isEmpty(etNama.getText().toString())
                        || TextUtils.isEmpty(etEmail.getText().toString())
                        || TextUtils.isEmpty(etPassword.getText().toString())
                        || TextUtils.isEmpty(etPhone.getText().toString())
                        || TextUtils.isEmpty(spinnerJabatan.getSelectedItem().toString())
                        || TextUtils.isEmpty(spinnerUnit.getSelectedItem().toString())){
                    Toast.makeText( getActivity(), "data yang diperlukan belum lengkap", Toast.LENGTH_SHORT).show();
                } else {
                    loading = ProgressDialog.show(context, null, "Harap Tunggu...", true, false);
                    requestRegister();
                }
            }
        });
//        initComponents();
        return view;
    }
    private void initComponents(){

    }

    private void requestRegister(){
        String noInduk = etNoInduk.getText().toString();
        String nama = etNama.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String phone = etPhone.getText().toString();
        String jabatan = spinnerJabatan.getSelectedItem().toString();
        String unit = spinnerUnit.getSelectedItem().toString();

            mApiService.registerRequest(noInduk, nama, email, password, phone, jabatan, unit)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                Log.i("debug", "onResponse: BERHASIL");
                                loading.dismiss();
                                try {
                                    JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                    if (jsonRESULTS.getString("error").equals("false")){
                                        Toast.makeText(context, "akun berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                    } else {
                                        String error_message = jsonRESULTS.getString("error_msg");
                                        Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.i("debug", "onResponse: GA BERHASIL");
                                loading.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                            Toast.makeText(context, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
}
