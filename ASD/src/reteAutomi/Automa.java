package reteAutomi;
import java.util.ArrayList;

public class Automa {
	private String id;
	private StatoAutoma statoIniziale;
	private ArrayList<StatoAutoma> stati;
	private ArrayList<Transizione> transizioni;
	private StatoAutoma statoCorrente;
	

	/**
	 * ATTENZIONE Lo stato iniziale è un doppione 
	 * @param id
	 * @param stati
	 * @param transizioni
	 * @param statoIniziale
	 */
	public Automa(String id, ArrayList<StatoAutoma> stati, ArrayList<Transizione> transizioni, StatoAutoma statoIniziale) {
		this.id = id;
		this.stati = stati;
		this.transizioni = transizioni;
		this.statoIniziale = statoIniziale;
		this.statoCorrente = statoIniziale;
	}


	public boolean isTransizioneAbilitata(Transizione t, Evento e){

		if(t.getStatoPartenza().equals(this.statoCorrente) && t.getEventoIngresso().equals(e)) return true;

		return false;
	}

	public ArrayList<Transizione> getTransizioni(){
		return this.transizioni;
	}

	public StatoAutoma getStatoCorrente(){
		return this.statoCorrente;
	}

	public void aggiungiStato() {
	}

}
