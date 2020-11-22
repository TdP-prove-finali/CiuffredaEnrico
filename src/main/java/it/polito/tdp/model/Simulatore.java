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
	private F1DAO dao=new F1DAO();
	private Map <Integer,Pilota> pilotiMap=new HashMap<>();
	private Map <Integer,Circuito> circuitiMap=new HashMap<>();
	private Map <Integer,Scuderia> scuderieMap=new HashMap<>();
	private Map<String, Gara> gareMap=new HashMap<>();
	// OUTPUT DA CALCOLARE
	private Map<Pilota,Integer> classificaPiloti;
	private Map<Scuderia,Integer> classificaScuderie;
	// STATO DEL SISTEMA
	private Map<Integer,Integer> infortuni;
	private Map<Integer,Pilota> PosizioniInizialiTmp;
	// CODA DEGLI EVENTI
	private PriorityQueue<Evento> queue;

	// INIZIALIZZAZIONE
	public void init() {
		//inizializzo 
		this.queue = new PriorityQueue<Evento>();
		this.infortuni=new HashMap<>();
		this.circuitiMap=dao.loadAllCircuiti();
		this.scuderieMap=dao.loadAllScuderie();
		this.pilotiMap=dao.loadAllPiloti(scuderieMap);
		this.gareMap=dao.loadAllGara(circuitiMap,21);
		this.dao.loadAllQ1(gareMap);
		this.dao.loadAllQ2(gareMap);
		this.dao.loadAllQ3(gareMap);
		this.dao.loadAllGiri(gareMap);
		this.punteggioscuderia();
		this.classificaPiloti=new HashMap<>();
		this.classificaScuderie=new HashMap<>();
		// generiamo eventi iniziali
		for(Gara g: gareMap.values()) {
			Evento tmpe=new Evento(g,g.getData().minusDays(1),EventType.QUALIFICA);
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
			System.out.println(e);
			processEvent(e);
		}
		List<StampaPilota> ClassificaPilotiFinale=new ArrayList<>();
		List<StampaScuderia> ClassificaScuderieFinale=new ArrayList<>();
		for(Pilota p:classificaPiloti.keySet()) {
			ClassificaPilotiFinale.add(new StampaPilota(p,classificaPiloti.get(p)));
		}
		for(Scuderia s:classificaScuderie.keySet()) {
			ClassificaScuderieFinale.add(new StampaScuderia(s,classificaScuderie.get(s)));
		}
		Collections.sort(ClassificaPilotiFinale);
		Collections.sort(ClassificaScuderieFinale);
		System.out.println(ClassificaPilotiFinale);
		System.out.println(ClassificaScuderieFinale);
	}
	

	private void processEvent(Evento e) {
		switch (e.getType()) {
		case QUALIFICA:
			SimulatoreQualifica simq=new SimulatoreQualifica();
			PosizioniInizialiTmp=new HashMap<>(simq.simula(pilotiMap, e.getGara(), infortuni));
			//System.out.println(PosizioniInizialiTmp);
			break;
		case GARA:
			SimulatoreGara simg=new SimulatoreGara();
			Map<Pilota,Integer> tmpPunteggi=new HashMap<>(simg.simula(pilotiMap, e.getGara(), infortuni, PosizioniInizialiTmp));
			AggiungiPunteggiGara(tmpPunteggi);
			break;
		}
	}
	
	private void punteggioscuderia() {
		for(Pilota p: pilotiMap.values()) {
			p.getScuderia().setPunteggio(p.getScuderia().getPunteggio()+p.getPunteggio());
		}
		for(Scuderia s: scuderieMap.values()) {
			s.setPunteggio(s.getPunteggio()/2);
		}
	}
	
	
	private void AggiungiPunteggiGara(Map<Pilota, Integer> tmpPunteggi) {
		for(Pilota p: tmpPunteggi.keySet()) {
			if(classificaPiloti.containsKey(p)) {
			//System.out.println(classificaPiloti.get(p)+" "+tmpPunteggi.get(p));
			classificaPiloti.put(p, classificaPiloti.get(p)+tmpPunteggi.get(p));
			classificaScuderie.put(p.getScuderia(), classificaScuderie.get(p.getScuderia())+tmpPunteggi.get(p));
			//System.out.println("altre volte "+classificaPiloti);
			//System.out.println("altre volte "+classificaScuderie);
			}
			else {
				classificaPiloti.put(p, tmpPunteggi.get(p));
				if(!classificaScuderie.containsKey(p.getScuderia())) {
				classificaScuderie.put(p.getScuderia(), tmpPunteggi.get(p));
				}
				else {
					classificaScuderie.put(p.getScuderia(), tmpPunteggi.get(p)+classificaScuderie.get(p.getScuderia()));
				}
				//System.out.println("prima volta "+classificaPiloti);
				//System.out.println("prima volta "+classificaScuderie);
			}
		}
		//System.out.println(classificaPiloti);
		//System.out.println(classificaScuderie);
	}

	
	

}
