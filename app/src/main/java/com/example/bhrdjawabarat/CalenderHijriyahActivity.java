package com.example.bhrdjawabarat;

import android.content.pm.ActivityInfo;
import android.icu.util.Calendar;
import android.icu.util.IslamicCalendar;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CalenderHijriyahActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_hijriyah);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ImageView imageViewBack = findViewById(R.id.imageView15);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        calendarView = findViewById(R.id.calendarView2);

        // Atur listener untuk meng-handle perubahan tanggal pada kalender Hijriyah
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Buat objek IslamicCalendar berdasarkan tanggal yang dipilih
                ULocale locale = new ULocale("ar_SA"); // Lokalisasi untuk Arab Saudi
                IslamicCalendar islamicCalendar = new IslamicCalendar();
                islamicCalendar.setTimeInMillis(view.getDate()); // Set tanggal berdasarkan yang dipilih

                // Dapatkan informasi tanggal Hijriyah
                int hijriYear = islamicCalendar.get(Calendar.YEAR);
                int hijriMonth = islamicCalendar.get(Calendar.MONTH);
                int hijriDay = islamicCalendar.get(Calendar.DAY_OF_MONTH);

                // Tampilkan tanggal Hijriyah yang dipilih dalam format yang sesuai
                String hijriDate = hijriYear + "/" + (hijriMonth + 1) + "/" + hijriDay;
                Toast.makeText(CalenderHijriyahActivity.this, "Tanggal Hijriyah: " + hijriDate, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
