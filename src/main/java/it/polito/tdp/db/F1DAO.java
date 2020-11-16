package it.polito.tdp.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	public Map<Integer,Pilota> loadAllPiloti(){
		String sql = "SELECT d.driverId,d.driverRef,d.number,d.code,d.forename,d.surname,d.dob,d.nationality,pp.punteggio,COUNT(r.raceId) as gare,COUNT(r.position) as garefinite " + 
				"FROM drivers AS d,punteggiopiloti AS pp,results AS r " + 
				"WHERE d.driverId=pp.driverId && r.driverId=d.driverId " + 
				"GROUP BY d.driverId";
		Map<Integer, Pilota> result = new HashMap<Integer,Pilota>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Pilota tmp=new Pilota(rs.getInt("d.driverId"),rs.getString("d.driverRef"),rs.getInt("d.number"),
						rs.getString("d.code"),rs.getString("d.forename"),rs.getString("d.surname"),rs.getDate("d.dob").toLocalDate(),
						rs.getString("d.nationality"),rs.getInt("pp.punteggio"),rs.getInt("gare"),rs.getInt("garefinite"));
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
	
	public Map<Integer,Gara> loadAllGara(Map<Integer,Circuito> circuitiMap){
		String sql = "SELECT r.raceId,r.round,r.circuitId,r.name,r.date,MAX(rs.laps) as lap " + 
				"FROM races AS r, results AS rs " + 
				"WHERE r.raceId=rs.raceId && YEAR(r.date)>2018 " + 
				"GROUP BY r.raceId";
		Map<Integer,Gara> result = new HashMap<Integer,Gara>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Circuito tmpc= circuitiMap.get(rs.getInt("r.circuitId"));
				Gara tmpg=new Gara(rs.getInt("r.raceId"),tmpc,rs.getDate("r.date").toLocalDate(),
						rs.getInt("r.round"),rs.getString("r.name"),rs.getInt("lap"));
				result.put(tmpg.getRaceId(),tmpg);
			}
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public void loadAllPosizioniI(Map<Integer,Gara> garaMap,Map<Integer,Pilota> pilotaMap){
		String sql ="SELECT rs.raceId,rs.driverId,rs.grid,rs.positionOrder "+ 
				"FROM results AS rs, races AS r "+
				"WHERE rs.raceId=r.raceId && YEAR(r.date)>2018 "+
				"ORDER BY raceId";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if(pilotaMap.containsKey(rs.getInt("driverId"))) {
				Gara tmpg= garaMap.get(rs.getInt("raceId"));
				Pilota tmpp= pilotaMap.get(rs.getInt("driverId"));
				tmpg.getPosizioniI().put(rs.getInt("grid"),tmpp);
				tmpg.getPosizioniF().put(rs.getInt("positionOrder"),tmpp);
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	public void loadAllGiri(Map<Integer,Gara> garaMap,Map<Integer,Pilota> pilotaMap, Map<Integer,Scuderia> scuderiaMap){
		String sql = "SELECT lpt.raceId,lpt.driverId,q.constructorId,lpt.lap,lpt.time,q.q1,q.q2,q.q3 " + 
				"FROM laptimes AS lpt, qualifying AS q, races AS r " + 
				"WHERE lpt.raceId=q.raceId && lpt.driverId=q.driverId && r.raceId=lpt.raceId && Year(r.date)>2018 " + 
				"ORDER BY lpt.raceId,lpt.driverId,lap";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Gara tmpg= garaMap.get(rs.getInt("lpt.raceId"));
				Pilota tmpp= pilotaMap.get(rs.getInt("lpt.driverId"));
				Scuderia tmps= scuderiaMap.get(rs.getInt("q.constructorId"));
				if(pilotaMap.containsValue(tmpp)) {
					Pattern pattern = Pattern.compile("[:\\.]");
				    LocalTime q1=LocalTime.of(0, 0, 0, 0);
				    LocalTime q2=LocalTime.of(0, 0, 0, 0);
				    LocalTime q3=LocalTime.of(0, 0, 0, 0);
				    if(rs.getString("q.q1")!=null) {
				    if(!rs.getString("q.q1").equals("")) {
				    String string = rs.getString("q.q1");	
					String[] result = pattern.split(string);
					int minuti = Integer.valueOf(result[0]);
					int secondi = Integer.valueOf(result[1]);
					int millisecondi = Integer.valueOf(result[2]);
					q1= LocalTime.of(0, minuti, secondi, millisecondi*1000000);
				    }
				    }
				    if(rs.getString("q.q2")!=null) {
				    String string = rs.getString("q.q2");
				    if(!string.equals("null")) {
					    if(!rs.getString("q.q2").equals("")) {
				    String[] result = pattern.split(string);
					int minuti = Integer.valueOf(result[0]);
					int secondi = Integer.valueOf(result[1]);
				    int millisecondi = Integer.valueOf(result[2]);
					q2= LocalTime.of(0, minuti, secondi, millisecondi*1000000);
				    }
				    }
				    }
				    if(rs.getString("q.q3")!=null) {
				    String string = rs.getString("q.q3");
				    if(!string.equals("null")) {
					    if(!rs.getString("q.q3").equals("")) {
				    String[] result = pattern.split(string);
					int minuti = Integer.valueOf(result[0]);
					int secondi = Integer.valueOf(result[1]);
				    int millisecondi = Integer.valueOf(result[2]);
					q3= LocalTime.of(0, minuti, secondi, millisecondi*1000000);
				    }
				    }
				    }
					Prestazione p=new Prestazione(tmpp,tmps,q1,q2,q3);
					tmpg.getPrestazioni().add(p);
					String string = rs.getString("lpt.time");
					String[] result = pattern.split(string);
					int minuti = Integer.valueOf(result[0]);
					int secondi = Integer.valueOf(result[1]);
				    int millisecondi = Integer.valueOf(result[2]);
					LocalTime lap= LocalTime.of(0, minuti, secondi, millisecondi*1000000);
					p.getTempigiro().add(lap);
			}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	

	
	

}
