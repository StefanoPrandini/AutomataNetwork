package reteAutomi;

import java.util.*;
import javafx.util.Pair;
import static java.util.Objects.isNull;

/**
 * Dizionario Completo delle osservazioni:
 * DFA (Automa a stati Finiti Deterministico) risultante dalla determinazione dell'intero spazio di rilevanza (tramite Subset Construction), in cui gli stati sono corredati
 * dalla loro diagnosi (insieme di tutte le decorazioni degli stati del NFA contenuti nello stato del DFA)
 *
 */
public class DizionarioCompleto {
	//mappo stati Rilevanza del DFA con coppie<etichettaO, statoArrivo>
	private Map<StatoRilevanzaReteDeterminizzata, Set<Pair<String, StatoRilevanzaReteDeterminizzata>>> mappaStatoRilevanzaDetTransizione;
	
	public DizionarioCompleto(SpazioRilevanza spazioRilevanza) {
		mappaStatoRilevanzaDetTransizione = new HashMap<>();
		determinazioneSpazio(spazioRilevanza);
	}
	
	

	// Lo spazio di rilevanza etichettato e' un NFA (Automa a stati Finiti Nondeterministico) nell’alfabeto Omega (etichette di osservabilita').
	// Esso puo' essere sottoposto all’operazione di determinizzazione per trasformarlo in un automa finito deterministico (DFA), tramite l'algoritmo SUBSET CONSTRUCTION
	private void determinazioneSpazio(SpazioRilevanza spazioRilevanza) {
		Queue<StatoRilevanzaReteDeterminizzata>coda = new LinkedList<>();
		// si parte dallo stato di rilevanza iniziale della rete
		Set<StatoRilevanzaRete>insiemeIniziale = new HashSet<>();
		insiemeIniziale.add(spazioRilevanza.getStatoRilevanzaIniziale());
		Set<StatoRilevanzaRete>epsClosureIniziale = epsClosure(spazioRilevanza, insiemeIniziale);
		StatoRilevanzaReteDeterminizzata statoIniziale = new StatoRilevanzaReteDeterminizzata(epsClosureIniziale);
		coda.add(statoIniziale);
		
		while(!coda.isEmpty()) {
			StatoRilevanzaReteDeterminizzata stato = coda.remove();
			// mappo le etichette di osservabilita' delle transizioni con gli stati di destinazione di tali transizioni (servono per calcolare eps-closure)
			Map<String, Set<StatoRilevanzaRete>>transizioniOsservabiliUscenti = cercaTransizioniOsservabiliUscenti(spazioRilevanza, stato);
			Set<Pair<String, StatoRilevanzaReteDeterminizzata>>coppieTransizione_NuovoStato = new HashSet<>();
			// per ogni etichetta osservabile delle transizioni uscenti, calcolo la epsClosure degli stati destinazione di tali transizioni
			for(String etichettaO : transizioniOsservabiliUscenti.keySet()) {
				Set<StatoRilevanzaRete>epsClosure = epsClosure(spazioRilevanza, transizioniOsservabiliUscenti.get(etichettaO));
				StatoRilevanzaReteDeterminizzata statoaArrivo = new StatoRilevanzaReteDeterminizzata(epsClosure);
				if(!mappaStatoRilevanzaDetTransizione.containsKey(statoaArrivo)) {
					coda.add(statoaArrivo);
				}
				coppieTransizione_NuovoStato.add(new Pair<>(etichettaO, statoaArrivo));
			}
			mappaStatoRilevanzaDetTransizione.put(stato, coppieTransizione_NuovoStato);
		}
		
	}
	
	private Map<String, Set<StatoRilevanzaRete>> cercaTransizioniOsservabiliUscenti(SpazioRilevanza spazioRilevanza, StatoRilevanzaReteDeterminizzata stato) {
		Map<String, Set<StatoRilevanzaRete>> mappa = new HashMap<>();
		for(StatoRilevanzaRete s : stato.getStatiRilevanza()) {
			Set<Pair<Transizione, StatoRilevanzaRete>> transizioniOsservabili = spazioRilevanza.getTransizioniOsservabili(s);
			//per ogni transizione osservabile uscente dagli stati nello stato del DFA, prendo lo stato di arrivo e lo associo all'etichetta di osservabilita' della transizioni, usando una mappa
			for(Pair<Transizione, StatoRilevanzaRete> transizione : transizioniOsservabili) {
				Set<StatoRilevanzaRete> statiArrivo;
				// se l'etichetta c'e' gia' come chiave, prendo la sua lista di stati di arrivo e aggiungo quello nuovo
				if(mappa.containsKey(transizione.getKey().getEtichettaOsservabilita())) {
					statiArrivo = mappa.get(transizione.getKey().getEtichettaOsservabilita());
					statiArrivo.add(transizione.getValue());
				}
				// se l'etichetta non c'era nella mappa, inserisco la coppia chiave valore
				else {
					statiArrivo = new HashSet<>();
					statiArrivo.add(transizione.getValue());
					mappa.put(transizione.getKey().getEtichettaOsservabilita(), statiArrivo);
				}
			}
		}
		
		return mappa;
	}



	private Set<StatoRilevanzaRete> epsClosure(SpazioRilevanza spazioRilevanza, Set<StatoRilevanzaRete> stati){
		//gli stati di partenza fanno parte della loro eps-closure
		Set<StatoRilevanzaRete> result = new HashSet<>(stati);
		//metto gli stati passati in una coda: dovro' aggiungere stati da verificare
		Queue<StatoRilevanzaRete>codaStati = new LinkedList<>(stati);
		
		while(!codaStati.isEmpty()) {
			StatoRilevanzaRete s = codaStati.remove();
			// prendo le transizioni uscenti dallo statoRilevanza dalla mappa nello spazioRilevanza
			for(Pair<Transizione, StatoRilevanzaRete> transizione : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(s)) {
				// se l'etichetta della transizione uscente e' eps (null)
				if(isNull(transizione.getKey().getEtichettaOsservabilita())) {
					// lo aggiungo alla coda per vedere se anche le sue transizioni uscenti hanno etichetta null: se si' le aggiungo alla eps-closure
					if(!codaStati.contains(s)) {
						codaStati.add(transizione.getValue());
						result.add(transizione.getValue());
					}
				}
			}
		}
		return result;
	}
	
	
	public Map<StatoRilevanzaReteDeterminizzata, Set<Pair<String, StatoRilevanzaReteDeterminizzata>>> getMappaStatoRilevanzaDetTransizione(){
		return this.mappaStatoRilevanzaDetTransizione;
	}
	
}
