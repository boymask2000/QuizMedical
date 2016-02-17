package com.quiz.quizmedical;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.posbeu.quiz.db.Parameter;
import com.posbeu.quiz.db.bean.Domanda;

import data.DataInfo;
import data.DataReceiver;

public class TestataFragment extends Fragment implements DataReceiver {
    private int totale;
    private int esatte;
    private int errate;
    private int corrente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                long secs = intent.getLongExtra("time", 0);
                long min = secs / 60;
                secs -= 60 * min;
                long secss = secs;
                if (getActivity() == null)
                    return;
                TextView d_time = (TextView) getActivity().findViewById(
                        R.id.time);
                d_time.setText("" + min + ":" + secss);

                DataInfo info = new DataInfo();
                // info.setDomandaCorrente(0);
                sendData(info);
            }

        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mMessageReceiver, new IntentFilter("timestatus"));

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.testatafragment, container, false);
    }

    @Override
    public void sendData(DataInfo info) {

        totale = Heap.getListaDomande().size();
        esatte = Domanda.conta(true);
        errate = Domanda.conta(false);
        corrente = Heap.getDomanda_corrente();// info.getDomandaCorrente();

        TextView d_materia = (TextView) getActivity()
                .findViewById(R.id.materia);
        d_materia.setText(Heap.getMateria().getDescrizione());

        TextView d_corrente = (TextView) getActivity().findViewById(
                R.id.corrente);
        TextView d_errate = (TextView) getActivity().findViewById(R.id.errate);
        TextView d_giuste = (TextView) getActivity().findViewById(R.id.giuste);
        d_corrente.setText("" + (corrente + 1) + "/" + totale);
        d_errate.setText("" + errate);
        d_giuste.setText("" + esatte);

        TextView t_livello = (TextView) getActivity()
                .findViewById(R.id.livello);
        t_livello.setText(Livello.decodeLivello(Parameter.getCurrentLevel(getActivity())));

        TextView n_domanda = (TextView) getActivity()
                .findViewById(R.id.num_domanda);
        Domanda d = Heap.getListaDomande().get(Heap.getDomanda_corrente());
        n_domanda.setText("" + d.getDomanda_id());
    }
}