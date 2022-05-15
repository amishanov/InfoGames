package com.example.infogames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.infogames.model.User;
import com.example.infogames.retrofit.RetrofitService;
import com.example.infogames.retrofit.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        if (data.isLogin()) {
            setContentView(R.layout.activity_profile);
            initElementsProf();
        }
        else {
            setContentView(R.layout.sign_in);
            initElementsLogin();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initElementsLogin() {
        etLogin = (EditText) findViewById(R.id.editTextLogin);
        etPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonSignUp.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);



    }

    private void initElementsReg() {
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonReg = (Button) findViewById(R.id.buttonReg);
        buttonSignIn.setOnClickListener(this);
        buttonReg.setOnClickListener(this);

        etRegLogin = (EditText) findViewById(R.id.editTextRegLogin);
        etRegEmail = (EditText) findViewById(R.id.editTextEmail);
        etRegPassword = (EditText) findViewById(R.id.editTextRegPassword);
        etRegRePassword = (EditText) findViewById(R.id.editTextRePassword);

    }

    private void initElementsProf() {
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSignUp){
            setContentView(R.layout.sing_up);
            toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            initElementsReg();

        } else if (id == R.id.buttonSignIn) {
            setContentView(R.layout.sign_in);
            toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            initElementsLogin();

        } else if (id == R.id.buttonLogin) {
            //TODO Не забыть добавить обновление переменной isLogin

            RetrofitService retrofitService = data.getRetrofitService();
            UserService userService = retrofitService.getRetrofit().create(UserService.class);
            String login = etLogin.getText().toString();
            String password = etPassword.getText().toString();
            userService.getUserByLogin(login+":"+password).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User user = response.body();
                        System.out.println(user);
                        setContentView(R.layout.activity_profile);
                        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        initElementsProf();
                        data.setIsLogin(true);

                    } else if (response.code() == 404) {
                        Toast.makeText(ProfileActivity.this,
                                "Неправильный логин или пароль", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProfileActivity.this,
                                "Странная ошибка... свяжись с администратором",
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this,
                            "Ой-ой, не могу подключиться к серверу", Toast.LENGTH_LONG).show();
                    Logger.getLogger(ProfileActivity.class.getName()).log(Level.SEVERE, "", t);
                    System.out.println("FAILED");
                }
            });

        } else if (id == R.id.buttonReg) {
            RetrofitService retrofitService = data.getRetrofitService();
            UserService userService = retrofitService.getRetrofit().create(UserService.class);
            // TODO Получение из User
            Boolean[] acc = {true, false, false, false, false, false};
		    Integer[] testsBests = {null, null, null, null, null, null};
		    Integer[] gamesBests = {0};
            String loginR = etRegLogin.getText().toString();
            String email = etRegEmail.getText().toString();
		    String passwordR = etRegPassword.getText().toString();
		    String rePassword = etRegRePassword.getText().toString();
		    User user = new User(email, loginR, passwordR, rePassword, 5, acc,
				testsBests, gamesBests);
            userService.createUser(user).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.code() == 201) {
                        Toast.makeText(ProfileActivity.this,
                                "SUCCCCC",
                                Toast.LENGTH_LONG).show();
                        System.out.println("PASS");
                        user.setToken(response.body());
                        System.out.println(user.getToken());
                        buttonSignIn.performClick();
                    } else if (response.code() == 409) {
                        Toast.makeText(ProfileActivity.this,
                                "Похоже, пользователь с таким email или логином уже существует...",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(ProfileActivity.this,
                                "Странная ошибка... свяжись с администратором",
                                Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ProfileActivity.this,
                            "Ой-ой, не могу подключиться к серверу", Toast.LENGTH_LONG).show();
                    Logger.getLogger(ProfileActivity.class.getName()).log(Level.SEVERE, "", t);
                    System.out.println("FAILED");
                }
            });

        } else if (id == R.id.buttonSignOut) {
            setContentView(R.layout.sign_in);
            toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            initElementsLogin();


            data.setIsLogin(false);
        }
    }
}