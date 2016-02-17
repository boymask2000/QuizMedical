package com.quiz.quizmedical;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.posbeu.quiz.db.XMLDataLoaderParser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;


public class DataLoaderActivity extends Activity implements OnClickListener {
    private String ip = null;
    private EditText text = null;
    private View imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataloader);

        text = (EditText) findViewById(R.id.ipaddress);

        ip = text.getText().toString().trim();

        imageButton = findViewById(R.id.load);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load:
                ip = text.getText().toString().trim();
                imageButton.setEnabled(false);
                new RetrieveFeedTask().execute(ip);
                break;

        }

    }


    class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... ip) {
            StringBuffer buffer = new StringBuffer();
            try {
                Socket s = new Socket(ip[0], 21000);

                BufferedReader read = new BufferedReader(new InputStreamReader(
                        s.getInputStream()));
                while (true) {
                    String line = read.readLine();
                    if (line == null)
                        break;
                    buffer.append(line);
                }
                read.close();
                s.close();

            } catch (Exception e) {
            }
            return buffer.toString();
        }

        protected void onPostExecute(String feed) {

            StringReader r = new StringReader(feed);
            InputSource is = new InputSource(r);
            XMLDataLoaderParser p = new XMLDataLoaderParser(is, getBaseContext());
            try {
                p.startParsing();
                Toast.makeText(getBaseContext(), "Remote DB loaded!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageButton.setEnabled(true);
        }

    }


    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0)
                .getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}
