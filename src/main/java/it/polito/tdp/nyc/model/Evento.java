package it.polito.tdp.nyc.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Evento implements Comparable<Evento>{
		
	public enum Tipologia{
		REVISIONE,
		FINEREVISIONE,
	}
	
	private Tipologia tipologia;
	private LocalDateTime time;
	
	public Evento(Tipologia tipologia, LocalDateTime time) {
		super();
		this.tipologia = tipologia;
		this.time = time;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}


	public LocalDateTime getTime() {
		return time;
	}

	@Override
	public int compareTo(Evento o) {
		return this.time.compareTo(o.time);
	}
	
	
}
