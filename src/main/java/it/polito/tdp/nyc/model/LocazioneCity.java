package it.polito.tdp.nyc.model;

import com.javadocmd.simplelatlng.LatLng;

public class LocazioneCity {
	
	private String city;
	private LatLng posizione;
	
	public LocazioneCity(String city, Double latitudine, Double longitudine) {
		this.city = city;
		this.posizione = new LatLng(latitudine, longitudine);
	}

	public String getCity() {
		return city;
	}

	public LatLng getPosizione() {
		return posizione;
	}
	
	
	
}
