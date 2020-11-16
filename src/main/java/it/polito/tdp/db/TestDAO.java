package it.polito.tdp.db;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.model.Circuito;
import it.polito.tdp.model.Gara;
import it.polito.tdp.model.Pilota;
import it.polito.tdp.model.Scuderia;

public class TestDAO {
	
public static void main(String[] args) {
	
	F1DAO dao=new F1DAO();
	Map <Integer,Pilota> pilotiMap=new HashMap<>();
	Map <Integer,Circuito> circuitiMap=new HashMap<>();
	Map <Integer,Scuderia> scuderieMap=new HashMap<>();
	Map <Integer,Gara> gareMap=new HashMap<>();
	pilotiMap=dao.loadAllPiloti();
	circuitiMap=dao.loadAllCircuiti();
	scuderieMap=dao.loadAllScuderie();
	gareMap=dao.loadAllGara(circuitiMap);
	dao.loadAllPosizioniI(gareMap, pilotiMap);
	dao.loadAllGiri(gareMap, pilotiMap, scuderieMap);
	System.out.println(pilotiMap);
	System.out.println(circuitiMap);
	System.out.println(scuderieMap);
	System.out.println(gareMap);
	System.out.println(gareMap.get(1043).getPosizioniF());
	System.out.println(gareMap.get(1043).getPosizioniI());
	System.out.println(gareMap.get(1043).getPrestazioni());
}
}
