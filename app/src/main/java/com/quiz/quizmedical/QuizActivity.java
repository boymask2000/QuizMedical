package com.quiz.quizmedical;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.posbeu.quiz.db.DBHelper;
import com.posbeu.quiz.db.Parameter;
import com.posbeu.quiz.db.bean.Domanda;
import com.posbeu.quiz.db.bean.Statistiche;
import com.posbeu.utils.DateUtils;
import com.quiz.quizmedical.opt.Message;

import data.DataInfo;
import data.DataReceiver;

import static com.posbeu.quiz.db.Parameter.getCurrentLevel;
import static com.posbeu.quiz.db.Parameter.getSogliaPromozione;
import static com.posbeu.quiz.db.Parameter.incSequence;
import static com.posbeu.quiz.db.Parameter.resetSequence;

public class QuizActivity extends FragmentActivity implements OnClickListener,
        DataReceiver {
    private int domanda_corrente = 0;
    private TestataFragment testata = null;
    private DataInfo info;
    private ImageButton next = null;
    private BroadcastReceiver mMessageReceiverTime;
    private BroadcastReceiver mMessageReceiverClick;
    private BroadcastReceiver mMessageReceiverMSG_OK;
    private BroadcastReceiver mMessageReceiverMSG_KO;

    public static final String MSG_RISPOSTA_OK = "MSG_REPLY_OK";
    public static final String MSG_RISPOSTA_KO = "MSG_REPLY_KO";


    private int mode = 0;

    @Override
    protected void onStart() {
        domanda_corrente = 0;
        Heap.setDomanda_corrente(domanda_corrente);
        Bundle b = getIntent().getExtras();
        if (b != null)
            mode = b.getInt("mode");
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizactivity);

        ImageButton home = (ImageButton) findViewById(R.id.homebutton);
        // Button prev = (Button) findViewById(R.id.prevquestion);
        next = (ImageButton) findViewById(R.id.nextquestion);


        // prev.setOnClickListener(this);
        home.setOnClickListener(this);
        next.setOnClickListener(this);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container1) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            testata = new TestataFragment();
            QuizFragment quiz = new QuizFragment();
            quiz.setDomanda_corrente(domanda_corrente);


            testata.setArguments(getIntent().getExtras());


            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();


            transaction.replace(R.id.fragment_container1, testata);
            transaction.replace(R.id.fragment_container2, quiz);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();

            startService(new Intent(getBaseContext(), TimerService.class));
            next.setEnabled(false);
            Heap.setScaduto(false);
        }
    }

    @Override
    protected void onPause() {
        TimerService.sospendi();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiverClick);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiverTime);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiverMSG_OK);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiverMSG_KO);
        super.onPause();
    }

    @Override
    protected void onResume() {
        TimerService.riprendi();
        setMessageListener();
        super.onResume();
    }


    private void setMessageListener() {
        mMessageReceiverMSG_OK = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //		String action = intent.getAction();
                Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
                int seqLen = incSequence(getBaseContext());
                checkLivello(seqLen);
            }

        };
        mMessageReceiverMSG_KO = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //		String action = intent.getAction();
                Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show();
                resetSequence(getBaseContext());
                // if( mode==1)finish();
            }

        };
        mMessageReceiverTime = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //		String action = intent.getAction();
                Toast.makeText(context, "Scaduto", Toast.LENGTH_LONG).show();
                Heap.setScaduto(true);
                showExpiredTime();
            }

        };
        mMessageReceiverClick = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //		String action = intent.getAction();

                if (Heap.isFineLista())
                    showFineDomande();

                if (!Heap.isScaduto() && !Heap.isFineLista())
                    next.setEnabled(true);

            }

        };
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiverTime, new IntentFilter("timeexpired"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiverClick, new IntentFilter("clicked"));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiverMSG_OK, new IntentFilter(MSG_RISPOSTA_OK));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiverMSG_KO, new IntentFilter(MSG_RISPOSTA_KO));
    }


    private void checkLivello(int seqLen) {
        String curr = getCurrentLevel(getBaseContext());
        int ncur;
        ncur = Integer.parseInt(curr);

        long val = getSogliaPromozione(getBaseContext(), curr);
        if (seqLen >= val) {

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getBaseContext(),
                            FirstMenuActivity.class);
                    startActivity(i);
                }
            };

            ncur++;
            if (mode == 0)
                Message.infoDialog(this,
                        "Complimenti !!",
                        "Sei passato a livello " + Livello.decodeLivello("" + ncur), R.drawable.promosso, listener);

            if (mode == 1) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "ok");
                setResult(RESULT_OK, returnIntent);
                finish();
                return;
            }

            Parameter.incLivello(getBaseContext());
            Parameter.resetSequence(getBaseContext());

        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.homebutton:
                this.finish();
                break;

            case R.id.nextquestion:
                if (domanda_corrente < Heap.getListaDomande().size() - 1)
                    domanda_corrente++;

                break;
        }
        Heap.setDomanda_corrente(domanda_corrente);
        QuizFragment quiz = new QuizFragment();
        quiz.setDomanda_corrente(domanda_corrente);
        showFragment(quiz);

        updateTestata();

        next.setEnabled(false);
    }

    private void showFineDomande() {
        TimerService.sospendi();
        FineDomandeFragment msg = new FineDomandeFragment();

        showFragment(msg);
        TimerService.stop();
    }

    private void showFragment(android.app.Fragment fragment) {
        int t1 = R.anim.fade_in;
        int t2 = R.anim.fade_out;
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.setCustomAnimations(t1, t2);
        transaction.replace(R.id.fragment_container2, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void updateTestata() {
        if (info == null)
            info = new DataInfo();
        info.setDomandaCorrente(domanda_corrente);
        sendData(info);
    }


    private void showExpiredTime() {
        ExpiredTimeFragment msg = new ExpiredTimeFragment();

        showFragment(msg);

    }

    @Override
    public void sendData(DataInfo info) {
        this.info = info;
        testata.sendData(info);

    }

    public static Statistiche getStat(Context context) {
        Statistiche stat = new Statistiche();
        stat.setData(DateUtils.getDate(context) + " " + DateUtils.getTime() + "," + Heap.getMateria());
        stat.setNumDomande(Heap.getListaDomande().size());
        int esatte = Domanda.conta(true);
        int errate = Domanda.conta(false);
        stat.setNumRisposteOK(esatte);
        stat.setNumRisposteKO(errate);
        for (int i = 1; i <= 5; i++) {
            stat.setRispostaKO(Domanda.conta(false, i), i - 1);
            stat.setRispostaOK(Domanda.conta(true, i), i - 1);
        }
        stat.setLivello(getCurrentLevel(context));
        return stat;
    }

    public int getMode() {
        return mode;
    }

    @Override
    protected void onStop() {
        TimerService.stop();

        Statistiche stat = getStat(this);

        DBHelper.writeStat(this, stat);
        super.onStop();
    }

}
