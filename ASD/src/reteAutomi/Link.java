package reteAutomi;

public class Link {
	Automa automaIniziale;
	Automa automaFinale;
	String evento;
	
	public Link(Automa automaIniziale, Automa automaFinale) {
		this.automaIniziale = automaIniziale;
		this.automaFinale = automaFinale;
		evento = null;
	}

}
