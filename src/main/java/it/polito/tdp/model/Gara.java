package it.polito.tdp.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gara {
	private String nome;
	private Circuito circuito;
	private LocalDate data;
	private Map <Integer,Prestazione> prestazioni;
	private Integer numerogiri;
	

	/**
	 * @param nome
	 * @param circuito
	 * @param data
	 * @param numerogiri
	 */
	public Gara(String nome, Circuito circuito, LocalDate data, Integer numerogiri) {
		super();
		this.nome = nome;
		this.circuito = circuito;
		this.data = data;
		this.numerogiri = numerogiri;
		this.prestazioni=new HashMap<>();
	}
	
	


	public String getNome() {
		return nome;
	}




	public void setNome(String nome) {
		this.nome = nome;
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




	public Map<Integer, Prestazione> getPrestazioni() {
		return prestazioni;
	}




	public void setPrestazioni(Map<Integer, Prestazione> prestazioni) {
		this.prestazioni = prestazioni;
	}




	public Integer getNumerogiri() {
		return numerogiri;
	}




	public void setNumerogiri(Integer numerogiri) {
		this.numerogiri = numerogiri;
	}




	@Override
	public String toString() {
		return circuito.toString();
	}
	
	

}
