package com.example.bhrdjawabarat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailBeritaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);


        // Terima data berita dari intent
        Intent intent = getIntent();
        String judulBerita = intent.getStringExtra("judul_berita");
        String isiBerita = intent.getStringExtra("isi_berita"); // Mengambil isi berita
        int gambarResId = intent.getIntExtra("gambar_berita", 0);

        // Tampilkan data berita di layout
        TextView judulTextView = findViewById(R.id.judul_berita);
        TextView isiBeritaTextView = findViewById(R.id.isi_berita); // TextView untuk isi berita
        ImageView gambarImageView = findViewById(R.id.gambar_berita);

        judulTextView.setText(judulBerita);
        isiBeritaTextView.setText(isiBerita); // Set teks isi berita
        gambarImageView.setImageResource(gambarResId);

        ImageView imageViewBack = findViewById(R.id.backButton);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


}
