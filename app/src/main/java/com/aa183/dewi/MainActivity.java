package com.aa183.dewi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int READ_EXTERNAL_STORAGE_PERMISSION = 1;
    private int WRITE_EXTERNAL_STORAGE = 1;
    private FloatingActionButton btn_tambah;
    private RecyclerView recyclerView;
    private RecyclerAdapterMovie recyclerAdapterMovie;
    private ArrayList<Movie> movieArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean refresh = false;
    private JSONArray list_movie;
    private RequestQueue queue;
    private String url = service.URL_GET_MOVIE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init
        btn_tambah = findViewById(R.id.btn_tambah);
        recyclerView = findViewById(R.id.rcv_movie);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        //Get Permission
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //onRefresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //do something
                getMovie();
            }
        });

        //create Recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 25 && btn_tambah.getVisibility() == View.VISIBLE){
                    btn_tambah.hide();
                }
                else if(dy < 0 && btn_tambah.getVisibility() != View.VISIBLE){
                    btn_tambah.show();
                }
            }
        });

        btn_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, add_movie_activity.class);
                startActivity(i);
            }
        });
        getMovie();

    }

    private void getMovie(){
        movieArrayList = new ArrayList<Movie>();
        queue = Volley.newRequestQueue(MainActivity.this);
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                String id_movie, String judul, String kategori, String rating, String deskripsi, String image_movie
                Log.d("responData", "onResponse: " + response.toString());
                try {
                    JSONObject respon = new JSONObject(response);
                    if(respon.getInt("status") == 1){
                        list_movie = respon.getJSONArray("listmovie");
                        for(int i=0;i<list_movie.length();i++){
                            JSONObject tmp = list_movie.getJSONObject(i);
                            Movie movie = new Movie(
                                    tmp.getString("id_movie"),
                                    tmp.getString("judul"),
                                    tmp.getString("kategori"),
                                    tmp.getString("rating"),
                                    tmp.getString("deskripsi"),
                                    tmp.getString("image_movie")
                            );
                            movieArrayList.add(movie);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        setRecyclerView();
                    }
                    else {
                        showMessage(respon.getString("message"));
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
        smr.setShouldCache(false);
        queue.add(smr);
        queue = null;
    }

    private void setRecyclerView(){
        Log.d("responData", "setRecyclerView: " + movieArrayList.size());
        recyclerAdapterMovie = new RecyclerAdapterMovie(movieArrayList, MainActivity.this);
        recyclerView.setAdapter(recyclerAdapterMovie);
    }

    private void showMessage(String msg){
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

}
