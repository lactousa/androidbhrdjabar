package com.example.bhrdjawabarat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SplashScreen2 extends AppCompatActivity {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen2);

        ImageView imageView = findViewById(R.id.imageView2); // Ganti dengan ID yang sesuai pada XML
        gestureDetector = new GestureDetector(this, new SwipeGestureListener());

        imageView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        long delayMillis = 1000;
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen2.this, SplashScreen3.class);
            startActivity(intent);
            finish();
        }, delayMillis);
    }

    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY) &&
                    Math.abs(diffX) > SWIPE_THRESHOLD &&
                    Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    // Swipe right
                    Intent intent = new Intent(SplashScreen2.this, SplashScreen3.class);
                    startActivity(intent);
                    finish();
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
