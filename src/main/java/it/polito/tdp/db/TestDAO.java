package it.polito.tdp.db;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.model.Circuito;
import it.polito.tdp.model.Gara;
import it.polito.tdp.model.Pilota;
import it.polito.tdp.model.Prestazione;
import it.polito.tdp.model.Scuderia;

public class TestDAO {
	
public static void main(String[] args) {
	
	F1DAO dao=new F1DAO();
	Map <Integer,Pilota> pilotiMap=new HashMap<>();
	Map <Integer,Circuito> circuitiMap=new HashMap<>();
	Map <Integer,Scuderia> scuderieMap=new HashMap<>();
	Map <String,Gara>gareMap=new HashMap<>();
	circuitiMap=dao.loadAllCircuiti();
	scuderieMap=dao.loadAllScuderie();
	pilotiMap=dao.loadAllPiloti(scuderieMap);
	gareMap=dao.loadAllGara(circuitiMap,24);
	dao.loadAllQ1(gareMap);
	dao.loadAllQ2(gareMap);
	dao.loadAllQ3(gareMap);
	dao.loadAllGiri(gareMap);
	System.out.println(pilotiMap);
	System.out.println(circuitiMap);
	System.out.println(scuderieMap);
	System.out.println(gareMap);

}
}
