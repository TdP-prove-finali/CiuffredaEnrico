package it.polito.tdp.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gara {
	private int raceId;
	private Circuito circuito;
	private LocalDate data;
	private Integer round;
	private String nome;
	private List <Prestazione> prestazioni;
	private Integer numerogiri;
	private Map<Integer,Pilota> posizioniI;
	private Map<Integer,Pilota> posizioniF;

	/**
	 * @param circuito
	 * @param anno
	 * @param round
	 * @param nome
	 * @param prestazioni
	 * @param numerogiri
	 * @param posizioniI
	 * @param posizioniF
	 */
	public Gara(int raceId,Circuito circuito, LocalDate anno, Integer round, String nome, Integer numerogiri) {
		super();
		this.raceId=raceId;
		this.circuito = circuito;
		this.data = anno;
		this.round = round;
		this.nome = nome;
		this.prestazioni = new ArrayList<>();
		this.numerogiri = numerogiri;
		this.posizioniI = new HashMap<>();
		this.posizioniF = new HashMap<>();
	}
	
	
	/**
	 * 
	 */
	public Gara() {
		super();
	}


	public int getRaceId() {
		return raceId;
	}

	public void setRaceId(int raceId) {
		this.raceId = raceId;
	}

	public Circuito getCircuito() {
		return circuito;
	}
	public void setCircuito(Circuito circuito) {
		this.circuito = circuito;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public Integer getRound() {
		return round;
	}
	public void setRound(Integer round) {
		this.round = round;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Prestazione> getPrestazioni() {
		return prestazioni;
	}
	public void setPrestazioni(List<Prestazione> prestazioni) {
		this.prestazioni = prestazioni;
	}
	public Integer getNumerogiri() {
		return numerogiri;
	}
	public void setNumerogiri(Integer numerogiri) {
		this.numerogiri = numerogiri;
	}
	public Map<Integer, Pilota> getPosizioniI() {
		return posizioniI;
	}
	public void setPosizioniI(Map<Integer, Pilota> posizioniI) {
		this.posizioniI = posizioniI;
	}
	public Map<Integer, Pilota> getPosizioniF() {
		return posizioniF;
	}
	public void setPosizioniF(Map<Integer, Pilota> posizioniF) {
		this.posizioniF = posizioniF;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + raceId;
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
		Gara other = (Gara) obj;
		if (raceId != other.raceId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return data +" "+circuito+" "+round;
	}
	
	

}
