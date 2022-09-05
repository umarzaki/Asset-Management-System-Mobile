package com.example.sarprasfilkom.model;

public class Result {
    String pegawaiID;
    String noInduk;
    String namaPegawai;
    String jabatanPegawai;
    String UnitPegawai;
    String namaBarang;
    String namaLokasi;
    String namaPart;
    String nomorLaporan;
    String kodeKegiatan;
    String namaPemeriksaan;
    String namaPemeliharaan;
    String namaKegiatan;
    String tanggalPemeriksaan;
    String tanggalPemeliharaan;
    String tanggalKegiatan;
    String kodeKeluhan;
    String statusKeluhan;
    String item;
    String status;

    public String getComplaintNumber() {
        return kodeKeluhan;
    }

    public String getComplaintStatus() {
        return statusKeluhan;
    }

    public String getNomorLaporan() {
        return nomorLaporan;
    }

    public String getKodeKegiatan() {
        return kodeKegiatan;
    }

    public String getNamaPemeriksaan() {
        return namaPemeriksaan;
    }

    public String getNamaPemeliharaan() { return namaPemeliharaan; }

    public String getNamaKegiatan() {
        return namaKegiatan;
    }

    public String getTanggalPemeriksaan() {
        return tanggalPemeriksaan;
    }

    public String getTanggalPemeliharaan() {
        return tanggalPemeliharaan;
    }

    public String getTanggalKegiatan() {
        return tanggalKegiatan;
    }

    public String getID() {
        return pegawaiID;
    }

    public String getNoInduk() {
        return noInduk;
    }

    public String getNama() {
        return namaPegawai;
    }

    public String getJabatan() {
        return jabatanPegawai;
    }

    public String getUnit() {
        return UnitPegawai;
    }

    public String getNamaBarang() { return namaBarang; }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public String getNamaPart() { return namaPart; }

    public String getItem() { return item; }

    public String getStatus() { return status; }
}
