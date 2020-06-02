package reteAutomi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
/**
 * Spazio di rilevanza e' un automa -> grafo: StatiRilevanza sono i vertici e transizioni sono gli archi
 */
public class SpazioRilevanza2 {
	
	private List<Automa>automi;
	private List<Link>links;
	
	private Map<StatoRilevanzaReteAutomi, List<Transizione>> mappaStatoRilevanzaTransizioni;
	
	public SpazioRilevanza2(List<Automa>automi, List<Link>links) {
		this.automi = automi;
		this.links = links;
		this.mappaStatoRilevanzaTransizioni = new HashMap<>();
	}
	
	public SpazioRilevanza2 creaSpazioRilevanza() {
		Queue<StatoRilevanzaReteAutomi> coda = new LinkedList<>();
		StatoRilevanzaReteAutomi statoRilevanza = getStatoRilevanzaIniziale();
		coda.add(statoRilevanza);
		
		while(!coda.isEmpty()) {
			StatoRilevanzaReteAutomi stato = coda.remove();
			ArrayList<Transizione>transizioniAbilitate = getTransizioniAbilitate(stato);
			this.mappaStatoRilevanzaTransizioni.put(stato, transizioniAbilitate);
			
			for(Transizione t : transizioniAbilitate) {
				StatoRilevanzaReteAutomi nuovoStatoRilevanza = getStatoRilevanza(statoRilevanza, t);
				
			}
			while(transizioniAbilitate.size() > 0 ) {
				ArrayList<StatoRilevanzaReteAutomi>statiRilevanzaSuccessivi = getStatiRilevanzaSuccessivi();
			}

			for(StatoRilevanzaReteAutomi s : getStatiRilevanzaSuccessivi(stato)) {
				
			}

		}
		
		ArrayList<Transizione>transizioniAbilitate = getTransizioniAbilitate(stato);

		
		
		
		
		return this;
	}
	
	private StatoRilevanzaReteAutomi getStatoRilevanza(StatoRilevanzaReteAutomi statoRilevanza, Transizione t) {
		// TODO Auto-generated method stub
		return null;
	}

	private StatoRilevanzaReteAutomi getStatoRilevanzaIniziale() {
		ArrayList<Stato>statiIniziali = new ArrayList<>();
		for(Automa a : this.automi) {
			statiIniziali.add(a.getStatoIniziale());
		}
		ArrayList<Evento>eventiLink = new ArrayList<>();
		ArrayList<String>decorazione = new ArrayList<>();
		
		return new StatoRilevanzaReteAutomi("", statiIniziali, new ArrayList<Evento>(), decorazione);
	}
	
}
