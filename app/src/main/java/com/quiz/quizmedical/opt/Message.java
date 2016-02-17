package com.quiz.quizmedical.opt;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

/**
 * Created by giovanni on 1/7/15.
 */
public class Message {
    private Effectstype effects[] = {//
            Effectstype.Fadein,//
            Effectstype.Slideleft,//
            Effectstype.Slidetop, //
            Effectstype.SlideBottom, //
            Effectstype.Slideright,//
            Effectstype.Fall, //
            Effectstype.Newspager,//
            Effectstype.Fliph, //
            Effectstype.Flipv, //
            Effectstype.RotateBottom,//
            Effectstype.RotateLeft,//
            Effectstype.Slit, //
            Effectstype.Shake, //
            Effectstype.Sidefill};

    private static final String ROSSO = "#FFE74C3C";
    private static final String VERDE = "#00A86B";

    public static void infoDialog(Context ctx, String title, String msg) {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(ctx);

        dialogBuilder
                .withTitle(title)
                .withMessage(msg)
                .show();
    }

    public static void infoDialog(Context ctx, String title, String msg, int drawable, View.OnClickListener listener) {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(ctx);

        dialogBuilder
                .withTitle(title).withEffect(Effectstype.Fall)
                .withButton1Text("OK")
                .withMessage(msg)
                .withIcon(ctx.getResources().getDrawable(drawable))
                .setButton1Click(listener)
                .withDialogColor(VERDE)
                .show();
    }

    static NiftyDialogBuilder dialogBuilder;

    public static void infoDialog(Context ctx, String title, String msg, int drawable, final View.OnClickListener listener, DialogInterface.OnDismissListener dlistener) {

        View.OnClickListener kl = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialogBuilder.dismiss();

            }
        };
        dialogBuilder = NiftyDialogBuilder.getInstance(ctx);
        dialogBuilder.setOnDismissListener(dlistener);
        dialogBuilder
                .withTitle(title).withEffect(Effectstype.Fall)
                .withButton1Text("OK")
                .withMessage(msg).withDialogColor(VERDE)
                .withIcon(ctx.getResources().getDrawable(drawable))
                .setButton1Click(kl);
        dialogBuilder.show();

    }

    public static void errDialog(Context ctx, String title, String msg, int drawable, View.OnClickListener listener) {
        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(ctx);

        dialogBuilder
                .withTitle(title).withEffect(Effectstype.Fall)
                .withButton1Text("OK")
                .withMessage(msg)
                .withIcon(ctx.getResources().getDrawable(drawable))
                .setButton1Click(listener)
                .withDialogColor(ROSSO)
                .show();
    }
}
