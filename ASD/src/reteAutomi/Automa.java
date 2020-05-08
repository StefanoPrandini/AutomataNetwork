package reteAutomi;
import java.util.ArrayList;

import static java.util.Objects.isNull;

public class Automa {
	private String id;
	private Stato statoIniziale;
	private ArrayList<Stato> stati;
	private ArrayList<Transizione> transizioni;
	private Stato statoCorrente;
	

	/**
	 * ATTENZIONE Lo stato iniziale è un doppione 
	 * @param id
	 * @param stati
	 * @param transizioni
	 * @param statoIniziale
	 */
	public Automa(String id, ArrayList<Stato> stati, ArrayList<Transizione> transizioni, Stato statoIniziale) {
		this.id = id;
		this.stati = stati;
		this.transizioni = transizioni;
		this.statoIniziale = statoIniziale;
		this.statoCorrente = statoIniziale;
	}


	/**
	 * Una transizione dotata di evento in ingresso è abilitata allo scatto solo se l'evento è disponibile sul link di provenienza
	 * @param t
	 * @param e
	 * @return
	 */
	public boolean transizioneAbilitata(Transizione t, Evento e){

		// se evento in ingresso della transizione è null, transizione è abilitata
		if (isNull(t.getEventoIngresso()) && t.getStatoPartenza().equals(this.statoCorrente)) return true;


		if(t.getStatoPartenza().equals(this.statoCorrente) && t.getEventoIngresso().equals(e)) return true;

		return false;
	}


	/**
	 * riceve un evento da un link, trova transizioni adatte in base a stato corrente e ritorna eventi in uscita da mettere su links
	 * @param evento
	 * @return
	 */
	public ArrayList<Evento> scattoTransizioneDaEvento(Evento evento){
		ArrayList<Evento> eventiUscita = new ArrayList<>();
		//Trova transizioni adatte

		for (Transizione t : transizioniPartentiDaStatoCorrente()) {

			if (t.getEventoIngresso().equals(evento)){
				eventiUscita.addAll(t.getEventiUscita());
			}
		}

		return eventiUscita;
	}

	public ArrayList<Transizione> transizioniPartentiDaStatoCorrente(){
		ArrayList<Transizione> result = new ArrayList<>();
		for (Transizione t : transizioni) {
			if (t.getStatoPartenza().equals(statoCorrente)){
				result.add(t);
			}
		}
		return result;
	}


	/**
	 * una transizione porta in un nuovo stato? non è chiaro dal testo, se sì, questo metodo esegue la transizione
	 * @param transizione
	 * @param evento
	 * @return int result code
	 */
	public int eseguiTransizione(Transizione transizione, Evento evento){
		if (transizioneAbilitata(transizione, evento)){
			this.statoCorrente = transizione.getStatoArrivo();
			//TODO
		}

		return 1;
	}






	public void inizializzaAutoma(){
		this.statoCorrente = statoIniziale;
	}

	public ArrayList<Transizione> getTransizioni(){
		return this.transizioni;
	}

	public Stato getStatoCorrente(){
		return this.statoCorrente;
	}

	public void aggiungiStato() {
	}

}
