package it.polito.tdp.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Evento implements Comparable<Evento>{
	
	public enum EventType {
		QUALIFICA,  // simulazione della qualifica
		GARA,  //la gara inizia con la griglia assegnata nella qualifica
		GIRO,// viene simulato un giro
		SORPASSO, // viene simulato un sorpasso
		ASSEGNAPUNTI, // vengono dati i punti alle persone 
		}
	private Gara gara ;
	private LocalDate data;
	private Integer id;//-1 qualifica,0 gara, 1-n giri+sorpassi, n+1 assegna punti (n=giri+sorpassi)
	private EventType type ;
	
	
	
	/**
	 * @param gara
	 * @param data
	 * @param id
	 * @param type
	 */
	public Evento(Gara gara, LocalDate data, Integer id, EventType type) {
		super();
		this.gara = gara;
		this.data = data;
		this.id = id;
		this.type = type;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Evento other = (Evento) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Gara getGara() {
		return gara;
	}
	public void setGara(Gara gara) {
		this.gara = gara;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	@Override
	public int compareTo(Evento arg0) {
		if(this.data.isAfter(arg0.data))
		return 1;
		if(this.data.isEqual(arg0.data))
			return this.getId()-this.getId();
		return -1;
	}
	
	
	@Override
	public String toString() {
		return "Evento [gara=" + gara + ", data=" + data + ", type=" + type + "]\n";
	}
	
	

}
