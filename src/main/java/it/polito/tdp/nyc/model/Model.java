package it.polito.tdp.nyc.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private NYCDao dao;
	private SimpleWeightedGraph<String, DefaultWeightedEdge> grafo;
	
	
	public Model() {
		this.dao = new NYCDao();
	}

	public List<String> getAllProviders(){
		return dao.getAllProviders();
	}

	public NYCDao getDao() {
		return dao;
	}

	public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	public List<String> getAllVertexes(String provider){
		return dao.getAllVertexes(provider);
	}
	
	public void creaGrafo(String provider) {
		this.grafo = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.getAllVertexes(provider));
		Map<String, LocazioneCity> mappa = this.dao.getAllLocazioneCity(provider);
		for (String v1 : this.grafo.vertexSet()) {
			for (String v2 : this.grafo.vertexSet()) {
				if (v1.compareTo(v2) < 0) {
					LatLng pos1 = mappa.get(v1).getPosizione();
					LatLng pos2 = mappa.get(v2).getPosizione();
					double distanza = LatLngTool.distance(pos1, pos2, LengthUnit.KILOMETER);
					Graphs.addEdge(this.grafo, v1, v2, distanza);
				}
			}
		}
	}
	
	public List<QuartiereAdiacente> quartieriAdiacenti(String city){
		List<String> adiacenti = Graphs.neighborListOf(this.grafo, city);
		List<QuartiereAdiacente> result = new LinkedList<>();
		for (String adiacente : adiacenti) {
			DefaultWeightedEdge edge = this.grafo.getEdge(city, adiacente);
			result.add(new QuartiereAdiacente(adiacente, this.grafo.getEdgeWeight(edge)));
		}
		Collections.sort(result);
		return result;
	}
	
	public QuartiereAdiacente daVisitare(String city, List<String> giaVisitate) {
		for (QuartiereAdiacente qa : this.quartieriAdiacenti(city)) {
			if (!giaVisitate.contains(qa.getCity())) {
				return qa;
			}
		}
		return null;
	}
}
