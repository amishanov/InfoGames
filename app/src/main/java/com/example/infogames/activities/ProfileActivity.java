package com.example.infogames.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infogames.Data;
import com.example.infogames.JSONHelper;
import com.example.infogames.R;
import com.example.infogames.model.User;
import com.example.infogames.retrofit.RetrofitService;
import com.example.infogames.retrofit.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    private Toolbar toolbar;
    private Data data;
    private TextView tvResults;
    private Button buttonLogin, buttonSignOut, buttonReg, buttonSignUp, buttonSignIn;
    private EditText etRegLogin, etRegEmail, etRegPassword, etRegRePassword, etLogin, etPassword;

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
        initToolbar();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSignUp){
            setContentView(R.layout.sing_up);
            initToolbar();
            initElementsReg();
        }
        else if (id == R.id.buttonSignIn) {
            setContentView(R.layout.sign_in);
            initToolbar();
            initElementsLogin();
        }
        else if (id == R.id.buttonSignOut) {
            setContentView(R.layout.sign_in);
            initToolbar();
            initElementsLogin();
            data.sendUserData();
            data.getUser().setToken(null);
            JSONHelper.exportUserToJSON(this, data.getUser());
            data.setIsLogin(false);
        }
        else if (id == R.id.buttonLogin) {
            Login();

        } else if (id == R.id.buttonReg) {
            Registration();
        }
    }

    // Метод для входа
    private void Login() {
        // Отправка данных при попытке входа. Пароль переводится в hex число (пародия на шифрование)
        RetrofitService retrofitService = data.getRetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);
        String login = etLogin.getText().toString();
        String password = etPassword.getText().toString();
        StringBuilder hexPassword = new StringBuilder();
        for (byte aByteData : password.getBytes()) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) hexPassword.append('0');
            hexPassword.append(hex);
        }
        // Если вход успешный, пользователь выбирает тип синхронизации
        userService.getUserByLogin(login+":"+ hexPassword).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    User user = response.body();
                    System.out.println(user);
                    setContentView(R.layout.activity_profile);
                    initToolbar();
                    initElementsProf();
                    data.setIsLogin(true);
                    AlertDialog.Builder aBuilder = new AlertDialog.Builder(ProfileActivity.this);
                    aBuilder.setMessage("Выбери, данные откуда будут использоваться далее")
                            .setCancelable(false)
                            .setPositiveButton("Приложение", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int btId) {
                                    data.getUser().setLogin(user.getLogin());
                                    data.getUser().setEmail(user.getEmail());
                                    data.getUser().setPassword(user.getPassword());
                                    data.getUser().setToken(user.getToken());
                                    JSONHelper.exportUserToJSON(ProfileActivity.this, data.getUser());
                                    data.sendUserData();
                                }
                            })
                            .setNegativeButton("Сервер", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int btId) {
                                    data.getUser().clone(user);
                                    JSONHelper.exportUserToJSON(ProfileActivity.this, data.getUser());
                                    String results = "Лучшие результаты за тестирования:\nТест 1: 0/3\nТест 2: 5/10\nТест 3: 0/10\nТест 4: 0/10\nТест 5: 0/10\nТест 6: 0/10\n\nРекорды:\n";
                                    User user = data.getUser();
                                    Integer[] gamesBests = user.getGamesBests();
                                    if (gamesBests.length > 1)
                                        results += "Логическая схема: " +  gamesBests[0] + " очков\n" + "Викторина: " + gamesBests[1]+ " очков";
                                    tvResults.setText(results);
                                }
                            });
                    AlertDialog alert = aBuilder.create();
                    alert.setTitle("Синхронизация");
                    alert.show();
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
                Log.i(TAG, "Login: FAILED");
            }
        });
    }

    // Метод для регистрации
    private void Registration() {
        RetrofitService retrofitService = data.getRetrofitService();
        UserService userService = retrofitService.getRetrofit().create(UserService.class);
        User currentUser = data.getUser();
        String loginReg = etRegLogin.getText().toString();
        String email = etRegEmail.getText().toString();
        String passwordReg = etRegPassword.getText().toString();
        String rePassword = etRegRePassword.getText().toString();
        if (!passwordReg.equals(rePassword)) {
            Toast.makeText(ProfileActivity.this,
                    "Введённые пароли не совпадают",
                    Toast.LENGTH_LONG).show();
            return;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder hexPasswordReg = new StringBuilder();
        for (byte aByteData : passwordReg.getBytes()) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) hexPasswordReg.append('0');
            hexPasswordReg.append(hex);
        }
        User user = new User(email, loginReg, hexPasswordReg.toString(), null, currentUser.getScore(), currentUser.getProgress(),
                currentUser.getAccess(), currentUser.getTestsBests(), currentUser.getTestsBests());
        userService.createUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 201) {
                    Toast.makeText(ProfileActivity.this,
                            "Ты успешно зарегистрирован!",
                            Toast.LENGTH_LONG).show();
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
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProfileActivity.this,
                        "Ой-ой, не могу подключиться к серверу", Toast.LENGTH_LONG).show();
                Logger.getLogger(ProfileActivity.class.getName()).log(Level.SEVERE, "", t);
                Log.i(TAG, "Registration: FAILED");
            }
        });
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
        tvResults = findViewById(R.id.tvResults);
        String results = "Лучшие результаты за тестирования:\nТест 1: 0/3\nТест 2: 5/10\nТест 3: 0/10\nТест 4: 0/10\nТест 5: 0/10\nТест 6: 0/10\n\nРекорды:\n";
        User user = data.getUser();
        Integer[] gamesBests = user.getGamesBests();
        if (gamesBests.length > 1)
            results += "Логическая схема: " +  gamesBests[0] + " очков\n" + "Викторина: " + gamesBests[1]+ " очков";
        tvResults.setText(results);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}