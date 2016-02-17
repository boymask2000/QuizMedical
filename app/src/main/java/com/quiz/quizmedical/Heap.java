package com.quiz.quizmedical;

import com.posbeu.quiz.db.bean.Domanda;
import com.posbeu.quiz.db.bean.Materia;

import java.util.List;

public class Heap {
    private static long maxTime = 2;
    private static int domanda_corrente = 0;
    private static List<Domanda> listaDomande = null;
    private static Materia materia;
    private static boolean scaduto = false;

    public static boolean isFineLista() {
        return domanda_corrente == listaDomande.size() - 1;
    }

    public static List<Domanda> getListaDomande() {
        return listaDomande;
    }

    public static void setListaDomande(List<Domanda> listaDomande) {
        Heap.listaDomande = listaDomande;
    }

    public static long getMaxTime() {
        return maxTime;
    }

    public static void setMaxTime(long maxTime) {
        Heap.maxTime = maxTime;
    }

    public static void setMateria(Materia materia) {
        Heap.materia = materia;

    }

    public static Materia getMateria() {
        return materia;
    }

    public static int getDomanda_corrente() {
        return domanda_corrente;
    }

    public static void setDomanda_corrente(int domanda_corrente) {
        Heap.domanda_corrente = domanda_corrente;
    }

    public static boolean isScaduto() {
        return scaduto;
    }

    public static void setScaduto(boolean scaduto) {
        Heap.scaduto = scaduto;
    }


}
