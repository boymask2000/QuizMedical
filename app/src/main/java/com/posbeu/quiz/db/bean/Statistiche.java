package com.posbeu.quiz.db.bean;

public class Statistiche {
	private String data;
	private int numDomande = 0;
	private int numRisposteOK = 0;
	private int numRisposteKO = 0;
	private int risposteOK[] = new int[5];
	private int risposteKO[] = new int[5];
    private String livello;

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    public void incRispostaOK(int livello) {
		risposteOK[livello]++;
	}

	public void incRispostaKO(int livello) {
		risposteKO[livello]++;
	}

	public void setRispostaOK(int val, int livello) {
		risposteOK[livello] = val;
	}

	public void setRispostaKO(int val, int livello) {
		risposteKO[livello] = val;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getNumDomande() {
		return numDomande;
	}

	public void setNumDomande(int numDomande) {
		this.numDomande = numDomande;
	}

	public int getNumRisposteOK() {
		return numRisposteOK;
	}

	public void setNumRisposteOK(int numRisposteOK) {
		this.numRisposteOK = numRisposteOK;
	}

	public int getNumRisposteKO() {
		return numRisposteKO;
	}

	public void setNumRisposteKO(int numRisposteKO) {
		this.numRisposteKO = numRisposteKO;
	}

	public int[] getRisposteOK() {
		return risposteOK;
	}

	public void setRisposteOK(int[] risposteOK) {
		this.risposteOK = risposteOK;
	}

	public int[] getRisposteKO() {
		return risposteKO;
	}

	public void setRisposteKO(int[] risposteKO) {
		this.risposteKO = risposteKO;
	}

}
