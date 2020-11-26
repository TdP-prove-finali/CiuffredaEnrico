package it.polito.tdp.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import it.polito.tdp.db.F1DAO;
import it.polito.tdp.model.Evento.EventType;

public class Simulatore {
	
	// PARAMETRI DI SIMULAZIONE
	/*private int pqualifica = 2; 
	private double pgarasospesa = 0.0001; 
	private int pscorrettezza=3;
	private int pincidentedebole=3;
	private int pincidentepesante=4;
	private int ppioggia=4;
	private int ppioggiaincidenti=3;
	private int numerogare=24;
	private int pcasualesorpasso=30; 
	private int pdrs=30;
	private int pabpilota=30;
	private int pabmacchina=10;
	private int nsorpassi=1;
	private double ssorpasso;*/
	private Map <Integer,Pilota> pilotiMap=new HashMap<>();
	private Map<String, Gara> gareMap=new HashMap<>();
	// OUTPUT DA CALCOLARE
	private Map<Pilota,Integer> classificaPiloti;
	private Map<Scuderia,Integer> classificaScuderie;
	private Map<Gara,StampaGara> stampaGare;
	// STATO DEL SISTEMA
	private Map<Integer,Integer> infortuni;
	private Map<Integer,Pilota> PosizioniInizialiTmp;
	// CODA DEGLI EVENTI
	private PriorityQueue<Evento> queue;

	// INIZIALIZZAZIONE
	public void init(Map<Integer,Pilota> pilotiMap,Map<String, Gara> gareMap) {
		//inizializzo 
		this.queue = new PriorityQueue<Evento>();
		this.infortuni=new HashMap<>();
		this.pilotiMap=pilotiMap;
		this.gareMap=gareMap;
		this.classificaPiloti=new HashMap<>();
		this.classificaScuderie=new HashMap<>();
		this.stampaGare=new HashMap<>();
		// generiamo eventi iniziali
		for(Gara g: this.gareMap.values()) {
			Evento tmpe=new Evento(g,g.getData().minusDays(1),EventType.QUALIFICA);
			stampaGare.put(g, new StampaGara());
			Evento tmpeg=new Evento(g,g.getData(),EventType.GARA);
			queue.add(tmpe);
			queue.add(tmpeg);
		}
	}

	// ESECUZIONE
	public void run() {
		//System.out.println(queue);
		while (!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			//System.out.println(e);
			processEvent(e);
		}
	}
	

	private void processEvent(Evento e) {
		switch (e.getType()) {
		case QUALIFICA:
			SimulatoreQualifica simq=new SimulatoreQualifica();
			PosizioniInizialiTmp=new HashMap<>(simq.simula(pilotiMap, e.getGara(), infortuni));
			stampaGare.get(e.getGara()).setPosizioniIniziali(PosizioniInizialiTmp);
			break;
		case GARA:
			SimulatoreGara simg=new SimulatoreGara();
			Map<Pilota,Integer> tmpPunteggi=new HashMap<>(simg.simula(pilotiMap, e.getGara(), infortuni, stampaGare));
			//System.out.println(tmpPunteggi);
			AggiungiPunteggiGara(tmpPunteggi);
			break;
		}
	}
		
	private void AggiungiPunteggiGara(Map<Pilota, Integer> tmpPunteggi) {
		for(Pilota p: tmpPunteggi.keySet()) {
			if(classificaPiloti.containsKey(p)) {
			classificaPiloti.put(p, classificaPiloti.get(p)+tmpPunteggi.get(p));
			classificaScuderie.put(p.getScuderia(), classificaScuderie.get(p.getScuderia())+tmpPunteggi.get(p));
			}
			else {
				classificaPiloti.put(p, tmpPunteggi.get(p));
				if(!classificaScuderie.containsKey(p.getScuderia())) {
				classificaScuderie.put(p.getScuderia(), tmpPunteggi.get(p));
				}
				else {
					classificaScuderie.put(p.getScuderia(), tmpPunteggi.get(p)+classificaScuderie.get(p.getScuderia()));
				}
			}
		}
	}

	public Map<Pilota, Integer> getClassificaPiloti() {
		return classificaPiloti;
	}

	public void setClassificaPiloti(Map<Pilota, Integer> classificaPiloti) {
		this.classificaPiloti = classificaPiloti;
	}

	public Map<Scuderia, Integer> getClassificaScuderie() {
		return classificaScuderie;
	}

	public void setClassificaScuderie(Map<Scuderia, Integer> classificaScuderie) {
		this.classificaScuderie = classificaScuderie;
	}

	public Map<Gara, StampaGara> getStampaGare() {
		return stampaGare;
	}

	public void setStampaGare(Map<Gara, StampaGara> stampaGare) {
		this.stampaGare = stampaGare;
	}
	
	
		

}
