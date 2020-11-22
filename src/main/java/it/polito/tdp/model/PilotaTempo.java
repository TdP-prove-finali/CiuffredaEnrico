package it.polito.tdp.model;

import java.time.Duration;

public class PilotaTempo implements Comparable<PilotaTempo> {
	private Pilota pilota;
	private Duration tempo;
	/**
	 * @param pilota
	 * @param tempo
	 */
	public PilotaTempo(Pilota pilota, Duration tempo) {
		super();
		this.pilota = pilota;
		this.tempo = tempo;
	}
	
	public Pilota getPilota() {
		return pilota;
	}
	public void setPilota(Pilota pilota) {
		pilota = pilota;
	}
	public Duration getTempo() {
		return tempo;
	}
	public void setTempo(Duration tempo) {
		tempo = tempo;
	}

	@Override
	public int compareTo(PilotaTempo o) {
		return tempo.compareTo(o.tempo);
	}

	@Override
	public String toString() {
		return "PilotaTempo [pilota=" + pilota + ", tempo=" + tempo + "]\n";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pilota == null) ? 0 : pilota.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PilotaTempo other = (PilotaTempo) obj;
		if (pilota == null) {
			if (other.pilota != null)
				return false;
		} else if (!pilota.equals(other.pilota))
			return false;
		return true;
	}
	
	

	

}
