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

import com.example.infogames.JSONHelper;
import com.example.infogames.R;
import com.example.infogames.model.Question;
import com.example.infogames.model.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class GameQuizFragment extends Fragment implements View.OnClickListener {

    Button btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4;
    TextView tvQuestion;
    ConstraintLayout clQuizContainer;
    ImageView ivTimer, ivWrong1, ivWrong2, ivWrong3;
    TextView tvPoints, tvTimer;
    Button btnStartEnd;
    long timeLeft = 30000;
    boolean gameRunning = false;
    int errors = 0, points = 0;
    SoundPool soundPool;
    int soundCorrect, soundWrong;
    CountDownTimer countDownTimer;
    List<Question> questionList;
    Question currentQuestion;
    int currentQuestionNum = -1;
    List<Test> tests;

    public GameQuizFragment() {
    }

    // TODO: Rename and change types and number of parameters
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

        clQuizContainer = view.findViewById(R.id.quizContainer);
        btnAnswer1 = view.findViewById(R.id.buttonAnswer1);
        btnAnswer1.setOnClickListener(this);
        btnAnswer2 = view.findViewById(R.id.buttonAnswer2);
        btnAnswer2.setOnClickListener(this);
        btnAnswer3 = view.findViewById(R.id.buttonAnswer3);
        btnAnswer3.setOnClickListener(this);
        btnAnswer4 = view.findViewById(R.id.buttonAnswer4);
        btnAnswer4.setOnClickListener(this);
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
        ivTimer.setVisibility(View.VISIBLE);
        tvPoints.setVisibility(View.VISIBLE);
        tvTimer.setVisibility(View.VISIBLE);
        btnStartEnd = getActivity().findViewById(R.id.btnStartFinishGame);
        btnStartEnd.setVisibility(View.VISIBLE);
        btnStartEnd.setOnClickListener(this);
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

        } else if (id == R.id.btnStartFinishGame) {
            if (!gameRunning) {
                btnStartEnd.setText("Завершить игру");
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
                        btnStartEnd.performClick();
                    }
                };
                updateQuestion();
                countDownTimer.start();

            } else {
                // TODO завершение игры
                clQuizContainer.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void updateQuestion() {
        currentQuestionNum++;
        if (currentQuestionNum >= questionList.size()) {
            //TODO принудительное окончание
            btnStartEnd.performClick();
            return;
        }
        currentQuestion = questionList.get(currentQuestionNum);
        tvQuestion.setText(currentQuestion.getQuestion());
        String[] answers = currentQuestion.getAnswers();
        btnAnswer1.setText(answers[0]);
        btnAnswer2.setText(answers[1]);
        btnAnswer3.setText(answers[2]);
        btnAnswer4.setText(answers[3]);

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
        System.out.println(tests.get(0));
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
            tvPoints.setText(Integer.toString(points));
        } else {
            soundPool.play(soundWrong, 1, 1, 1, 0, 1);
            if (updateErrors())
                updateQuestion();
            else
                btnStartEnd.performClick();
        }
    }

}