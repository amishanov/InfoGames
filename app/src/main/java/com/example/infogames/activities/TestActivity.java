package com.example.infogames.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infogames.Data;
import com.example.infogames.JSONHelper;
import com.example.infogames.R;
import com.example.infogames.model.Question;
import com.example.infogames.model.Review;
import com.example.infogames.model.Test;
import com.example.infogames.model.User;
import com.example.infogames.retrofit.RetrofitService;

import java.util.ArrayList;
import java.util.List;


// TODO ФИКСАЦИЯ РЕЗУЛЬТАТОВ, ДОБАВЛЕНИЕ ОЧКОВ, ФИКСАЦИЯ ОТЗЫВА
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private Data data;
    private User user;
    private int testId;
    private Test test;
    private int itemChecked;

    // Проверка запущен ли тест для изменения элементов интерфейса
    private boolean isTestFinished;
    // Массив ответов
    List<String> answers;
    // Сам тест
    List<Question> questionList;
    int currentQuestion, testSize;

    // Элементы интерфейса
    Toolbar toolbar;
    TextView textViewScore;
    RatingBar ratingBar;
    Button buttonNext, buttonPrev, buttonReview, buttonControl, buttonSendError;
    RadioButton rb1, rb2, rb3, rb4;
    RadioGroup rg;
    EditText editTextAnswer;
    ConstraintLayout layoutInput, layoutChoose;
    TextView textViewQuestion1, textViewQuestion2, questionCounter;
    SoundPool soundPool;
    int soundAchievement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            testId = arguments.getInt("testId");
            test = (Test) arguments.getSerializable("test");
            questionList = test.getQuestionList();
        } else {
            finish();
        }

        data = Data.getInstance();
        user = data.getUser();
        System.out.println(user);
        textViewScore = (TextView) findViewById(R.id.textViewScore);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .build();
        soundPool =  new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(audioAttributes)
                .build();
        soundAchievement = soundPool.load(this, R.raw.achivment_bell, 1);
        if (data.isLogin())
        {
            ImageButton buttonProfile = (ImageButton) findViewById(R.id.buttonProfile);
            buttonProfile.setImageResource(R.drawable.ic_profile_login);
        }

        isTestFinished = false;
        // Инициализация элементов
        viewInit();
        currentQuestion = 0;
        testSize = questionList.size();

        String welcomeString = "Количество вопросов в тесте: " + testSize;
        if (user.getTestsBests()[testId] != null)
            welcomeString += "\nТвой прошлый лучший результат: " + user.getTestsBests()[testId] ;
        textViewQuestion1.setText(welcomeString);

        // TODO переписать это непотребство под что-нибудь, что не будет null'ом
        answers = new ArrayList<>();
        for (int i = 0; i < testSize; i++) answers.add(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        data = Data.getInstance();
        user = data.getUser();
        System.out.println("Initial user: " + user);
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


    private void viewInit() {
        buttonControl = (Button) findViewById(R.id.buttonControl);
        buttonControl.setOnClickListener(this);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(this);
        buttonPrev = (Button) findViewById(R.id.buttonPrev);
        buttonPrev.setOnClickListener(this);
        buttonReview = (Button) findViewById(R.id.buttonReview);
        buttonReview.setOnClickListener(this);
        buttonSendError = findViewById(R.id.buttonSendErrorTest);
        buttonSendError.setOnClickListener(this);
        editTextAnswer = (EditText) findViewById(R.id.editTextAnswer);
        textViewQuestion1 = (TextView) findViewById(R.id.textViewQuestion1);
        textViewQuestion2 = (TextView) findViewById(R.id.textViewQuestion2);
        questionCounter = (TextView) findViewById(R.id.textViewCounter);
        rb1 = (RadioButton) findViewById(R.id.rbAnswer1);
        rb2 = (RadioButton) findViewById(R.id.rbAnswer2);
        rb3 = (RadioButton) findViewById(R.id.rbAnswer3);
        rb4 = (RadioButton) findViewById(R.id.rbAnswer4);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        layoutInput = (ConstraintLayout) findViewById(R.id.layoutInput);
        layoutChoose = (ConstraintLayout) findViewById(R.id.layoutChoose);
    }


    // Отладочная функция
    private ArrayList<Question> generateTestForTest() {
        String[] answers = {"Ответ 1", "Ответ 2", "Ответ 3", "Ответ 4"};
        String[] answers1 = {"Ох", "Лол", "Я", "Ответ"};
        Question q1 = new Question("Вопрос 1", "Ответ");
        Question q2 = new Question("Вопрос 2", answers, 1);
        Question q3 = new Question("Вопрос 3", answers1, 3);
        Question q4 = new Question("Вопрос 4", "Смищной ответ");
        Question q5 = new Question("Вопрос 5", "Ответ 5");
        ArrayList<Question> test = new ArrayList<>();
        test.add(q1);
        test.add(q2);
        test.add(q3);
        test.add(q4);
        test.add(q5);
        return test;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonControl) {
            if (isTestFinished) {
                finish();
            }
            else {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
                aBuilder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int btId) {
                                TestActivity.this.finishTest();
                            }
                        })
                        .setNegativeButton("Отмена", null);
                AlertDialog alert = aBuilder.create();
                alert.setTitle("Ты уверен, что хочешь завершить тестирование?");
                alert.show();
            }
        } else if (id == R.id.buttonNext) {
            answers.set(currentQuestion, getAnswer());
            if (currentQuestion != testSize-1)
                currentQuestion += 1;
            else
                Toast.makeText(getApplicationContext(), "Последний вопрос теста", Toast.LENGTH_SHORT).show();
            showQuestion(questionList.get(currentQuestion));
            if (answers.get(currentQuestion) != null)
                setAnswer();
            else {
                editTextAnswer.setText("");
                rg.clearCheck();
            }
        } else if (id == R.id.buttonPrev) {
            answers.set(currentQuestion, getAnswer());
            if (currentQuestion != 0)
                currentQuestion -= 1;
            else
                Toast.makeText(getApplicationContext(), "Первый вопрос теста", Toast.LENGTH_SHORT).show();
            showQuestion(questionList.get(currentQuestion));
            if (answers.get(currentQuestion) != null)
                setAnswer();
            else {
                editTextAnswer.setText("");
                rg.clearCheck();
            }

        } else if (id == R.id.buttonReview) {
            if (isTestFinished) {
                RetrofitService retrofitService = data.getRetrofitService();
                Review review = new Review();
                review.setEvaluation((int) ratingBar.getRating());
                review.setType(1);
                review.setMaterialId(testId);
                data.sendReview(review);
                Toast.makeText(getApplicationContext(), "Спасибо за отзыв!", Toast.LENGTH_SHORT).show();
            } else {
                buttonControl.setVisibility(View.VISIBLE);
                buttonControl.setClickable(true);
                buttonReview.setVisibility(View.INVISIBLE);
                buttonReview.setClickable(false);
                buttonNext.setVisibility(View.VISIBLE);
                buttonNext.setClickable(true);
                buttonPrev.setVisibility(View.VISIBLE);
                buttonPrev.setClickable(true);
                questionCounter.setVisibility(View.VISIBLE);
                questionCounter.setText((currentQuestion + 1) + "/" + testSize);
                showQuestion(questionList.get(currentQuestion));
                editTextAnswer.setVisibility(View.VISIBLE);
                editTextAnswer.setClickable(true);
                buttonSendError.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.buttonSendErrorTest) {
            AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
            itemChecked = 0;
            String [] errors = new String[]{"Ошибка в вопросе", "Ошибка в ответе", "Что-то ещё"};
            aBuilder.setSingleChoiceItems(errors,
                    0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TestActivity.this.setItemChecked(i);
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int btId) {
                            Toast.makeText(TestActivity.this, "Спасибо! Мы обязательно исправим ошибку", Toast.LENGTH_SHORT).show();
                            // TODO send error report + questNum
                        }
                    })
                    .setNegativeButton("Отмена", null);
            AlertDialog alert = aBuilder.create();
            alert.setTitle("Выбери тип ошибки");
            alert.show();
        }
    }

    private void finishTest() {
        answers.set(currentQuestion, getAnswer());
        setUpLayout(true);
        editTextAnswer.setVisibility(View.INVISIBLE);
        editTextAnswer.setClickable(false);
        int result = check();
        buttonSendError.setVisibility(View.INVISIBLE);
        String strResult = "Результат твоего тестирования: " + result;
        Integer prevBest = user.getTestsBests()[testId];
        if (prevBest == null) {
            soundPool.play(soundAchievement, 1, 1, 1, 0, 1);
            strResult += "\nТы поставил новый рекорд, так держать!";
            user.setScore(user.getScore()+result*2);
            textViewScore.setText(Integer.toString(user.getScore()));
            Toast.makeText(this, "Очки зачислены", Toast.LENGTH_LONG).show();
            user.getTestsBests()[testId] = result;
            JSONHelper.exportUserToJSON(this, user);
            if (data.isLogin()) {
                data.sendUserData();
            }
        } else if (prevBest > result) {
            strResult += "\nВ прошлый раз ты справился лучше... Повтори материал и поробуй ещё раз!";
        } else if (prevBest == result && result == testSize) {
            strResult += "\nТы снова выбил  максимум... Так держать!";
        }
        else if (prevBest == result) {
            strResult += "\nНеплохо, но ты не побил рекорд... Повтори материал и поробуй ещё раз!";
        }
        else {
            soundPool.play(soundAchievement, 1, 1, 1, 0, 1);
            strResult += "\nТы поставил новый рекорд, так держать!";
            user.setScore(user.getScore()+result*2 - prevBest*2);
            textViewScore.setText(Integer.toString(user.getScore()));
            user.getTestsBests()[testId] = result;
            Toast.makeText(this, "Очки зачислены", Toast.LENGTH_LONG).show();
            JSONHelper.exportUserToJSON(this, user);
            if (data.isLogin()) {
                data.sendUserData();
            }
        }
        textViewQuestion1.setText(strResult);


        buttonNext.setVisibility(View.INVISIBLE);
        buttonNext.setClickable(false);
        buttonPrev.setVisibility(View.INVISIBLE);
        buttonPrev.setClickable(false);
        questionCounter.setVisibility(View.INVISIBLE);

        buttonReview.setText("Оцените материал");
        buttonReview.setVisibility(View.VISIBLE);
        buttonReview.setClickable(true);

        ratingBar = (RatingBar) findViewById(R.id.ratingBarTest);
        ratingBar.setStepSize(1);
        ratingBar.setVisibility(View.VISIBLE);
        ratingBar.setClickable(true);

        buttonControl.setText("Вернуться к темам");
        isTestFinished = true;
    }

    private void showQuestion(Question question) {
        if (question.getType() == 1) {
            setUpLayout(true);
            textViewQuestion1.setText(question.getQuestion());
        } else {
            setUpLayout(false);
            textViewQuestion2.setText(question.getQuestion());
            String[] answersToRadioButtons = question.getAnswers();
            rb1.setText(answersToRadioButtons[0]);
            rb2.setText(answersToRadioButtons[1]);
            rb3.setText(answersToRadioButtons[2]);
            rb4.setText(answersToRadioButtons[3]);

        }
        questionCounter.setText((currentQuestion + 1) + "/" + testSize);
    }

    // Извлекает ответ из представлений
    private String getAnswer() {
        if (questionList.get(currentQuestion).getType() == 1) {
            return editTextAnswer.getText().toString();
        } else {
            if (rb1.isChecked())
                return "0";
            else if (rb2.isChecked())
                return "1";
            else if (rb3.isChecked())
                return "2";
            else if (rb4.isChecked())
                return "3";
        }
        return null;
    }

    // Добавляет ответ из массива ответов в поля
    private void setAnswer() {
        if (answers.get(currentQuestion) != null) {
            String answer = answers.get(currentQuestion);
            if (questionList.get(currentQuestion).getType() == 1)
                editTextAnswer.setText(answers.get(currentQuestion));
            else {
                if (answer.equals("0"))
                    rb1.setChecked(true);
                if (answer.equals("1"))
                    rb2.setChecked(true);
                if (answer.equals("2"))
                    rb3.setChecked(true);
                if (answer.equals("3"))
                    rb4.setChecked(true);
            }
        }
    }

    // Выставляет нужное представление (true - ручной ввод, false - выбор вариантов)
    private void setUpLayout(boolean turn) {
        if (turn) {
            // Доп. условие чтобы лишний раз не настраивать элементы
            if (layoutInput.getVisibility() != View.VISIBLE) {
                layoutInput.setVisibility(View.VISIBLE);
                editTextAnswer.setClickable(true);
                editTextAnswer.setVisibility(View.VISIBLE);
                layoutChoose.setVisibility(View.INVISIBLE);
                rb1.setClickable(false);
                rb2.setClickable(false);
                rb3.setClickable(false);
                rb4.setClickable(false);
            }
        } else {
            if (layoutChoose.getVisibility() != View.VISIBLE) {
                layoutInput.setVisibility(View.INVISIBLE);
                editTextAnswer.setClickable(false);
                editTextAnswer.setVisibility(View.INVISIBLE);
                layoutChoose.setVisibility(View.VISIBLE);
                rb1.setClickable(true);
                rb2.setClickable(true);
                rb3.setClickable(true);
                rb4.setClickable(true);
            }
        }
    }

    // Проверка теста
    private int check() {
        int result = 0;
        for (int i = 0; i < testSize; i++)
            if (questionList.get(i).getType() == 1) {
                if (questionList.get(i).getAnswer().equals(answers.get(i)))
                    result += 1;
            } else {
                if (answers.get(i) != null)
                    if (questionList.get(i).getRightAnswer() == Integer.parseInt(answers.get(i)))
                        result += 1;
            }

        return result;
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

    public int getItemChecked() {
        return itemChecked;
    }

    public void setItemChecked(int itemChecked) {
        this.itemChecked = itemChecked;
    }
}