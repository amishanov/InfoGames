package com.example.infogames;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSetUp();
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