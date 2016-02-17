package com.quiz.quizmedical;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.posbeu.quiz.db.DBHelper;

public class DataSenderActivity extends Activity implements OnClickListener {
    private String ip = null;
    private EditText text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataloader);

        text = (EditText) findViewById(R.id.ipaddress);

        ip = text.getText().toString().trim();

        View imageButton = findViewById(R.id.load);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load:
                ip = text.getText().toString().trim();
                new RetrieveFeedTask().execute(ip);
                break;

        }

    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... ip) {

            int size = DBHelper.exportDataBaseNet(getBaseContext(), ip[0]);

            return "" + size;
        }

        protected void onPostExecute(String feed) {

            Toast.makeText(getBaseContext(), "DB sent! size= " + feed,
                    Toast.LENGTH_LONG).show();

        }
    }

}
