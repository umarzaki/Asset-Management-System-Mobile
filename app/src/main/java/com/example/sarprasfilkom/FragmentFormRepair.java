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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sarprasfilkom.api.BaseApiService;
import com.example.sarprasfilkom.api.UtilsApi;
import com.example.sarprasfilkom.model.Result;
import com.example.sarprasfilkom.model.SharedPrefManager;
import com.example.sarprasfilkom.model.Value;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

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

public class  FragmentFormRepair extends Fragment {

    BaseApiService mApiService;
    final Calendar myCalendar = Calendar.getInstance();
    TimePickerDialog timePicker;
    List<Result> results;
    ProgressDialog progressDialog;
    ImageView previewImage1, previewImage2;
    Button btnTakePhoto1, btnTakePhoto2, btnKirim;
    Uri picUri;
    View navView;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    TextView nomorKeluhan;
    EditText dateFormRepair, timeFormRepair, imageName1, imageName2, uraianPemeliharaan, etNomorKeluhan, etNomorPengadaan;
    Spinner ruangSpinner, jenisPerbaikanSpinner, assetSpinner;
    String timeFormat, dateFormat, dateTime, timeStamp, photoName1, photoName2, filePath1, filePath2, btnChooser, ticket;
    ArrayList<String> asset, lokasi;
    int posAsset, posLoc;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_repair, container, false);
        TextView toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Form Perbaikan");
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        BottomNavigationView bnv = getActivity().findViewById(R.id.bn_main);
        bnv.setVisibility(View.INVISIBLE);
        mApiService = UtilsApi.getAPIService();
        progressDialog = new ProgressDialog(getActivity());
        dateFormRepair = view.findViewById(R.id.et_dateFormRepair);
        timeFormRepair = view.findViewById(R.id.et_timeFormRepair);
        nomorKeluhan = view.findViewById(R.id.tv_nomorKeluhanFormPerbaikan);
        etNomorKeluhan = view.findViewById(R.id.et_nomorKeluhanFormPerbaikan);
        ruangSpinner = view.findViewById(R.id.spinnerFormRepair);
        jenisPerbaikanSpinner = view.findViewById(R.id.jenisPerbaikanSpinner);
        assetSpinner = view.findViewById(R.id.spinnerNamaAset);
        btnTakePhoto1 = view.findViewById(R.id.btnUploadFotoBefore);
        btnTakePhoto2 = view.findViewById(R.id.btnUploadFotoAfter);
        imageName1 = view.findViewById(R.id.et_imageNameBeforeRepair);
        imageName2 = view.findViewById(R.id.et_imageNameAfterRepair);
        uraianPemeliharaan = view.findViewById(R.id.et_uraianPemeliharaan);
        etNomorPengadaan = view.findViewById(R.id.et_nomorPengadaan);
        previewImage1 = view.findViewById(R.id.cameraPreviewBefore);
        previewImage2 = view.findViewById(R.id.cameraPreviewAfter);
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        photoName1 = "MT_1_"+timeStamp+".jpg";
        photoName2 = "MT_2_"+timeStamp+".jpg";
        loadDataLokasi();
        loadDataBarang();


        ArrayAdapter<CharSequence> jenisPerbaikanAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.JenisPerbaikan, android.R.layout.simple_spinner_item);
        jenisPerbaikanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jenisPerbaikanSpinner.setAdapter(jenisPerbaikanAdapter);

        if(getArguments() != null){
            nomorKeluhan.setVisibility(View.VISIBLE);
            etNomorKeluhan.setVisibility(View.VISIBLE);
            etNomorKeluhan.setText(getArguments().getString("tiket"));
            jenisPerbaikanSpinner.setSelection(2);
            int i = 0;


        }

        jenisPerbaikanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("pemeliharaan keluhan")){
                    nomorKeluhan.setVisibility(View.VISIBLE);
                    etNomorKeluhan.setVisibility(View.VISIBLE);
                } else {
                    nomorKeluhan.setVisibility(View.GONE);
                    etNomorKeluhan.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




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

        dateFormRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        timeFormRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minutes = myCalendar.get(Calendar.MINUTE);
                // time picker dialog
                timePicker = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                timeFormat = String.format("%02d", sHour) + ":" + String.format("%02d", sMinute);
                                timeFormRepair.setText(timeFormat);
                            }
                        }, hour, minutes, true);
                timePicker.show();
            }
        });

        btnTakePhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChooser = "button1";
                openCameraTocaptureImage();
            }
        });
        btnTakePhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnChooser = "button2";
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

        btnKirim = view.findViewById(R.id.btnKirimRepair);
        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navView = v;
                if (TextUtils.isEmpty(ruangSpinner.getSelectedItem().toString())
                        || TextUtils.isEmpty(dateFormRepair.getText().toString())
                        || TextUtils.isEmpty(timeFormRepair.getText().toString())
                        || TextUtils.isEmpty(assetSpinner.getSelectedItem().toString())
                        || TextUtils.isEmpty(jenisPerbaikanSpinner.getSelectedItem().toString())
                        || TextUtils.isEmpty(uraianPemeliharaan.getText().toString())
                        || TextUtils.isEmpty(imageName1.getText().toString())
                        || TextUtils.isEmpty(imageName2.getText().toString())) {
                    Toast.makeText(getActivity(), "harap lengkapi data yang diperlukan", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFormRepair();
                    progressDialog.show();
                }
            }
        });

        return view;
    }

    private void updateDateLabel() {
        dateFormat = "yyyy-MM-dd";//In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        dateFormRepair.setText(sdf.format(myCalendar.getTime()));
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
        switch (btnChooser){
            case "button1":
                filePath1 = image.getAbsolutePath();
                break;
            case "button2":
                filePath2 = image.getAbsolutePath();
                break;
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_RESULT && resultCode == RESULT_OK) {
            //remove bundle in has null value


            //get file uri to upload to firestore Storage
            File f1 = new File(filePath1);
            File f2 = new File(filePath1);
            Uri contentUri1 = Uri.fromFile(f1);
            Uri contentUri2 = Uri.fromFile(f2);
            Log.d(TAG, "onActivityResult: "+ contentUri1);
            Log.d(TAG, "onActivityResult: "+ contentUri2);
//            Toast.makeText(getActivity(), "uri "+contentUri, Toast.LENGTH_SHORT).show();

            //showing image to image view
            setPic();

            //run the app

        }
    }

    private void setPic() {

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

        switch (btnChooser){
            case "button1":
                Bitmap selectedImage1 = BitmapFactory.decodeFile(filePath1, bmOptions);
                imageName1.setText(photoName1);
                Log.d(TAG, "onActivityResult: "+ filePath1);
                previewImage1.setVisibility(View.VISIBLE);
                previewImage1.setImageBitmap(selectedImage1);
                previewImage1.setRotation(90);
                break;
            case "button2":
                Bitmap selectedImage2 = BitmapFactory.decodeFile(filePath2, bmOptions);
                imageName2.setText(photoName2);
                Log.d(TAG, "onActivityResult: "+ filePath2);
                previewImage2.setVisibility(View.VISIBLE);
                previewImage2.setImageBitmap(selectedImage2);
                previewImage2.setRotation(90);
                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == IMAGE_RESULT) {
//                switch (btnChooser){
//                    case "button1":
//                        filePath1 = getImageFilePath(data);
//                        break;
//                    case "button2":
//                        filePath2 = getImageFilePath(data);
//                        break;
//                }
//                if (filePath1 != null || filePath2 != null) {
//                    switch (btnChooser){
//                        case "button1":
//                            Bitmap selectedImage1 = BitmapFactory.decodeFile(filePath1);
//                            imageName1.setText(photoName1);
//                            Log.d(TAG, "onActivityResult: "+ filePath1);
//                            previewImage1.setVisibility(View.VISIBLE);
//                            previewImage1.setImageBitmap(selectedImage1);
//                            break;
//                        case "button2":
//                            Bitmap selectedImage2 = BitmapFactory.decodeFile(filePath2);
//                            imageName2.setText(photoName2);
//                            Log.d(TAG, "onActivityResult: "+ filePath2);
//                            previewImage2.setVisibility(View.VISIBLE);
//                            previewImage2.setImageBitmap(selectedImage2);
//                            break;
//                    }
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
                        lokasi = new ArrayList<>();
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
                        ruangSpinner.setAdapter(adapter);

                        if(getArguments() != null){
                            int j = 0;
                            while(j<lokasi.size()){
                                if(lokasi.get(j).equals(getArguments().getString("ruang"))){
                                    Log.d(TAG, "onResponse: "+ lokasi.get(j));
                                    ruangSpinner.setSelection(j);
                                    j = lokasi.size();
                                } else {
                                    j++;
                                }
                            }
                        }
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
                try {
                    if (value.equals("1")) {
                        asset = new ArrayList<>();
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
                        assetSpinner.setAdapter(adapter);

                        if(getArguments() != null){
                            int j = 0;
                            while(j<asset.size()){
                                if(asset.get(j).equals(getArguments().getString("asset"))){
                                    assetSpinner.setSelection(j);
                                    j = asset.size();
                                } else {
                                    j++;
                                }
                            }
                        }
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

    private void uploadFormRepair(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES); // read timeout

        builder.build();
        SharedPrefManager sp = new SharedPrefManager(getActivity());
        dateTime = dateFormRepair.getText().toString()+" "+timeFormRepair.getText().toString();
        String id = sp.getSPId();
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        String aset = assetSpinner.getSelectedItem().toString();
        String jenisPemeliharaan = jenisPerbaikanSpinner.getSelectedItem().toString();
        String nomorPengadaan = etNomorPengadaan.getText().toString();
        String ruang = ruangSpinner.getSelectedItem().toString();
        String uraian = uraianPemeliharaan.getText().toString();
        MultipartBody.Part filePart1 = MultipartBody.Part.createFormData("image1",
                photoName1, RequestBody.create(MediaType.parse("image/jpg"), file1));
        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData("image2",
                photoName2, RequestBody.create(MediaType.parse("image/jpg"), file2));
        RequestBody ReqJenisPemeliharaan = RequestBody.create(MediaType.parse("text/plain"), jenisPemeliharaan);
        RequestBody lokasi = RequestBody.create(MediaType.parse("text/plain"), ruang);
        RequestBody tanggalWaktu = RequestBody.create(MediaType.parse("text/plain"), dateTime);
        RequestBody RequestUraian = RequestBody.create(MediaType.parse("text/plain"), uraian);
        RequestBody pegawaiID = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody ReqNomorPengadaan = RequestBody.create(MediaType.parse("text/plain"), nomorPengadaan);
        RequestBody namaFoto1 = RequestBody.create(MediaType.parse("text/plain"), photoName1);
        RequestBody namaFoto2 = RequestBody.create(MediaType.parse("text/plain"), photoName2);
        RequestBody barang = RequestBody.create(MediaType.parse("text/plain"), aset);

        mApiService.sendFormRepair(filePart1, filePart2, ReqJenisPemeliharaan, lokasi, tanggalWaktu, pegawaiID,
                ReqNomorPengadaan, namaFoto1, namaFoto2, barang, RequestUraian).enqueue(new Callback<MultipartBody>() {
            @Override
            public void onResponse(Call<MultipartBody> call, Response<MultipartBody> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "laporan berhasil dibuat", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(navView).popBackStack();

                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "gagal membuat laporan perbaikan", Toast.LENGTH_SHORT).show();
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
