package com.example.infogames.games;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

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
    int[] inState;
    int[] outState;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public GameSchemeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameSchemeFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        inState = new int[]{0, 0};
        outState = new int[] {0, 0, 0, 0};
        buttonsIn = new ImageButton[] {view.findViewById(R.id.buttonIn1),view.findViewById(R.id.buttonIn2)};
        buttonsIn[0].setOnClickListener(this);
        buttonsIn[1].setOnClickListener(this);

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
}