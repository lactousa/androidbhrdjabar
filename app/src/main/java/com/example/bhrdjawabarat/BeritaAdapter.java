package com.example.bhrdjawabarat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.ViewHolder> {

    private ArrayList<BeritaModel> beritaModel;

    public BeritaAdapter(ArrayList<BeritaModel> beritaModel) {
        this.beritaModel = beritaModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.berita_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.judulBerita.setText(beritaModel.get(position).getJudulBerita());
        holder.gambarBerita.setImageResource(beritaModel.get(position).getGambarBerita());

        final String isiBerita = beritaModel.get(position).getIsiBerita();

        // Menambahkan OnClickListener ke setiap item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dapatkan data berita yang sesuai dengan posisi item yang diklik
                int clickedPosition = holder.getAdapterPosition();
                BeritaModel berita = beritaModel.get(clickedPosition);

                // Buat Intent untuk membuka DetailBeritaActivity dan kirim data berita
                Intent intent = new Intent(view.getContext(), DetailBeritaActivity.class);
                intent.putExtra("judul_berita", berita.getJudulBerita());
                intent.putExtra("gambar_berita", berita.getGambarBerita());
                intent.putExtra("isi_berita", isiBerita);


                // Mulai aktivitas DetailBeritaActivity
                view.getContext().startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return beritaModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView judulBerita;
        ImageView gambarBerita;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judulBerita = itemView.findViewById(R.id.judulberitatest);
            gambarBerita = itemView.findViewById(R.id.gambarberitatest);
        }
    }
}
