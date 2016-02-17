package com.quiz.quizmedical;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class BarraFragment extends Fragment implements OnClickListener {


    public BarraFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.barrafragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton home = (ImageButton) getActivity().findViewById(R.id.homebutton);
        ImageButton stat = (ImageButton) getActivity().findViewById(R.id.statbutton);
        home.setOnClickListener(this);
        stat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homebutton:
                getActivity().finish();
                break;
            case R.id.statbutton:
                Intent intent0 = new Intent(getActivity(), StatActivityAlf.class);
                startActivity(intent0);
                break;
        }
    }

}