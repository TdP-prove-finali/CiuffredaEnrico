package it.polito.tdp.model;

public class StampaPilota implements Comparable<StampaPilota> {
	Pilota pilota;
	Integer punteggio;
	/**
	 * @param pilota
	 * @param punteggio
	 */
	public StampaPilota(Pilota pilota, Integer punteggio) {
		super();
		this.pilota = pilota;
		this.punteggio = punteggio;
	}
	@Override
	public String toString() {
		return pilota + " " + punteggio;
	}

	@Override
	public int compareTo(StampaPilota o) {
		// TODO Auto-generated method stub
		return o.punteggio-this.punteggio;
	}
	
	

}
