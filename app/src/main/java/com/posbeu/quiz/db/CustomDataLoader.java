package com.posbeu.quiz.db;

import android.content.Context;

import com.posbeu.quiz.db.bean.Domanda;
import com.posbeu.quiz.db.bean.Materia;
import com.posbeu.quiz.db.bean.Risposta;

public class CustomDataLoader {
	public static void load(Context context) {
		Materia m1 = new Materia();
		m1.setCodice("A001");
		m1.setDescrizione("Chimica");
		long id1 = DBHelper.insertMateria(context, m1);
		insertDomandeQuiz1(context, id1);
		insertDomandeQuiz2(context, id1);
		insertDomandeQuiz3(context, id1);
		insertDomandeQuiz4(context, id1);
		m1.setCodice("A002");
		m1.setDescrizione("Fisica");
		long id2 = DBHelper.insertMateria(context, m1);
	}

	public static void insertMateriaTree(Context context) {
		Materia m1 = new Materia();
		m1.setCodice("A001");
		m1.setDescrizione("Area Clinica");
		m1.setId_padre(0);
		long id1 = DBHelper.insertMateria(context, m1);
		addMateria(context, "Chirurgia Generale", id1);
		addMateria(context, "Chirurgia Specialistica", id1);
		addMateria(context, "Medicina Interna", id1);
		addMateria(context, "Medicina Legale", id1);
		addMateria(context, "Medicina Specialistica", id1);
		long l = addMateria(context, "Pediatria", id1);
	
		m1.setDescrizione("Chirurgia Generale");
		m1.setId_padre(id1);
		long id2 = DBHelper.insertMateria(context, m1);
		m1 = new Materia();
		m1.setCodice("A001");
		m1.setDescrizione("Chimica3");
		m1.setId_padre(id2);
		long id3 = DBHelper.insertMateria(context, m1);
		
		
	}

	private static long addMateria(Context context, String s, long id) {
		Materia m1 = new Materia();
		m1.setCodice("A001");
		m1.setDescrizione(s);
		m1.setId_padre(id);
		return DBHelper.insertMateria(context, m1);
	}

	

	private static void insertDomandeQuiz1(Context context, long id) {
		Domanda d = new Domanda();
		d.setMateria_id(id);
		d.setDifficolta(1);
		d.setTestoDomanda("Tutte le seguenti definizioni rappresentano fattori prognostici favorevolinella leucemia linfoblastica in età pediatrica, tranne:");
		long idq = DBHelper.insertDomanda(context, d);
		insertRisposte(context, idq, "A età tra i 3 e i 7 anni", 1, true);
		insertRisposte(context, idq, "B sesso maschile", 2);
		insertRisposte(context, idq,
				"C una conta dei globuli bianchi iniziali < 10.000/mm3", 3);
		insertRisposte(context, idq, "D un valore di emoglobina < 7 g/dl", 4);
		insertRisposte(context, idq,
				"E una conta delle piastrine > 100.000/mm3", 5);
	}

	private static void insertDomandeQuiz2(Context context, long id) {
		Domanda d = new Domanda();
		d.setMateria_id(id);
		d.setDifficolta(1);
		d.setTestoDomanda("Nel sospetto di un'invaginazipone intestinale del bambino qual è , tra i seguenti, l'esame diagnostico dirimente?");
		long idq = DBHelper.insertDomanda(context, d);
		insertRisposte(context, idq, "A Clisma opaco", 1);
		insertRisposte(context, idq, "B Rx diretta addome", 2);
		insertRisposte(context, idq, "C Rx digerente con mezzo di contrasto", 3);
		insertRisposte(context, idq, "D Rx stratigrafia", 4);
		insertRisposte(context, idq, "E Invertogramma", 5);
	}
	private static void insertDomandeQuiz3(Context context, long id) {
		Domanda d = new Domanda();
		d.setMateria_id(id);
		d.setDifficolta(1);
		d.setTestoDomanda("Quale eì il sintomo o segno di esordio più frequente di un craniofaringioma in un bambino?");
		long idq = DBHelper.insertDomanda(context, d);
		insertRisposte(context, idq, "A Strabismo", 1);
		insertRisposte(context, idq, "B Cefalee", 2);
		insertRisposte(context, idq, "C Arresto della crescita", 3);
		insertRisposte(context, idq, "D Disturbi di vista", 4);
		insertRisposte(context, idq, "E Vertugini", 5);
	}
	private static void insertDomandeQuiz4(Context context, long id) {
		Domanda d = new Domanda();
		d.setMateria_id(id);
		d.setDifficolta(1);
		d.setTestoDomanda("Un bambino di due anni viene portato in pronto soccorso dopo che la mamma ha notato il passaggio di feci con sangue rosso. Il bambino non lamenta dolori addominali, non ha febbre né vomito . L'anamnesi familiare è positiva per la presenza di cancro del colon in alcuni zii paterni. Al momento del ricovero l'ematrocrito è 26%, Quale delle seguenti è la diagnosi più probabile?");
		long idq = DBHelper.insertDomanda(context, d);
		insertRisposte(context, idq, "C Colite ulcerosa", 1);
		insertRisposte(context, idq, "D Iperplasia linfonodulare", 2);
		insertRisposte(context, idq, "E Diverticolo di Meckel", 3);
		
	}

	private static void insertRisposte(Context context, long idq, String s,
			int ord) {
		Risposta r1 = new Risposta();
		r1.setOrdine(ord);
		r1.setDomanda_id(idq);
		r1.setRisposta(s);
		long idz = DBHelper.insertRisposta(context, r1);

	}
	private static void insertRisposte(Context context, long idq, String s,
			int ord, boolean esatta) {
		Risposta r1 = new Risposta();
		r1.setOrdine(ord);
		r1.setDomanda_id(idq);
		r1.setRisposta(s);
		r1.setEsatta(esatta);
		long idz = DBHelper.insertRisposta(context, r1);

	}
}
