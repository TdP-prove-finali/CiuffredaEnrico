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
	private List<Duration> tempigiro;
	private Duration q1;
	private Duration q2;
	private Duration q3;
	
	
	/**
	 * 
	 */
	public Prestazione(Duration q1) {
		super();
		this.q1=q1;
		q2=Duration.ofMillis(0);
		q3=Duration.ofMillis(0);
		tempigiro=new ArrayList<>();
	}
	
	
	
	public List<Duration> getTempigiro() {
		return tempigiro;
	}
	public void setTempigiro(List<Duration> tempigiro) {
		this.tempigiro = tempigiro;
	}
	
	public Duration getQ1() {
		return q1;
	}
	public void setQ1(Duration q1) {
		this.q1 = q1;
	}
	public Duration getQ2() {
		return q2;
	}
	public void setQ2(Duration q2) {
		this.q2 = q2;
	}
	public Duration getQ3() {
		return q3;
	}
	public void setQ3(Duration q3) {
		this.q3 = q3;
	}



	@Override
	public String toString() {
		return q1+"  "+q2+"  "+q3;
		//return" ";
	}
	
	
		

}
