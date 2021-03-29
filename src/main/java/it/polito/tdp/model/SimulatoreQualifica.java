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
	final static double PPILOTA=0.00010;
	final static double PSCUDERIA=0.00020;
	final static double PTEMPI=0.0010;
	private double effettopioggia= 1.00;
	private Integer numeroincidenti=0;
	private List<Pilota> infortunitmp=new ArrayList<>();

	
	public Map<Integer,Pilota> simula(Map<Integer,Pilota> pilotiMap,Gara gara,Map<Pilota,Integer> infortuni,String pioggia) {
		Map<Integer,Pilota> posizioniIniziali=new HashMap<>();
		List<Pilota> pilotiInGara=new ArrayList<>(pilotiMap.values());
		verificaPioggia(pioggia);
		//SOSTITUISCO GLI INFORTUNATI
		SostituzioneInfortunati(pilotiInGara, infortuni);
		for(int i=1;i<4;i++) {
		simulaQualifica(pilotiInGara,gara,i,posizioniIniziali,pilotiMap,infortuni);
		}
		AggiornaClassifica(posizioniIniziali);
		return posizioniIniziali;
	}
	

	private void simulaQualifica(List<Pilota> pilotiInGara,Gara gara,Integer i,Map<Integer,Pilota> posizioniIniziali,Map<Integer,Pilota> pilotiMap,Map<Pilota,Integer> infortuni) {
		List<PilotaTempo> tempiPiloti=new ArrayList<>();
		//System.out.println("numero piloti che partono per la qualifica"+pilotiInGara.size());
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
			}
			d=tempoProbabilita(d);
			Incidente(p,d,infortuni);
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
	}

	private Duration calcolaTempo(Pilota p,Gara g,int i,Map<Integer,Pilota> mappaPiloti) {
		boolean trovato=false;
		Pilota pilotaConfronto=p;
		Long tempoConfronto=(long) 0;
		for(Integer id:mappaPiloti.keySet()) {
			switch(i) {
			case 1:
				if(trovato==false && !(g.getPrestazioni().get(mappaPiloti.get(id).getId()).getQ1()==null)) {
			     	pilotaConfronto=mappaPiloti.get(id);
					tempoConfronto = g.getPrestazioni().get(id).getQ1().toMillis();
		     		trovato=true;
				}
				break;
			case 2:
				if(trovato==false && !(g.getPrestazioni().get(mappaPiloti.get(id).getId()).getQ2()==null)) {
			     	pilotaConfronto=mappaPiloti.get(id);
					tempoConfronto = g.getPrestazioni().get(id).getQ2().toMillis();
		     		trovato=true;
				}
				break;
			case 3:
				if(trovato==false && !(g.getPrestazioni().get(mappaPiloti.get(id).getId()).getQ3()==null)) {
			     	pilotaConfronto=mappaPiloti.get(id);
					tempoConfronto = g.getPrestazioni().get(id).getQ3().toMillis();
		     		trovato=true;
				}
				break;
			}
		}
      	Long ppilota=(long) (tempoConfronto*PPILOTA*(pilotaConfronto.getPunteggio()-p.getPunteggio()));
		Long pscuderia=(long) (tempoConfronto*PSCUDERIA*(pilotaConfronto.getScuderia().getPunteggio()-p.getScuderia().getPunteggio()));
		tempoConfronto=(long)((tempoConfronto+ppilota+pscuderia)*effettopioggia);
		return Duration.ofMillis(tempoConfronto);
	}

	private Duration tempoProbabilita(Duration t) {
		Random r = new Random();
		double rangeMin = (1-PTEMPI);
		double rangeMax = (1+PTEMPI);
		double result = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		long millis=t.toMillis();
		millis=(long) (millis*result*effettopioggia);
		return Duration.ofMillis(millis);
	}

	private void verificaPioggia(String pioggia) {
		if(pioggia.equals("SI")) {
			Random r = new Random();
			double probp=r.nextFloat();
			if(probp<=0.10) {
				effettopioggia=1.25;
			}
		}
	}
	
	private void SostituzioneInfortunati(List<Pilota> pilotiInGara, Map<Pilota, Integer> infortuni) {
		for(Pilota p:infortuni.keySet()) {
			pilotiInGara.remove(p);
			pilotiInGara.add(new Pilota(p));
			infortuni.put(p, infortuni.get(p)-1);
			if(infortuni.get(p)==-1) {
				pilotiInGara.remove(p);
				pilotiInGara.add(p);
			}
		}
	}
	

	private void AggiornaClassifica(Map<Integer,Pilota> posizioniIniziali) {
		Map<Integer,Pilota> classificaDopoIncidente=new HashMap<>();
		for(Pilota p:infortunitmp) {
			boolean trovato=false;
		for(Integer n: posizioniIniziali.keySet()) {
			if(trovato==false) {
				if(p.equals(posizioniIniziali.get(n))) {
					trovato=true;
					classificaDopoIncidente.put(19-numeroincidenti,new Pilota(p));
					numeroincidenti++;
				}
				else {
				classificaDopoIncidente.put(n, posizioniIniziali.get(n));
				}
			}
			else if(trovato==true && n>=19-numeroincidenti+2) {
				classificaDopoIncidente.put(n, posizioniIniziali.get(n));
			}
			else if(trovato==true) {
				classificaDopoIncidente.put(n-1, posizioniIniziali.get(n));
			}
		}
		System.out.println(classificaDopoIncidente);
		posizioniIniziali.clear();
		posizioniIniziali.putAll(classificaDopoIncidente);
		}
	}
	
	private void Incidente(Pilota p, Duration d,Map<Pilota,Integer> infortuni) {
		Random r = new Random();
		double probp=r.nextFloat();
		probp=probp*effettopioggia;
		if(probp<=0.0002) {
			d=Duration.ofMillis(999999999);
			infortunitmp.add(p);
			infortuni.put(p, 2);
		}
		else if(probp<=0.0004) {
			d=Duration.ofMillis(999999999);
			infortunitmp.add(p);
			infortuni.put(p, 1);
		}
		else if(probp<=0.0008) {
			d=Duration.ofMillis(999999999);
		}
	}

}
