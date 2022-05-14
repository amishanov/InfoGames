package com.example.infogames;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.infogames.model.Token;
import com.example.infogames.model.User;
import com.example.infogames.retrofit.RetrofitService;
import com.example.infogames.retrofit.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Data data;
    private User user;

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonSetUp();


        // TODO Изначальная инициализация файлов
        data = Data.getInstance();
        user = data.getUser();
        System.out.println(user);
        user.setScore(5);
        TextView textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewScore.setText(Integer.toString(user.getScore()));

        Token testToken =  new Token("a");
        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);

        userService.getUserByToken("token").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Toast.makeText(MainActivity.this, "AAAAAA", Toast.LENGTH_LONG).show();
                System.out.println("PASS");
                User u  = response.body();
                MainActivity.this.user.clone(u);

                System.out.println(MainActivity.this.user);
//                Data.getInstance().setUser(response.body());
                textViewScore.setText(Integer.toString(MainActivity.this.user.getScore()));

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Can not load profile", Toast.LENGTH_LONG).show();
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "", t);
                System.out.println("FAILED");
            }
        });
    }

    // Метод для настройки кнопок
    private void buttonSetUp() {
        Button learnButton = (Button) findViewById(R.id.buttonLearn);
        Button playButton = (Button) findViewById(R.id.buttonPlay);
        ImageButton profileButton = (ImageButton) findViewById(R.id.buttonProfile);

        learnButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);

        Button a = (Button) findViewById(R.id.te);
        a.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonLearn) {
            Intent intent = new Intent(this, ThemesActivity.class);
            startActivity(intent);
        } else if (id == R.id.buttonPlay) {
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
            // TODO Переход на игровую активность
        } else if (id == R.id.buttonProfile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }  else if (id == R.id.te) {
            Log.i("MYTAG", user.toString());
            Toast.makeText(this, Integer.toString(user.getScore()), Toast.LENGTH_LONG).show();
            System.out.println(user);
//            setContentView(R.layout.activity_profile);
        }

    }
}