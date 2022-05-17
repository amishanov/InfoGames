package com.example.infogames;

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

import com.example.infogames.model.Review;
import com.example.infogames.model.User;
import com.example.infogames.model.UserData;
import com.example.infogames.retrofit.RetrofitService;
import com.example.infogames.retrofit.ReviewService;
import com.example.infogames.retrofit.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Data data;
    private User user;
    RatingBar ratingBar;
    private Toolbar toolbar;
    //TODO во всех активостях раскидать задачи между onStart и onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonSetUp();

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
        TextView textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewScore.setText(Integer.toString(user.getScore()));
        RetrofitService retrofitService = data.getRetrofitService();
//        RetrofitService retrofitService = new RetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);

        userService.getUserByToken("token").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    System.out.println("Token PASS");
                    User u = response.body();
                    MainActivity.this.user.clone(u);
                    System.out.println(MainActivity.this.user);
                    // Как ниже представлено делать обновление данных
                    MainActivity.this.user.setScore(222);
                    User a = MainActivity.this.user;
                    UserData userData = new UserData(a.getToken(), a.getScore(), a.getAccess(), a.getTestsBests(), a.getGamesBests());
//                Data.getInstance().setUser(response.body());
                    textViewScore.setText(Integer.toString(MainActivity.this.user.getScore()));
                }

            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Can not load profile", Toast.LENGTH_LONG).show();
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, "", t);
                System.out.println("FAILED");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        else {
            ImageButton buttonProfile = (ImageButton) findViewById(R.id.buttonProfile);
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
            startActivity(intent);
            // TODO Переход на игровую активность
        } else if (id == R.id.buttonProfile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }  else if (id == R.id.te) {
            RetrofitService retrofitService = data.getRetrofitService();
            Review review = new Review();
            review.setEvaluation((int) ratingBar.getRating());
            review.setMaterialId(2);
            review.setType("Theme");
            ReviewService reviewService = retrofitService.getRetrofit().create(ReviewService.class);
            reviewService.createReview(review).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    System.out.println("Review send SUCC");

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("Review send FAILED" + t);
                }
            });
//            TestService testService = retrofitService.getRetrofit().create(TestService.class);
//            testService.getTests().enqueue(new Callback<List<Test>>() {
//                @Override
//                public void onResponse(Call<List<Test>> call, Response<List<Test>> response) {
//                    if (response.code() == 200) {
//                        System.out.println("GET TEST SUCC");
//                        List<Test> tests = response.body();
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

//            CustomDialogFragment dialog = new CustomDialogFragment();
//            dialog.show(getSupportFragmentManager(), "custom");
//            Log.i("MYTAG", user.toString());
//            Toast.makeText(this, Integer.toString(user.getScore()), Toast.LENGTH_LONG).show();
//            System.out.println(user);

        }

    }
}