package com.quiz.quizmedical;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ImportExportDBActivity extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inportexportdb);

        handle_buttons();
    }

    private void handle_buttons() {
        View aboutButton = findViewById(R.id.importdb_button);
        aboutButton.setOnClickListener(this);

        View newButton = findViewById(R.id.exportdb_button);
        newButton.setOnClickListener(this);
        View dataloading_button = findViewById(R.id.dataloading_button);
        dataloading_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.importdb_button:
                //		DBHelper.importDB(this);
                break;
            case R.id.exportdb_button:
                //		DBHelper.exportDB(this);

                Intent intent0 = new Intent(getBaseContext(),
                        DataSenderActivity.class);
                startActivity(intent0);


                break;
            case R.id.dataloading_button:
                Intent intent1 = new Intent(getBaseContext(),
                        DataLoaderActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }

    }

}
