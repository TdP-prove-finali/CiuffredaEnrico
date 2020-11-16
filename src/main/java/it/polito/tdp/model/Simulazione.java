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

import com.zaxxer.hikari.util.ClockSource.NanosecondClockSource;

import it.polito.tdp.db.F1DAO;
import it.polito.tdp.model.Evento.EventType;

public class Simulazione {
	
	// PARAMETRI DI SIMULAZIONE
	private int pqualifica = 2; 
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
	private double ssorpasso;
	private F1DAO dao=new F1DAO();
	private Map <Integer,Pilota> pilotiMap=new HashMap<>();
	private Map <Integer,Circuito> circuitiMap=new HashMap<>();
	private Map <Integer,Scuderia> scuderieMap=new HashMap<>();
	private Map <Integer,Gara> gareMap=new HashMap<>();
	private List<PilotaScuderia> assegnazioni=new ArrayList<>();
	// OUTPUT DA CALCOLARE
	private Map<Integer,Integer> classificapiloti;
	private Map<Integer,Integer> classificascuderie;
	// STATO DEL SISTEMA
	private Map<Integer,Integer> infortuni;
	private Map<Integer,Integer> classificaattuale;
	private List<Gara> garecampionato;
	// CODA DEGLI EVENTI
	private PriorityQueue<Evento> queue;

	// INIZIALIZZAZIONE
	public void init() {
		this.queue = new PriorityQueue<>();
		this.infortuni=new HashMap<>();
		this.classificaattuale=new HashMap<>();
		this.garecampionato=new ArrayList<>();
		this.pilotiMap=dao.loadAllPiloti();
		this.circuitiMap=dao.loadAllCircuiti();
		this.scuderieMap=dao.loadAllScuderie();
		this.gareMap=dao.loadAllGara(circuitiMap);
		this.dao.loadAllPosizioniI(gareMap, pilotiMap);
		this.dao.loadAllGiri(gareMap, pilotiMap, scuderieMap);
		caricaGare(gareMap);
		caricascuderie();
		// generiamo eventi iniziali
		for(Gara g: garecampionato) {
			Evento tmpe=new Evento(g,g.getData().minusDays(1),-1,EventType.QUALIFICA);
			Evento tmpeg=new Evento(g,g.getData(),-1,EventType.GARA);
			queue.add(tmpe);
			queue.add(tmpeg);
		}
		System.out.println(queue);
		System.out.println("SF "+assegnazioni);
	}


	// ESECUZIONE
	public void run() {
		System.out.println(garecampionato);
		while (!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			processEvent(e);
		}
	}
	

	private void processEvent(Evento e) {
		switch (e.getType()) {
		case QUALIFICA:
			System.out.println(e.getGara());
			caricatempigaraqualifica(e);
			break;
		}
	}
	
	private void caricatempigaraqualifica(Evento e) {
		List <Prestazione> prestazionimediegarepassate=new ArrayList<>();
		for(PilotaScuderia ps: this.assegnazioni) {
			prestazionimediegarepassate.add(new Prestazione(ps.getPilota(),ps.getScuderia()));
		}
		for(Prestazione pr:prestazionimediegarepassate) {
		//System.out.println(pr);
		Map <Integer,Double> tmpp=new HashMap<>();
		tmpp.put(-1, (double) 0);
		tmpp.put(-2, (double) 0);
		tmpp.put(-3, (double) 0);
		int q1=0;
		int q2=0;
		int q3=0;
		Gara garatrovata = new Gara();
		for(Gara g: gareMap.values()) {
			if(e.getGara().getNome().equals(g.getNome())) {
			for(Prestazione p: g.getPrestazioni()) {
				if(pr.getPilota().equals(p.getPilota()) && !garatrovata.equals(g)) {
					garatrovata=g;
					if(!p.getTempoq1().equals(LocalTime.of(0, 0, 0, 0))) {
					Double tempotmp=calcolaTempo(p.getTempoq1());
					tmpp.put(-1,tmpp.get(-1)+tempotmp);
					q1++;
					}
					if(!p.getTempoq2().equals(LocalTime.of(0, 0, 0, 0))) {
				    Double tempotmp=calcolaTempo(p.getTempoq2());
					tmpp.put(-2,tmpp.get(-2)+tempotmp);
					q2++;
					}
					if(!p.getTempoq3().equals(LocalTime.of(0, 0, 0, 0))) {
					Double tempotmp=calcolaTempo(p.getTempoq2());
					tmpp.put(-3,tmpp.get(-3)+tempotmp);
					q3++;
					}
				}
			}
			}
		}
		pr.setTempoq1(calcolaLocalTime(tmpp.get(-1)/q1));
		pr.setTempoq2(calcolaLocalTime(tmpp.get(-2)/q2));
		pr.setTempoq3(calcolaLocalTime(tmpp.get(-3)/q3));
		System.out.println(pr);
		}
	}


