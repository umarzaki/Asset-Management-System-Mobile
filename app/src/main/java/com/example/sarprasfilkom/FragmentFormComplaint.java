package com.example.sarprasfilkom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.Result;
import com.example.sarprasfilkom.model.SharedPrefManager;
import com.example.sarprasfilkom.model.Value;
import com.google.android.material.appbar.AppBarLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FragmentFormComplaint extends Fragment {

    final Calendar myCalendar = Calendar.getInstance();
    BaseApiService mApiService;
    Button btnTakePhoto;
    ProgressDialog progressDialog;
    List<Result> results;
    ImageView previewImage;
    Uri picUri;
    View navView;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    EditText  dateFormComplaint, timeFormRoom, imageName, uraianKeluhan;
    String timeFormat, dateFormat, dateTime, timeStamp, photoName, filePath;
    Spinner locFormComplaint, asetFormComplaint;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_complaint, container, false);
        progressDialog = new ProgressDialog(getActivity());
        mApiService = UtilsApi.getAPIService();
        btnTakePhoto = view.findViewById(R.id.btnUploadFotoComplaint);
        dateFormComplaint = view.findViewById(R.id.et_dateFormComplaint);
        locFormComplaint = view.findViewById(R.id.et_locFormComplaint);
        asetFormComplaint = view.findViewById(R.id.et_assetFormComplaint);
        imageName = view.findViewById(R.id.et_imageNameComplaint);
        uraianKeluhan = view.findViewById(R.id.et_uraianFormComplaint);
        previewImage = view.findViewById(R.id.cameraPreviewComplaint);
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        photoName = "COMP_"+timeStamp+".jpg";
        loadDataLokasi();
        loadDataBarang();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }
        };
        dateFormComplaint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCameraTocaptureImage();
            }
        });

        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        Button btnKirim = view.findViewById(R.id.btnKirimComplaint);
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navView = v;
                if (TextUtils.isEmpty(locFormComplaint.getSelectedItem().toString())
                        || TextUtils.isEmpty(asetFormComplaint.getSelectedItem().toString())
                        || TextUtils.isEmpty(dateFormComplaint.getText().toString())
                        || TextUtils.isEmpty(imageName.getText().toString())){
                    Toast.makeText( getActivity(), "data yang diperlukan belum lengkap", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFormComplaint();
                    progressDialog.show();
                }
            }
        });

        return view;
    }

    private void updateDateLabel() {
        String myFormat = "yyyy-MM-dd";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateFormComplaint.setText(sdf.format(myCalendar.getTime()));
    }

    private void openCameraTocaptureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.example.sarprasfilkom.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        filePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_RESULT && resultCode == RESULT_OK) {
            //remove bundle in has null value


            //get file uri to upload to firestore Storage
            File f = new File(filePath);
            Uri contentUri = Uri.fromFile(f);
            Log.d(TAG, "onActivityResult: "+ contentUri);
//            Toast.makeText(getActivity(), "uri "+contentUri, Toast.LENGTH_SHORT).show();

            //showing image to image view
            setPic();

            //run the app

        }
    }

    private void setPic() {
        previewImage.setVisibility(View.VISIBLE);
        // Get the dimensions of the View
        int targetW = previewImage.getWidth();
        int targetH = previewImage.getHeight();
        Log.d(TAG, "setPic: "+targetW+" "+targetH);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/2, photoH/2);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        imageName.setText(photoName);

        previewImage.setImageBitmap(bitmap);
        previewImage.setRotation(90);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == IMAGE_RESULT) {
//                filePath = getImageFilePath(data);
//                if (filePath != null) {
//                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
//                    imageName.setText(photoName);
//                    Log.d(TAG, "onActivityResult: "+ filePath);
//                    previewImage.setVisibility(View.VISIBLE);
//                    previewImage.setImageBitmap(selectedImage);
//                }
//            }
//        }
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("pic_uri", picUri);
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void loadDataLokasi(){
        mApiService.namaLokasi().enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                try {
                    if (value.equals("1")) {
                        ArrayList<String> lokasi = new ArrayList<>();
                        results = response.body().getResult();
                        int i=0;
                        while(i<results.size()){
                            Result res = results.get(i);
                            lokasi.add(res.getNamaLokasi());
                            i++;
                        }
                        // Create an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, lokasi);
                        locFormComplaint.setAdapter(adapter);
                    }
                } catch (Exception e){
                    Log.e(TAG, "onResponse: ", e );
                }
            }
            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void loadDataBarang(){
        mApiService.namaBarang().enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
//                progressBar.setVisibility(View.GONE);
                try {
                    if (value.equals("1")) {
                        ArrayList<String> asset = new ArrayList<>();
                        results = response.body().getResult();
                        int i=0;
                        while(i<results.size()){
                            Result res = results.get(i);
                            asset.add(res.getNamaBarang());
                            i++;
                        }
                        // Create an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, asset);
                        asetFormComplaint.setAdapter(adapter);
                    }
                } catch (Exception e){
                    Log.e(TAG, "onResponse: ", e );
                }
            }
            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());
            }
        });
    }

    private void uploadFormComplaint(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES); // read timeout

        builder.build();
        SharedPrefManager sp = new SharedPrefManager(getActivity());
        dateTime = dateFormComplaint.getText().toString();
        String id = sp.getSPId();
        File file = new File(filePath);
        String uraianComplaint = uraianKeluhan.getText().toString();
        String lokasiComplaint = locFormComplaint.getSelectedItem().toString();
        String barangComplaint = asetFormComplaint.getSelectedItem().toString();
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image",
                photoName, RequestBody.create(MediaType.parse("image/jpg"), file));
        RequestBody pegawaiID = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody tanggalWaktu = RequestBody.create(MediaType.parse("text/plain"), dateTime);
        RequestBody lokasi = RequestBody.create(MediaType.parse("text/plain"), lokasiComplaint);
        RequestBody barang = RequestBody.create(MediaType.parse("text/plain"), barangComplaint);
        RequestBody uraian = RequestBody.create(MediaType.parse("text/plain"), uraianComplaint);
        RequestBody namaFoto = RequestBody.create(MediaType.parse("text/plain"), photoName);

        mApiService.sendFormComplaint(filePart, pegawaiID, tanggalWaktu, lokasi, barang,
                uraian, namaFoto).enqueue(new Callback<MultipartBody>() {
            @Override
            public void onResponse(Call<MultipartBody> call, Response<MultipartBody> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "laporan berhasil dibuat", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(navView).popBackStack();

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "pembuatan laporan keluhan gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MultipartBody> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("debug", "onFailure: ERROR > " + t.toString());
                Toast.makeText(getActivity(), "periksa jaringan anda", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
