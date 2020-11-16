package it.polito.tdp.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Prestazione {
	private Pilota pilota;
	private Scuderia scuderia;
	private List<LocalTime> tempigiro;
	private LocalTime tempoq1;
	private LocalTime tempoq2;
	private LocalTime tempoq3;
	
	/**
	 * @param pilota
	 * @param posizione
	 * @param tempigiro
	 * @param tempoqualifica
	 */
	public Prestazione(Pilota pilota,Scuderia scuderia,LocalTime tempoq1,LocalTime tempoq3,LocalTime tempoq2) {
		super();
		this.pilota = pilota;
		this.scuderia=scuderia;
		this.tempigiro = new ArrayList<>();
		this.tempoq1 = tempoq1;
		this.tempoq2 = tempoq2;
		this.tempoq3 = tempoq3;
	}

	public Prestazione(Pilota pilota, Scuderia scuderia) {
		this.pilota=pilota;
		this.scuderia=scuderia;
		this.tempigiro=new ArrayList<>();
		this.tempoq1=LocalTime.of(0, 0, 0, 0);
		this.tempoq2=LocalTime.of(0, 0, 0, 0);
		this.tempoq3=LocalTime.of(0, 0, 0, 0);
	}

	public Pilota getPilota() {
		return pilota;
	}

	public void setPilota(Pilota pilota) {
		this.pilota = pilota;
	}


	public List<LocalTime> getTempigiro() {
		return tempigiro;
	}

	public void setTempigiro(List<LocalTime> tempigiro) {
		this.tempigiro = tempigiro;
	}

	public LocalTime getTempoqualifica() {
		return tempoq1;
	}

	public void setTempoqualifica(LocalTime tempoq1) {
		this.tempoq1 = tempoq1;
	}

	public Scuderia getScuderia() {
		return scuderia;
	}

	public void setScuderia(Scuderia scuderia) {
		this.scuderia = scuderia;
	}

	public LocalTime getTempoq1() {
		return tempoq1;
	}

	public void setTempoq1(LocalTime tempoq1) {
		this.tempoq1 = tempoq1;
	}

	public LocalTime getTempoq2() {
		return tempoq2;
	}

	public void setTempoq2(LocalTime tempoq2) {
		this.tempoq2 = tempoq2;
	}

	public LocalTime getTempoq3() {
		return tempoq3;
	}

	public void setTempoq3(LocalTime tempoq3) {
		this.tempoq3 = tempoq3;
	}

	@Override
	public String toString() {
		return "Prestazione [pilota=" + pilota + ", scuderia=" + scuderia + ", tempigiro=" + tempigiro + ", tempoq1="
				+ tempoq1 + ", tempoq2=" + tempoq2 + ", tempoq3=" + tempoq3 + "]";
	}
	
	

}
