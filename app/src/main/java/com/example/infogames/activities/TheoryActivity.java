package com.example.infogames.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infogames.Data;
import com.example.infogames.JSONHelper;
import com.example.infogames.R;
import com.example.infogames.model.Review;
import com.example.infogames.model.Theme;
import com.example.infogames.model.User;

public class TheoryActivity extends AppCompatActivity implements View.OnClickListener {

    RatingBar ratingBar;
    Button send;
    TextView text;
    User user;
    Data data;
    Toolbar toolbar;
    int theoryId;
    Theme theme;
    TextView textViewTheory;
    TextView textViewScore;
    Button buttonReview, buttonFinishTests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ratingBar = (RatingBar) findViewById(R.id.ratingBarTheory);
        textViewTheory = (TextView) findViewById(R.id.textViewTheory);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        buttonReview = (Button) findViewById(R.id.buttonReviewTheory);
        buttonReview.setOnClickListener(this);
        buttonFinishTests = (Button) findViewById(R.id.buttonFinishTheory);
        buttonFinishTests.setOnClickListener(this);

        // TODO Доставать тему и отображать её, ID нужен для работы со списком в User
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            theoryId = arguments.getInt("theoryId");
            theme = (Theme) arguments.getSerializable("theory");
        } else {
            finish();
        }

    }


    protected void onStart() {
        super.onStart();
        data = Data.getInstance();
        user = data.getUser();
        //TODO Штуки, которые могут понадобиться при возобновлении работы
        System.out.println(data.isLogin());
        textViewScore.setText(Integer.toString(user.getScore()));
        ImageButton buttonProfile = (ImageButton) findViewById(R.id.buttonProfile);
        if (data.isLogin())
        {
            buttonProfile.setImageResource(R.drawable.ic_profile_login);
        }
        else {
            buttonProfile.setImageResource(R.drawable.ic_profile);
        }
        textViewTheory.setText(Html.fromHtml(theme.getTheory(), Html.FROM_HTML_MODE_COMPACT));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        System.out.println(theoryId);
        System.out.println(user.getProgress()[0]);
        System.out.println(user.getProgress().length);
        if (id == R.id.buttonFinishTheory) {
            if (!user.getProgress()[theoryId]) {
                user.getProgress()[theoryId] = true;
                user.setScore(user.getScore()+5);
                textViewScore.setText(Integer.toString(user.getScore()));
                JSONHelper.exportUserToJSON(this, user);
                Toast.makeText(this,
                        "Поздравляю с изучением новой темы! Тебе были начислены очки",
                        Toast.LENGTH_LONG).show();
                if (data.isLogin()) {
                    data.sendUserData();
                }
            } else {
                Toast.makeText(this,
                        "Мне казалось, ты уже изучил эту тему...Но я рад, что ты повторяешь изученный материал",
                        Toast.LENGTH_LONG).show();
            }

        } else if (id == R.id.buttonReviewTheory) {
            if (data.isLogin()) {
            int evaluation = (int) ratingBar.getRating();
            Review review = new Review(2, theoryId, evaluation);
            data.sendReview(review);
            Toast.makeText(this,
                    "Спасибо за отзыв!",
                    Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Отзывы могут оставлять только зарегистрированные пользователи!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}