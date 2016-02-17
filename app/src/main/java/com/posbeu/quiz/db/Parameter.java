package com.posbeu.quiz.db;

import android.content.Context;

import com.posbeu.quiz.db.bean.Parametro;

import static com.posbeu.quiz.db.DBHelper.getParametro;

/**
 * Created by giovanni on 1/5/15.
 */
public class Parameter {
    public static final String CURRENT_LEVEL = "CURRENT_LEVEL";
    public static final String SEQUENCE = "SEQUENCE";
    public static final String LEVEL_0_PROMOTION = "LEVEL_0_PROMOTION";
    public static final String LEVEL_1_PROMOTION = "LEVEL_1_PROMOTION";
    public static final String LEVEL_2_PROMOTION = "LEVEL_2_PROMOTION";
    public static final String LEVEL_3_PROMOTION = "LEVEL_3_PROMOTION";
    public static final String LEVEL_4_PROMOTION = "LEVEL_4_PROMOTION";

    public static String getCurrentLevel(Context ctx) {
        String v = getParametro(ctx, CURRENT_LEVEL);
        if (v == null || v.trim().length() == 0) v = "1";
        return v;
    }

    public static void incLivello(Context ctx) {
        String v = getParametro(ctx, CURRENT_LEVEL);
        int vv = 0;
        try {
            vv = Integer.parseInt(v);
        } catch (Exception e) {
        }
        vv++;
        DBHelper.updateParametro(ctx, CURRENT_LEVEL, "" + vv);

    }
    public static void setLivello(Context ctx, String vv) {

        DBHelper.updateParametro(ctx, CURRENT_LEVEL,  vv);

    }

    public static long getSogliaPromozione(Context ctx, String livello) {
        String par = "LEVEL_" + livello + "_PROMOTION";
        String v = getParametro(ctx, par);
        return Long.parseLong(v);
    }

    public static int incSequence(Context ctx) {
        String v = getParametro(ctx, SEQUENCE);
        int vv = 0;
        try {
            vv = Integer.parseInt(v);
        } catch (Exception e) {
        }
        vv++;
        int r = DBHelper.updateParametro(ctx, SEQUENCE, "" + vv);
        if (r == 0) {
            Parametro par = new Parametro();
            par.setParm(Parameter.SEQUENCE);
            par.setValue(""+vv);
            DBHelper.insertParametro(ctx, par);
        }
        return vv;

    }

    public static void resetSequence(Context ctx) {
        DBHelper.updateParametro(ctx, SEQUENCE, "0");
    }

    public static void reset(Context ctx) {
        DBHelper.updateParametro(ctx, CURRENT_LEVEL, "0");
    }
}
