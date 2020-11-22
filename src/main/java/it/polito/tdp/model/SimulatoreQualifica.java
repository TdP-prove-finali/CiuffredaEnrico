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
import java.util.Random;

import it.polito.tdp.db.F1DAO;
import it.polito.tdp.model.Evento.EventType;

public class SimulatoreQualifica {
	
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
	// OUTPUT DA CALCOLARE
	// STATO DEL SISTEMA
	// CODA DEGLI EVENTI
	// INIZIALIZZAZIONE
	public Map<Integer,Pilota> simula(Map<Integer,Pilota> pilotiMap,Gara gara,Map<Integer,Integer> infortuni) {
		Map<Integer,Pilota> posizioniIniziali=new HashMap<>();
		List<Pilota> pilotiInGara=new ArrayList<>(pilotiMap.values());
		for(int i=1;i<4;i++) {
		simulaQualifica(pilotiInGara,gara,i,posizioniIniziali,pilotiMap);
		}
		return posizioniIniziali;
	}
	
	private void simulaQualifica(List<Pilota> pilotiInGara,Gara gara,Integer i,Map<Integer,Pilota> posizioniIniziali,Map<Integer,Pilota> pilotiMap) {
		List<PilotaTempo> tempiPiloti=new ArrayList<>();
		for(Pilota p: pilotiInGara) {
			Duration d=Duration.ofMillis(0);
			if(gara.getPrestazioni().containsKey(p.getId())) {
			Prestazione tmp=gara.getPrestazioni().get(p.getId());
			switch (i) {
			case 1:
				if(tmp.getQ1().equals(Duration.ofMillis(0))) {
					d=calcolaTempo(p,gara,1,pilotiMap);	
				}
				else {
				d=tmp.getQ1();
				}
				break;
			case 2:
				if(tmp.getQ2().equals(Duration.ofMillis(0))) {
					d=calcolaTempo(p,gara,2,pilotiMap);	
				}
				else {
				d=tmp.getQ2();
				}
				break;
			case 3:
				if(tmp.getQ3().equals(Duration.ofMillis(0))) {
					d=calcolaTempo(p,gara,3,pilotiMap);
				}
				else {
				d=tmp.getQ2();
				}
			}
			}
			//se il pilota non ha mai gareggiato su questa pista
			else {
				d=calcolaTempo(p,gara,i,pilotiMap);
				//System.out.println(d);
				//System.out.println(p);
			}
			d=tempoProbabilita(d);
			tempiPiloti.add(new PilotaTempo(p,d));
		}
		Collections.sort(tempiPiloti);
		for(int j=0;j<5;j++) {
			posizioniIniziali.put(pilotiInGara.size()-1,tempiPiloti.get(pilotiInGara.size()-1).getPilota());
			pilotiInGara.remove(tempiPiloti.get(pilotiInGara.size()-1).getPilota());
		}
		if(i==3) {
			for(int j=0;j<5;j++) {
				posizioniIniziali.put(pilotiInGara.size()-1,tempiPiloti.get(pilotiInGara.size()-1).getPilota());
				pilotiInGara.remove(tempiPiloti.get(pilotiInGara.size()-1).getPilota());
			}
			}
		//System.out.println(tempiPiloti);
	}

	private Duration calcolaTempo(Pilota p,Gara g,int i,Map<Integer,Pilota> mappaPiloti) {
		Long tempohamilton=null;
		switch(i) {
		case 1:
			tempohamilton=g.getPrestazioni().get(1).getQ1().toMillis();
			break;
		case 2:
			tempohamilton=g.getPrestazioni().get(1).getQ2().toMillis();
			break;
		case 3:
			tempohamilton=g.getPrestazioni().get(1).getQ3().toMillis();
			break;
		}
		Long ppilota=(long) (tempohamilton*0.10/100*(mappaPiloti.get(1).getPunteggio()-p.getPunteggio()));
		Long pscuderia=(long) (tempohamilton*0.30/100*(mappaPiloti.get(1).getScuderia().getPunteggio()-p.getScuderia().getPunteggio()));
		tempohamilton=tempohamilton+ppilota+pscuderia;
		return Duration.ofMillis(tempohamilton);
	}

	private Duration tempoProbabilita(Duration t) {
		Random r = new Random();
		double probabilita=0.0010;
		double rangeMin = (1-probabilita);
		double rangeMax = (1+probabilita);
		double result = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		long millis=t.toMillis();
		millis=(long) (millis*result);
		return Duration.ofMillis(millis);
	}

	

}
