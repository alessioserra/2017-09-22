package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.formulaone.model.Race;
import it.polito.tdp.formulaone.model.Season;

public class FormulaOneDAO {

	public List<Season> getAllSeasons() {
		String sql = "SELECT year, url FROM seasons ORDER BY year";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			List<Season> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Season(rs.getInt("year"), rs.getString("url")));
			}
			conn.close();
			return list;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Race> getRaces(Season year) {
		
		String sql = "SELECT * FROM races WHERE year=?";
		List<Race> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, year.getYear());
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				
				if ( rs.getTime("time")==null ) {
				Race r = new Race(rs.getInt("raceId"), null, rs.getInt("round"), rs.getInt("circuitId"), rs.getString("name"), rs.getDate("date").toLocalDate(), null, rs.getString("url"));	
				list.add(r);
				}
				
				else {
					Race r = new Race(rs.getInt("raceId"), null, rs.getInt("round"), rs.getInt("circuitId"), rs.getString("name"), rs.getDate("date").toLocalDate(), rs.getTime("time").toLocalTime(), rs.getString("url"));	
					list.add(r);
				}
			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		    System.out.println(e);
		}
		return list;
	}
	
	public int getWight(int raceID) {
	
	   final String sql="SELECT COUNT(driverId) AS cnt FROM results WHERE raceId=? AND statusId=1";
	   
	   int result=0;
	   
	   try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, raceID);
			
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {

				result = rs.getInt("cnt");

			}
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		    System.out.println(e);
		}
		return result;
}
	
}
