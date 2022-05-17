package com.example.infogames.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.example.infogames.R;
import com.example.infogames.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThemesActivity extends AppCompatActivity {

    private Data data;
    private User user;
    private String[] expList = {"Тема 1: Общие понятия информатики", "Тема 2: Системы счисления",
            "Тема 3: Булева алгебра", "Тема 4: Логические функции",
            "Тема 5: Цифровая схемотехника",  "Тема 6: Алгоритмы и элементы программирования"};
    private String[] innerList1 = {"1", "2", "3", "4", "Тест"};
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    ExpListAdapter adapter;

    private Toolbar toolbar;
    TextView textViewScore;


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
        TextView textViewScore = (TextView) findViewById(R.id.textViewScore);
        textViewScore.setText(Integer.toString(user.getScore()));



        if (data.isLogin())
        {
            ImageButton buttonProfile = (ImageButton) findViewById(R.id.buttonProfile);
            buttonProfile.setImageResource(R.drawable.ic_profile_login);
        }

        expandableListView = findViewById(R.id.expListView);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();

        adapter = new ExpListAdapter(this, listGroup, listItem);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int groupPosition, int childPosition, long id) {
                // TODO Переход на активность теории/теста
                Toast.makeText(getApplicationContext(), listGroup.get(groupPosition)
                        +listItem.get(listGroup.get(groupPosition)).get(childPosition),
                        Toast.LENGTH_LONG).show();
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
        //TODO Штуки, которые могут понадобиться при возобновлении работы
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

    public void click(View v) {
        Toast.makeText(getApplicationContext(), "AAAa", Toast.LENGTH_SHORT).show();
    }

    private void initListData() {
        // listGroup = expList
        listGroup.add(expList[0]);
        listGroup.add(expList[1]);
        listGroup.add(expList[2]);
        listGroup.add(expList[3]);
        listGroup.add(expList[4]);
        listGroup.add(expList[5]);

        List<String> list1 = new ArrayList<>();
        for (String item : innerList1) {
            list1.add(item);
        }

        List<String> list2 = new ArrayList<>();
        for (String item : innerList1) {
            list2.add(item);
        }
        listItem.put(expList[0], list1);
        listItem.put(expList[1], list1);
        listItem.put(expList[2], list1);
        listItem.put(expList[3], list1);
        listItem.put(expList[4], list1);
        listItem.put(expList[5], list1);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}