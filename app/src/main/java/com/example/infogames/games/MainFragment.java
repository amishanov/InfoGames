package com.example.infogames.games;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.infogames.R;


public class MainFragment extends Fragment implements View.OnClickListener {

    Button buttonScheme, buttonQuiz;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MainFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        buttonScheme = view.findViewById(R.id.buttonSchemeGame);
        buttonScheme.setOnClickListener(this);
        buttonQuiz = view.findViewById(R.id.buttonQuizGame);
        buttonQuiz.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonSchemeGame) {
            GameSchemeFragment gameSchemeFragment = new GameSchemeFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, gameSchemeFragment, "gameSchemeFragment").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        } else if (id == R.id.buttonQuizGame) {
            GameQuizFragment gameQuizFragment = new GameQuizFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, gameQuizFragment, "gameQuizFragment" ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
    }
}