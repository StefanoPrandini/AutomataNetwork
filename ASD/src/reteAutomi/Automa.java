package reteAutomi;
import java.util.ArrayList;

public class Automa {
	String id;
	StatoAutoma statoIniziale;
	ArrayList<StatoAutoma> stati;
	ArrayList<Transizione> transizioni;
	StatoAutoma statoCorrente;
	
	public Automa(String id, ArrayList<StatoAutoma> stati, ArrayList<Transizione> transizioni, StatoAutoma statoIniziale) {
		this.id = id;
		this.stati = stati;
		this.transizioni = transizioni;
		this.statoIniziale = statoIniziale;
		this.statoCorrente = statoIniziale;
	}
	
	public void aggiungiStato() {
		
	}

}
