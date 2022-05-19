package com.example.infogames.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
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

    Data data;
    User user;
    Toolbar toolbar;
    RetrofitService retrofitService;
    TextView textViewScore;
    List<Theme> themes;
    List<Test> tests;
    //TODO во всех активостях раскидать задачи между onStart и onCreate
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

        // Здесь скачивание данных

        if (!JSONHelper.check(this)) {
            JSONHelper.exportUserToJSON(this, user);
            // TODO вместо null сделать экспорт из материалов ресурсов
            JSONHelper.exportThemesToJSON(this, null);
            JSONHelper.exportTestsToJSON(this, null);
        } else {
            user.clone(JSONHelper.importUserFromJSON(this));
//            user.getAccess()[1] = false;
//            JSONHelper.exportUserToJSON(this, user);
        }

        if (user.getToken() != null) {
            data.setIsLogin(true);
        }
        // TODO получение данных тем и тестов с сервера
//        initialStart();
//        user.setScore(100);

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

    // Получает теоретический материал и материал для тестирования
    public void initialStart() {
        TestService testService = retrofitService.getRetrofit().create(TestService.class);
        testService.getTests().enqueue(new Callback<List<Test>>() {
                @Override
                public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
                    if (response.code() == 200) {
                        System.out.println("GET TEST: PASS");
//                        tests = response.body();
                        JSONHelper.exportTestsToJSON(MainActivity.this, response.body());
//                        System.out.println(tests.get(0).getQuestionList().get(0));
//                        System.out.println(tests.get(1));

                    } else if (response.code() == 404){
                        System.out.println("GET TEST NOT FOUND");
                    } else {
                        System.out.println("GET TEST SOMETHING GOES WRONG...");
                    }
                }

                @Override
                public void onFailure(Call<List<Test>> call, Throwable t) {
                    System.out.println("GET TEST FAILED");
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
//                        MainActivity.this.themes = response.body();
                        JSONHelper.exportThemesToJSON(MainActivity.this, response.body());
                        System.out.println("getThemes: themes was found");
                    } else if (response.code() == 404) {
                        System.out.println("Error: Themes was not found");
                    }
                }

                @Override
                public void onFailure(Call<List<Theme>> call, Throwable t) {
                    System.out.println("Error: не удалось подключиться для получения тем");
                    Toast.makeText(MainActivity.this, "" +
                                    "Не удалось подключиться к серверу для синхронизации тем",
                            Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonLearn) {
            System.out.println("user: " + user);
            System.out.println("data user: " + data.getUser());
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