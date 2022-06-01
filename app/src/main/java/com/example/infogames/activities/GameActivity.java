package com.example.infogames.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.infogames.Data;
import com.example.infogames.R;
import com.example.infogames.games.MainFragment;
import com.example.infogames.model.User;

public class GameActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView textViewScore;
    Data data;
    User user;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        data = Data.getInstance();
        user = data.getUser();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        MainFragment mainFragment = new MainFragment();
        ft.add(R.id.fragmentContainerView, mainFragment, "mainFragment");
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO Штуки, которые могут понадобиться при возобновлении работы
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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}