	private void caricaGare(Map <Integer,Gara> gareMap) {
		Integer garaid=1;
		boolean trovato=false;
		LocalDate data= LocalDate.of(2021, 3, 7);
		for(Gara g: gareMap.values()) {
			trovato=false;
			for(Gara g2: gareMap.values()) {
				if(trovato==false) {
				if(g.getNome().equals(g2.getNome()) && g.getData().getYear()!=g2.getData().getYear()) {
					Gara tmpg=new Gara(garaid,g.getCircuito(),data,garaid,g.getNome(),g.getNumerogiri());
					System.out.println(tmpg);
					trovato=true;
					if(garecampionato.size()<numerogare) {
						boolean inserito=false;
						for(Gara g3: garecampionato) {
							if(g3.getNome().equals(tmpg.getNome()))
							{
								inserito=true;
							}
						}
						if(inserito==false) {
						garaid++;
						data=data.plusDays(7);
						garecampionato.add(tmpg);
						}
					}
				}
				}
			}
		}
		List<Gara> tmpgare;
			for(Gara g: gareMap.values()) {
				tmpgare=new ArrayList<>(garecampionato);
				trovato=false;
				for(Gara g2: tmpgare) {
					if(g.getNome().equals(g2.getNome())) {
						trovato=true;	
					}
				}
				if(garecampionato.size()<numerogare && trovato==false) {
					Gara tmpg=new Gara(garaid,g.getCircuito(),data,garaid,g.getNome(),g.getNumerogiri());
					garecampionato.add(tmpg);
					garaid++;
					data=data.plusDays(7);
				}
				}
			}
	

	private void caricascuderie() {
		List<Gara> garetmp=new ArrayList<>(gareMap.values());
		Collections.sort(garetmp,new ComparatoreDate());
		for(Pilota p: pilotiMap.values()) {
			boolean trovato=false;
			for(Gara g: garetmp) {
				if(trovato==false)
				for(Prestazione pr: g.getPrestazioni()) {
					System.out.println(pr);
					if(trovato==false) {
						if(pr.getPilota().equals(p)) {
							System.out.println(pr);
							PilotaScuderia ps=new PilotaScuderia(p,pr.getScuderia());
							assegnazioni.add(ps);
							trovato=true;
						}
					}
				}
			}
		}
	}
	
	private Double calcolaTempo(LocalTime tempo) {
		Double minuti= (double) tempo.getMinute();
		Double secondi=(double) tempo.getSecond();
		secondi=secondi+minuti*60;
		Double nano=(double) tempo.getNano();
		nano=nano+secondi*1000000000;
		return nano;
	}
	private LocalTime calcolaLocalTime(Double tempo) {
		Double prova= tempo;
		Integer minuti=(int) (prova/60000000000.00);
		prova=prova-60000000000.00*minuti;
		Integer secondi=(int)(prova/1000000000.00);
		prova=prova-1000000000.00*secondi;
		Integer nanosecondi=(int) (prova/1.00);
		LocalTime tmpprova=LocalTime.of(0, minuti, secondi, nanosecondi);
		return tmpprova;
	}
	
	public List<Gara> getGaredadisputare() {
		return garecampionato;
	}


	public void setGaredadisputare(List<Gara> garecampionato) {
		this.garecampionato = garecampionato;
	}
	
	

}
