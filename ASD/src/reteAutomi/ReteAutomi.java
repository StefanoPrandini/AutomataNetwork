package reteAutomi;

import java.util.ArrayList;

public class ReteAutomi {
	
	private ArrayList<Automa> automi;
	private ArrayList<Link> links;
	
	public ReteAutomi(ArrayList<Automa> automi,	ArrayList<Link> links) {
		this.automi = automi;
		this.links = links;
	}


	/**
	 * alfabeto: insieme transizioni di tutti i componenti della rete
	 * linguaggio: insieme delle traiettorie della rete
	 * traiettoria della rete: sequenza di transizioni di componenti che porta dallo stato di rilevanza iniziale della rete a un altro stato
	 * @return automa deterministico finito
	 */
	public Automa calcolaSpazioRilevanza() {

		//start: stato di rilevanza iniziale, ogni componente è in stato iniziale, link sono tutti vuoti e insieme etichette rilevanza incontrate è vuoto
		//
		Etichette etichetteRilevanza = null;
		//
		
		return null;
	}

	/**
	 * porta automi della rete allo stato iniziale
	 */
	public void inizializzaAutomi(){
		for (Automa automa : automi) {
			automa.inizializzaAutoma();
		}
	}

	/**
	 * imposta gli eventi dei link a null
	 */
	public void inizializzaLinks(){
		for (Link link : links) {
			link.setEvento(null);
		}
	}
	
}
