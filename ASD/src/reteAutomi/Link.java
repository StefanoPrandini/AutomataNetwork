package reteAutomi;

public class Link {
	Automa automaPartenza;
	Automa automaArrivo;
	String evento;
	
	public Link(Automa automaPartenza, Automa automaArrivo) {
		this.automaPartenza = automaPartenza;
		this.automaArrivo = automaArrivo;
		evento = null;
	}

}
