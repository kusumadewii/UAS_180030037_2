package com.aa183.dewi;

public class Movie {
    private String id_movie;
    private String judul;
    private String kategori;
    private String rating;
    private String deskripsi;
    private String image_movie;

    public Movie(String id_movie, String judul, String kategori, String rating, String deskripsi, String image_movie){
        this.image_movie = image_movie;
        this.id_movie = id_movie;
        this.judul = judul;
        this.kategori = kategori;
        this.rating = rating;
        this.deskripsi = deskripsi;
    }

    public String getId_movie() {
        return id_movie;
    }

    public void setId_movie(String id_movie) {
        this.id_movie = id_movie;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImage_movie() {
        return image_movie;
    }

    public void setImage_movie(String image_movie) {
        this.image_movie = image_movie;
    }

}
