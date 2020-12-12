package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Evento implements Serializable {
	private static final long serialVersionUID = 4223632259206990356L;
	
	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;
	private String nome;

	private Link link;

	
	public Evento(String nome, Link link) {
		this.id = ai.incrementAndGet();
		this.nome = nome;
		this.link = link;
	}


	public Link getLink() {
		return link;
	}
	
	public String getNome() {
		return nome;
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


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Evento)) return false;
		Evento evento = (Evento) o;
		return getNome().equals(evento.getNome()) &&
				getLink().equals(evento.getLink());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getNome(), getLink());
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
