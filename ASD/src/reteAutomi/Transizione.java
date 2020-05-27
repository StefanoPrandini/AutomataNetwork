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
		this.eventiUscita = new ArrayList<>();
		if (!isNull(eventiOut)){
			eventiUscita = eventiOut;
		}
		this.etichettaRilevanza = etichettaR;
		this.etichettaOsservabilita = etichettaO;
	}



	/**
	 * una transizione è sempre abilitata allo scatto se evento in ingresso ed eventi in uscita sono null
	 * @return boolean flag
	 */
	public boolean isSempreAbilitataAlloScatto(){

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

	public int getId() {
		return id;
	}

	public void setStatoPartenza(Stato statoPartenza) {
		this.statoPartenza = statoPartenza;
	}

	public void setStatoArrivo(Stato statoArrivo) {
		this.statoArrivo = statoArrivo;
	}

	public void setEventoIngresso(Evento eventoIngresso) {
		this.eventoIngresso = eventoIngresso;
	}

	public void setEventiUscita(ArrayList<Evento> eventiUscita) {
		this.eventiUscita = eventiUscita;
	}

	public void addEventoUscita(Evento e){
		if (!this.eventiUscita.contains(e))
			this.eventiUscita.add(e);
	}

	public void setEtichettaRilevanza(String etichettaRilevanza) {
		this.etichettaRilevanza = etichettaRilevanza;
	}

	public void setEtichettaOsservabilita(String etichettaOsservabilita) {
		this.etichettaOsservabilita = etichettaOsservabilita;
	}

	@Override
	public String toString() {

		if (!isNull(eventoIngresso))
			return "Transizione{" +
					"id=" + id +
					"(in " + statoPartenza.getId() +
					", out " + statoArrivo.getId() +
					", eIn " + eventoIngresso.getId() +
					')'+ "\n";
		else return "Transizione{" +
				"id=" + id +
				"(in " + statoPartenza.getId() +
				", out " + statoArrivo.getId() +
				", eIn " + null +
				')'+ "\n";
	}



	public String toStringCompleto() {
		return "Transizione{" +
				"id=" + id +
				", statoPartenza=" + statoPartenza +
				", statoArrivo=" + statoArrivo +
				", eventoIngresso=" + eventoIngresso +
				", eventiUscita=" + eventiUscita +
				", etichettaRilevanza='" + etichettaRilevanza + '\'' +
				", etichettaOsservabilita='" + etichettaOsservabilita + '\'' +
				'}'+ "\n";
	}



	public boolean hasEtichettaRilevanza(){
		return !isNull(this.etichettaRilevanza);
	}

	public boolean haEtichettaOsservabilita(){
		return !isNull(this.etichettaOsservabilita);
	}

	public String getEtichettaRilevanza() {
		return etichettaRilevanza;
	}

	public String getEtichettaOsservabilita() {
		return etichettaOsservabilita;
	}
}
