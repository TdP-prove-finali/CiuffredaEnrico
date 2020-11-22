package it.polito.tdp.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class TestSimulatore {

	public static void main(String[] args) {
		Simulatore sim = new Simulatore();
		sim.init();
		sim.run();
	}

}

