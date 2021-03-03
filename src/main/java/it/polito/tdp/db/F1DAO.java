package it.polito.tdp.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import it.polito.tdp.model.Circuito;
import it.polito.tdp.model.Gara;
import it.polito.tdp.model.Pilota;
import it.polito.tdp.model.Prestazione;
import it.polito.tdp.model.Scuderia;

public class F1DAO {
	
	//estrarre i piloti e inserirli in una mappa
	public Map<Integer,Pilota> loadAllPiloti(Map<Integer,Scuderia> ScuderieMap){
		String sql = "SELECT d.driverId,c.constructorId,d.driverId,d.driverRef,d.number,d.code,d.forename,d.surname,d.dob,d.nationality,pp.punteggio,COUNT(r.raceId) as gare,COUNT(r.position) as garefinite " + 
				"FROM drivers AS d,constructors AS c,results AS r , races AS rs, punteggiopiloti AS pp " + 
				"WHERE d.driverId=r.driverId && r.constructorId=c.constructorId && r.raceId=rs.raceId && YEAR(rs.date)>2019 && d.driverId=pp.driverId " + 
				"GROUP BY d.driverId";
		Map<Integer, Pilota> result = new HashMap<Integer,Pilota>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Pilota tmp=new Pilota(rs.getInt("d.driverId"),rs.getString("d.driverRef"),rs.getInt("d.number"),
						rs.getString("d.code"),rs.getString("d.surname"),rs.getString("d.forename"),rs.getDate("d.dob").toLocalDate(),
						rs.getString("d.nationality"),rs.getInt("pp.punteggio"),rs.getInt("gare"),rs.getInt("garefinite"),ScuderieMap.get(rs.getInt("c.constructorId")));
				result.put(rs.getInt("d.driverId"),tmp);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//estrarre le scuderie e creare una mappa 
	public Map<Integer,Scuderia> loadAllScuderie(){
		String sql = "SELECT c.constructorId,c.constructorRef,c.name,c.nationality,s.importo " + 
				"FROM constructors AS c,spesa AS s " + 
				"WHERE c.constructorId=s.constructorId";
		Map<Integer, Scuderia> result = new HashMap<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Scuderia tmp=new Scuderia(rs.getInt("c.constructorId"),rs.getString("c.constructorRef"),rs.getString("c.name"),
						rs.getString("c.nationality"),rs.getInt("s.importo"));
				result.put(rs.getInt("c.constructorId"),tmp);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//estrarre i circuiti e creare una mappa 
	public Map<Integer,Circuito> loadAllCircuiti(){
		String sql = "SELECT c.circuitId,c.circuitRef,c.name,c.location,c.country,c.lat,c.lng " + 
				"FROM circuits AS c";
		Map<Integer,Circuito> result = new HashMap<Integer,Circuito>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Circuito tmp=new Circuito(rs.getInt("c.circuitId"),rs.getString("c.circuitRef"),rs.getString("c.name"),
						rs.getString("c.location"),rs.getString("c.country"),rs.getDouble("c.lat"),rs.getDouble("c.lng"));
				result.put(rs.getInt("c.circuitId"),tmp);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//estrarre le gare dall 2018 e creare una mappa
	public Map<String,Gara> loadAllGara(Map<Integer,Circuito> circuitiMap,int numerogare){
		String sql = "SELECT DISTINCT(rs.name) as nome,rs.circuitId,COUNT(rs.raceId),MAX(r.laps) as lap " + 
				"FROM results AS r, races AS rs " + 
				"WHERE r.raceId=rs.raceId && year(rs.date)>2018 " + 
				"GROUP BY rs.name " + 
				"ORDER BY COUNT(rs.raceId) DESC";
		Map<String,Gara> result = new HashMap<>();
		LocalDate data=LocalDate.of(2021, 3, 7);
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if(result.size()<numerogare) {
				Circuito tmpc= circuitiMap.get(rs.getInt("rs.circuitId"));
				Gara tmpg=new Gara(rs.getString("nome"),tmpc,data,rs.getInt("lap"));
				data=data.plusDays(7);
				result.put(rs.getString("nome"),tmpg);
				}
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
    //estrarre i dati relativi alla q1 e inserirli nella mappa delle gare 
	public void loadAllQ1(Map<String,Gara> mappaGare){
		String sql ="SELECT r.name,q.driverId,AVG(qualifica.q1) AS t1 " + 
				"FROM races AS r,qualifying AS q, " + 
				"( " + 
				"SELECT r.raceId AS gara,q.driverId AS pilota,CAST(SUBSTRING_INDEX(q.q1,\":\",1) AS INT)*60000+CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(q.q1,\":\",-1),\".\",1) AS INT)*1000+CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(q.q1,\":\",-2),\".\",-1),\" \",1) AS INT) AS q1 " + 
				"FROM races AS r,qualifying AS q " + 
				"WHERE year(r.date)>2018 && r.raceId=q.raceId " + 
				") AS qualifica " + 
				"WHERE r.raceId=qualifica.gara && q.driverId=qualifica.pilota && q.raceId=r.raceId " + 
				"GROUP BY r.name,q.driverId " + 
				"ORDER BY r.name,q.driverId";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if(mappaGare.containsKey(rs.getString("r.name"))) {
				Gara tmpg=mappaGare.get(rs.getString("r.name"));
				if(!tmpg.getPrestazioni().containsKey(rs.getInt("q.driverId"))) {
				tmpg.getPrestazioni().put(rs.getInt("q.driverId"),new Prestazione(Duration.ofMillis((rs.getLong("t1")))));
				}
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//estrarre i dati relativi alla q2 e inserirli nella mappa delle gare 
	public void loadAllQ2(Map<String,Gara> mappaGare){
		String sql ="SELECT r.name,q.driverId,q.number,AVG(qualifica.q2) AS t2 " + 
				"FROM races AS r,qualifying AS q, " + 
				"( " + 
				"SELECT r.raceId AS gara,q.driverId AS pilota,CAST(SUBSTRING_INDEX(q.q2,\":\",1) AS INT)*60000+CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(q.q2,\":\",-1),\".\",1) AS INT)*1000+CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(q.q2,\":\",-2),\".\",-1),\" \",1) AS INT) AS q2 " + 
				"FROM races AS r,qualifying AS q " + 
				"WHERE year(r.date)>2018 && r.raceId=q.raceId && q.q2<>\"NULL\" && q.q2<>0 " + 
				") AS qualifica " + 
				"WHERE r.raceId=qualifica.gara && q.driverId=qualifica.pilota && q.raceId=r.raceId " + 
				"GROUP BY r.name,q.driverId " + 
				"ORDER BY r.name,q.driverId";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if(mappaGare.containsKey(rs.getString("r.name"))) {
				Gara tmpg=mappaGare.get(rs.getString("r.name"));
				if(tmpg.getPrestazioni().containsKey(rs.getInt("q.driverId"))) {
				tmpg.getPrestazioni().get(rs.getInt("q.driverId")).setQ2(Duration.ofMillis(rs.getLong("t2")));;
				}
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//estrarre i dati relativi alla q3 e inserirli nella mappa delle gare 
	public void loadAllQ3(Map<String,Gara> mappaGare){
		String sql ="SELECT r.name,q.driverId,q.number,AVG(qualifica.q3) AS t3 " + 
				"FROM races AS r,qualifying AS q, " + 
				"( " + 
				"SELECT r.raceId AS gara,q.driverId AS pilota,CAST(SUBSTRING_INDEX(q.q3,\":\",1) AS INT)*60000+CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(q.q3,\":\",-1),\".\",1) AS INT)*1000+CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(q.q3,\":\",-2),\".\",-1),\" \",1) AS INT) AS q3 " + 
				"FROM races AS r,qualifying AS q " + 
				"WHERE year(r.date)>2018 && r.raceId=q.raceId && q.q3<>\"NULL\" && q.q3<>0 " + 
				") AS qualifica " + 
				"WHERE r.raceId=qualifica.gara && q.driverId=qualifica.pilota && q.raceId=r.raceId " + 
				"GROUP BY r.name,q.driverId " + 
				"ORDER BY r.name,q.driverId";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if(mappaGare.containsKey(rs.getString("r.name"))) {
				Gara tmpg=mappaGare.get(rs.getString("r.name"));
				if(tmpg.getPrestazioni().containsKey(rs.getInt("q.driverId"))) {
				tmpg.getPrestazioni().get(rs.getInt("q.driverId")).setQ3(Duration.ofMillis(rs.getLong("t3")));;
				}
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//estrarre i dati relativi ai giri e inserirli nella mappa delle gare 
	public void loadAllGiri(Map<String,Gara> mappaGare){
		String sql ="SELECT r.name,l.driverId,ROUND(CEILING(l.lap/5)),avg(l.milliseconds) AS milli " + 
				"FROM races AS r,laptimes AS l " + 
				"WHERE r.raceId=l.raceId && year(r.date)>2018 " + 
				"GROUP BY r.name,l.driverId,Round(ceiling(l.lap/5)) "+
				"ORDER BY Round(ceiling(l.lap/5))";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if(mappaGare.containsKey(rs.getString("r.name"))) {
				Gara tmpg=mappaGare.get(rs.getString("r.name"));
				if(tmpg.getPrestazioni().containsKey(rs.getInt("l.driverId"))) {
				Prestazione tmpp=tmpg.getPrestazioni().get(rs.getInt("l.driverId"));
				tmpp.getTempigiro().add(Duration.ofMillis(rs.getLong("milli")));
				}
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	//trasformare una stringa in un tempo
	public Duration calcolaDuration(String stringa) {
		Duration tmp = null;
		Pattern pattern = Pattern.compile("[:\\.]");
		String string = stringa;	
		String[] risultato = pattern.split(string);
		int minuti = Integer.valueOf(risultato[0]);
		int secondi = Integer.valueOf(risultato[1]);
		int millisecondi = Integer.valueOf(risultato[2]);
		millisecondi=minuti*60000+secondi*1000;
		tmp=Duration.ofMillis(millisecondi);
		return tmp;
	}
	
	

}
