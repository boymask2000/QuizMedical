package com.quiz.quizmedical;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.posbeu.quiz.db.DBHelper;
import com.posbeu.quiz.db.Parameter;
import com.posbeu.quiz.db.bean.Domanda;
import com.posbeu.quiz.db.bean.Materia;
import com.posbeu.quiz.db.bean.SelectionParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    private Materia materia;
    private Long livello = (long) 0;
    private String minuti = null;

    private static Random rand = new Random((new Date()).getTime());
    private String hotword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        fillSpinnerMaterie();
        fillSpinnerHotwords();
        fillSpinnerLivello();

        final EditText tempo = (EditText) findViewById(R.id.minuti);
        tempo.setText("5");

        Button start = (Button) findViewById(R.id.start_button);
        start.setBackgroundResource(R.drawable.buttonstyle);
        start.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                minuti = tempo.getText().toString();
                List<Domanda> lista = generateListaDomande();
                getDomande(lista, minuti);

                if (Heap.getListaDomande().size() == 0) {
                    Toast.makeText(getBaseContext(), "Nessuna domanda!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                TimerService.reset();
                Intent intent = new Intent(getBaseContext(), QuizActivity.class);
                startActivity(intent);
            }

        });

        View mat = findViewById(R.id.scorcia);
        mat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ScorciatoiaActivity.class);
                startActivity(intent);
            }

        });

        showFragment();
    }

    private void showFragment() {
        android.app.Fragment fragment = new BarraFragment();
        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container1, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private long getLivello() {
        TextView liv = (TextView) findViewById(R.id.livello);
        String slivello = liv.getText().toString();
        long livell = Livello.undecodeLivello(slivello);
        return livell;
    }

    private List<Domanda> generateListaDomande() {
        livello = getLivello();
        SelectionParam param = new SelectionParam();
        param.setMateria(materia);
        param.setLivello(livello);
        Heap.setMateria(materia);


        param.setParolaChiave(hotword);


        List<Domanda> lista = DBHelper.getDomande(this, param);

        Toast.makeText(this, "" + lista.size(), Toast.LENGTH_SHORT).show();

        List<Domanda> ll = filterByHotWord(lista);
        return ll;
    }


    private List<Domanda> filterByHotWord(List<Domanda> lista) {

        if (hotword == null || hotword.trim().length() == 0) return lista;

        List<Domanda> out = new ArrayList<Domanda>();
        for (Domanda domanda : lista) {
            String hots = domanda.getHotWords();
            if (hots != null && hots.indexOf(hotword) != -1) out.add(domanda);
        }
        return out;
    }


    public static void getDomande(List<Domanda> lista, String minuti) {
        int m = 0;
        int iminuti = 0;
        try {
            m = Integer.parseInt(minuti);
        } catch (NumberFormatException e) {
            m = 10;
        }
        if (m < 0)
            m = 10;
        iminuti = m;
        Heap.setMaxTime(60 * m);

        List<Domanda> out = new ArrayList<Domanda>();

        for (int i = 0; i < iminuti; i++) {
            if (lista.size() == 0)
                break;
            int v = randInt(0, lista.size() - 1);
            Domanda d = lista.get(v);
            out.add(d);
            lista.remove(v);
        }
        Heap.setListaDomande(out);

        return;
    }

    private void fillSpinnerLivello() {
        TextView lev = (TextView) findViewById(R.id.livello);

        String l = Livello.decodeLivello(Parameter.getCurrentLevel(this));
        lev.setText(l);
    }


    private void fillSpinnerMaterie() {
        final Spinner spinner = (Spinner) findViewById(R.id.materie);
        ArrayAdapter<Materia> spinnerAdapter = new ArrayAdapter<Materia>(this,
                android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        Materia dummy = new Materia();
        dummy.setAll(true);
        spinnerAdapter.add(dummy);

        List<Materia> lista = DBHelper.getAllMateria(this);
        for (Materia m : lista) {
            spinnerAdapter.add(m);
        }
        if (lista.size() == 0)
            return;
        materia = lista.get(0);

        spinnerAdapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                materia = (Materia) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        View mat = findViewById(R.id.select_button_materia);
        mat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                spinner.performClick();
            }

        });
    }

    private void fillSpinnerHotwords() {
        final Spinner spinner = (Spinner) findViewById(R.id.hotwords);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        spinnerAdapter.add("");

        List<String> lista = DBHelper.getHotwords(this);
        for (String m : lista) {
            spinnerAdapter.add(m);
        }
        if (lista.size() == 0)
            return;
        hotword = lista.get(0);

        spinnerAdapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                hotword = (String) parent.getItemAtPosition(position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        View mat = findViewById(R.id.select_button_hot);
        mat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                spinner.performClick();
            }

        });
    }

    private static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    @Override
    protected void onResume() {
        TimerService.reset();
        fillSpinnerLivello();

        super.onResume();
    }
}
