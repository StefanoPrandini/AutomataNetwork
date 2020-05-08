package reteAutomi;

public class Link {
	private Automa automaPartenza;
	private Automa automaArrivo;
	private String evento;

	/**
	 * inizializza con Evento = null
	 * @param automaPartenza
	 * @param automaArrivo
	 */
	public Link(Automa automaPartenza, Automa automaArrivo) {
		this.automaPartenza = automaPartenza;
		this.automaArrivo = automaArrivo;
		evento = null;
	}

	public boolean scattoAbilitato(){
		return false;
		//automaPartenza.get
	}

	public String getEvento(){
		return this.evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}
}
