package com.aa183.dewi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

public class detail_activity extends AppCompatActivity {

    private ImageView img_back, movie_image;
    private String judul, deskripsi, rating, kategori,path;
    private TextView txt_judul, txt_deskripsi, txt_rating, txt_kategori;
    private String url = service.URL_IMAGE_MOVIE;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activity);
        getSupportActionBar().hide();

        img_back = findViewById(R.id.img_back);
        txt_deskripsi = findViewById(R.id.deskripsi);
        txt_judul = findViewById(R.id.judul);
        txt_kategori = findViewById(R.id.kategori);
        txt_rating = findViewById(R.id.rating);
        movie_image = findViewById(R.id.image_movie);
        collapsingToolbarLayout = findViewById(R.id.collapsing_id);

        Intent i = getIntent();

        judul = i.getStringExtra("judul");
        deskripsi = i.getStringExtra("deskripsi");
        rating = i.getStringExtra("rating");
        kategori = i.getStringExtra("kategori");
        path = i.getStringExtra("path");

        txt_deskripsi.setText(deskripsi);
        txt_judul.setText(judul);
        txt_kategori.setText(kategori);
        txt_rating.setText(rating);
        collapsingToolbarLayout.setTitleEnabled(true);
        collapsingToolbarLayout.setTitle(judul);

        Picasso.get().load(url + path).fit().placeholder(R.drawable.loading_anime).error(R.drawable.ic_warning_black_24dp).into(movie_image);




        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
