package reteAutomi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.isNull;

public class ReteAutomi {
	
	private ArrayList<Automa> automi;
	private ArrayList<Link> links;
	
	public ReteAutomi(ArrayList<Automa> automi,	ArrayList<Link> links) {
		this.automi = automi;
		this.links = links;
		inizializzaRete();
	}


	/**
	 * inizializza automi e links
	 */
	public void inizializzaRete(){
		inizializzaAutomi();
		inizializzaLinks();
	}

	/**
	 * genero una hashmap usando come chiave id dell'automa e con contenuto la lista delle transizioni eseguibili
	 * @return
	 */
	public HashMap<Integer, List<Integer>> mappaAutomiTransizioniAbilitate(){
		HashMap<Integer, List<Integer>> result = new HashMap<>();
		for (Automa automa : automi) {
			//transizioni disponibili nello stato corrente
			ArrayList<Transizione> transizioniAbilitate = automa.getTransizioniAbilitate();
			//lista, da riempire, delle transizioni con eventi disponibili allo scatto
			ArrayList<Integer> idTransizioniConEventiAbilitati = new ArrayList<>();
			for (Transizione transizione : transizioniAbilitate) {
				//se la transizione è con eventi in entrata e in uscita null
				if (transizione.isSempreAbilitataAlloScatto()){
					idTransizioniConEventiAbilitati.add(transizione.getId());
				}
				//se l'evento in ingresso è null o corrisponde a quello presente sul link indicato dall'evento
				else if (transizione.getEventoIngresso().equals(transizione.getEventoIngresso().getLink().getEvento())
						|| isNull(transizione.getEventoIngresso())){
					//controllo che i link di uscita siano vuoti
					boolean linkUscitaDisponibili = true;
					for (Evento e : transizione.getEventiUscita()) {
						if (!e.getLink().isVuoto()){
							linkUscitaDisponibili = false;
							break;
						}
					}
					//se i link di uscita degli eventi sono vuoti allora aggiungo alla lista delle transizioni che possono scattare
					if (linkUscitaDisponibili){
						idTransizioniConEventiAbilitati.add(transizione.getId());
					}
				}
			}
			//aggiungo alla mappa l'automa e le rispettive transizioni che possono scattare
			result.put(automa.getId(), idTransizioniConEventiAbilitati);
		}
		return result;
	}

	/**
	 * precondizione: la transizione deve essere eseguibile
	 * @param t
	 */
	public void svolgiTransizione(Automa a, Transizione t){
		//se l'evento in ingresso non è null lo svolgo
		if (!isNull(t.getEventoIngresso())){
			t.getEventoIngresso().svolgiEvento();
		}
		//se gli eventi in uscita non sono null li aggiungo ai link dedicati
		if (!isNull(t.getEventiUscita())){
			for (Evento evento : t.getEventiUscita()) {
				evento.aggiungiEventoSuLink();
			}
		}
		//eseguo il passaggio di stato sull'automa
		a.eseguiTransizione(t);
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
			link.aggiungiEvento(null);
		}
	}

	/**
	 * alfabeto: insieme transizioni di tutti i componenti della rete
	 * linguaggio: insieme delle traiettorie della rete
	 * traiettoria della rete: sequenza di transizioni di componenti che porta dallo stato di rilevanza iniziale della rete a un altro stato
	 * @return automa deterministico finito
	 */
	public Automa calcolaSpazioRilevanza() {

		//start: stato di rilevanza iniziale, ogni componente è in stato iniziale, link sono tutti vuoti e insieme etichette rilevanza incontrate è vuoto
		//l'insieme delle etichette di rilevanza è anche detto decorazione
		ArrayList<Transizione> transizioni = new ArrayList<>();
		Etichette etichetteRilevanza = null;
		//per ogni transizione che scatta, se è rilevante, si inserisce l'etichetta di t nella decorazione
		return null;
	}
	
}
