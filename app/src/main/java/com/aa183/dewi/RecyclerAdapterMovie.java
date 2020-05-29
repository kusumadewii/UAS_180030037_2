package com.aa183.dewi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecyclerAdapterMovie  extends RecyclerView.Adapter<RecyclerAdapterMovie.ViewHolder> {
    private ArrayList<Movie> movieArrayList;
    private Context context;
    private String url_image = service.URL_IMAGE_MOVIE;
    private String url_hapus = service.URL_HAPUS_MOVIE;

    public RecyclerAdapterMovie(ArrayList<Movie> movieArrayList, Context context){
        this.movieArrayList = movieArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_movie, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.judul.setText(movieArrayList.get(position).getJudul());
        holder.kategori.setText(movieArrayList.get(position).getKategori());
        holder.rating.setText(movieArrayList.get(position).getRating());
        holder.id_movie.setText(movieArrayList.get(position).getId_movie());

        Picasso.get().load(url_image + movieArrayList.get(position).getImage_movie())
                .fit().placeholder(R.drawable.loading_anime)
                .error(R.drawable.ic_warning_black_24dp).into(holder.img_movie);
    }

    @Override
    public int getItemCount() {
        return movieArrayList != null? movieArrayList.size():0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView judul, kategori, rating, id_movie, deskripsi;
        private ImageView img_movie;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.movie_name);
            kategori = itemView.findViewById(R.id.kategori);
            rating = itemView.findViewById(R.id.rating);
            id_movie = itemView.findViewById(R.id.id_movie);
            img_movie = itemView.findViewById(R.id.image_movie);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Log.d("posisi", "onClick: " + getAdapterPosition());
            int posisi = getAdapterPosition();
            Intent i = new Intent(context, detail_activity.class);
            i.putExtra("judul", movieArrayList.get(posisi).getJudul());
            i.putExtra("rating", movieArrayList.get(posisi).getRating());
            i.putExtra("deskripsi", movieArrayList.get(posisi).getDeskripsi());
            i.putExtra("path", movieArrayList.get(posisi).getImage_movie());
            i.putExtra("kategori", movieArrayList.get(posisi).getKategori());
            itemView.getContext().startActivity(i);
        }

        @Override
        public boolean onLongClick(View view) {
            popupMenuShow(view, getAdapterPosition());
            Log.d("TEST", "onClick: " + "MAsuk LONG");
            return true;
        }

        private void popupMenuShow(View v, final int posisi){
            PopupMenu popupMenu = new PopupMenu(context, v);
            MenuInflater menuInflater = popupMenu.getMenuInflater();
            menuInflater.inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.edit:
                            Intent i = new Intent(context, edit_activity.class);
                            i.putExtra("judul", movieArrayList.get(posisi).getJudul());
                            i.putExtra("rating", movieArrayList.get(posisi).getRating());
                            i.putExtra("deskripsi", movieArrayList.get(posisi).getDeskripsi());
                            i.putExtra("path", movieArrayList.get(posisi).getImage_movie());
                            i.putExtra("kategori", movieArrayList.get(posisi).getKategori());
                            i.putExtra("id", movieArrayList.get(posisi).getId_movie());
                            itemView.getContext().startActivity(i);
                            break;
                        case R.id.hapus:
                            hapus(movieArrayList.get(posisi).getId_movie());
                            break;
                    }
                    return true;
                }
            });
        }
    }

    private void hapus(final String id_movie){
        AlertDialog.Builder arBuilder = new AlertDialog.Builder(context);
        arBuilder.setTitle("Hapus Movie");
        arBuilder.setMessage("Ya Untuk Menghapus")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //hapus
                        Log.d("responData", "onClick: " + id_movie);
                        prosesHapus(id_movie);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = arBuilder.create();
        alertDialog.show();
    }

    private void prosesHapus(String id_movie){
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url_hapus, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responData", "onResponse: " + response.toString());
                try {
                    JSONObject respon = new JSONObject(response);
                    if(respon.getInt("status") == 1){
                        showMessage(respon.getString("message"));
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


        smr.addStringParam("id_movie", id_movie);
        smr.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(smr);
    }

    private void showMessage(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
