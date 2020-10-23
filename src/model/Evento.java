package model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Evento implements Serializable {

	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;
	private String nome;

	private Link link;

	
	public Evento(String nome, Link link) {
		this.id = ai.incrementAndGet();
		this.nome = nome;
		this.link = link;
	}

	public int getId() {
		return id;
	}

	public Link getLink() {
		return link;
	}
	
	public String getNome() {
		return nome;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public void svolgiEvento(){
		link.svuota();
	}

	public void aggiungiEventoSuLink(){
		link.aggiungiEvento(this);
	}


	//overload
	public boolean equals(Evento o) {
		if(o == null) return false;
		return this.nome.equals(o.getNome());
	}

	@Override
	public String toString() {
		return "Evento{" +
				"id=" + id +
				", nome=" + nome +
				", link=" + link.getNome() +
				'}';
	}
}
