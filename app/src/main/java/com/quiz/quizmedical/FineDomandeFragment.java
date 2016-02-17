package com.quiz.quizmedical;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.posbeu.quiz.db.bean.Statistiche;

public class FineDomandeFragment extends Fragment implements OnClickListener {


    public FineDomandeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.finedomandefragment, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        createlayout();
    }

    @Override
    public void onClick(View v) {

    }

    private TableLayout tableLayout;
    private TableRow.LayoutParams rowParams;
    private TableRow.LayoutParams itemParams = new TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT, 1f);

    private void createlayout() {
        Statistiche stat = QuizActivity.getStat(getActivity());
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT);
        rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, 0, 1f);

        tableLayout = new TableLayout(getActivity());
        tableLayout.setLayoutParams(tableParams);
        tableLayout.setBackgroundColor(0xffffff);

        addRow("Totale domande", stat.getNumDomande());
        addRow("Risposte esatte", stat.getNumRisposteOK());
        addRow("Risposte errate", stat.getNumRisposteKO());
        //	addRow( "Punteggio", StatActivity.calcolaPunti(stat));

        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.layoutQ);
        layout.addView(tableLayout);
    }

    private void addRow(String s1, int s2) {
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(rowParams);

        addVal(tableRow, s1);
        addVal(tableRow, "" + s2);
        tableLayout.addView(tableRow);
    }

    private void addVal(TableRow tableRow, String val) {
        TextView textView = new TextView(getActivity());
        textView.setTextSize(22);
        textView.setLayoutParams(itemParams);

        textView.setText(" " + val);
        tableRow.addView(textView);
    }
}