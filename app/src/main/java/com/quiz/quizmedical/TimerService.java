package com.quiz.quizmedical;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class TimerService extends Service {
    static final int UPDATE_INTERVAL = 1000;
    private static boolean stopped = false;
    private static boolean sospeso = false;
    private static boolean run = false;
    private int counter = 0;
    private Handler handler = null;

    public static void sospendi() {
        sospeso = true;

    }

    public static void riprendi() {
        sospeso = false;

    }

    public static void stop() {
        stopped = true;
    }

    public static void reset() {
        stopped = false;
    }

    public static boolean isSospeso() {
        return sospeso;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so retbooleanurn sticky.
        // Toast.makeText(this, “Service Started”, Toast.LENGTH_LONG).show();
        doSomethingRepeatedly();

        return START_STICKY;
    }

    private void doSomethingRepeatedly() {
        if (run) return;
        run = true;

        Runnable job = new DoBackgroundTask(this);

        handler = new Handler();
        handler.postDelayed(job, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    private class DoBackgroundTask extends Thread {
        private TimerService srv;

        public DoBackgroundTask(TimerService srv) {
            this.srv = srv;
        }

        public void run() {
//Log.d("", ""+srv.isSospeso());
            long secs = Heap.getMaxTime();
            if (!srv.isSospeso() && !stopped) {
                Heap.setMaxTime(secs - 1);
                secs = Heap.getMaxTime();
                if (secs <= 0) {
                    segnalaTempoScaduto();
                    stopped = true;
                }
            }
        /*    if (stopped) {
                stopSelf();
                return;
            }*/
            publishProgress(secs);
            handler.postDelayed(this, 1000);
        }

        private void segnalaTempoScaduto() {
            Intent intent = new Intent("timeexpired");

            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                    intent);

        }

        protected void publishProgress(Long... progress) {
            long val = progress[0];

            Intent intent = new Intent("timestatus");
            intent.putExtra("time", val);

            LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(
                    intent);
        }
    }
}