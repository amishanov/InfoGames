package com.example.infogames.games;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infogames.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameSchemeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameSchemeFragment extends Fragment implements View.OnClickListener {

    ImageButton buttonIn1, buttonIn2, buttonOut0, buttonOut1, buttonOut2, buttonOut3;
    ImageButton[] buttonsIn;
    ImageButton[] buttonsOut;
    Button buttonEnterCode;
    TextView tvCode;
    int[] inState;
    int[] outState;
    ImageView ivTimer, ivWrong1, ivWrong2, ivWrong3;
    TextView tvPoints, tvTimer;
    Button btnStartEnd;
    long timeLeft = 60000;
    boolean gameRunning = false;
    int currentCode = 0, errors = 0, points = 0;
    SoundPool soundPool;
    int soundCorrect, soundWrong;
    CountDownTimer countDownTimer;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public GameSchemeFragment() {
        // Required empty public constructor
    }

    public static GameSchemeFragment newInstance(String param1, String param2) {
        GameSchemeFragment fragment = new GameSchemeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_scheme, container, false);

        // Инициализация элементов для игры
        inState = new int[]{0, 0};
        outState = new int[] {0, 0, 0, 0};
        buttonsIn = new ImageButton[] {view.findViewById(R.id.buttonIn1),view.findViewById(R.id.buttonIn2)};
        buttonsIn[0].setOnClickListener(this);
        buttonsIn[1].setOnClickListener(this);
        buttonEnterCode = view.findViewById(R.id.buttonEnterCode);
        buttonEnterCode.setOnClickListener(this);
        tvCode = view.findViewById(R.id.textViewCode);

        // Инициализация элементов для звуков
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

//        buttonIn1.setOnClickListener(this);
//        buttonIn2 = view.findViewById(R.id.buttonIn2);
//        buttonIn2.setOnClickListener(this);
//
//        buttonOut0 = view.findViewById(R.id.buttonOut0);
//        buttonOut0.setOnClickListener(this);
//
//        buttonOut1 = view.findViewById(R.id.buttonOut1);
//        buttonOut1.setOnClickListener(this);
//        buttonOut2 = view.findViewById(R.id.buttonOut2);
//        buttonOut2.setOnClickListener(this);
//
//        buttonOut3 = view.findViewById(R.id.buttonOut3);
//        buttonOut3.setOnClickListener(this);

        buttonsOut = new ImageButton[] {view.findViewById(R.id.buttonOut0),view.findViewById(R.id.buttonOut1),
                view.findViewById(R.id.buttonOut2), view.findViewById(R.id.buttonOut3)};
        for (ImageButton btn: buttonsOut) {
            btn.setOnClickListener(this);
        }

        return view;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonIn1) {
            //TODO
            System.out.println(inState[0]);
            if (inState[0] == 0) {
                buttonsIn[0].setImageResource(R.drawable.round_button_green);
//            buttonsOut[1].setImageResource(R.drawable.halfround_button_green);
                inState[0] = 1;
                setUpOut(transfer(inState));
            } else {
                buttonsIn[0].setImageResource(R.drawable.round_button_gray);
                inState[0] = 0;
                setUpOut(transfer(inState));
            }

        } else if (id == R.id.buttonIn2) {
            if (inState[1] == 0) {
                buttonsIn[1].setImageResource(R.drawable.round_button_green);
                inState[1] = 1;
                setUpOut(transfer(inState));
            } else  {
                buttonsIn[1].setImageResource(R.drawable.round_button_gray);
                inState[1] = 0;
                setUpOut(transfer(inState));
            }
        } else if (id == R.id.btnStartFinishGame) {
            if (!gameRunning) {
                startGame();
                gameRunning = true;
            } else {
                // TODO завершение игры
            }
        } else if (id == R.id.buttonEnterCode) {
            if (gameRunning) {
                if (checkCode()) {
                    soundPool.play(soundCorrect,1, 1, 1, 0, 1);
                    updateGame();
                    points++;
                    tvPoints.setText(Integer.toString(points));
                } else {
                    soundPool.play(soundWrong,1, 1, 1, 0, 1);
                    if (!updateErrors()) {
                        updateGame();
                    }
                    else {
                        btnStartEnd.performClick();
                    }
                }
            }
        }
    }

    public int transfer(int[] inState) {
        // Сделать перевод
        int res = 0;
        for (int i = 0; i < inState.length; i++) {
            res += inState[i] * Math.pow(2, i);
        }
        return res;
    }

    public void setUpOut(int out) {
        for (int i = 0; i < buttonsOut.length; i++) {
            buttonsOut[i].setImageResource(R.drawable.halfround_button_gray);
        }
        buttonsOut[out].setImageResource(R.drawable.halfround_button_green);
    }

    public void startGame() {
        btnStartEnd.setText("Закончить игру");
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
        currentCode = (int) (Math.random() * 4);
        tvCode.setText(Integer.toString(currentCode));
        countDownTimer.start();
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

    public void updateGame() {
        currentCode = (int) (Math.random() * 4);
        tvCode.setText(Integer.toString(currentCode));
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

    public boolean checkCode() {
        if (transfer(inState) == currentCode)
            return true;
        return false;
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
    public void onDestroy() {
        soundPool.release();
        super.onDestroy();
    }
}