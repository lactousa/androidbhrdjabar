package com.example.bhrdjawabarat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class NotifikasiAdzanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi_adzan);

        // Mengatur event onClick untuk ImageView sebagai tombol kembali
        ImageView imageViewBack = findViewById(R.id.imageView15);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Ini akan mengakibatkan perilaku seperti tombol kembali standar
            }
        });



        // Anda bisa menambahkan logika atau interaksi tambahan di sini sesuai kebutuhan
    }
}
