package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Integer> anni(){
		String sql = "SELECT DISTINCT(YEAR(reported_date)) AS y "
				+ "FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
		
		List<Integer> list = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try {
			  list.add(res.getInt("y"));
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println(res.getInt("id"));
			}
		}
		
		conn.close();
		Collections.sort(list);
		return list ;

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
}
	
	public List<String> categorie(){
		String sql = "SELECT DISTINCT(offense_category_id) "
				+ "FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
		
		List<String> list = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try {
			  list.add(res.getString("offense_category_id"));
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println(res.getInt("id"));
			}
		}
		
		conn.close();
		Collections.sort(list);
		return list ;

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
	}
	

	
	public List<String> vertici(int anno, String categoria){
		String sql = "SELECT DISTINCT(offense_type_id) "
				+ "FROM events "
				+ "WHERE YEAR(reported_date)=? AND offense_category_id=? "
				+ "ORDER BY offense_type_id" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setString(2, categoria);
		
		List<String> list = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try {
			  list.add(res.getString("offense_type_id"));
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println(res.getInt("id"));
			}
		}
		
		conn.close();
		Collections.sort(list);
		return list ;

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
	}
	
	public List<Adiacenza> getArchi(int anno, String categoria){
		String sql = "SELECT t1.t AS t1, t2.t AS t2,COUNT(DISTINCT(t1.d)) AS peso "
				+ "FROM "
				+ "(SELECT  offense_type_id AS t ,district_id AS d "
				+ "FROM events "
				+ "WHERE YEAR(reported_date)=? AND offense_category_id=? "
				+ "GROUP BY offense_type_id,district_id) t1, "
				+ "(SELECT  offense_type_id AS t ,district_id AS d "
				+ "FROM events "
				+ "WHERE YEAR(reported_date)=? AND offense_category_id=? "
				+ "GROUP BY offense_type_id,district_id) t2 "
				+ "WHERE t1.t<t2.t AND t1.d=t2.d "
				+ "GROUP BY t1.t,t2.t" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno);
			st.setString(2, categoria);
			st.setInt(3, anno);
			st.setString(4, categoria);
		
		List<Adiacenza> list = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try {
			 Adiacenza a=new Adiacenza(res.getString("t1"),res.getString("t2"),res.getDouble("peso"));
			 list.add(a);
			} catch (Throwable t) {
				t.printStackTrace();
				System.out.println(res.getInt("id"));
			}
		}
		
		conn.close();
		return list ;

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
	}
	
}


