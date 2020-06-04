package reteAutomi;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static java.util.Objects.isNull;

/**
 * Spazio di rilevanza e' un automa -> grafo: StatiRilevanza sono i vertici e transizioni sono gli archi
 */
public class SpazioRilevanza2 {
	private ReteAutomi ra;
	
	private Map<StatoRilevanzaRete, List<Transizione>> mappaStatoRilevanzaTransizioni;
	
	public SpazioRilevanza2(ReteAutomi ra) {
		this.ra = ra;
		this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<>();
	}
	
	public SpazioRilevanza2 creaSpazioRilevanza() {
		Queue<StatoRilevanzaRete> coda = new LinkedList<>();
		ArrayList<String>decorazioneIniziale = new ArrayList<>();
		//la rete deve essere nella condizione iniziale
		StatoRilevanzaRete statoIniziale = new StatoRilevanzaRete(ra, decorazioneIniziale);
		coda.add(statoIniziale);
		
		while(!coda.isEmpty()) {
			StatoRilevanzaRete statoRilevanza = coda.remove();
			
			// faccio andare la rete nella condizione descritta dallo statoRilevanza appena estratto, cosi' poi posso usare i metodi di ReteAutomi 
			// per cercare le transizioni abilitate e gli stati successivi
			ra.setReteAutomi(statoRilevanza);
			
			// ArrayList<Transizione>transizioniAbilitate = getTransizioniAbilitate(statoRilevanza);
			ArrayList<Transizione>transizioniAbilitate = ra.getTutteTransizioniAbilitate();
			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, transizioniAbilitate);
			
			for(Transizione t : transizioniAbilitate) {
				StatoRilevanzaRete nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(t, statoRilevanza.getDecorazione());
				// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
				if(!mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) {
					coda.add(nuovoStatoRilevanza);
				}
			}
		}
		
		return this;
	}
	
	/**
	private ArrayList<Transizione> getTransizioniAbilitate(StatoRilevanzaRete statoRilevanza) {
		for(int i=0; i<statoRilevanza.getStatiCorrentiAutoma().size(); i++) {
			int idAutoma = statoRilevanza.getStatiCorrentiAutoma().get(i).getKey();
			int idStato = statoRilevanza.getStatiCorrentiAutoma().get(i).getValue();
			Automa automa = null;
			for(Automa a : ra.getAutomi()) {
				if(a.getNome().equals(idAutoma)) {
					automa = a;
				}
			}
			ArrayList<Transizione>transizioniUscenti = automa.getTransizioniUscenti(idStato);
			
			ArrayList<Transizione>transizioniAbilitate = new ArrayList<>();
			for(Transizione t : transizioniUscenti) {
				Evento eIn = t.getEventoIngresso();
				ArrayList<Evento>eOut = t.getEventiUscita();
				// ... controlli gia' fatti in ReteAutomi
				
			}

		}
		return null;
	}
	*/

	private StatoRilevanzaRete calcolaStatoRilevanzaSucc(Transizione t, ArrayList<String> decorazione) {		
		
		ArrayList<Pair<Integer, Evento>> contenutoLinks = new ArrayList<>();
		ArrayList<Pair<Integer, Integer>> statiAutomi = new ArrayList<>();
		
		ra.svolgiTransizione(t);
		
		for(Link l : ra.getLinks()) {
			contenutoLinks.add(new Pair<>(l.getId(), l.getEvento()));
		}
		for(Automa a : ra.getAutomi()) {
			statiAutomi.add(new Pair<>(a.getId(), a.getStatoCorrente().getId()));
		}
		//aggiungo eventuale etichetta di rilevanza
		if (t.hasEtichettaRilevanza() && !decorazione.contains(t.getEtichettaRilevanza())){
			decorazione.add(t.getEtichettaRilevanza());
		}
		return new StatoRilevanzaRete(contenutoLinks, statiAutomi , decorazione);
		
		/**

		// link pieni ed eventi, stati dell'automa, decorazione


		//valuta esecuzione rispetto a statoRilevanza --> link liberi, automa nello stato giusto, eventi necessari presenti su link

		if (isEseguibile(t)){
			//svuoto links in entrata
			Evento inEntrata = t.getEventoIngresso();
			contenutoLinks.add(svolgiEventoInEntrata(inEntrata));

			//riempio links in uscita
			ArrayList<Evento> inUscita = t.getEventiUscita();
			contenutoLinks.addAll(linkUscitaAggiornati(inUscita));

			//aggiorno automa
			//ATTENZIONE, SIMULA
			ra.simulaPassaggioDiStato(t);
			for (Automa automa : ra.getAutomi()) {
				statiAutomi.add(new Pair<>(automa.getId(), automa.getStatoCorrente().getId()));
			}

			return new StatoRilevanzaRete(contenutoLinks, statiAutomi , decorazione);
		}

		return null;
		*/
	}


	/**
	 * precondizione: gli eventi sono destinati a link effettivamente liberi
	 * @param eventi
	 * @return

	private ArrayList<Pair<Integer, Evento>> linkUscitaAggiornati(ArrayList<Evento> eventi){
		ArrayList<Pair<Integer, Evento>> linkAggiornati = new ArrayList<>();

		for (Evento evento : eventi) {

			//se vogliamo effettivmanete svolgere le transizioni
			evento.getLink().aggiungiEvento(evento);

			//se vogliamo una cosa teorica, senza svolgere le transizioni
			linkAggiornati.add(new Pair<>(evento.getLink().getId(), evento));
		}
		return linkAggiornati;
	}


	private Pair<Integer, Evento> svolgiEventoInEntrata(Evento evento){
		Link daSvuotare = evento.getLink();
		if (!isNull(evento)){


			//se vogliamo eseguire effetivamente la transizione
			daSvuotare.svuota();


			//se invece vogliamo fare una cosa teorica

			return (new Pair<>(daSvuotare.getId(), null));
		}
		return new Pair<>(daSvuotare.getId(), null);
	}
	 */
	
}
