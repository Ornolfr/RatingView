package com.github.ornolfr.ratingview_sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.ornolfr.ratingview.RatingView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RatingView ratingView;

    private Button buttonRating, buttonIndicator;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingView = (RatingView) findViewById(R.id.ratingView);

        buttonIndicator = (Button) findViewById(R.id.buttonIndicator);
        buttonRating = (Button) findViewById(R.id.buttonRating);

        buttonIndicator.setOnClickListener(this);
        buttonRating.setOnClickListener(this);

        ratingView.setOnRatingChangedListener(new RatingView.OnRatingChangedListener() {
            @Override
            public void onRatingChange(float oldRating, float newRating) {
                Toast.makeText(MainActivity.this, "Old rating = " + oldRating + "; New rating = " + newRating, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonIndicator:
                ratingView.setIsIndicator(!ratingView.isIndicator());
                break;
            case R.id.buttonRating:
                ratingView.setRating(random.nextFloat() * ratingView.getMaxCount());
                break;
        }
    }
}
