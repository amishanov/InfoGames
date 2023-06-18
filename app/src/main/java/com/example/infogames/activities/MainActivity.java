package com.example.infogames.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.infogames.Data;
import com.example.infogames.JSONHelper;
import com.example.infogames.R;
import com.example.infogames.model.Test;
import com.example.infogames.model.Theme;
import com.example.infogames.model.User;
import com.example.infogames.retrofit.RetrofitService;
import com.example.infogames.retrofit.TestService;
import com.example.infogames.retrofit.ThemeService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    Data data;
    User user;
    Toolbar toolbar;
    RetrofitService retrofitService;
    TextView textViewScore;
    List<Theme> themes;
    List<Test> tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonSetUp();
        textViewScore = (TextView) findViewById(R.id.textViewScore);

        data = Data.getInstance();
        user = data.getUser();
        retrofitService = data.getRetrofitService();

        // Загрузка из ресурсных файлов
        // TODO сделать поддержку загрузки при старте приложения, а не активности
        //  (Можно в Singletone добавить переменную для проверки)
        tests = JSONHelper.importTestsFromRes(this);
        themes = JSONHelper.importThemesFromRes(this);
        JSONHelper.exportTestsToJSON(this, tests);
        JSONHelper.exportThemesToJSON(this, themes);

        // Проверка существования данных пользователя в локальном хранилище
        // TODO сломано из-за nullpointer, починить
        if (!JSONHelper.check(this)) {
            Log.i(TAG, "Пользовательских данных не существует, создание...");
            JSONHelper.exportUserToJSON(this, user);

        } else {
            Log.i(TAG, "Пользовательские данные найдены");

//            user.clone(JSONHelper.importUserFromJSON(this));
        }

        // Установка проверки входа в онлайн, если есть, будет осуществляться передача прогресса
        // на сервер
        if (user.getToken() != null) {
            data.setIsLogin(true);
        }
        initialStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        textViewScore.setText(Integer.toString(user.getScore()));
        ImageButton buttonProfile = (ImageButton) findViewById(R.id.buttonProfile);
        if (data.isLogin())
        {
            buttonProfile.setImageResource(R.drawable.ic_profile_login);
        }
        else {
            buttonProfile.setImageResource(R.drawable.ic_profile);
        }
    }

    // Получение теории и тестов с сервера
    public void initialStart() {
        TestService testService = retrofitService.getRetrofit().create(TestService.class);
        testService.getTests().enqueue(new Callback<List<Test>>() {
                @Override
                public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                    if (response.code() == 200) {
                        Log.i(TAG, "GET TEST: PASS");
                        JSONHelper.exportTestsToJSON(MainActivity.this, response.body());

                    } else if (response.code() == 404){
                        Log.i(TAG, "GET TEST: NOT FOUND");
                    } else {
                        Log.i(TAG, "GET TEST: SOMETHING GOES WRONG...");
                    }
                }

                @Override
                public void onFailure(Call<List<Test>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "" +
                            "Не удалось подключиться к серверу для синхронизации тестов",
                            Toast.LENGTH_LONG).show();
                }
            });
        RetrofitService retrofitService = data.getRetrofitService();
        ThemeService themeService = retrofitService.getRetrofit().create(ThemeService.class);
        themeService.getThemes().enqueue(new Callback<List<Theme>>() {
                @Override
                public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
                    if (response.code() == 200) {
                        JSONHelper.exportThemesToJSON(MainActivity.this, response.body());
                        Log.i(TAG, "GET THEMES: PASS");
                    } else if (response.code() == 404) {
                        Log.i(TAG, "GET THEMES: NOT FOUND");
                    } else {
                        Log.i(TAG, "GET THEMES: SOMETHING GOES WRONG...");
                    }
                }

                @Override
                public void onFailure(Call<List<Theme>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "" +
                                    "Не удалось подключиться к серверу для синхронизации тем",
                            Toast.LENGTH_LONG).show();
                }
            });
    }


    // Настройка кнопок
    private void buttonSetUp() {
        Button learnButton = (Button) findViewById(R.id.buttonLearn);
        Button playButton = (Button) findViewById(R.id.buttonPlay);
        ImageButton profileButton = (ImageButton) findViewById(R.id.buttonProfile);
        learnButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonLearn) {
            Intent intent = new Intent(this, ThemesActivity.class);
            startActivity(intent);
        } else if (id == R.id.buttonPlay) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        } else if (id == R.id.buttonProfile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }
}