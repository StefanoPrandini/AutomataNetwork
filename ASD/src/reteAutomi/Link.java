package reteAutomi;

public class Link {
	Automa automaIniziale;
	Automa automaFinale;
	
	public Link(Automa automaIniziale, Automa automaFinale) {
		this.automaIniziale = automaIniziale;
		this.automaFinale = automaFinale;
	}

}
