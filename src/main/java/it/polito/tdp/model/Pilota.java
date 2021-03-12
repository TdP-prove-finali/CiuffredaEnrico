package it.polito.tdp.model;

import java.time.LocalDate;

public class Pilota {
	private Integer id;
	private String ref;
	private Integer number;
	private String code;
	private String cognome;
	private String nome;
	private LocalDate nascita;
	private String nazionalita;
	private Integer punteggio;
	private Integer nGare;
	private Integer nGareConcluse;
	private Integer nRitiri;
	private Scuderia scuderia;
	/**
	 * @param id
	 * @param ref
	 * @param number
	 * @param code
	 * @param cognome
	 * @param nome
	 * @param nascita
	 * @param nazionalita
	 * @param punteggio
	 * @param scuderia
	 */
	public Pilota(Integer id, String ref, Integer number, String code, String cognome, String nome, LocalDate nascita,
			String nazionalita, Integer punteggio,Integer nGare, Integer nGareConcluse, Scuderia scuderia) {
		super();
		this.id = id;
		this.ref = ref;
		this.number = number;
		this.code = code;
		this.cognome = cognome;
		this.nome = nome;
		this.nascita = nascita;
		this.nazionalita = nazionalita;
		this.punteggio = punteggio;
		this.nGare=nGare;
		this.nGareConcluse=nGareConcluse;
		this.nRitiri=nGare-nGareConcluse;
		this.scuderia=scuderia;
	}
	
	public Pilota(Pilota p) {
		this.id = p.getId();
		this.ref = p.getRef();
		this.number = p.getNumber();
		this.code = "SOS";
		this.cognome = "Sostituto";
		this.nome = "Pilota";
		this.nascita = p.getNascita();
		this.nazionalita = p.getNazionalita();
		this.punteggio = p.getPunteggio()-10;
		this.nGare=p.getnGare();
		this.nGareConcluse=p.getnGareConcluse();
		this.nRitiri=nGare-nGareConcluse;
		this.scuderia=p.getScuderia();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LocalDate getNascita() {
		return nascita;
	}
	public void setNascita(LocalDate nascita) {
		this.nascita = nascita;
	}
	public String getNazionalita() {
		return nazionalita;
	}
	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}
	public Integer getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(Integer punteggio) {
		this.punteggio = punteggio;
	}
	
	public Integer getnGare() {
		return nGare;
	}
	public void setnGare(Integer nGare) {
		this.nGare = nGare;
	}
	public Integer getnGareConcluse() {
		return nGareConcluse;
	}
	public void setnGareConcluse(Integer nGareConcluse) {
		this.nGareConcluse = nGareConcluse;
	}
	public Integer getnRitiri() {
		return nRitiri;
	}
	public void setnRitiri(Integer nRitiri) {
		this.nRitiri = nRitiri;
	}
	public Scuderia getScuderia() {
		return scuderia;
	}
	public void setScuderia(Scuderia scuderia) {
		this.scuderia = scuderia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Pilota other = (Pilota) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return code +" "+nome+" "+cognome+" "+number+"  "+scuderia;
	}
	
}
