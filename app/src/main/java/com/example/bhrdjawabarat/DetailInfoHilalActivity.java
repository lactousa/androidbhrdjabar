package com.example.bhrdjawabarat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailInfoHilalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info_hilal);

        // Terima data info hilal dari intent
        Intent intent = getIntent();
        String judulInfoHilal = intent.getStringExtra("judul_info_hilal");
        String isiInfoHilal = intent.getStringExtra("isi_info_hilal"); // Mengambil isi info hilal
        int gambarResId = intent.getIntExtra("gambar_info_hilal", 0);

        // Tampilkan data info hilal di layout
        TextView judulTextView = findViewById(R.id.judul_info_hilal);
        TextView isiInfoHilalTextView = findViewById(R.id.isi_info_hilal); // TextView untuk isi info hilal
        ImageView gambarImageView = findViewById(R.id.gambar_info_hilal);

        judulTextView.setText(judulInfoHilal);
        isiInfoHilalTextView.setText(isiInfoHilal); // Set teks isi info hilal
        gambarImageView.setImageResource(gambarResId);

        ImageView imageViewBack = findViewById(R.id.imageView15);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
