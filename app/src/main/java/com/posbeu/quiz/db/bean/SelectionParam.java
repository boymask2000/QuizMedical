package com.posbeu.quiz.db.bean;

public class SelectionParam {
    private Materia materia;
    private long livello;
    private int tempo;


    private String parolaChiave;

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public long getLivello() {
        return livello;
    }

    public void setLivello(long livello) {
        this.livello = livello;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public String getParolaChiave() {
        return parolaChiave;
    }

    public void setParolaChiave(String parolaChiave) {
        this.parolaChiave = parolaChiave;
    }
}
