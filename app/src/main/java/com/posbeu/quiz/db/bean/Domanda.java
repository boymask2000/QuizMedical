package com.posbeu.quiz.db.bean;

import com.quiz.quizmedical.Heap;

import java.util.List;

public class Domanda {
    private boolean selected = false;


    private String capitolo = null;
    private long punti = 0;
    private long domanda_id = 0;
    private long materia_id = 0;
    private String testoDomanda = "";
    private long difficolta = 0;
    private String image = "";
    private long ordine = 0;
    private String spiegazione = "";
    private String categoria = "";
    private String argomento = "";
    private boolean haRisposto;
    private boolean rispostaEsatta = false;

    private String hotWords = null;
    private List<Risposta> risposte = null;

    public void setHotWords(String hw) {
        hotWords = hw;
    }

    public String getHotWords() {
        return hotWords;
    }


    public long getDomanda_id() {
        return domanda_id;
    }

    public void setDomanda_id(long domanda_id) {
        this.domanda_id = domanda_id;
    }

    public String getTestoDomanda() {
        return testoDomanda;
    }

    public void setTestoDomanda(String testoDomanda) {
        this.testoDomanda = testoDomanda;
    }

    public long getDifficolta() {
        return difficolta;
    }

    public void setDifficolta(long difficolta) {
        this.difficolta = difficolta;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpiegazione() {
        return spiegazione;
    }

    public void setSpiegazione(String spiegazione) {
        this.spiegazione = spiegazione;
    }

    public long getPunti() {
        return punti;
    }

    public void setPunti(long punti) {
        this.punti = punti;
    }

    @Override
    public String toString() {

        return "Domanda " + domanda_id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getArgomento() {
        return argomento;
    }

    public void setArgomento(String argomento) {
        this.argomento = argomento;
    }

    public long getOrdine() {
        return ordine;
    }

    public void setOrdine(long ordine) {
        this.ordine = ordine;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isRispostaEsatta() {
        return rispostaEsatta;
    }

    public void setRispostaEsatta(boolean rispostaEsatta) {
        this.rispostaEsatta = rispostaEsatta;
    }

    public List<Risposta> getRisposte() {
        return risposte;
    }

    public void setRisposte(List<Risposta> risposte) {
        this.risposte = risposte;
    }

    public long getMateria_id() {
        return materia_id;
    }

    public void setMateria_id(long materia_id) {
        this.materia_id = materia_id;
    }

    public boolean haRisposto() {
        return haRisposto;
    }

    public void setHaRisposto(boolean haRisposto) {
        this.haRisposto = haRisposto;
    }

    public static int conta(boolean ok) {
        int sum = 0;
        for (int i = 0; i < Heap.getListaDomande().size(); i++) {
            Domanda d = Heap.getListaDomande().get(i);
            if (d.haRisposto())
                sum += (d.isRispostaEsatta() == ok) ? 1 : 0;
        }
        return sum;
    }

    public static int conta(boolean ok, int livello) {
        int sum = 0;
        for (int i = 0; i < Heap.getListaDomande().size(); i++) {
            Domanda d = Heap.getListaDomande().get(i);
            if (d.haRisposto() && d.getDifficolta() == livello)
                sum += (d.isRispostaEsatta() == ok) ? 1 : 0;
        }
        return sum;
    }

    public String getCapitolo() {
        return capitolo;
    }

    public void setCapitolo(String capitolo) {
        this.capitolo = capitolo;
    }
}
