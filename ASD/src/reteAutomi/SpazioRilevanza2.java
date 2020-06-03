package reteAutomi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
					coda.add(statoRilevanza);
				}
			}
		}
		
		return this;
	}
	
	private ArrayList<Transizione> getTransizioniAbilitate(StatoRilevanzaReteAutomi statoRilevanza) {
		
		return null;
	}

	private StatoRilevanzaReteAutomi calcolaStatoRilevanza(StatoRilevanzaReteAutomi statoRilevanza, Transizione t) {
		// link pieni, stati dell'automa

		
		return null;
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
