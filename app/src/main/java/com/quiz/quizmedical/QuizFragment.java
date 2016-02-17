package com.quiz.quizmedical;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.posbeu.quiz.db.bean.Domanda;
import com.posbeu.quiz.db.bean.Risposta;
import com.posbeu.utils.ImageHandler;
import com.quiz.quizmedical.opt.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.DataInfo;
import data.DataReceiver;

public class QuizFragment extends Fragment implements OnClickListener {
    private RadioGroup mRadioGroup = null;
    private Domanda domanda = null;
    private HashMap<Integer, Risposta> map = new HashMap<Integer, Risposta>();
    private List<Button> lista_bottoni = new ArrayList<Button>();
    private int domanda_corrente;


    public QuizFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.quizfragment, container, false);

    }

    private void fillDomanda() {
        if (Heap.getListaDomande().size() == 0)
            return;
        domanda = Heap.getListaDomande().get(domanda_corrente);
        TextView text = (TextView) getActivity().findViewById(R.id.testo);
        text.setText(domanda.getTestoDomanda());

        processImage();

    }

    private void processImage() {
        String image = domanda.getImage();
        if (image == null || image.trim().length() == 0) return;
        if (image.trim().length() < 50) return;
        ;
        Bitmap bitmap = ImageHandler.string2bitmap(image);

        ImageView imageView = (ImageView) getActivity().findViewById(R.id.immagine);
        imageView.setImageBitmap(bitmap);
    }

    private void fillRisposte(List<Risposta> list) {
        LinearLayout layout = (LinearLayout) getActivity().findViewById(
                R.id.layoutQ);

        for (Risposta risposta : list) {
            Button button = new Button(getActivity());
            lista_bottoni.add(button);
            button.setText(risposta.getRisposta());
            button.setGravity(Gravity.LEFT);
            button.setLayoutParams(new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            button.setBackgroundResource(R.drawable.buttonstyle);
            layout.addView(button);

            if (risposta.isEsatta())
                setRispostaEsatta(button);
            else
                setRispostaErrata(button, risposta);

        }

    }

    private void disableButtons() {
        for (Button b : lista_bottoni) {
            b.setClickable(false);
            b.setBackgroundColor(getResources().getColor(
                    android.R.color.darker_gray));
        }
    }

    private void setRispostaErrata(final Button button, final Risposta risposta) {
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                segnalaRispostaEsattaOErrata(false);
                segnalaClick();
                disableButtons();
                domanda.setHaRisposto(true);

                domanda.setRispostaEsatta(false);

                showSpiegazione(risposta);
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.select_err, 0, 0, 0);
            }

            private void showSpiegazione(Risposta risposta) {

                TimerService.sospendi();

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimerService.riprendi();
                        QuizActivity q = (QuizActivity) getActivity();
                        if (q.getMode() == 1)
                            q.finish();
                    }
                };
                DialogInterface.OnDismissListener dlistener = new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        TimerService.riprendi();
                        QuizActivity q = (QuizActivity) getActivity();
                        if (q.getMode() == 1)
                            q.finish();
                    }
                };
                Message.infoDialog(getActivity(),
                        "Risposta errata",
                        risposta.getSpiegazione(), R.drawable.errore, listener, dlistener);
            }
        });

    }

    private void setRispostaEsatta(final Button button) {
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                segnalaClick();
                segnalaRispostaEsattaOErrata(true);
                disableButtons();
                domanda.setHaRisposto(true);
                Toast.makeText(getActivity(), "Risposta Esatta !!!!",
                        Toast.LENGTH_LONG).show();
                domanda.setRispostaEsatta(true);
                button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.select, 0, 0, 0);
            }

        });

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        fillDomanda();
        fillRisposte(domanda.getRisposte());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        RadioButton r = (RadioButton) getActivity().findViewById(selectedId);
        int id = r.getId();

        Risposta risp = map.get(id);
        if (risp.isEsatta()) {

            domanda.setRispostaEsatta(true);
            Toast.makeText(getActivity(), "Esatta " + selectedId,
                    Toast.LENGTH_SHORT).show();
        } else {
            domanda.setRispostaEsatta(false);

            Toast.makeText(getActivity(), "" + selectedId, Toast.LENGTH_SHORT)
                    .show();
        }
        segnalaClick();
        DataInfo info = new DataInfo();

        info.setTotDomande(Heap.getListaDomande().size());
        DataReceiver d = (DataReceiver) getActivity();
        d.sendData(info);
    }

    private void segnalaClick() {
        Intent intent = new Intent("clicked");

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    private void segnalaRispostaEsattaOErrata(boolean esatta) {
        Intent intent = new Intent(esatta ? QuizActivity.MSG_RISPOSTA_OK : QuizActivity.MSG_RISPOSTA_KO);

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    public void setDomanda_corrente(int domanda_corrente) {
        this.domanda_corrente = domanda_corrente;
    }
}