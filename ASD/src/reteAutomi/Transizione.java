package reteAutomi;

import java.util.ArrayList;

public class Transizione {
	StatoAutoma statoPartenza;
	StatoAutoma statoArrivo;
	Evento eventoIngresso;
	ArrayList<Evento> eventiUscita;
	String etichettaRilevanza;
	String etichettaOsservabilità;
	
	public Transizione(StatoAutoma statoPartenza, StatoAutoma statoArrivo, Evento eventoIn, ArrayList<Evento> eventiOut, String etichettaR, String etichettaO) {
		this.statoPartenza = statoPartenza;
		this.statoArrivo = statoArrivo;
		this.eventoIngresso = eventoIn;
		this.eventiUscita = eventiOut;
		this.etichettaRilevanza = etichettaR;
		this.etichettaOsservabilità = etichettaO;
	}

}
