package reteAutomi;

import java.util.ArrayList;

public class Transizione {
	Stato statoIniziale;
	Stato statoFinale;
	String eventoIngresso;
	ArrayList<String> eventiUscita;
	String etichettaRilevanza;
	String etichettaOsservabilità;
	
	public Transizione(Stato statoIniziale, Stato statoFinale, String eventoIn, ArrayList<String> eventiOut, String etichettaR, String etichettaO) {
		this.statoIniziale = statoIniziale;
		this.statoFinale = statoFinale;
		this.eventoIngresso = eventoIn;
		this.eventiUscita = eventiOut;
		this.etichettaRilevanza = etichettaR;
		this.etichettaOsservabilità = etichettaO;
	}

}
