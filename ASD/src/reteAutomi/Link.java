package reteAutomi;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

public class Link {
	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;

	private Automa automaPartenza;
	private Automa automaArrivo;
	private Evento evento;

	/**
	 * inizializza con Evento = null
	 * @param automaPartenza
	 * @param automaArrivo
	 */
	public Link(Automa automaPartenza, Automa automaArrivo) {
		this.id = ai.incrementAndGet();
		this.automaPartenza = automaPartenza;
		this.automaArrivo = automaArrivo;
		evento = null;
	}

	public Evento getEvento(){
		return this.evento;
	}

	public void aggiungiEvento(Evento evento) {
		this.evento = evento;
	}

	public void svuota(){
		this.evento = null;
	}

	public boolean isVuoto(){
		return isNull(this.evento);
	}

	public void setAutomaPartenza(Automa automaPartenza) {
		this.automaPartenza = automaPartenza;
	}

	public void setAutomaArrivo(Automa automaArrivo) {
		this.automaArrivo = automaArrivo;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}


	/**
	 * non puo contenere una reference a evento, si genera una ricorsione infinita (stack overflow error)
	 * @return
	 */
	@Override
	public String toString() {
		return "Link{" +
				"id=" + id +
				", automaPartenza=" + automaPartenza.getId() +
				", automaArrivo=" + automaArrivo.getId() +
				'}';
	}

	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Link)) return false;
		Link link = (Link) o;
		return getId() == link.getId() &&
				automaPartenza.equals(link.automaPartenza) &&
				automaArrivo.equals(link.automaArrivo);
	}

	public Automa getAutomaPartenza() {
		return automaPartenza;
	}

	public Automa getAutomaArrivo() {
		return automaArrivo;
	}
}
