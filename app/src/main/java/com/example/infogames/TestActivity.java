package com.example.infogames;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


// TODO ФИКСАЦИЯ РЕЗУЛЬТАТОВ, ДОБАВЛЕНИЕ ОЧКОВ, ФИКСАЦИЯ ОТЗЫВА
public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    // Проверка запущен ли тест для изменения элементов интерфейса
    private boolean isTestStarted;
    // Массив ответов
    private ArrayList<String> answers;
    // Сам тест
    ArrayList<Question> test;
    int currentQuestion, testSize;

    // Элементы интерфейса
    Button buttonNext, buttonPrev, buttonReview, buttonControl;
    RadioButton rb1, rb2, rb3, rb4;
    RadioGroup rg;
    EditText editTextAnswer;
    ConstraintLayout layoutInput, layoutChoose;
    TextView textViewQuestion1, textViewQuestion2, questionCounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        isTestStarted = false;
        // Инициализация элементов
        viewInit();

        // TODO Сюда помещается тема теста (Через Intent должна передаваться?)
        String theme = "Тестовая тема";
        String welcomeString = "Вы хотите пройти тестирование по теме: \"" + theme + "\"?";
        textViewQuestion1.setText(welcomeString);

        // TODO Где-то здесь получаются данные теста
        test = generateTestForTest();
        currentQuestion = 0;
        testSize = test.size();
        answers = new ArrayList<>();
        for (int i = 0; i < testSize; i++) answers.add(null);

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
            if (isTestStarted) {

                //TODO Подтверждение завершения

                answers.set(currentQuestion, getAnswer());
                setUpLayout(true);
                editTextAnswer.setVisibility(View.INVISIBLE);
                editTextAnswer.setClickable(false);
                int result = check();
                textViewQuestion1.setText("Результат вашего тестирования: " + result);

                buttonNext.setVisibility(View.INVISIBLE);
                buttonNext.setClickable(false);
                buttonPrev.setVisibility(View.INVISIBLE);
                buttonPrev.setClickable(false);
                questionCounter.setVisibility(View.INVISIBLE);
                buttonReview.setVisibility(View.VISIBLE);
                buttonReview.setClickable(true);

                // TODO Отображение предыдущих результатов
                // TODO Фиксация результатов
                // TODO ВОЗВРАЩЕНИЕ НА ПРЕДЫДУЩУЮ АКТИВНОСТЬ
                isTestStarted = false;

            }
            else {
                buttonControl.setText("Завершить тест");
                buttonNext.setVisibility(View.VISIBLE);
                buttonNext.setClickable(true);
                buttonPrev.setVisibility(View.VISIBLE);
                buttonPrev.setClickable(true);
                questionCounter.setVisibility(View.VISIBLE);
                questionCounter.setText((currentQuestion + 1) + "/" + testSize);
                showQuestion(test.get(currentQuestion));
                editTextAnswer.setVisibility(View.VISIBLE);
                editTextAnswer.setClickable(true);
                isTestStarted = true;
            }
        } else if (id == R.id.buttonNext) {
            answers.set(currentQuestion, getAnswer());
            if (currentQuestion != testSize-1)
                currentQuestion += 1;
            else
                Toast.makeText(getApplicationContext(), "Последний вопрос теста", Toast.LENGTH_SHORT).show();
            showQuestion(test.get(currentQuestion));
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
            showQuestion(test.get(currentQuestion));
            if (answers.get(currentQuestion) != null)
                setAnswer();
            else {
                editTextAnswer.setText("");
                rg.clearCheck();
            }

        } else if (id == R.id.buttonReview) {
            // TODO КНОПКА ОТЗЫВА
        }
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
        if (test.get(currentQuestion).getType() == 1) {
            return editTextAnswer.getText().toString();
//            answers.set(currentQuestion, editTextAnswer.getText().toString());
        } else {
            if (rb1.isChecked())
                return "0";
//                answers.set(currentQuestion, "0");
            else if (rb2.isChecked())
                return "1";
//                answers.set(currentQuestion, "1");
            else if (rb3.isChecked())
                return "2";
//                answers.set(currentQuestion, "2");
            else if (rb4.isChecked())
                return "3";
//                answers.set(currentQuestion, "3");
        }
        return null;
    }

    // Добавляет ответ из массива ответов в поля
    private void setAnswer() {
        if (answers.get(currentQuestion) != null) {
            String answer = answers.get(currentQuestion);
            if (test.get(currentQuestion).getType() == 1)
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
            if (test.get(i).getType() == 1) {
                if (test.get(i).getAnswer().equals(answers.get(i)))
                    result += 1;
            } else {
                if (answers.get(i) != null)
                    if (test.get(i).getRightAnswer() == Integer.parseInt(answers.get(i)))
                        result += 1;
            }

        return result;
    }
}