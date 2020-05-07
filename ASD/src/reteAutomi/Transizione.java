package reteAutomi;

import java.util.ArrayList;

public class Transizione {
	private Stato statoPartenza;
	private Stato statoArrivo;
	private Evento eventoIngresso;
	private ArrayList<Evento> eventiUscita;

	
	private String etichettaRilevanza;
	private String etichettaOsservabilita;
	
	public Transizione(Stato statoPartenza, Stato statoArrivo, Evento eventoIn, ArrayList<Evento> eventiOut, String etichettaR, String etichettaO) {
		this.statoPartenza = statoPartenza;
		this.statoArrivo = statoArrivo;
		this.eventoIngresso = eventoIn;
		this.eventiUscita = eventiOut;
		this.etichettaRilevanza = etichettaR;
		this.etichettaOsservabilita = etichettaO;
	}



	public Stato getStatoPartenza(){
		return this.statoPartenza;
	}

	public Evento getEventoIngresso(){
		return this.eventoIngresso;
	}


	public Stato getStatoArrivo() {
		return statoArrivo;
	}

	public ArrayList<Evento> getEventiUscita() {
		return eventiUscita;
	}
}
