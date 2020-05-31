package reteAutomi;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Evento {

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Evento)) return false;
		Evento evento = (Evento) o;
		return getId() == evento.getId() && Objects.equals(getLink(), evento.getLink());
	}

	@Override
	public String toString() {
		return "Evento{" +
				"id=" + id +
				", link=" + link.getId() +
				'}';
	}
}
