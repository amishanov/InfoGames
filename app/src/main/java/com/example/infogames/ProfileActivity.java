package com.example.infogames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Data data;

    private Button buttonLogin, buttonSignOut, buttonReg, buttonSignUp, buttonSignIn;
    private EditText etRegLogin, etRegEmail, etRegPassword, etRegRePassword, etLogin, etPassword;

    //TODO Не забыть проверку логинов и паролей на ненужные символы

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = Data.getInstance();
//        initElements();
        System.out.println(buttonSignIn);
        if (data.isLogin()) {
            setContentView(R.layout.activity_profile);
            buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
            buttonSignOut.setOnClickListener(this);
        }
        else {
            setContentView(R.layout.sign_in);
            buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
            buttonLogin = (Button) findViewById(R.id.buttonLogin);
            buttonSignUp.setOnClickListener(this);
            buttonLogin.setOnClickListener(this);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initElements() {
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonReg = (Button) findViewById(R.id.buttonReg);
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        etRegLogin = (EditText) findViewById(R.id.editTextRegLogin);
        etRegEmail = (EditText) findViewById(R.id.editTextEmail);
        etRegPassword = (EditText) findViewById(R.id.editTextRegPassword);
        etRegRePassword = (EditText) findViewById(R.id.editTextRePassword);
        etLogin = (EditText) findViewById(R.id.editTextLogin);
        etPassword = (EditText) findViewById(R.id.editTextPassword);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSignUp){
            setContentView(R.layout.sing_up);
            toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
            buttonReg = (Button) findViewById(R.id.buttonReg);
            buttonSignIn.setOnClickListener(this);
            buttonReg.setOnClickListener(this);
        } else if (id == R.id.buttonSignIn) {
            setContentView(R.layout.sign_in);
            toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
            buttonLogin = (Button) findViewById(R.id.buttonLogin);
            buttonSignUp.setOnClickListener(this);
            buttonLogin.setOnClickListener(this);

        } else if (id == R.id.buttonLogin) {
            //TODO Не забыть добавить обновление переменной isLogin
            setContentView(R.layout.activity_profile);
            toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
            buttonSignOut.setOnClickListener(this);


        } else if (id == R.id.buttonReg) {

        } else if (id == R.id.buttonSignOut) {
            setContentView(R.layout.sign_in);
            toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
            buttonLogin = (Button) findViewById(R.id.buttonLogin);
            buttonSignUp.setOnClickListener(this);
            buttonLogin.setOnClickListener(this);

            data.setIsLogin(false);
        }
    }
}