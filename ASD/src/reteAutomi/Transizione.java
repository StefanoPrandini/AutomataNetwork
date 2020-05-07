package reteAutomi;

import java.util.ArrayList;

public class Transizione {
	private StatoAutoma statoPartenza;
	private StatoAutoma statoArrivo;
	private Evento eventoIngresso;
	private ArrayList<Evento> eventiUscita;

	
	private String etichettaRilevanza;
	private String etichettaOsservabilita;
	
	public Transizione(StatoAutoma statoPartenza, StatoAutoma statoArrivo, Evento eventoIn, ArrayList<Evento> eventiOut, String etichettaR, String etichettaO) {
		this.statoPartenza = statoPartenza;
		this.statoArrivo = statoArrivo;
		this.eventoIngresso = eventoIn;
		this.eventiUscita = eventiOut;
		this.etichettaRilevanza = etichettaR;
		this.etichettaOsservabilita = etichettaO;
	}



	public StatoAutoma getStatoPartenza(){
		return this.statoPartenza;
	}

	public Evento getEventoIngresso(){
		return this.eventoIngresso;
	}
}
