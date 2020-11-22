package it.polito.tdp.model;

public class StampaScuderia implements Comparable<StampaScuderia>{
	Scuderia scuderia;
	Integer punteggio;
	/**
	 * @param pilota
	 * @param punteggio
	 */
	public StampaScuderia(Scuderia scuderia, Integer punteggio) {
		super();
		this.scuderia = scuderia;
		this.punteggio = punteggio;
	}
	@Override
	public String toString() {
		return scuderia + "  " + punteggio + "\n";
	}
	@Override
	public int compareTo(StampaScuderia o) {
		return o.punteggio-this.punteggio;
	}
	
	

}
