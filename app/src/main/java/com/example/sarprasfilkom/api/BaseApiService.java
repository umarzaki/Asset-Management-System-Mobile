package com.example.sarprasfilkom.api;

import com.example.sarprasfilkom.model.Value;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginRequest(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerRequest(@Field("noInduk") String noInduk,
                                        @Field("nama") String nama,
                                       @Field("email") String email,
                                       @Field("password") String password,
                                       @Field("phone") String phone,
                                       @Field("jabatan") String jabatan,
                                       @Field("unit") String unit);

    @FormUrlEncoded
    @POST("detailComplaint.php")
    Call<ResponseBody> detailComplaintRequest(@Field("ticket") String ticket);

    @FormUrlEncoded
    @POST("saveRegistrationKey.php")
    Call<ResponseBody> saveToken(@Field("token") String token,
                                 @Field("email") String email);

    @Multipart
    @POST("saveFormPemeriksaanRuang.php")
    Call<MultipartBody> sendFormRoom(@Part MultipartBody.Part filePart,
                                    @Part ("jenisPemeriksaan") RequestBody jenisPemeriksaan,
                                    @Part ("lokasi") RequestBody lokasi,
                                    @Part ("tanggalWaktu") RequestBody tanggalWaktu,
                                    @Part ("uraian") RequestBody uraian,
                                    @Part ("pegawaiID") RequestBody pegawaiID,
                                    @Part ("namaFoto") RequestBody namaFoto,
                                    @Part ("barang1") RequestBody barang1,
                                    @Part ("barang2") RequestBody barang2,
                                    @Part ("barang3") RequestBody barang3,
                                    @Part ("barang4") RequestBody barang4,
                                    @Part ("barang5") RequestBody barang5,
                                    @Part ("barang6") RequestBody barang6,
                                    @Part ("barang7") RequestBody barang7,
                                    @Part ("status1") RequestBody status1,
                                    @Part ("status2") RequestBody status2,
                                    @Part ("status3") RequestBody status3,
                                    @Part ("status4") RequestBody status4,
                                    @Part ("status5") RequestBody status5,
                                    @Part ("status6") RequestBody status6,
                                    @Part ("status7") RequestBody status7
                                    );

    @Multipart
    @POST("saveFormKendaraanDinas.php")
    Call<MultipartBody> sendFormCar(@Part MultipartBody.Part filePart,
                                    @Part ("jenisPemeriksaan") RequestBody jenisPemeriksaan,
                                    @Part ("nomorPlat") RequestBody nomorPlat,
                                    @Part ("jarakTempuh") RequestBody jarakTempuh,
                                    @Part ("tanggalWaktu") RequestBody tanggalWaktu,
                                    @Part ("uraian") RequestBody uraian,
                                    @Part ("pegawaiID") RequestBody pegawaiID,
                                    @Part ("namaFoto") RequestBody namaFoto,
                                    @Part ("part1") RequestBody part1,
                                    @Part ("part2") RequestBody part2,
                                    @Part ("part3") RequestBody part3,
                                    @Part ("part4") RequestBody part4,
                                    @Part ("part5") RequestBody part5,
                                    @Part ("part6") RequestBody part6,
                                    @Part ("part7") RequestBody part7,
                                    @Part ("status1") RequestBody status1,
                                    @Part ("status2") RequestBody status2,
                                    @Part ("status3") RequestBody status3,
                                    @Part ("status4") RequestBody status4,
                                    @Part ("status5") RequestBody status5,
                                    @Part ("status6") RequestBody status6,
                                    @Part ("status7") RequestBody status7
    );

    @Multipart
    @POST("saveFormPemeliharaan.php")
    Call<MultipartBody> sendFormRepair(@Part MultipartBody.Part filePart1,
                                       @Part MultipartBody.Part filePart2,
                                       @Part ("jenisPemeliharaan") RequestBody jenisPemeliharaan,
                                       @Part ("lokasi") RequestBody lokasi,
                                       @Part ("tanggalWaktu") RequestBody tanggalWaktu,
                                       @Part ("pegawaiID") RequestBody pegawaiID,
                                       @Part ("nomorPengadaan") RequestBody nomorPengadaan,
                                       @Part ("namaFoto1") RequestBody namaFoto1,
                                       @Part ("namaFoto2") RequestBody namaFoto2,
                                       @Part ("barang") RequestBody barang,
                                       @Part ("uraian") RequestBody uraian
    );

    @Multipart
    @POST("saveFormKeluhan.php")
    Call<MultipartBody> sendFormComplaint(@Part MultipartBody.Part filePart,
                                          @Part ("pegawaiID") RequestBody pegawaiID,
                                          @Part ("tanggalWaktu") RequestBody tanggalWaktu,
                                          @Part ("lokasi") RequestBody lokasi,
                                          @Part ("barang") RequestBody barang,
                                          @Part ("uraian") RequestBody uraian,
                                          @Part ("namaFoto") RequestBody namaFoto
    );

    @Multipart
    @POST("submitProfile.php")
    Call<MultipartBody> submitProfile(@Part MultipartBody.Part filePart,
                                      @Part ("noInduk") RequestBody noInduk,
                                      @Part ("nama") RequestBody nama,
                                      @Part ("email") RequestBody email,
                                      @Part ("password") RequestBody password,
                                      @Part ("phone") RequestBody phone,
                                      @Part ("namaFoto") RequestBody namaFoto
    );

    @FormUrlEncoded
    @POST("deleteUser.php")
    Call<ResponseBody> deleteUser(@Field("user") String user);

    @GET("staffManager.php")
    Call<Value> staffManager();

    @GET("allStaffHistory.php")
    Call<Value> allStaffHistory();

    @GET("complaintList.php")
    Call<Value> complaintList();

    @GET("userHistory.php")
    Call<Value> userHistory(@Query("user") String user);

    @GET("namaBarang.php")
    Call<Value> namaBarang();

    @GET("namaPart.php")
    Call<Value> namaPart();

    @GET("namaLokasi.php")
    Call<Value> namaLokasi();

    @GET("getRegistrationKey.php")
    Call<ResponseBody> getToken(@Query("type") String type);

    @GET("dataProfile.php")
    Call<ResponseBody> dataProfile(@Query("nama") String nama);

    @GET("dataRoomReport.php")
    Call<Value> dataRoomReport();

    @GET("dataRepairReport.php")
    Call<Value> dataRepairReport();

    @GET("dataCarReport.php")
    Call<Value> dataCarReport();

    @GET("dataNotification.php")
    Call<Value> dataNotification();

    @FormUrlEncoded
    @POST("detailRoomReport.php")
    Call<ResponseBody> detailRoomReport(@Field("reportId") String reportId);

    @GET("statusReportRoom.php")
    Call<Value> statusReportRoom(@Query("reportId") String ReportId);

    @FormUrlEncoded
    @POST("detailCarReport.php")
    Call<ResponseBody> detailCarReport(@Field("reportId") String reportId);

    @GET("statusReportCar.php")
    Call<Value> statusReportCar(@Query("reportId") String ReportId);

    @FormUrlEncoded
    @POST("detailRepairReport.php")
    Call<ResponseBody> detailRepairReport(@Field("reportId") String reportId);

    @FormUrlEncoded
    @POST("verifyReport.php")
    Call<ResponseBody> verifyReport(@Field("reportId") String reportId,
                                    @Field("pegawaiId") String pegawaiId);

    @FormUrlEncoded
    @POST("verifyRepair.php")
    Call<ResponseBody> verifyRepair(@Field("reportId") String reportId,
                                    @Field("pegawaiId") String pegawaiId);
}
