package com.aa183.dewi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class edit_activity extends AppCompatActivity {

    private EditText ed_judul, ed_rating, ed_kategori, ed_deskripsi;
    private Button btn_pilihImage, btn_tambah;
    private ImageView imageView;
    private TextView txt_nama_image;
    private int PICK_IMAGE_REQUEST = 1;
    private String filepath, imageName, id_movie;
    private String judul, rating, kategori, deskripsi;
    private String url_image = service.URL_IMAGE_MOVIE;
    private String url_edit = service.URL_EDIT_MOVIE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);


        ed_deskripsi = findViewById(R.id.deskripsi_movie);
        ed_judul = findViewById(R.id.nama_movie);
        ed_rating = findViewById(R.id.rating_movie);
        ed_kategori = findViewById(R.id.kategori_movie);
        imageView = findViewById(R.id.image_movie);
        btn_pilihImage = findViewById(R.id.btn_pilih_image);
        btn_tambah = findViewById(R.id.btn_tambah);
        txt_nama_image = findViewById(R.id.movie_image_name);

        Intent i = getIntent();

        id_movie = i.getStringExtra("id");
        imageName = i.getStringExtra("path");
        ed_deskripsi.setText(i.getStringExtra("deskripsi"));
        ed_judul.setText(i.getStringExtra("judul"));
        ed_rating.setText(i.getStringExtra("rating"));
        ed_kategori.setText(i.getStringExtra("kategori"));
        txt_nama_image.setText(imageName);
        Log.d("responData", "onCreate: " + id_movie);


        Picasso.get().load(url_image + imageName)
                .fit().placeholder(R.drawable.loading_anime)
                .error(R.drawable.ic_warning_black_24dp).into(imageView);

        btn_pilihImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBrowse();
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == PICK_IMAGE_REQUEST){
                Uri picuri = data.getData();
                Log.d("add1", "onActivityResult: " + picuri.toString());
                filepath = getPath(picuri);
                imageName = filepath.substring(filepath.lastIndexOf("/") + 1);
                txt_nama_image.setText(imageName);
                Log.d("add1", "onActivityResult: " + filepath);
                imageView.setImageURI(picuri);
            }
        }
    }

    private String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, proj,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columIndex);
        return  result;
    }

    private void imageBrowse(){
        Intent galery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galery, PICK_IMAGE_REQUEST);
    }

    private void showMessage(String msg){
        Toast.makeText(edit_activity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void edit(){
        judul = ed_judul.getText().toString();
        rating = ed_rating.getText().toString();
        kategori = ed_kategori.getText().toString();
        deskripsi = ed_deskripsi.getText().toString();

        if (judul.length() > 0 && rating.length() > 0 && kategori.length() > 0 && deskripsi.length() > 0){
            SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("responData", "onResponse: " + response.toString());
                    try {
                        JSONObject respon = new JSONObject(response);
                        if(respon.getInt("status") == 1){
                            showMessage(respon.getString("message"));
                            finish();
                        }
                        else{
                            showMessage(respon.getString(("message")));
                        }
                    }
                    catch (JSONException e){
                        showMessage(e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showMessage(error.toString());
                }
            });

            if(filepath != null){
                smr.addFile("image_movie", filepath);
            }
            smr.addStringParam("judul", judul);
            smr.addStringParam("kategori", kategori);
            smr.addStringParam("rating", rating);
            smr.addStringParam("deskripsi", deskripsi);
            smr.addStringParam("path", imageName);
            smr.addStringParam("id_movie", id_movie);
            smr.setShouldCache(false);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(smr);
        }
        else {
            showMessage("Mohon Lengkapi Data");
        }
    }
}
