package com.example.infogames.games;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infogames.Data;
import com.example.infogames.JSONHelper;
import com.example.infogames.QuestionGenerator;
import com.example.infogames.R;
import com.example.infogames.activities.GameActivity;
import com.example.infogames.model.Question;
import com.example.infogames.model.Test;
import com.example.infogames.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GameQuizFragment extends Fragment implements View.OnClickListener {

    Button buttonAnswer1, buttonAnswer2, buttonAnswer3, buttonAnswer4;
    TextView tvQuestion;
    ConstraintLayout clQuizContainer;
    ImageView ivTimer, ivWrong1, ivWrong2, ivWrong3;
    TextView tvPoints, tvTimer, tvScore;
    Button buttonStartEnd;
    long timeLeft = 30000;
    boolean gameRunning = false;
    int errors = 0, points = 0;
    SoundPool soundPool;
    int soundCorrect, soundWrong, soundRecord;
    CountDownTimer countDownTimer;
    List<Question> questionList;
    Question currentQuestion;
    int currentQuestionNum = -1;
    List<Test> tests;
    GameActivity gameActivity;

    public GameQuizFragment() {
    }

    public static GameQuizFragment newInstance(String param1, String param2) {
        GameQuizFragment fragment = new GameQuizFragment();
        Bundle args = new Bundle();;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_quiz, container, false);

        gameActivity = (GameActivity) getActivity();
        clQuizContainer = view.findViewById(R.id.quizContainer);
        buttonAnswer1 = view.findViewById(R.id.buttonAnswer1);
        buttonAnswer1.setOnClickListener(this);
        buttonAnswer2 = view.findViewById(R.id.buttonAnswer2);
        buttonAnswer2.setOnClickListener(this);
        buttonAnswer3 = view.findViewById(R.id.buttonAnswer3);
        buttonAnswer3.setOnClickListener(this);
        buttonAnswer4 = view.findViewById(R.id.buttonAnswer4);
        buttonAnswer4.setOnClickListener(this);
        tvQuestion = view.findViewById(R.id.textViewQuizQuestion);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .build();
        soundPool =  new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();
        soundCorrect = soundPool.load(getActivity(), R.raw.correct_answer, 1);
        soundWrong = soundPool.load(getActivity(), R.raw.error_sound, 1);
        soundRecord = soundPool.load(getActivity(), R.raw.new_record, 1);

        initActivityElements();
        setUpQuestionList();
        return view;
    }

    public void initActivityElements() {
        ivTimer = getActivity().findViewById(R.id.ivTimer);
        ivWrong1 = getActivity().findViewById(R.id.ivWrong1);
        ivWrong2 = getActivity().findViewById(R.id.ivWrong2);
        ivWrong3 = getActivity().findViewById(R.id.ivWrong3);
        tvPoints = getActivity().findViewById(R.id.textViewPoints);
        tvTimer = getActivity().findViewById(R.id.textViewTimer);
        tvScore = getActivity().findViewById(R.id.textViewScore);
        ivTimer.setVisibility(View.VISIBLE);
        tvPoints.setVisibility(View.VISIBLE);
        tvTimer.setVisibility(View.VISIBLE);
        buttonStartEnd = getActivity().findViewById(R.id.buttonStartFinishGame);
        buttonStartEnd.setVisibility(View.VISIBLE);
        buttonStartEnd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonAnswer1) {
            updatePoints(checkQuestion(0));

        } else if (id == R.id.buttonAnswer2) {
            updatePoints(checkQuestion(1));

        } else if (id == R.id.buttonAnswer3) {
            updatePoints(checkQuestion(2));

        } else if (id == R.id.buttonAnswer4) {
            updatePoints(checkQuestion(3));

        } else if (id == R.id.buttonStartFinishGame) {
            if (!gameRunning) {
                buttonStartEnd.setText(R.string.finish_game);
                gameRunning = true;
                clQuizContainer.setVisibility(View.VISIBLE);
                countDownTimer = new CountDownTimer(timeLeft, 1000) {
                    @Override
                    public void onTick(long l) {
                        timeLeft = l;
                        updateTimer();
                    }

                    @Override
                    public void onFinish() {
                        buttonStartEnd.performClick();
                    }
                };
                updateQuestion();
                countDownTimer.start();

            } else {
                countDownTimer.cancel();
                buttonStartEnd.setVisibility(View.INVISIBLE);
                clQuizContainer.setVisibility(View.INVISIBLE);
                String res = "";
                User user = gameActivity.getUser();
                Data data = gameActivity.getData();
                Integer [] gamesBests = new Integer[]{0,0};
                if (user.getGamesBests().length < 2)
                    gamesBests[0] = user.getGamesBests()[0];
                else
                    gamesBests = user.getGamesBests();

                if (points > gamesBests[1]) {
                    soundPool.play(soundRecord, 1, 1, 1, 0, 1);
                    res = "Вау! Это же новый рекорд. Поздравляю!";
                    int sc = user.getScore();
                    sc = sc - gamesBests[1] + points;
                    gamesBests[1] = points;
                    user.setGamesBests(gamesBests);
                    user.setScore(sc);
                    tvScore.setText(user.getScore()+"");
                    Toast.makeText(getActivity(),
                            "Очки зачислены",
                            Toast.LENGTH_LONG).show();
                    JSONHelper.exportUserToJSON(getActivity(), user);
                    if (data.isLogin()){
                        data.sendUserData();
                    }
                } else {
                    res = "Твой лучший результат: " + gamesBests[1];
                }
                res += "\nТы набрал: " + points;
                tvQuestion.setText(res);
                tvQuestion.setVisibility(View.VISIBLE);

            }
        }

    }

    public void updateQuestion() {
        int choice = (int) (Math.random() * 4);
        if (currentQuestionNum >= questionList.size() || choice > 0) {
            if (choice == 1)
                currentQuestion = QuestionGenerator.generateHexToDecimal();
            else if (choice == 2)
                currentQuestion = QuestionGenerator.generateDecimalToBinary();
            else if (choice == 3)
                currentQuestion = QuestionGenerator.generateDecimalToHex();
            else
                currentQuestion = QuestionGenerator.generateBinaryToDecimal();
            setUpQuestion();
            System.out.println(Arrays.toString(currentQuestion.getAnswers()));
        } else {
            currentQuestionNum++;
            currentQuestion = questionList.get(currentQuestionNum);
            setUpQuestion();
        }
    }

    public void setUpQuestion() {
        tvQuestion.setText(currentQuestion.getQuestion());
        String[] answers = currentQuestion.getAnswers();
        buttonAnswer1.setText(answers[0]);
        buttonAnswer2.setText(answers[1]);
        buttonAnswer3.setText(answers[2]);
        buttonAnswer4.setText(answers[3]);
    }

    public boolean checkQuestion(int answer) {
        return currentQuestion.getRightAnswer() == answer;
    }

    public void updateTimer() {
        int minutes = (int) timeLeft / 60000;
        int seconds = (int) timeLeft % 60000 / 1000;

        String timeLeft = "";
        if (minutes < 1)
            timeLeft += "0";
        timeLeft += minutes + ":";
        if (seconds < 10)
            timeLeft += "0";
        timeLeft += seconds;
        tvTimer.setText(timeLeft);
    }
    public void setUpQuestionList() {
        tests = JSONHelper.importTestsFromJSON(getActivity());
        questionList = new ArrayList<>();
        if (tests == null) {
            Toast.makeText(getContext(), "Нет доступных вопросов", Toast.LENGTH_LONG).show();
            return;
        }
        for (Test test: tests) {
            for (Question question: test.getQuestionList()) {
                if (question.getType() == 2)
                    questionList.add(question);
            }
        }
        Collections.shuffle(questionList);
    }
    public boolean updateErrors() {
        errors++;
        switch (errors) {
            case (1):
                ivWrong1.setVisibility(View.VISIBLE);
                break;
            case (2):
                ivWrong2.setVisibility(View.VISIBLE);
                break;
            case (3):
                ivWrong3.setVisibility(View.VISIBLE);
        }
        return errors != 3;
    }

    public void updatePoints(boolean check) {
        if (check) {
            soundPool.play(soundCorrect, 1, 1, 1, 0, 1);
            updateQuestion();
            points++;
            tvPoints.setText("Счёт: " + Integer.toString(points));
        } else {
            soundPool.play(soundWrong, 1, 1, 1, 0, 1);
            if (updateErrors())
                updateQuestion();
            else
                buttonStartEnd.performClick();
        }
    }

}