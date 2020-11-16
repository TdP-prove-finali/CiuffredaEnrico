package it.polito.tdp.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TestSimulatore {

	public static void main(String[] args) {
		Simulazione sim = new Simulazione() ;
		sim.init();
		sim.run();
	}

}

