package com.posbeu.quiz.db.bean;

public class Risposta {
	private int ordine;
	private long risposta_id;
	private long domanda_id;
	private String risposta;
	private String spiegazione;
	private boolean esatta;

	public long getRisposta_id() {
		return risposta_id;
	}

	public void setRisposta_id(long risposta_id) {
		this.risposta_id = risposta_id;
	}

	public long getDomanda_id() {
		return domanda_id;
	}

	public void setDomanda_id(long domanda_id) {
		this.domanda_id = domanda_id;
	}

	public String getRisposta() {
		return risposta;
	}

	public void setRisposta(String risposta) {
		this.risposta = risposta;
	}

	public boolean isEsatta() {
		return esatta;
	}

	public void setEsatta(boolean esatta) {
		this.esatta = esatta;
	}

	public int getOrdine() {
		return ordine;
	}

	public void setOrdine(int ordine) {
		this.ordine = ordine;
	}

	public String getSpiegazione() {
		return spiegazione;
	}

	public void setSpiegazione(String spiegazione) {
		this.spiegazione = spiegazione;
	}

}
