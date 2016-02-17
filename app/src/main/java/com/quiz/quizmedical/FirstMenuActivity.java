package com.quiz.quizmedical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class FirstMenuActivity extends Activity implements OnClickListener {
    private Animation animTranslateR = null;
    private Animation animTranslateL = null;
    private Button start_button = null;
    private Button stat_button = null;
    private Button info_button = null;
    private Button dataloading_button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstmenuactivity);
        animTranslateR = AnimationUtils.loadAnimation(this,
                R.anim.translater);
        animTranslateL = AnimationUtils.loadAnimation(this,
                R.anim.translatel);

        start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(this);
        stat_button = (Button) findViewById(R.id.stat_button);
        stat_button.setOnClickListener(this);
        info_button = (Button) findViewById(R.id.info_button);
        info_button.setOnClickListener(this);
        dataloading_button = (Button) findViewById(R.id.dataloading_button);
        dataloading_button.setOnClickListener(this);

        start_button.setBackgroundResource(R.drawable.buttonstyle);
        stat_button.setBackgroundResource(R.drawable.buttonstyle);
        info_button.setBackgroundResource(R.drawable.buttonstyle);
        dataloading_button.setBackgroundResource(R.drawable.buttonstyle);

        stat_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.stat, 0, 0, 0);
        info_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.info, 0, 0, 0);
        dataloading_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.carica, 0, 0, 0);
        start_button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.start, 0, 0, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_button:
                TimerService.reset();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.stat_button:
                Intent intent0 = new Intent(getBaseContext(), StatActivityAlf.class);
                startActivity(intent0);
                break;
            case R.id.info_button:
                Intent intent2 = new Intent(getBaseContext(),
                        InfoActivity.class);
                startActivity(intent2);
                break;
            case R.id.dataloading_button:
                Intent intent1 = new Intent(getBaseContext(),
                        ImportExportDBActivity.class);
                startActivity(intent1);
                break;

        }
    }

    @Override
    protected void onResume() {
        if (start_button != null)
            start_button.startAnimation(animTranslateR);
        if (dataloading_button != null)
            dataloading_button.startAnimation(animTranslateR);
        if (info_button != null)
            info_button.startAnimation(animTranslateL);
        if (stat_button != null)
            stat_button.startAnimation(animTranslateL);
        super.onResume();
    }
}
