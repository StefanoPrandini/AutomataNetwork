package reteAutomi;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;

public class Transizione {

	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;

	private Stato statoPartenza;
	private Stato statoArrivo;
	private Evento eventoIngresso;
	private ArrayList<Evento> eventiUscita;

	
	private String etichettaRilevanza;
	private String etichettaOsservabilita;
	
	public Transizione(Stato statoPartenza, Stato statoArrivo, Evento eventoIn, ArrayList<Evento> eventiOut, String etichettaR, String etichettaO) {
		this.id = ai.incrementAndGet();
		this.statoPartenza = statoPartenza;
		this.statoArrivo = statoArrivo;
		this.eventoIngresso = eventoIn;
		this.eventiUscita = eventiOut;
		this.etichettaRilevanza = etichettaR;
		this.etichettaOsservabilita = etichettaO;
	}


	/**
	 * una transizione è sempre abilitata allo scatto se evento in ingresso ed eventi in uscita sono null
	 * @return boolean flag
	 */
	public boolean transizioneSempreAbilitataAlloScatto(){

		if (isNull(this.eventoIngresso) && isNull(this.eventiUscita)) return true;

		return false;
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
