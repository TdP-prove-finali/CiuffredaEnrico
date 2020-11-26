package it.polito.tdp.model;

import java.util.HashMap;
import java.util.Map;

public class StampaGara {
	Map<Integer,Pilota> posizioniIniziali;
	Map<Integer,Pilota> classificaFinale;
	PilotaTempo poleman;
	
	
	/**
	 * 
	 */
	public StampaGara() {
		super();
		posizioniIniziali=new HashMap<>();
		classificaFinale=new HashMap<>();
	}


	public Map<Integer, Pilota> getPosizioniIniziali() {
		return posizioniIniziali;
	}


	public void setPosizioniIniziali(Map<Integer, Pilota> posizioniIniziali) {
		this.posizioniIniziali = posizioniIniziali;
	}


	public Map<Integer, Pilota> getClassificaFinale() {
		return classificaFinale;
	}


	public void setClassificaFinale(Map<Integer, Pilota> classificaFinale) {
		this.classificaFinale = classificaFinale;
	}


	public PilotaTempo getPoleman() {
		return poleman;
	}


	public void setPoleman(PilotaTempo poleman) {
		this.poleman = poleman;
	}


	@Override
	public String toString() {
		return "StampaGara [posizioniIniziali=" + posizioniIniziali + ", classificaFinale=" + classificaFinale
				+ ", poleman=" + poleman + "]";
	}
	
	
}
