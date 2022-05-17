package com.example.infogames.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

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
    RatingBar ratingBar;
    Toolbar toolbar;
    RetrofitService retrofitService;
    TextView textViewScore;
    List<Theme> themes;
    //TODO во всех активостях раскидать задачи между onStart и onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonSetUp();
        textViewScore = (TextView) findViewById(R.id.textViewScore);


        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setStepSize(1);

        // TODO Изначальная инициализация файлов
        data = Data.getInstance();
        user = data.getUser();
        System.out.println("Initial user: " + user);
        //TODO Проверка на то, залогинен ли пользователь, синхронизация материалов
        System.out.println(data.isLogin());
        if (data.isLogin())
        {
            ImageButton buttonProfile = (ImageButton) findViewById(R.id.buttonProfile);
            buttonProfile.setImageResource(R.drawable.ic_profile_login);
        }
        user.setScore(5);


//        RetrofitService retrofitService = data.getRetrofitService();
//        RetrofitService retrofitService = new RetrofitService();
//        UserService userService = retrofitService.getRetrofit().create(UserService.class);
//
//        userService.getUserByToken("token").enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    System.out.println("Token PASS");
//                    User u = response.body();
//                    MainActivity.this.user.clone(u);
//                    System.out.println(MainActivity.this.user);
//                    // Как ниже представлено делать обновление данных
//                    MainActivity.this.user.setScore(222);
//                    User a = MainActivity.this.user;
//                    UserData userData = new UserData(a.getToken(), a.getScore(), a.getAccess(), a.getTestsBests(), a.getGamesBests());
////                Data.getInstance().setUser(response.body());
//                    textViewScore.setText(Integer.toString(MainActivity.this.user.getScore()));
//                }
//
//            }
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Can not load profile", Toast.LENGTH_LONG).show();
//                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "", t);
//                System.out.println("FAILED");
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        data = Data.getInstance();
        user = data.getUser();
        retrofitService = data.getRetrofitService();
        System.out.println("Initial user: " + user);
        textViewScore.setText(Integer.toString(user.getScore()));
        //TODO Проверка на то, залогинен ли пользователь, синхронизация материалов
        System.out.println(data.isLogin());
        ImageButton buttonProfile = (ImageButton) findViewById(R.id.buttonProfile);
        if (data.isLogin())
        {
            buttonProfile.setImageResource(R.drawable.ic_profile_login);
        }
        else {
            buttonProfile.setImageResource(R.drawable.ic_profile);
        }
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
//            TestService testService = retrofitService.getRetrofit().create(TestService.class);
//            testService.getTests().enqueue(new Callback<List<Test>>() {
//                @Override
//                public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
//                    if (response.code() == 200) {
//                        System.out.println("GET TEST SUCC");
//                        List<Test> tests = response.body();
//                        JSONHelper.exportTestsToJSON(MainActivity.this, tests);
//                        System.out.println(tests.get(0).getQuestionList().get(0));
//                        System.out.println(tests.get(1));
//
//                    } else if (response.code() == 404){
//                        System.out.println("GET TEST NOT FOUND");
//                    } else {
//                        System.out.println("GET TEST SOMETHING GOES WRONG...");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<Test>> call, Throwable t) {
//                    System.out.println("GET TEST FAILED");
//                }
//            });
            //TODO передавать реальный ID/реальный тест теста
            List<Test> tests = JSONHelper.importTestsFromJSON(this);
            intent.putExtra("test", tests.get(0));
            intent.putExtra("testId", 0);
            startActivity(intent);
            // TODO Переход на игровую активность
        } else if (id == R.id.buttonProfile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }  else if (id == R.id.te) {
            //TODO передавать реальный ID теории
            RetrofitService retrofitService = data.getRetrofitService();
            ThemeService themeService = retrofitService.getRetrofit().create(ThemeService.class);
//            List<Theme> themes = null;
//            try {
//                Response response = themeService.getThemes().execute();
//                if (response.code() == 200) {
//                    themes = (List<Theme>) response.body();
//                    System.out.println("Sync themes: Pass");
//                } else if (response.code() == 404) {
//                    System.out.println("Sync themes: Failed");
//                }
//            } catch (IOException e) {
//                System.out.println("sync themes cant connect");
//                e.printStackTrace();
//            }
//
//            JSONHelper.exportThemesToJSON(this, themes);
//            themes = JSONHelper.importThemesFromJSON(this);
//            themeService.getThemes().enqueue(new Callback<List<Theme>>() {
//                @Override
//                public void onResponse(Call<List<Theme>> call, Response<List<Theme>> response) {
//                    if (response.code() == 200) {
//                        MainActivity.this.themes = response.body();
//                        JSONHelper.exportThemesToJSON(MainActivity.this, themes);
//                        System.out.println("getThemes: themes was found");
//                    } else if (response.code() == 404) {
//                        System.out.println("Error: Themes was not found");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<Theme>> call, Throwable t) {
//                    System.out.println("Error: не удалось подключиться для получения тем");
//                }
//            });

            //            TestService testService = retrofitService.getRetrofit().create(TestService.class);
//            testService.getTests().enqueue(new Callback<List<Test>>() {
//                @Override
//                public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
//                    if (response.code() == 200) {
//                        System.out.println("GET TEST SUCC");
//                        List<Test> tests = response.body();
//                        JSONHelper.exportTestsToJSON(MainActivity.this, tests);
//                        System.out.println(tests.get(0).getQuestionList().get(0));
//                        System.out.println(tests.get(1));
//
//                    } else if (response.code() == 404){
//                        System.out.println("GET TEST NOT FOUND");
//                    } else {
//                        System.out.println("GET TEST SOMETHING GOES WRONG...");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<List<Test>> call, Throwable t) {
//                    System.out.println("GET TEST FAILED");
//                }
//            });
            Intent intent = new Intent(this, TheoryActivity.class);
            themes = JSONHelper.importThemesFromJSON(this);
            Theme theme = themes.get(0);
            System.out.println(theme);
            intent.putExtra("theory", theme);
            intent.putExtra("theoryId", 0);
            startActivity(intent);

//            RetrofitService retrofitService = data.getRetrofitService();
//            Review review = new Review();
//            review.setEvaluation((int) ratingBar.getRating());
//            review.setMaterialId(2);
//            review.setType("Theme");
//            ReviewService reviewService = retrofitService.getRetrofit().create(ReviewService.class);
//            reviewService.createReview(review).enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    System.out.println("Review send SUCC");
//
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    System.out.println("Review send FAILED" + t);
//                }
//            });
//            List<Test> tests = JSONHelper.importTestsFromJSON(this);
//            System.out.println(tests.get(0).getQuestionList().get(0));


//            CustomDialogFragment dialog = new CustomDialogFragment();
//            dialog.show(getSupportFragmentManager(), "custom");
//            Log.i("MYTAG", user.toString());
//            Toast.makeText(this, Integer.toString(user.getScore()), Toast.LENGTH_LONG).show();
//            System.out.println(user);

        }

    }
}