package com.example.bhrdjawabarat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TentangBhrdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tentang_bhrd);

        ImageView imageViewBack = findViewById(R.id.imageView29);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kode yang akan dijalankan saat ImageView "back" diklik
                finish(); // Menutup activity saat ini dan kembali ke activity sebelumnya
            }

        // Anda bisa menambahkan logika atau interaksi tambahan di sini sesuai kebutuhan

    });
}
}
