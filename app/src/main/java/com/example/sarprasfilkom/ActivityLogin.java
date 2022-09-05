package com.example.sarprasfilkom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.SharedPrefManager;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ActivityLogin extends AppCompatActivity {

    TextView etPassword;
    TextView etEmail;
    TextView forgetPassword;
    Button btnLogin;
    Context context;
    ProgressDialog progressDialog;
    BaseApiService mApiService;
    String role, nama, email, phone, jabatan, unit, id, noInduk, photo;
    String token;
    Bundle bundle;

    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = ActivityLogin.this;
        sharedPrefManager = new SharedPrefManager(context);

        if (sharedPrefManager.getSPIsActive()){
            if (sharedPrefManager.getSPRole().equals("3")) {
                startActivity(new Intent(ActivityLogin.this, ActivityHomeVisitor.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
                startActivity(new Intent(ActivityLogin.this, ActivityHome.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            finish();
        }

        mApiService = UtilsApi.getAPIService(); // meng-init yang ada di package apihelper

        bundle = getIntent().getExtras();
        initComponents();

    }

    protected void initComponents() {
        etEmail = findViewById(R.id.tv_EmailLogin);
        etPassword = findViewById(R.id.et_PassLogin);
        btnLogin = findViewById(R.id.btnLogin);
        forgetPassword = findViewById(R.id.tv_LupaPass);
        progressDialog = new ProgressDialog(context);

        forgetPassword.setLinksClickable(true);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ActivityLogin.this);
                alertDialogBuilder.setMessage("Harap menghubungi admin untuk melakukan perubahan password");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etEmail.getText().toString()) && TextUtils.isEmpty(etPassword.getText().toString())){

                }else{
                    requestLogin();
                    progressDialog.setMessage("Login Authentication...");
                    progressDialog.show();
                }
            }
        });

    }


    private void requestLogin() {
        mApiService.loginRequest(etEmail.getText().toString(), etPassword.getText().toString())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("error").equals("false")) {
                                    // Jika login berhasil maka data nama yang ada di response API
                                    // akan diparsing ke activity selanjutnya.
                                    Toast.makeText(context, "Berhasil Login", Toast.LENGTH_SHORT).show();
                                    id = jsonRESULTS.getString("uid");
                                    noInduk = jsonRESULTS.getJSONObject("user").getString("noInduk");
                                    role = jsonRESULTS.getJSONObject("user").getString("role");
                                    nama = jsonRESULTS.getJSONObject("user").getString("nama");
                                    email = jsonRESULTS.getJSONObject("user").getString("email");
                                    phone = jsonRESULTS.getJSONObject("user").getString("phone");
                                    photo = jsonRESULTS.getJSONObject("user").getString("photo");
                                    jabatan = jsonRESULTS.getJSONObject("user").getString("jabatan");
                                    unit = jsonRESULTS.getJSONObject("user").getString("unit");
                                    switch (role){
                                        case "1":
                                        case "2":
                                            goToHome();
                                            break;
                                        case "3":
                                            goToEcomplaint();
                                            break;
                                    }

                                } else {
                                    // Jika login gagal
                                    progressDialog.dismiss();
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        Toast.makeText(context, "periksa jaringan anda", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    private void goToHome(){
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_ACTIVE, true);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NOMOR_INDUK, noInduk);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, id);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ROLE, role);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, nama);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, email);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_PHONE, phone);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_JABATAN, jabatan+" "+unit);
        startActivity(new Intent(context, ActivityHome.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    private void goToEcomplaint(){
        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_IS_ACTIVE, true);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, id);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_ROLE, role);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, nama);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, email);
        startActivity(new Intent(context, ActivityHomeVisitor.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}