package it.polito.tdp.model;

import java.util.Comparator;

public class ComparatoreDate implements Comparator<Gara> {

	@Override
	public int compare(Gara arg0, Gara arg1) {
		if(arg0.getData().isAfter(arg1.getData()))
		return -1;
		return +1;
	}

}
