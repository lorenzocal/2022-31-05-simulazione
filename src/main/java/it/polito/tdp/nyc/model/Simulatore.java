package it.polito.tdp.nyc.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;


import it.polito.tdp.nyc.db.NYCDao;
import it.polito.tdp.nyc.model.Evento.Tipologia;

public class Simulatore {

	
	private Integer nTecnici; //Immutabili
	private Model model;
	private NYCDao dao;
	private LocalDateTime start;
	private String provider;
	
	private String city; //Mutabili
	private PriorityQueue<Evento> queue;
	private List<String> visitedCities;
	private Integer tecniciDisponibili;
	private Integer visitedHotsposts;
	private Integer nHotspots;
	
	public Simulatore(String provider, String city, Integer nTecnici, Model model) {
		super();
		this.nTecnici = nTecnici;
		this.model = model;
		this.dao = new NYCDao();
		this.queue = new PriorityQueue<Evento>();
		this.visitedCities = new LinkedList<String>();
		this.start = LocalDateTime.of(2015, 1, 1, 0, 0);
		this.provider = provider;
		this.city = city;
	}
	
	public void revisionaQuartiere() {
		this.tecniciDisponibili = this.nTecnici;
		this.visitedHotsposts = 0;
		this.nHotspots = this.dao.counterHotspots(this.provider, this.city);
		if (this.nHotspots <= this.nTecnici) {
			for (Integer i=0; i < this.nHotspots; i++) {
				this.queue.add(new Evento(Tipologia.REVISIONE, this.start));
				this.tecniciDisponibili--;
			}
		}
		else {
			for (Integer i=0; i < this.nTecnici; i++) {
				this.queue.add(new Evento(Tipologia.REVISIONE, this.start));
				this.tecniciDisponibili--;
			}
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			Tipologia tipologia = e.getTipologia();
			LocalDateTime time = e.getTime();
			switch(tipologia) {
				case REVISIONE:
					System.out.println("Inizio revisione nel quartiere " + this.city + " al tempo " + time + " .");
					Duration durataRevisione;
					Double random = Math.random();
					if (random <= 0.9) {
						durataRevisione = Duration.of(10, ChronoUnit.MINUTES);
					}
					else {
						durataRevisione = Duration.of(25, ChronoUnit.MINUTES);
					}
					this.queue.add(new Evento(Tipologia.FINEREVISIONE, time.plus(durataRevisione)));		
					this.visitedHotsposts++;
					break;
				case FINEREVISIONE:
					System.out.println("Fine revisione nel quartiere " + this.city + " al tempo " + time + " .");
					this.tecniciDisponibili++;
					if (this.visitedHotsposts < this.nHotspots) {
						Long random1 = Double.valueOf(10*(1+Math.random())).longValue();
						Duration durataSpostamento = Duration.of(random1, ChronoUnit.MINUTES);
						this.queue.add(new Evento(Tipologia.REVISIONE, time.plus(durataSpostamento)));
						this.tecniciDisponibili--;
					}
					else {
						if (this.nTecnici == this.tecniciDisponibili) {
							this.visitedCities.add(this.city);
							QuartiereAdiacente next = this.model.daVisitare(city, visitedCities);
							if (next != null) {
								Duration tempoSpostamentoQuartiere = Duration.of(Double.valueOf(next.getDistanza()/50).longValue(), ChronoUnit.HOURS);
								this.city = next.getCity();
								this.start = time.plus(tempoSpostamentoQuartiere);
								this.revisionaQuartiere();
							}
							else {
								System.out.println("TERMINE DELLA SIMULAZIONE AL TEMPO " + time + " .");
							}
						}
					}
					break;
			}
		}
	}
	
}
