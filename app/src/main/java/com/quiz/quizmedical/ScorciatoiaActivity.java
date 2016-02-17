package com.quiz.quizmedical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.posbeu.quiz.db.DBHelper;
import com.posbeu.quiz.db.Parameter;
import com.posbeu.quiz.db.bean.Domanda;
import com.posbeu.quiz.db.bean.Materia;
import com.posbeu.quiz.db.bean.SelectionParam;
import com.quiz.quizmedical.opt.Message;

import java.util.List;

/**
 * Created by giovanni on 1/8/15.
 */
public class ScorciatoiaActivity extends Activity {

    private static final String VERDE = "#00FF00";
    private String nextLevel = null;
    private Button base = null;
    private Button medio = null;
    private Button alto = null;
    private Button altissimo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorciatoia);

        base = (Button) findViewById(R.id.radio_base);
        base.setBackgroundResource(R.drawable.buttonstyle);
        medio = (Button) findViewById(R.id.radio_medio);
        medio.setBackgroundResource(R.drawable.buttonstyle);
        alto = (Button) findViewById(R.id.radio_alto);
        alto.setBackgroundResource(R.drawable.buttonstyle);
        altissimo = (Button) findViewById(R.id.radio_altissimo);
        altissimo.setBackgroundResource(R.drawable.buttonstyle);

        base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLevel = "1";
                cleanSelect();
                base.setCompoundDrawablesWithIntrinsicBounds(R.drawable.select, 0, 0, 0);

            }
        });
        medio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLevel = "2";
                cleanSelect();
                medio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.select, 0, 0, 0);
            }
        });
        alto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLevel = "3";
                cleanSelect();
                alto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.select, 0, 0, 0);
            }
        });
        altissimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextLevel = "4";
                cleanSelect();
                altissimo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.select, 0, 0, 0);
            }
        });

        ImageButton ok = (ImageButton) findViewById(R.id.select);
        ImageButton cancel = (ImageButton) findViewById(R.id.errore);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextLevel == null) {
                    Message.errDialog(ScorciatoiaActivity.this,
                            "Attenzione !!",
                            "Non hai scelto il livello ", R.drawable.promosso, null);
                    return;
                }


                performExam();


          /*      Parameter.setLivello(getBaseContext(), nextLevel);
                finish();*/
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private String startLevel = null;

    private void performExam() {
        String currlevel = Parameter.getCurrentLevel(this);
        startLevel = currlevel;
        final String levelExam = Livello.precLevelCode(nextLevel);
        if (Livello.compare(currlevel, nextLevel) >= 0) return;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListaDomande(Livello.getLivelloCode(levelExam));
                Parameter.resetSequence(getBaseContext());
                Parameter.setLivello(ScorciatoiaActivity.this, levelExam);
                Intent intent = new Intent(ScorciatoiaActivity.this, QuizActivity.class);
                Bundle b = new Bundle();
                b.putInt("mode", 1);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
            }
        };


        Message.infoDialog(ScorciatoiaActivity.this,
                "Attenzione !!",
                "Per passare al livello " + Livello.decodeLivello(nextLevel) + " dovrai rispondere esattamente a " + Parameter.getSogliaPromozione(this, levelExam) + " domande di livello " + Livello.decodeLivello(levelExam), R.drawable.promosso, listener);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            };
            if (resultCode == RESULT_OK) {
                Message.infoDialog(this,
                        "Complimenti !!",
                        "Hai supoerato il test. Ora sei a livello " + Livello.decodeLivello(nextLevel), R.drawable.promosso, listener);
                Parameter.setLivello(getBaseContext(), nextLevel);
            } else {
                Message.errDialog(this,
                        "Sorry !!",
                        "Non hai superato il test.", R.drawable.errore, listener);
                Parameter.setLivello(getBaseContext(), startLevel);
            }
        }

    }

    public void createListaDomande(long livello) {
        Materia dummy = new Materia();
        dummy.setAll(true);
        SelectionParam param = new SelectionParam();
        param.setMateria(dummy);
        param.setLivello(livello);
        Heap.setMateria(dummy);
        long nn = Parameter.getSogliaPromozione(this, "" + livello);


        List<Domanda> lista = DBHelper.getDomande(this, param);
        MainActivity.getDomande(lista, "" + nn);

        //   return lista;
    }

    private void cleanSelect() {
        base.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        medio.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        alto.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        altissimo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

}
