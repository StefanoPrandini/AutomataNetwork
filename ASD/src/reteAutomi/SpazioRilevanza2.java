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
	
	private List<Automa>automi;
	private List<Link>links;
	
	private Map<StatoRilevanzaReteAutomi, List<Transizione>> mappaStatoRilevanzaTransizioni;
	
	public SpazioRilevanza2(ReteAutomi ra) {
		this.ra = ra;
		this.automi = ra.getAutomi();
		this.links = ra.getLinks();
		this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<>();

	}
	
	public SpazioRilevanza2 creaSpazioRilevanza() {
		Queue<StatoRilevanzaReteAutomi> coda = new LinkedList<>();
		StatoRilevanzaReteAutomi statoIniziale = calcolaStatoRilevanzaIniziale();
		coda.add(statoIniziale);
		
		while(!coda.isEmpty()) {
			StatoRilevanzaReteAutomi statoRilevanza = coda.remove();
			ArrayList<Transizione>transizioniAbilitate = getTransizioniAbilitate(statoRilevanza);
			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, transizioniAbilitate);
			
			for(Transizione t : transizioniAbilitate) {
				StatoRilevanzaReteAutomi nuovoStatoRilevanza = calcolaStatoRilevanza(statoRilevanza, t);
				// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
				if(!mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) {
					coda.add(nuovoStatoRilevanza);
				}
			}
		}
		
		return this;
	}
	
	private ArrayList<Transizione> getTransizioniAbilitate(StatoRilevanzaReteAutomi statoRilevanza) {
		/*
		//NOTA
		//se si facesse ra.getTutteTransizioniAbilitate(); ?
		*/
		for(int i=0; i<statoRilevanza.getStatiCorrentiAutoma().size(); i++) {
			int idAutoma = statoRilevanza.getStatiCorrentiAutoma().get(i).getKey();
			int idStato = statoRilevanza.getStatiCorrentiAutoma().get(i).getValue();
			Automa automa = null;
			for(Automa a : this.automi) {
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

	private StatoRilevanzaReteAutomi calcolaStatoRilevanza(StatoRilevanzaReteAutomi statoRilevanza, Transizione t) {
		// link pieni ed eventi, stati dell'automa, decorazione


		//valuta esecuzione rispetto a statoRilevanza --> link liberi, automa nello stato giusto, eventi necessari presenti su link

		ArrayList<Pair<Integer, Evento>> contenutoLinks = new ArrayList<>();
		ArrayList<Pair<Integer, Integer>> statiAutomi = new ArrayList<>();
		ArrayList<String> decorazione = statoRilevanza.getDecorazione();
		if (isEseguibile(t)){
			//svuoto links in entrata
			Evento inEntrata = t.getEventoIngresso();
			contenutoLinks.add(svolgiEventoInEntrata(inEntrata));

			//riempio links in uscita
			ArrayList<Evento> inUscita = t.getEventiUscita();
			contenutoLinks.addAll(linkUscitaAggiornati(inUscita));

			//aggiungo eventuale etichetta di rilevanza
			if (t.hasEtichettaRilevanza() && !decorazione.contains(t.getEtichettaRilevanza())){
				decorazione.add(t.getEtichettaRilevanza());
			}


			//aggiorno automa
			//ATTENZIONE, SIMULA
			ra.simulaPassaggioDiStato(t);
			for (Automa automa : ra.getAutomi()) {
				statiAutomi.add(new Pair<>(automa.getId(), automa.getStatoCorrente().getId()));
			}

			return new StatoRilevanzaReteAutomi(contenutoLinks, statiAutomi , decorazione);
		}

		return null;
	}


	private boolean isEseguibile(Transizione t){

		//TODO valutare esecuzione rispetto a stato di rilevanza o ripsetto a stato attuale della rete di automi?
		return false;
	}

	/**
	 * precondizione: gli eventi sono destinati a link effettivamente liberi
	 * @param eventi
	 * @return
	 */
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


	private StatoRilevanzaReteAutomi calcolaStatoRilevanzaIniziale() {
		ArrayList<Stato>statiIniziali = new ArrayList<>();
		for(Automa a : this.automi) {
			statiIniziali.add(a.getStatoIniziale());
		}
		ArrayList<Evento>eventiLink = new ArrayList<>();
		ArrayList<String>decorazione = new ArrayList<>();
		
		return new StatoRilevanzaReteAutomi("", ra, decorazione);
	}
	
}
