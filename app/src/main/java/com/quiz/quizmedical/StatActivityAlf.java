package com.quiz.quizmedical;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.posbeu.quiz.db.DBHelper;
import com.posbeu.quiz.db.Parameter;
import com.posbeu.quiz.db.bean.Statistiche;

import java.util.List;

public class StatActivityAlf extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createlayout();
        setContentView(R.layout.statactivity);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        Button end_button = (Button) findViewById(R.id.end_button);
        Button reset_lev_button = (Button) findViewById(R.id.reset_lev_button);
        Button reset_stat_button = (Button) findViewById(R.id.reset_stat_button);
        end_button.setOnClickListener(this);
        reset_lev_button.setOnClickListener(this);
        reset_stat_button.setOnClickListener(this);
        createTestata();
        //	createTestata2();
        layout.addView(createTable());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.end_button:
                this.finish();
                break;
            case R.id.reset_lev_button:
                reset();
                break;
            case R.id.reset_stat_button:
                reset_stat();
                break;

        }
    }

    private void reset() {
        Parameter.reset(this);
    }

    private void reset_stat() {
        DBHelper.deleteStat(this);
        finish();
    }

    private TableRow.LayoutParams itemParams = new TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT, 1f);
    private TableLayout tableLayout;
    private TableRow.LayoutParams rowParams;

    private void createlayout() {
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT);
        rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, 0, 1f);

        tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(tableParams);
        tableLayout.setBackgroundColor(0xffffff);
    }

    public View createTestata() {

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(rowParams);

        addVal(tableRow, "Data");
        addVal(tableRow, "Argomento");
        addVal(tableRow, "Esatte/totale");
        addVal(tableRow, "livello");


        tableLayout.addView(tableRow);

        return tableLayout;
    }

    public View createTestata2() {

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(rowParams);

        addVal(tableRow, "");
        addVal(tableRow, "");
        addVal(tableRow, "1");
        addVal(tableRow, "2");
        addVal(tableRow, "3");
        addVal(tableRow, "4");
        addVal(tableRow, "5");
        addVal(tableRow, "");
        addVal(tableRow, "1");
        addVal(tableRow, "2");
        addVal(tableRow, "3");
        addVal(tableRow, "4");
        addVal(tableRow, "5");
        addVal(tableRow, "");

        tableLayout.addView(tableRow);

        return tableLayout;
    }

    public View createTable() {

        List<Statistiche> lista = DBHelper.getAllstat(this);

        for (Statistiche stat : lista) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(rowParams);

            String v[] = stat.getData().split(",");

            int totEsatte = stat.getRisposteOK()[0] + stat.getRisposteOK()[1]
                    + stat.getRisposteOK()[2] + stat.getRisposteOK()[3]
                    + stat.getRisposteOK()[4];

            // addVal(tableRow, stat.getData());
            if (v != null && v.length > 1) {
                addVal(tableRow, v[0]);
                addVal(tableRow, v[1]);
            }
            addVal(tableRow, "" + totEsatte + "/" + stat.getNumDomande());


            addVal(tableRow, "" + Livello.decodeLivello(stat.getLivello()));

            tableLayout.addView(tableRow);
        }

        return tableLayout;
    }

    public static int calcolaPunti(Statistiche stat) {
        int v = 0;
        for (int i = 0; i < 5; i++) {
            v += (stat.getRisposteOK()[i] - stat.getRisposteKO()[i]) * (i + 1);
        }
        return v;
    }

    private void addVal(TableRow tableRow, String val) {
        TextView textView = new TextView(this);
        textView.setTextSize(12);
        textView.setLayoutParams(itemParams);

        textView.setText(" " + val);
        tableRow.addView(textView);
    }
}
