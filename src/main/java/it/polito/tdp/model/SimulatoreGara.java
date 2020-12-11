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

public class SimulatoreGara {
	
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
	private Map <Integer,Integer> puntiDaAssegnare;
	private Map <Pilota,Integer> puntiGara;
	private Pilota pilotaveloce;
	private Duration giroveloce=Duration.ofDays(1);
	private Integer i;
	// STATO DEL SISTEMA
	// CODA DEGLI EVENTI
	// INIZIALIZZAZIONE
	public Map<Pilota,Integer> simula(Map<Integer,Pilota> pilotiMap,Gara gara,Map<Integer,Integer> infortuni,Map<Gara,StampaGara> stampaGare) {
		puntiDaAssegnare=new HashMap<>();
		puntiGara=new HashMap<>();
		Map<Integer,Long> distanze=new HashMap<>();
		sceltaPuntiGara();
		Map<Integer,Pilota> classificaGara=new HashMap<>(stampaGare.get(gara).getPosizioniIniziali());
		simulaPartenza(classificaGara,pilotiMap);
		for(Integer k=0;k<20;k++) {
			distanze.put(k, (long) 300);
		}
		for(Integer nGiro=1;nGiro<=gara.getNumerogiri();nGiro++) {
			simulagiro(classificaGara,pilotiMap,gara,nGiro,distanze);
		}
		stampaGare.get(gara).setClassificaFinale(classificaGara);
		stampaGare.get(gara).setPoleman(new PilotaTempo(pilotaveloce,giroveloce));
		calcolaPuntiGara(classificaGara);
		puntiGara.put(pilotaveloce, puntiGara.get(pilotaveloce)+1);
		return puntiGara;
	}


	private void simulagiro(Map<Integer, Pilota> classificaGara, Map<Integer, Pilota> pilotiMap, Gara gara, Integer nGiro,Map<Integer,Long> distanze) {
		Map<Pilota,Duration> tempiPilotiGiro=new HashMap<>();
		for(Integer i: classificaGara.keySet()) {
			if(i==0) {
					Duration tempogiro=simulotempo(gara,nGiro,classificaGara.get(i),pilotiMap);
					tempiPilotiGiro.put(classificaGara.get(i), tempogiro);
					if(giroveloce.compareTo(tempogiro)>0) {
						pilotaveloce=classificaGara.get(i);
						giroveloce=tempogiro;
					}
			}
			else if(i-1>=0) {
					Duration tempogiro=simulotempo(gara,nGiro,classificaGara.get(i),pilotiMap);
					tempiPilotiGiro.put(classificaGara.get(i), tempogiro);
					Long pilotaAvanti=tempiPilotiGiro.get(classificaGara.get(i-1)).toMillis();
					Long pilotaDietro=tempogiro.toMillis() ;
					Long distanzaTmp=pilotaDietro+distanze.get(i)-pilotaAvanti;
					if(distanzaTmp<500) {
					boolean sorpasso=calcolaSorpasso(classificaGara.get(i-1),classificaGara.get(i));
					if(sorpasso) {
						Pilota tmp1=classificaGara.get(i);
						Pilota tmp2=classificaGara.get(i-1);
						//System.err.println("pilota"+tmp1+"con tempo "+pilotaAvanti+"tenta di superare"+tmp2+" con tempo"+pilotaDietro+" la distanza prima era "+distanze.get(i));
						classificaGara.put(i-1,tmp1);
						classificaGara.put(i,tmp2);
						if(distanzaTmp<=0) {
							distanze.put(i,-distanzaTmp);
						}else {
							tempogiro=Duration.ofMillis(pilotaDietro-distanze.get(i));
							distanze.put(i, (long) 0);
						}
					}
					else {
						//System.out.println("sorpasso non riuscito");
						if(distanzaTmp<=300) {
							distanze.put(i, (long) 300);
							tempogiro=Duration.ofMillis(pilotaAvanti-distanze.get(i));
						}
						else {
							distanze.put(i,distanzaTmp);
						}
						//System.out.println(tempiPilotiGiro.get(classificaGara.get(i)).toMillis());
					}
					}
					else {
						distanze.put(i,distanze.get(i)+pilotaDietro-pilotaAvanti);
					}
					tempiPilotiGiro.put(classificaGara.get(i), tempogiro);
					if(giroveloce.compareTo(tempogiro)>0) {
						pilotaveloce=classificaGara.get(i);
						giroveloce=tempogiro;
					}
			}
		}
	}

	private boolean calcolaSorpasso(Pilota pilotaAvanti, Pilota pilotaDietro) {
		boolean risultato=false;
		Random r=new Random();
		Integer rangeMin = 0;
		Integer rangeMax = 35;
		Double result = (double) (r.nextInt(rangeMax - rangeMin)/100.00);
		Double drs=0.30;
		Double abpilota=(pilotaDietro.getPunteggio()-pilotaAvanti.getPunteggio())*0.0025;
		Double abmacchina=(pilotaDietro.getScuderia().getPunteggio()-pilotaAvanti.getScuderia().getPunteggio())*0.0010;
		result=result+drs+abpilota+abmacchina;
		if(result>=0.50) {
			risultato=true;
		}
		return risultato;
	}


