package com.example.bhrdjawabarat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class InfoHilalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InfoHilalAdapter infoHilalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_hilal);

        recyclerView = findViewById(R.id.recyclerViewInfoHilal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<InfoHilalModel> infoHilalModelList = generateInfoHilalModel();
        infoHilalAdapter = new InfoHilalAdapter((ArrayList<InfoHilalModel>) infoHilalModelList);
        recyclerView.setAdapter(infoHilalAdapter);

        // Mengatur event onClick untuk ImageView sebagai tombol kembali
        ImageView imageViewBack = findViewById(R.id.imageView15);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Metode ini akan dipanggil saat ImageView untuk menuju ke ActivityLocationRukyat diklik
    public void openLocationRukyatActivity(View view) {
        Intent intent = new Intent(this, ActivityLocationRukyat.class);
        startActivity(intent);
    }

    private ArrayList<InfoHilalModel> generateInfoHilalModel() {
        ArrayList<InfoHilalModel> infoHilalModelList = new ArrayList<>();
        infoHilalModelList.add(new InfoHilalModel("Pasirlasih, Kab. Pangandaran", R.drawable.iconbosca, "Pasirlasih merupakan lokasi pantai yang indah di Kabupaten Pangandaran. Pantai ini terkenal dengan pasir putihnya dan ombak yang cocok untuk berselancar. Selain itu, Anda dapat menikmati pemandangan matahari terbenam yang spektakuler di sini."));
        infoHilalModelList.add(new InfoHilalModel("Cipatujah, Kab. Tasikmalaya", R.drawable.iconbosca, "Cipatujah adalah sebuah desa di Kabupaten Tasikmalaya yang dikenal dengan pesona alamnya. Daerah ini memiliki keindahan alam yang menakjubkan, termasuk pegunungan, hutan, dan sungai yang jernih. Cocok untuk aktivitas wisata alam."));
        infoHilalModelList.add(new InfoHilalModel("Pondok Bali, Kab. Subang", R.drawable.iconbosca, "Pondok Bali adalah sebuah kawasan wisata yang terletak di Kabupaten Subang. Kawasan ini dikenal dengan pemandangan sawah hijau yang luas dan suasana pedesaan yang tenang. Tempat yang bagus untuk berlibur dari hiruk-pikuk perkotaan."));
        infoHilalModelList.add(new InfoHilalModel("Santolo, Kab. Garut", R.drawable.iconbosca, "Santolo adalah sebuah pantai yang terletak di Kabupaten Garut. Pantai ini menawarkan keindahan alam yang eksotis dengan pasir putih, laut biru, dan ombak yang cocok untuk berselancar. Pantai ini menjadi tujuan wisata yang populer di Jawa Barat."));
        infoHilalModelList.add(new InfoHilalModel("UNISBA", R.drawable.iconbosca, "UNISBA atau Universitas Islam Bandung adalah salah satu perguruan tinggi terkemuka di Kota Bandung. Kampus ini terkenal dengan program akademiknya yang berkualitas dan suasana kampus yang nyaman."));
        infoHilalModelList.add(new InfoHilalModel("Pantai Baro Gebang, Kab. Cirebon", R.drawable.iconbosca, "Pantai Baro Gebang adalah destinasi pantai yang menarik di Kabupaten Cirebon. Pantai ini dikenal dengan pemandangan pantai yang indah dan tempat yang bagus untuk bersantai sambil menikmati angin laut."));
        infoHilalModelList.add(new InfoHilalModel("POB Cibeas, Kab. Sukabumi", R.drawable.iconbosca, "POB Cibeas adalah salah satu observatorium astronomi terkemuka yang terletak di Kabupaten Sukabumi. Tempat ini digunakan untuk penelitian astronomi dan seringkali menjadi tempat pengamatan langit yang menarik."));
        infoHilalModelList.add(new InfoHilalModel("SMA Astha Hannas. Kab. Subang", R.drawable.iconbosca, "SMA Astha Hannas adalah sebuah sekolah menengah atas yang terletak di Kabupaten Subang. Sekolah ini berkomitmen untuk memberikan pendidikan berkualitas tinggi kepada siswa-siswinya."));
        infoHilalModelList.add(new InfoHilalModel("Observatorium Bosscha", R.drawable.iconbosca, "Observatorium Bosscha adalah salah satu observatorium astronomi tertua di Indonesia, terletak di Lembang, Bandung. Observatorium ini terkenal dengan teleskopnya yang besar dan sering digunakan untuk penelitian astronomi."));
        infoHilalModelList.add(new InfoHilalModel("POB Gunung Putri Sukamanah, Pataruman, Kota Banjar", R.drawable.iconbosca, "POB Gunung Putri Sukamanah adalah sebuah observatorium astronomi yang terletak di Kota Banjar. Tempat ini sering digunakan untuk pengamatan benda langit dan penelitian astronomi."));
        infoHilalModelList.add(new InfoHilalModel("Imah Noong, Kab. Bandung Barat", R.drawable.iconbosca, "Imah Noong adalah sebuah rumah makan dan tempat wisata kuliner yang terletak di Kabupaten Bandung Barat. Tempat ini terkenal dengan hidangan khas Sunda yang lezat dan suasana pedesaan yang ramah."));


        // Tambahkan data lainnya sesuai kebutuhan
        return infoHilalModelList;
    }

}
