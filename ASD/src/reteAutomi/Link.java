package reteAutomi;

public class Link {
	private Automa automaPartenza;
	private Automa automaArrivo;
	private String evento;
	
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

}
