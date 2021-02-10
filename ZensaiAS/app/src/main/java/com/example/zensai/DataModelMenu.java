package com.example.zensai;

public class DataModelMenu {

    private String nama;
    private String deskripsi;
    private String harga;
    private String urlgambar;

    public DataModelMenu(String nama, String deskripsi, String harga, String urlgambar) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.urlgambar = urlgambar;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getHarga() {
        return harga;
    }

    public String getUrlgambar() {
        return urlgambar;
    }
}
