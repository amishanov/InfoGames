package com.example.infogames.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infogames.Data;
import com.example.infogames.JSONHelper;
import com.example.infogames.R;
import com.example.infogames.TheoryListAdapter;
import com.example.infogames.model.Review;
import com.example.infogames.model.Theme;
import com.example.infogames.model.User;

import java.util.Arrays;
import java.util.List;

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
    int itemChecked = 0;
    SoundPool soundPool;
    int soundAchievement;
    TextView textViewScore;
    Button buttonReview, buttonFinishTheory, buttonSendErrorReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ratingBar = (RatingBar) findViewById(R.id.ratingBarTheory);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        buttonReview = (Button) findViewById(R.id.buttonReviewTheory);
        buttonReview.setOnClickListener(this);
        buttonFinishTheory = (Button) findViewById(R.id.buttonFinishTheory);
        buttonFinishTheory.setOnClickListener(this);
        buttonSendErrorReport = findViewById(R.id.buttonSendErrorTheory);
        buttonSendErrorReport.setOnClickListener(this);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .build();
        soundPool =  new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();
        soundAchievement = soundPool.load(this, R.raw.achivment_bell, 1);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            theoryId = arguments.getInt("theoryId");
            theme = (Theme) arguments.getSerializable("theory");
        } else {
            finish();
        }
        String theory = theme.getTheory();
        List<String> subStr = Arrays.asList(theory.split("<ref>"));
        TheoryListAdapter adapter = new TheoryListAdapter(this, subStr);
        RecyclerView recyclerView = findViewById(R.id.theoryList);
        recyclerView.setAdapter(adapter);
        for (String s: subStr) {
            System.out.println(s);
        }
        adapter.notifyDataSetChanged();
    }


    protected void onStart() {
        super.onStart();
        data = Data.getInstance();
        user = data.getUser();
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
                buttonFinishTheory.setVisibility(View.GONE);
                JSONHelper.exportUserToJSON(this, user);
                Toast.makeText(this,
                        "Поздравляю с изучением новой темы! Тебе были начислены очки",
                        Toast.LENGTH_LONG).show();
                soundPool.play(soundAchievement, 1, 1, 1, 0, 1);
                if (data.isLogin()) {
                    data.sendUserData();
                }
            } else {
                Toast.makeText(this,
                        "Мне казалось, ты уже изучил эту тему... Но я рад, что ты повторяешь изученный материал",
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
        } else if (id == R.id.buttonSendErrorTheory) {
            AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
            itemChecked = 0;
            String [] errors = new String[]{"Опечатка", "Неправильное отображение", "Что-то ещё"};
            aBuilder.setSingleChoiceItems(errors,
                            0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    TheoryActivity.this.setItemChecked(i);
                                }
                            })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int btId) {
                            Toast.makeText(TheoryActivity.this, "Спасибо! Мы обязательно исправим ошибку", Toast.LENGTH_SHORT).show();
                            // TODO send error report
                        }
                    })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int btId) {
                        }
                    });
            AlertDialog alert = aBuilder.create();
            alert.setTitle("Выбери тип ошибки");
            alert.show();

        }
    }

    public void setItemChecked(int itemChecked) {
        this.itemChecked = itemChecked;
    }

    public int getItemChecked() {
        return itemChecked;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onDestroy() {
        soundPool.release();
        super.onDestroy();
    }
}