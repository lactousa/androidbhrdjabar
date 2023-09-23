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

public class InfoHilalAdapter extends RecyclerView.Adapter<InfoHilalAdapter.ViewHolder> {

    private ArrayList<InfoHilalModel> infoHilalModelList;

    public InfoHilalAdapter(ArrayList<InfoHilalModel> infoHilalModelList) {
        this.infoHilalModelList = infoHilalModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_hilal_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoHilalModel infoHilalModel = infoHilalModelList.get(position);

        holder.judulInfoHilal.setText(infoHilalModel.getJudulInfoHilal());
        holder.gambarInfoHilal.setImageResource(infoHilalModel.getGambarInfoHilal());

        final String isiInfoHilal = infoHilalModel.getIsiInfoHilal();

        // Menambahkan OnClickListener ke setiap item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Buat Intent untuk membuka DetailInfoHilalActivity dan kirim data Info Hilal
                Intent intent = new Intent(view.getContext(), DetailInfoHilalActivity.class);
                intent.putExtra("judul_info_hilal", infoHilalModel.getJudulInfoHilal());
                intent.putExtra("gambar_info_hilal", infoHilalModel.getGambarInfoHilal());
                intent.putExtra("isi_info_hilal", isiInfoHilal);

                // Mulai aktivitas DetailInfoHilalActivity
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return infoHilalModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView judulInfoHilal;
        ImageView gambarInfoHilal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            judulInfoHilal = itemView.findViewById(R.id.judul_info_hilal);
            gambarInfoHilal = itemView.findViewById(R.id.gambar_info_hilal);
        }
    }
}
