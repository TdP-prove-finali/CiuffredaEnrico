package it.polito.tdp.model;

public class PilotaScuderia {
	private Pilota pilota;
	private Scuderia scuderia;

	/**
	 * @param pilota
	 * @param scuderia
	 */
	public PilotaScuderia(Pilota pilota, Scuderia scuderia) {
		super();
		this.pilota = pilota;
		this.scuderia = scuderia;
	}
	
	public Pilota getPilota() {
		return pilota;
	}
	public void setPilota(Pilota pilota) {
		this.pilota = pilota;
	}
	public Scuderia getScuderia() {
		return scuderia;
	}
	public void setScuderia(Scuderia scuderia) {
		this.scuderia = scuderia;
	}

	@Override
	public String toString() {
		return "PilotaScuderia [pilota=" + pilota + ", scuderia=" + scuderia + "]";
	}
	
	

}
