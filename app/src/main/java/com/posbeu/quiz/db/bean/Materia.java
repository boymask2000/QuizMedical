package com.posbeu.quiz.db.bean;

public class Materia {


    private long materia_id;
	private String descrizione;
	private String codice;
	private long id_padre=0;
    private boolean all=false;
	
	public long getMateria_id() {
		return materia_id;
	}
	public void setMateria_id(long materia_id) {
		this.materia_id = materia_id;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	@Override
	public String toString() {
		return descrizione;
	}
	public long getId_padre() {
		return id_padre;
	}
	public void setId_padre(long id_padre) {
		this.id_padre = id_padre;
	}   public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
        if( all)descrizione="Tutte";
    }
}
