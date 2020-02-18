package reteAutomi;
import java.util.ArrayList;

public class Automa {
	
	Stato statoIniziale;
	ArrayList<Stato> stati;
	ArrayList<Transizione> transizioni;
	Stato statoCorrente;
	
	public Automa(ArrayList<Stato> stati, ArrayList<Transizione> transizioni, Stato statoIniziale) {
		this.stati = stati;
		this.transizioni = transizioni;
		this.statoIniziale = statoIniziale;
		this.statoCorrente = statoIniziale;;;
	}

}
