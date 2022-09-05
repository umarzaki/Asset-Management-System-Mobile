package com.example.sarprasfilkom;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.SharedPrefManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FragmentEditAccount extends Fragment {

    final String serverFilepath = "https://sarprasfilkomub.000webhostapp.com/images/profile/";
    EditText etNama, etEmail, etPassword, etPhone;
    ImageView ivPhoto;
    Button btnPhoto, btnSubmit;
    BaseApiService mApiService;
    Context context;
    View navView;
    String nama, resNoInduk, resNama, resEmail, resPassword, resPhone, resFoto, photoName, filepath;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    File file;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_account, container, false);
        mApiService = UtilsApi.getAPIService();
        context = getActivity();
        nama = getArguments().getString("nama");
        etNama = view.findViewById(R.id.et_editNama);
        etEmail = view.findViewById(R.id.et_editEmail);
        etPassword = view.findViewById(R.id.et_editPassword);
        etPhone = view.findViewById(R.id.et_editPhone);
        btnPhoto = view.findViewById(R.id.btn_uploadFotoProfile);
        btnSubmit = view.findViewById(R.id.btn_submitProfile);
        ivPhoto = view.findViewById(R.id.IV_fotoProfile);
        progressDialog = new ProgressDialog(getActivity());
        dataProfile(nama);

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navView = v;
                progressDialog.show();
                submitProfile();
            }
        });



        return view;
    }

    private void submitProfile() {
        Log.d(TAG, "submitProfile: " + filepath);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES); // read timeout
        builder.build();
        photoName = "IMG_" + resNoInduk+".jpg";

        if (filepath != null){
            file = new File(filepath);
        } else
        {
            Bitmap img = ((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
//            file = new File(img);
            file = new File(context.getCacheDir(), resFoto);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image",
                photoName, RequestBody.create(MediaType.parse("image/jpg"), file));
        RequestBody noInduk = RequestBody.create(MediaType.parse("text/plain"), resNoInduk);
        RequestBody nama = RequestBody.create(MediaType.parse("text/plain"), etNama.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), etEmail.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), etPassword.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), etPhone.getText().toString());
        RequestBody namaFoto = RequestBody.create(MediaType.parse("text/plain"), photoName);

        mApiService.submitProfile(filePart, noInduk, nama, email, password, phone, namaFoto).enqueue(new Callback<MultipartBody>() {
            @Override
            public void onResponse(Call<MultipartBody> call, Response<MultipartBody> response) {
                if (response.isSuccessful()){
                        Toast.makeText(context, "edit data profile berhasil", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Navigation.findNavController(navView).popBackStack();
                } else {
                    Log.e(TAG, "onResponse: gagal" );
                    Toast.makeText(context, "edit data profile gagal", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MultipartBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                progressDialog.dismiss();
            }
        });

    }

    private void uploadPhoto() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            filepath = getPathFromURI(imageUri);
//            ivPhoto.setImageURI(imageUri);
            Glide.with(context).load(imageUri).override(300, 300).centerCrop().into(ivPhoto);
            Log.i(TAG, "onActivityResult: berhasil set image");
        }
    }

    private String getPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void dataProfile(String args) {
        progressDialog.show();
        mApiService.dataProfile(args).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonResults = new JSONObject(response.body().string());
                         resNoInduk = jsonResults.getJSONObject("user").getString("noInduk");
                         resNama = jsonResults.getJSONObject("user").getString("nama");
                         resEmail = jsonResults.getJSONObject("user").getString("email");
                         resPassword = jsonResults.getJSONObject("user").getString("password");
                         resPhone = jsonResults.getJSONObject("user").getString("phone");
                         resFoto = jsonResults.getJSONObject("user").getString("foto");
                         initComponents();

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void initComponents() {
        etNama.setText(resNama);
        etEmail.setText(resEmail);
        etPassword.setText(resPassword);
        etPhone.setText(resPhone);
        Glide.with(context)
                .load(serverFilepath+resFoto)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(300, 300)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_add_a_photo_24)
                .into(ivPhoto);
        progressDialog.dismiss();
    }
}