	private void simulaPartenza(Map<Integer, Pilota> classificaGara, Map<Integer, Pilota> pilotiMap) {
		Map<Pilota,Integer> punteggiPartenza=new HashMap<>();
		Random r=new Random();
		for(Pilota p:pilotiMap.values()) {
			Integer rangeMin = 0;
			Integer rangeMax = 101;
			Integer result = r.nextInt(rangeMax - rangeMin) + rangeMin;
			Integer partenza=p.getPunteggio()/3+result*2/3;
			if(partenza<33) {
				punteggiPartenza.put(p, 1);
			}
			else if(partenza>33 && partenza<67) {
				punteggiPartenza.put(p, 2);
			}
			else {
				punteggiPartenza.put(p, 3);
			}
		}
		for(Integer i: classificaGara.keySet()) {
			if(i!=0) {
				for(int j=0;j<2;j++) {
				if(i-1>=0) {
					if((punteggiPartenza.get(classificaGara.get(i))-punteggiPartenza.get(classificaGara.get(i-1))-j)>0) {
						Pilota tmp1=classificaGara.get(i);
						Pilota tmp2=classificaGara.get(i-1);
						//System.out.println("il pilota  "+tmp1+" sorpassa "+tmp2);
						classificaGara.put(i-1,tmp1);
						classificaGara.put(i,tmp2);
						//System.out.println(classificaGara);
						punteggiPartenza.put(tmp2, punteggiPartenza.get(tmp2)+1);
					}
				}
			}
			}
		}
	}


	private Duration simulotempo(Gara gara,Integer nGiro,Pilota p,Map<Integer,Pilota> pilotiMap) {
		Duration tempogiro=Duration.ofMillis(0);
		Integer tmpgiro=(int) Math.ceil(nGiro / 5.0);
			if(gara.getPrestazioni().containsKey(p.getId())) {
			Prestazione tmp=gara.getPrestazioni().get(p.getId());
			if(tmp.getTempigiro().size()>=tmpgiro) {
				tempogiro=tmp.getTempigiro().get(tmpgiro-1);
			}
			else {
				tempogiro=calcolaTempo(p,gara,tmpgiro,pilotiMap);
			}
		}
			else {
				tempogiro=calcolaTempo(p,gara,tmpgiro,pilotiMap);
			}
			tempogiro=tempoProbabilita(tempogiro);
			return tempogiro;
	}

	private Duration calcolaTempo(Pilota p,Gara gara,int tmpgiro,Map<Integer,Pilota> mappaPiloti) {
		boolean trovato=false;
		Pilota pilotaConfronto=p;
		Long tempoConfronto=(long) 0;
		for(Integer i:mappaPiloti.keySet()) {
			if(gara.getPrestazioni().containsKey(mappaPiloti.get(i).getId())) {
			if(trovato==false && gara.getPrestazioni().get(mappaPiloti.get(i).getId()).getTempigiro().size()>=tmpgiro) {
				pilotaConfronto=mappaPiloti.get(i);
				tempoConfronto = gara.getPrestazioni().get(mappaPiloti.get(i).getId()).getTempigiro().get(tmpgiro-1).toMillis();
				trovato=true;
			}
		}
		}
      	Long ppilota=(long) (tempoConfronto*0.0007*(pilotaConfronto.getPunteggio()-p.getPunteggio()));
		Long pscuderia=(long) (tempoConfronto*0.0015*(pilotaConfronto.getScuderia().getPunteggio()-p.getScuderia().getPunteggio()));
		tempoConfronto=tempoConfronto+ppilota+pscuderia;
		return Duration.ofMillis(tempoConfronto);
	}

	private Duration tempoProbabilita(Duration t) {
		Random r = new Random();
		double probabilita=0.010;
		double rangeMin = (1-probabilita);
		double rangeMax = (1+probabilita);
		double result = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		long millis=t.toMillis();
		millis=(long) (millis*result);
		return Duration.ofMillis(millis);
	}
	
	private void sceltaPuntiGara() {
		for(int i=0;i<20;i++) {
			puntiDaAssegnare.put(i, 0);
		switch(i) {
		case 0:
			puntiDaAssegnare.put(i, 25);
			break;
		case 1:
			puntiDaAssegnare.put(i, 18);
			break;
		case 2:
			puntiDaAssegnare.put(i, 15);
			break;
		case 3:
			puntiDaAssegnare.put(i, 12);
			break;
		case 4:
			puntiDaAssegnare.put(i, 10);
			break;
		case 5:
			puntiDaAssegnare.put(i, 8);
			break;
		case 6:
			puntiDaAssegnare.put(i, 6);
			break;
		case 7:
			puntiDaAssegnare.put(i, 4);
			break;
		case 8:
			puntiDaAssegnare.put(i, 2);
			break;
		case 9:
			puntiDaAssegnare.put(i, 1);
			break;
		}
		}
	}
	
	private void calcolaPuntiGara(Map<Integer, Pilota> classificaGara) {
		for(Integer i: classificaGara.keySet()) {
			puntiGara.put(classificaGara.get(i), puntiDaAssegnare.get(i));
		}
	}
	

}

