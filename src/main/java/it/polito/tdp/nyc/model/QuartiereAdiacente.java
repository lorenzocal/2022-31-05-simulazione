package it.polito.tdp.nyc.model;

public class QuartiereAdiacente implements Comparable<QuartiereAdiacente>{
	
	private String city;
	private Double distanza;
	
	public QuartiereAdiacente(String city, double distanza) {
		super();
		this.city = city;
		this.distanza = distanza;
	}

	@Override
	public String toString() {
		return city + " distante " + distanza + " km.";
	}

	@Override
	public int compareTo(QuartiereAdiacente o) {
		return this.distanza.compareTo(o.distanza);
	}

	public String getCity() {
		return city;
	}

	public Double getDistanza() {
		return distanza;
	}
	
	
}
