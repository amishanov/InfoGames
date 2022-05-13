package com.example.infogames;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Data data;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonSetUp();

        data = Data.getInstance();
        data.setScore(5);
        TextView textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewScore.setText(Integer.toString(data.getScore()));
    }

    // Метод для настройки кнопок
    private void buttonSetUp() {
        Button learnButton = (Button) findViewById(R.id.learnButton);
        Button playButton = (Button) findViewById(R.id.playButton);
        learnButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.learnButton) {
            Intent intent = new Intent(this, ThemesActivity.class);
            startActivity(intent);
        } else if (id == R.id.playButton) {
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
            // TODO Переход на игровую активность
        }

    }
}