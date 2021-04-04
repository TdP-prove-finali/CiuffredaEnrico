package it.polito.tdp.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.db.F1DAO;

public class Model {
	final static double TPUNTEGGIO=0.0030;
	final static double TSCAMBIO=0.0010;
	private F1DAO dao=new F1DAO();
	private Map <Integer,Pilota> pilotiMap=new HashMap<>();
	private Map <Integer,Scuderia> scuderieMap=new HashMap<>();
	private Map<String, Gara> gareMap=new HashMap<>();
	private Simulatore sim=new Simulatore();
	
	public Model() {
		this.scuderieMap=dao.loadAllScuderie();
		this.pilotiMap=dao.loadAllPiloti(scuderieMap);
		this.punteggioscuderia();
	}
	
	//carica tutti dati di qualifiche e gare
	public void caricaGare(int nGare) {
		//long startTime = System.currentTimeMillis(); 
		this.gareMap=dao.loadAllGara(dao.loadAllCircuiti(),nGare);
		this.dao.loadAllQ1(gareMap);
		this.dao.loadAllQ2(gareMap);
		this.dao.loadAllQ3(gareMap);
		this.dao.loadAllGiri(gareMap);
		//long duration = System.currentTimeMillis() - startTime; System.out.println("Program executed for " + duration + " milliseconds"); 
        //System.out.println(duration+"SIMULAZIONE");
	}
	
	public void iniziaSimulazione(String pioggia) {
		//long startTime = System.currentTimeMillis(); 
		sim.init(pilotiMap,gareMap,pioggia);
		sim.run();
		//long duration = System.currentTimeMillis() - startTime; System.out.println("Program executed for " + duration + " milliseconds"); 
        //System.out.println(duration);
	}
	
	
	//scambio di piloti di due scuderie diverse
	public void Scambio(Pilota p1, Pilota p2) {
		if(p1.getScuderia().getPunteggio()-p2.getScuderia().getPunteggio()<=0) {
			Pilota pscambio=p2;
			p2=p1;
			p1=pscambio;
		}
		Scuderia scuderiatmp=p1.getScuderia();
		this.pilotiMap.get(p1.getId()).setScuderia(p2.getScuderia());
		this.pilotiMap.get(p2.getId()).setScuderia(scuderiatmp);
		float punteggio=this.pilotiMap.get(p2.getId()).getScuderia().getPunteggio()-this.pilotiMap.get(p1.getId()).getScuderia().getPunteggio();
		for(Gara g:gareMap.values()) {
			if(g.getPrestazioni().containsKey(p1.getId())) {
			Prestazione tmp1=g.getPrestazioni().get(p1.getId());
			if(!(tmp1.getQ1()==null))
			tmp1.setQ1(Duration.ofMillis((long)( tmp1.getQ1().toMillis()+tmp1.getQ1().toMillis()*TSCAMBIO*(punteggio))));
			if(!(tmp1.getQ2()==null))
			tmp1.setQ2(Duration.ofMillis((long) ( tmp1.getQ2().toMillis()+tmp1.getQ2().toMillis()*TSCAMBIO*(punteggio))));
			if(!(tmp1.getQ3()==null)) {
			tmp1.setQ3(Duration.ofMillis((long) ( tmp1.getQ3().toMillis()+tmp1.getQ3().toMillis()*TSCAMBIO*(punteggio))));
			}
			for(Duration d:tmp1.getTempigiro()) {
				d=Duration.ofMillis((long) (d.toMillis()+d.toMillis()*TSCAMBIO*(punteggio)));
			}
			}
			if(g.getPrestazioni().containsKey(p2.getId())) {
			Prestazione tmp2=g.getPrestazioni().get(p2.getId());
			if(!(tmp2.getQ1()==null))
			tmp2.setQ1(Duration.ofMillis((long) (tmp2.getQ1().toMillis()-tmp2.getQ1().toMillis()*TSCAMBIO*(punteggio))));
			if(!(tmp2.getQ2()==null))
			tmp2.setQ2(Duration.ofMillis((long) (tmp2.getQ2().toMillis()-tmp2.getQ2().toMillis()*TSCAMBIO*(punteggio))));
			if(!(tmp2.getQ3()==null))
			tmp2.setQ3(Duration.ofMillis((long) (tmp2.getQ3().toMillis()-tmp2.getQ3().toMillis()*TSCAMBIO*(punteggio))));
			for(Duration d:tmp2.getTempigiro()) {
				d=Duration.ofMillis((long) (d.toMillis()-d.toMillis()*TSCAMBIO*(punteggio)));
			}
			}
		}
	}
	
	//aggiungo un pilota e elimino un pilota esistente
	public void AggiungiElimina(Pilota p1, Pilota p2) {
		pilotiMap.put(p1.getId(), p1);
		pilotiMap.get(p1.getId()).setScuderia(p2.getScuderia());
		pilotiMap.remove(p2.getId());
	}
	
