package com.example.infogames.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infogames.Data;
import com.example.infogames.ExpListAdapter;
import com.example.infogames.JSONHelper;
import com.example.infogames.R;
import com.example.infogames.model.Test;
import com.example.infogames.model.Theme;
import com.example.infogames.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ThemesActivity extends AppCompatActivity {

    Data data;
    User user;
    String[] expList = {"Тема 1: Общие понятия информатики", "Тема 2: Системы счисления",
            "Тема 3: Булева алгебра", "Тема 4: Логические функции",
            "Тема 5: Цифровая схемотехника",  "Тема 6: Алгоритмы и элементы программирования"};
    String[] innerList1 = {"Теория", "Тестирование"};
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    ExpListAdapter adapter;

    Toolbar toolbar;
    TextView textViewScore;
    List<Test> tests;
    List<Theme> themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        setTitle(R.string.learn);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = Data.getInstance();
        user = data.getUser();
        textViewScore = (TextView) findViewById(R.id.textViewScore);
//        System.out.println(user);

        tests = JSONHelper.importTestsFromJSON(this);
        themes = JSONHelper.importThemesFromJSON(this);

        expandableListView = findViewById(R.id.expListView);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();

        adapter = new ExpListAdapter(this, listGroup, listItem);
        adapter.setAccess(user.getAccess());
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view,
                                        int i, long l) {
                if (user.getAccess()[i])
                    return false;
                else {
                    AlertDialog.Builder aBuilder = new AlertDialog.Builder(ThemesActivity.this);
                    aBuilder.setMessage("Ты желаешь приобрести эту тему за 10 очков?")
                            .setCancelable(true)
                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int btId) {
                                    if (!buyTheme(i, 10)) {
                                        Toast.makeText(ThemesActivity.this,
                                                "Не хватает очков!", Toast.LENGTH_LONG).show();
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int btId) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = aBuilder.create();
                    alert.setTitle("Покупка темы");
                    alert.show();
                    return true;
                }
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int groupPosition, int childPosition, long id) {
                if (childPosition == 0) {
                    Intent intent = new Intent(ThemesActivity.this, TheoryActivity.class);
//                    themes = JSONHelper.importThemesFromJSON(this);
//                    Theme theme = themes.get(0);
//                    System.out.println(theme);
                    Theme theme = themes.get(groupPosition);
//                    System.out.println(groupPosition);
                    intent.putExtra("theory", theme);
                    intent.putExtra("theoryId", groupPosition);
                    startActivity(intent);
                } else if (childPosition == 1) {
                    Intent intent = new Intent(ThemesActivity.this, TestActivity.class);
                    Test test = tests.get(groupPosition);
                    intent.putExtra("test", test);
                    intent.putExtra("testId", groupPosition);
                    startActivity(intent);
                }
                return false;
            }
        });
        expandableListView.setAdapter(adapter);

        initListData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        data = Data.getInstance();
        user = data.getUser();
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

    public boolean buyTheme(int i, int cost) {
        if (user.getScore() < cost)
            return false;
        user.setScore(user.getScore()-cost);
        user.getAccess()[i] = true;
        textViewScore.setText(Integer.toString(user.getScore()));
        JSONHelper.exportUserToJSON(this, user);
        if (data.isLogin())
            data.sendUserData();
        return true;
    }
    public void click(View v) {
        Toast.makeText(getApplicationContext(), "AAAa", Toast.LENGTH_SHORT).show();
    }

    private void initListData() {
        if (themes == null) {
            Toast.makeText(getApplicationContext(), "Нет доступных тем", Toast.LENGTH_LONG).show();
            return;
        }
        for (Theme theme: themes) {
            listGroup.add(theme.getThemeName());
        }


        // TODO сделать, чтобы добавлялись
        List<String> list1 = new ArrayList<>();
        for (String item : innerList1) {
            list1.add(item);
        }

        for (int i=0; i<listGroup.size(); i++) {
            if (i > tests.size()) {
                listItem.put(listGroup.get(i), Arrays.asList("Теория"));
            } else {
                listItem.put(listGroup.get(i), Arrays.asList("Теория", "Тестирование"));
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}