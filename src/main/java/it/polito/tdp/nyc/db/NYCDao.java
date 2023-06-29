package it.polito.tdp.nyc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.nyc.model.Hotspot;
import it.polito.tdp.nyc.model.LocazioneCity;

public class NYCDao {
	
	public List<Hotspot> getAllHotspot(){
		String sql = "SELECT * FROM nyc_wifi_hotspot_locations";
		List<Hotspot> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Hotspot(res.getInt("OBJECTID"), res.getString("Borough"),
						res.getString("Type"), res.getString("Provider"), res.getString("Name"),
						res.getString("Location"),res.getDouble("Latitude"),res.getDouble("Longitude"),
						res.getString("Location_T"),res.getString("City"),res.getString("SSID"),
						res.getString("SourceID"),res.getInt("BoroCode"),res.getString("BoroName"),
						res.getString("NTACode"), res.getString("NTAName"), res.getInt("Postcode")));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public List<String> getAllProviders(){
		String sql = "SELECT DISTINCT Provider "
				+ "FROM nyc_wifi_hotspot_locations ";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("Provider"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}

	public List<String> getAllVertexes(String provider){
		String sql = "SELECT DISTINCT City "
				+ "FROM nyc_wifi_hotspot_locations "
				+ "WHERE Provider = ? ";
		List<String> result = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, provider);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(res.getString("City"));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public Map<String, LocazioneCity> getAllLocazioneCity(String provider){
		String sql = "SELECT City, AVG(Latitude) AS Latitudine, AVG(Longitude) AS Longitudine "
				+ "FROM nyc_wifi_hotspot_locations "
				+ "WHERE Provider = ? "
				+ "GROUP BY City ";
		Map<String, LocazioneCity> result = new HashMap<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, provider);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				String city = res.getString("City");
				double latitudine = res.getDouble("Latitudine");
				double longitudine = res.getDouble("Longitudine");
				result.put(city, new LocazioneCity(city, latitudine, longitudine));
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
	
	public Integer counterHotspots(String provider, String city) {
		String sql = "SELECT COUNT(*) AS result "
				+ "FROM nyc_wifi_hotspot_locations "
				+ "WHERE Provider = ? "
				+ "AND City = ?";
		Integer result = 0;
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, provider);
			st.setString(2, city);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result = res.getInt("result");
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Error");
		}

		return result;
	}
}
