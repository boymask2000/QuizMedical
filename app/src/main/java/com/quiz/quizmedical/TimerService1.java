package com.quiz.quizmedical;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService1 extends Service {
    private static boolean sospeso = false;
    int counter = 0;
    static final int UPDATE_INTERVAL = 1000;
    private Timer timer = new Timer();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        // Toast.makeText(this, “Service Started”, Toast.LENGTH_LONG).show();
        doSomethingRepeatedly();

        return START_STICKY;
    }

    private void doSomethingRepeatedly() {
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.d("MyService", String.valueOf(++counter));
                try {
                    new DoBackgroundTask().execute(new URL(
                            "http://www.amazon.com/somefiles.pdf"), new URL(
                            "http://www.wrox.com/somefiles.pdf"), new URL(
                            "http://www.google.com/somefiles.pdf"), new URL(
                            "http://www.learn2develop.net/somefiles.pdf"));
                } catch (MalformedURLException e) {// TODO Auto-generated catch
                    // block
                    e.printStackTrace();
                }
            }
        }, 0, UPDATE_INTERVAL);

    }

    private class DoBackgroundTask extends AsyncTask<URL, Long, Long> {
        protected Long doInBackground(URL... urls) {
            if (sospeso)
                return (long) 0;
            long secs = Heap.getMaxTime();
            Heap.setMaxTime(secs - 1);
            secs = Heap.getMaxTime();
            if (secs <= 0) {
                segnalaTempoScaduto();
                stopSelf();
            }

            publishProgress(secs);
            return (long) 0;
        }

        private void segnalaTempoScaduto() {
            Intent intent = new Intent("timeexpired");

            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                    intent);

        }

        protected void onProgressUpdate(Long... progress) {
            long val = progress[0];

            Intent intent = new Intent("timestatus");
            intent.putExtra("time", val);

            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                    intent);
        }

        protected void onPostExecute(Long result) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }

    }

    public static void sospendi() {
        sospeso = true;

    }

    public static void riprendi() {
        sospeso = false;

    }
}