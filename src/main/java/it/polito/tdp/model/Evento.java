package it.polito.tdp.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;

public class Evento implements Comparable<Evento> {
	
	public enum EventType {
		QUALIFICA,  // simulazione della qualifica
		GARA,  //la gara inizia con la griglia assegnata nella qualifica
		}
	private Gara gara ;
	private LocalDate data;
	private EventType type ;
	
	
	
	/**
	 * @param gara
	 * @param data
	 * @param id
	 * @param type
	 */
	public Evento(Gara gara, LocalDate data,EventType type) {
		super();
		this.gara = gara;
		this.data = data;
		this.type = type;
	}
	
	



	public Gara getGara() {
		return gara;
	}





	public void setGara(Gara gara) {
		this.gara = gara;
	}





	public LocalDate getData() {
		return data;
	}





	public void setData(LocalDate data) {
		this.data = data;
	}





	public EventType getType() {
		return type;
	}





	public void setType(EventType type) {
		this.type = type;
	}





	@Override
	public int compareTo(Evento o) {
		if(this.getData().isAfter(o.getData())) {
			return +1;	
		}
		return -1;
	}


	@Override
	public String toString() {
		return "Evento [gara=" + gara + ", data=" + data + ", type=" + type + "]\n";
	}
	
	
	

}
