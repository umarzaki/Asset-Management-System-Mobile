package com.example.sarprasfilkom.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String TOKEN = "to";
    public static final String SP_SARPRAS = "spSarpras";
    public static final String SP_ID = "spId";
    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_ROLE = "spRole";
    public static final String SP_PHONE = "spPhone";
    public static final String SP_PHOTO = "spPhoto";
    public static final String SP_JABATAN= "spJabatan";
    public static final String SP_IS_ACTIVE = "spIsActive";
    public static final String SP_NOMOR_INDUK = "spNoInduk";
    public static final String SP_BARANG_PINTU = "Pintu";
    public static final String SP_BARANG_LCD = "LCD Projector";
    public static final String SP_BARANG_KABEL = "Kabel VGA";
    public static final String SP_BARANG_MEJA = "Meja";
    public static final String SP_BARANG_KURSI = "Kursi";
    public static final String SP_BARANG_LAMPU = "Lampu";
    public static final String SP_BARANG_LISTRIK = "Listrik";
    public static final String SP_MOBIL_LAMPU_UTAMA = "Lampu Utama";
    public static final String SP_MOBIL_SIGN_LAMP = "Sign Lamp";
    public static final String SP_MOBIL_FOG_LAMP = "Fog Lamp";
    public static final String SP_MOBIL_TEKANAN_BAN = "Tekanan Ban";
    public static final String SP_MOBIL_AIR_RADIATOR = "Air Radiator";
    public static final String SP_MOBIL_AKI = "Aki";
    public static final String SP_MOBIL_OLI = "Oli";
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_SARPRAS, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void clearAll(){
        spEditor.clear();
        spEditor.commit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }
    public String getSPToken(){
        return sp.getString(TOKEN, "");
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public String getSPRole(){
        return sp.getString(SP_ROLE, "");
    }

    public String getSPJabatan(){
        return sp.getString(SP_JABATAN, "");
    }

    public String getSPNoInduk(){
        return sp.getString(SP_NOMOR_INDUK, "");
    }

    public String getSPId(){
        return sp.getString(SP_ID, "");
    }

    public String getSPPhone(){
        return sp.getString(SP_PHONE, "");
    }

    public String getSPPhoto(){
        return sp.getString(SP_PHOTO, "");
    }

    public Boolean getSPIsActive(){
        return sp.getBoolean(SP_IS_ACTIVE, false);
    }

    public String getSpBarangPintu(){
        return sp.getString(SP_BARANG_PINTU, "OK");
    }

    public String getSpBarangLcd(){
        return sp.getString(SP_BARANG_LCD, "OK");
    }

    public String getSpBarangKabel(){
        return sp.getString(SP_BARANG_KABEL, "OK");
    }

    public String getSpBarangMeja(){
        return sp.getString(SP_BARANG_MEJA, "OK");
    }

    public String getSpBarangKursi(){
        return sp.getString(SP_BARANG_KURSI, "OK");
    }

    public String getSpBarangLampu(){
        return sp.getString(SP_BARANG_LAMPU, "OK");
    }

    public String getSpBarangListrik(){
        return sp.getString(SP_BARANG_LISTRIK, "OK");
    }

    public String getSpMobilLampuUtama(){
        return sp.getString(SP_MOBIL_LAMPU_UTAMA, "OK");
    }

    public String getSpMobilSignLamp(){
        return sp.getString(SP_MOBIL_SIGN_LAMP, "OK");
    }

    public String getSpMobilFogLamp(){
        return sp.getString(SP_MOBIL_FOG_LAMP, "OK");
    }

    public String getSpMobilTekananBan(){
        return sp.getString(SP_MOBIL_TEKANAN_BAN, "OK");
    }

    public String getSpMobilAirRadiator(){
        return sp.getString(SP_MOBIL_AIR_RADIATOR, "OK");
    }

    public String getSpMobilAki(){
        return sp.getString(SP_MOBIL_AKI, "OK");
    }

    public String getSpMobilOli(){
        return sp.getString(SP_MOBIL_OLI, "OK");
    }
}
