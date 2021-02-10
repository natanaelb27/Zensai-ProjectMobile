package com.example.zensai;

public class DataModelCart {
    private String nama;
    private String deskripsi;
    private String harga;
    private String urlgambar;
    private int quantity;

    public DataModelCart(String nama, String deskripsi, String harga, String urlgambar, int quantity) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.harga = harga;
        this.urlgambar = urlgambar;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }
}
