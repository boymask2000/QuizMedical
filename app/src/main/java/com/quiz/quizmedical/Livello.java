package com.quiz.quizmedical;

/**
 * Created by giovanni on 1/9/15.
 */
public class Livello {
    public static final String BASE = "Base";
    public static final String MEDIO = "Medio";
    public static final String ALTO = "Alto";
    public static final String ALTISSIMO = "Altissimo";

    private static String livelli[] = {BASE, MEDIO, ALTO, ALTISSIMO};

    public static String decodeLivello(String slivello) {
        int livello = 0;
        try {
            livello = Integer.parseInt(slivello);
        } catch (Exception e) {
        }
        switch (livello) {
            case 1:
                return BASE;
            case 2:
                return MEDIO;
            case 3:
                return ALTO;
            case 4:
                return ALTISSIMO;
        }
        return "Base";
    }

    public static long undecodeLivello(String code) {
        for (int i = 0; i < livelli.length; i++)
            if (livelli[i].equals(code)) return i + 1;
        return 0;
    }

    public static int compare(String l1, String l2) {
        return l1.compareTo(l2);
    }


    public static String nextLevelCode(String slivello) {
        int livello = 0;
        try {
            livello = Integer.parseInt(slivello);
        } catch (Exception e) {
        }
        if (livello < 4) livello++;
        return "" + livello;
    }

    public static String precLevelCode(String slivello) {
        int livello = 1;
        try {
            livello = Integer.parseInt(slivello);
        } catch (Exception e) {
        }
        if (livello > 1) livello--;
        return "" + livello;
    }

    public static int getLivelloCode(String codelivello) {
        return Integer.parseInt(codelivello);
    }
}