	//miglioro la scuderia poichè sono stati investiti x milioni
	public void MiglioraScuderia(Scuderia scuderia,Integer importo) {
		Scuderia s=scuderia;
		importo=importo*1000000;
		float punteggio=(s.getPunteggio()/8/s.getImportospeso())*(s.getImportospeso()+importo)+s.getPunteggio();
		if(punteggio>99) {
			punteggio=99;
		}
		s.setPunteggio(punteggio);
		Pilota p1=null;
		Pilota p2=null;
		for(Pilota p:pilotiMap.values()) {
			if(p1==null && p.getScuderia().equals(s)) {
				p1=p;
			}
			else if(p1!=null && p.getScuderia().equals(s)) {
				p2=p;
				System.out.println(p2);
			}
		}
		for(Gara g:gareMap.values()) {
			if(g.getPrestazioni().containsKey(p1.getId())) {
			Prestazione tmp1=g.getPrestazioni().get(p1.getId());
			if(!(tmp1.getQ1()==null)) {
				tmp1.setQ1(Duration.ofMillis((long)( tmp1.getQ1().toMillis()-tmp1.getQ1().toMillis()*TPUNTEGGIO*(punteggio))));
			}
			if(!(tmp1.getQ2()==null))
			tmp1.setQ2(Duration.ofMillis((long) ( tmp1.getQ2().toMillis()-tmp1.getQ2().toMillis()*TPUNTEGGIO*(punteggio))));
			if(!(tmp1.getQ3()==null)) {
			tmp1.setQ3(Duration.ofMillis((long) ( tmp1.getQ3().toMillis()-tmp1.getQ3().toMillis()*TPUNTEGGIO*(punteggio))));
			}
			for(Duration d:tmp1.getTempigiro()) {
				d=Duration.ofMillis((long) (d.toMillis()-d.toMillis()*TPUNTEGGIO*(punteggio)));
			}
			}
			if(g.getPrestazioni().containsKey(p2.getId())) {
			Prestazione tmp2=g.getPrestazioni().get(p2.getId());
			if(!(tmp2.getQ1()==null))
			tmp2.setQ1(Duration.ofMillis((long) (tmp2.getQ1().toMillis()-tmp2.getQ1().toMillis()*TPUNTEGGIO*(punteggio))));
			if(!(tmp2.getQ2()==null))
			tmp2.setQ2(Duration.ofMillis((long) (tmp2.getQ2().toMillis()-tmp2.getQ2().toMillis()*TPUNTEGGIO*(punteggio))));
			if(!(tmp2.getQ3()==null))
			tmp2.setQ3(Duration.ofMillis((long) (tmp2.getQ3().toMillis()-tmp2.getQ3().toMillis()*TPUNTEGGIO*(punteggio))));
			for(Duration d:tmp2.getTempigiro()) {
				d=Duration.ofMillis((long) (d.toMillis()-d.toMillis()*TPUNTEGGIO*(punteggio)));
			}
			}
		}
	}
	
	//ritorna classifica piloti
	public List<StampaPilota> classificaGeneralePilota(){
		List<StampaPilota> classificaPilotiFinale=new ArrayList<>();
		for(Pilota p:sim.getClassificaPiloti().keySet()) {
			classificaPilotiFinale.add(new StampaPilota(p,sim.getClassificaPiloti().get(p)));
		}
		Collections.sort(classificaPilotiFinale);
		return classificaPilotiFinale;
	}
	
	//ritorna classifica scuderie
	public List<StampaScuderia> classificaGeneraleScuderia(){
		List<StampaScuderia> classificaScuderieFinale=new ArrayList<>();
		for(Scuderia s:sim.getClassificaScuderie().keySet()) {
			classificaScuderieFinale.add(new StampaScuderia(s,sim.getClassificaScuderie().get(s)));
		}
		Collections.sort(classificaScuderieFinale);
		return classificaScuderieFinale;
	}
	
	public Map<Gara,StampaGara> risultatiGare(){
		return sim.getStampaGare();
	}
	
	private void punteggioscuderia() {
		for(Pilota p: pilotiMap.values()) {
			p.getScuderia().setPunteggio(p.getScuderia().getPunteggio()+p.getPunteggio());
		}
		for(Scuderia s: scuderieMap.values()) {
			s.setPunteggio(s.getPunteggio()/2);
		}
	}
	
	
	public Map<Integer, Pilota> getPilotiMap() {
		return pilotiMap;
	}

	public void setPilotiMap(Map<Integer, Pilota> pilotiMap) {
		this.pilotiMap = pilotiMap;
	}

	public Map<Integer, Scuderia> getScuderieMap() {
		return scuderieMap;
	}

	public void setScuderieMap(Map<Integer, Scuderia> scuderieMap) {
		this.scuderieMap = scuderieMap;
	}
	
	
}
	
