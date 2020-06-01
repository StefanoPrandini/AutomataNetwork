package reteAutomi;

import java.util.concurrent.atomic.AtomicInteger;
import static java.util.Objects.isNull;

public class Link {
	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;
	private String nome;

	private Automa automaPartenza;
	private Automa automaArrivo;
	private Evento evento;

	/**
	 * inizializza con Evento = null
	 * @param automaPartenza
	 * @param automaArrivo
	 */
	public Link(String nome, Automa automaPartenza, Automa automaArrivo) {
		this.id = ai.incrementAndGet();
		this.nome = nome;
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


	//overload
	public int getId() {
		return id;
	}


	public boolean equals(Link link) {
		return getId() == link.getId();
	}

	public Automa getAutomaPartenza() {
		return automaPartenza;
	}

	public Automa getAutomaArrivo() {
		return automaArrivo;
	}

	public String getNome() {
		return nome;
	}
}
