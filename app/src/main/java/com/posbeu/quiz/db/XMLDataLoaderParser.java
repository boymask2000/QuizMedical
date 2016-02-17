package com.posbeu.quiz.db;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.content.Context;
import android.os.AsyncTask;

import com.posbeu.quiz.db.bean.Domanda;
import com.posbeu.quiz.db.bean.Materia;
import com.posbeu.quiz.db.bean.Risposta;
import com.quiz.quizmedical.DataLoaderActivity;

public class XMLDataLoaderParser {


    private InputSource is;
	private Context ctx;

	public XMLDataLoaderParser(InputSource is, Context ctx) {
		this.is = is;
		this.ctx = ctx;

	}

	public void startParsing() throws Exception {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);

		Element element = doc.getDocumentElement();
		element.normalize();
		NodeList nList = doc.getElementsByTagName("materie");
		for (int i = 0; i < nList.getLength(); i++) {

			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("materie"))
					handleMaterie(node);
			}
		}
	}

	private void handleMaterie(Node n) {
		NodeList list = n.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("materia")) {
					handleMateria(node, 0);
				}
			}
		}

	}

	private void handleMateria(Node n, long padre) {
		String nome = getTagValue("nome", n);

		Materia m = new Materia();
		m.setDescrizione(nome);
		m.setId_padre(padre);
		long id = DBHelper.insertMateria(ctx, m);

		NodeList list = n.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("domanda")) {
					handleDomanda(node, id);
				}
				if (node.getNodeName().equals("materia")) {
					handleMateria(node, id);
				}
			}
		}

	}



	private void handleDomanda(Node n, long idd) {
		String testo = getTagValue("testo", n);
		int idd0 = getTagValueI("id", n);
		String immagine = getTagValue("immagine", n);
		int difficolta = getTagValueI("difficolta", n);
		String categoria = getTagValue("categoria", n);
		String argomento = getTagValue("argomento", n);
        String capitolo = getTagValue("capitolo", n);
        String hot = getTagValue("parolechiave", n);
		int punti = getTagValueI("punti", n);

		Domanda m = new Domanda();
        m.setCapitolo(capitolo);
		m.setDomanda_id(idd0);
		m.setMateria_id(idd);
		m.setTestoDomanda(testo);
		m.setImage(immagine);
		m.setDifficolta(difficolta);
		m.setCategoria(categoria);
		m.setArgomento(argomento);
        m.setHotWords(hot);
		m.setPunti(punti);
		long id = DBHelper.insertDomanda(ctx, m);

		NodeList list = n.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals("risposta")) {
					handleRisposta(node, idd0);
				}
			}
		}

	}

	private void handleRisposta(Node n, long id) {
		String testo = getTagValue("testo", n);
		String esatta = getTagValue("esatta", n);
		int ordine =getTagValueI("ordine", n);
		String spiegazione=getTagValue("spiegazione", n);

		Risposta m = new Risposta();
		m.setRisposta(testo);
		m.setOrdine(ordine);
		m.setSpiegazione(spiegazione);
		m.setDomanda_id(id);
		m.setEsatta(esatta.equals("S"));
		DBHelper.insertRisposta(ctx, m);

	}

	private String getTagValue(String nome, Node n) {

		NodeList list = n.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getNodeName().equals(nome)) {
					return getValue(node);
				}
			}
		}
		return "";
	}

	private int getTagValueI(String nome, Node n) {
		int val = 0;
		String s = getTagValue(nome, n);
		if (s != null) {
			try {
				val = Integer.parseInt(s);
			} catch (Exception e) {
			}
		}
		return val;
	}

	private String getValue(Node n) {

		NodeList list = n.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {

			Node node = list.item(i);
			// if (node.getNodeType() == Node.ELEMENT_NODE)
			{

				return node.getNodeValue();

			}
		}
		return "";
	}

}
