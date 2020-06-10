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
	//tengo un insieme di tutti gli stati di rilevanza della rete determinizzata per evitare di riferirmi a stati uguali che sono oggetti diversi
	private Set<StatoDizionario> statiDizionario;
	//mappo stati Rilevanza del DFA con coppie<etichettaO, statoArrivo>
	private Map<StatoDizionario, Set<Pair<String, StatoDizionario>>> mappaDizionario;
	private StatoDizionario statoIniziale;
	
	public DizionarioCompleto(SpazioRilevanza spazioRilevanza) {
		this.statiDizionario = new LinkedHashSet<>(); // insieme con elementi in ordine di inserimento
		this.mappaDizionario = new LinkedHashMap<>(); // mappa con chiavi in ordine di inserimento
		determinizzazioneSpazio(spazioRilevanza);
	}


	// Lo spazio di rilevanza etichettato e' un NFA (Automa a stati Finiti Nondeterministico) nell'alfabeto Omega (etichette di osservabilita').
	// Esso puo' essere sottoposto all'operazione di determinizzazione per trasformarlo in un automa finito deterministico (DFA), tramite l'algoritmo SUBSET CONSTRUCTION
	private void determinizzazioneSpazio(SpazioRilevanza spazioRilevanza) {
		//StatoRilevanzaReteDeterminizzata contiene un insieme di StatoRilevanzaRete
		Queue<StatoDizionario>coda = new LinkedList<>();
		// si parte dallo stato di rilevanza iniziale della rete
		Set<StatoRilevanzaRete>insiemeIniziale = new HashSet<>();
		insiemeIniziale.add(spazioRilevanza.getStatoRilevanzaIniziale());
		// la eps-closure iniziale e' l'insieme degli stati da mettere nello StatoRilevanzaReteDeterminizzata iniziale
		Set<StatoRilevanzaRete>epsClosureIniziale = epsClosure(spazioRilevanza, insiemeIniziale);
		Set<StatoRilevanzaRete>outputIniziale = new HashSet<>();
		for(StatoRilevanzaRete s : epsClosureIniziale) {
			if(!spazioRilevanza.getTransizioniOsservabili(s).isEmpty()) {
				outputIniziale.add(s);
			}
		}
		StatoDizionario statoIniziale = new StatoDizionario(epsClosureIniziale, outputIniziale);
		this.statoIniziale = statoIniziale;
		coda.add(statoIniziale);
		
		while(!coda.isEmpty()) {
			StatoDizionario stato = coda.remove();
			statiDizionario.add(stato);
			// mappo le etichette di osservabilita' delle transizioni con gli stati di destinazione di tali transizioni (servono per calcolare eps-closure)
			// cosi' ho una associazione tra le etichette di osservabilita' e gli stati in cui portano (che dovranno essere raggruppati)
			Map<String, Set<StatoRilevanzaRete>>transizioniOsservabiliUscenti = cercaTransizioniOsservabiliUscenti(spazioRilevanza, stato);
			Set<Pair<String, StatoDizionario>>coppieTransizione_NuovoStato = new HashSet<>();
			
			// per ogni etichetta osservabile delle transizioni uscenti, calcolo la epsClosure degli stati destinazione di tali transizioni
			for(String etichettaO : transizioniOsservabiliUscenti.keySet()) {
				// input del nuovo stato del dizionario = stati destinazione delle transizioni osservabili uscenti da un altro stato del dizionario
				Set<StatoRilevanzaRete>input = transizioniOsservabiliUscenti.get(etichettaO);
				Set<StatoRilevanzaRete>epsClosure = epsClosure(spazioRilevanza, input);
				// cerco stati output dello stato del dizionario tra gli stati di rilevanza che lo compongono
				Set<StatoRilevanzaRete>output = new HashSet<>();
				for(StatoRilevanzaRete s : epsClosure) {
					if(!spazioRilevanza.getTransizioniOsservabili(s).isEmpty()) {
						output.add(s);
					}
				}
				// se ho gia' incontrato questo stato del dizionario, ritorno quello e non ne aggiungo uno nuovo
				// stati destinazione delle transizioni osservabili uscenti dallo stato precedente del dizionario sono gli stati Input del nuovo stato del dizionario
				// stati nella eps-closure che hanno transizioni osservabili uscenti sono gli stati Output del nuovo stato del dizionario
				StatoDizionario statoArrivo = new StatoDizionario(epsClosure, output);
				for(StatoDizionario statoGiaIncontrato : statiDizionario) {
					if(statoGiaIncontrato.equals(statoArrivo)) {
						statoArrivo = statoGiaIncontrato;
						// creando nuovi stati del dizionario si possono aggiungere input a stati del dizionario gia' esistenti (es. a stato iniziale)
						statoGiaIncontrato.aggiungiInput(input);
					}
					else {
						statoArrivo.aggiungiInput(input);
					}
				}

				if(!mappaDizionario.containsKey(statoArrivo)) {
					statiDizionario.add(statoArrivo);
					coda.add(statoArrivo);
				}
				coppieTransizione_NuovoStato.add(new Pair<>(etichettaO, statoArrivo));
			}
			mappaDizionario.put(stato, coppieTransizione_NuovoStato);
		}
		
		for(StatoDizionario statoDizionario : this.mappaDizionario.keySet()) {
			//collega I e O, ricerca BFS per vedere se esiste cammino
			Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> IO = coppieIO(statoDizionario, spazioRilevanza);
			statoDizionario.setIO(IO);
		}
	}
	
	
	// epsClosure(set<StatiRilevanza>) = set di stati di rilevanza raggiungibili da qualunque degli stati in ingresso, 
	// tramite una eps-transizione (transizione con etichetta di osservabilita' = eps = null)
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

	
	// ritorna una mappa che associa ad ogni etichetta osservabile delle transizioni uscenti da uno stato del dizionario, gli stati di rilevanza di arrivo di tali transizioni
	private Map<String, Set<StatoRilevanzaRete>> cercaTransizioniOsservabiliUscenti(SpazioRilevanza spazioRilevanza, StatoDizionario stato) {
		// mappo etichette osservabili con gli stati in cui portano
		Map<String, Set<StatoRilevanzaRete>> mappa = new HashMap<>();
		// guardo tutte le transizioni osservabili uscenti da tutti gli StatiRilevanzaRete contenuti nello StatoRilevanzaReteDeterminizzata
		for(StatoRilevanzaRete s : stato.getStatiRilevanza()) {
			Set<Pair<Transizione, StatoRilevanzaRete>> transizioniOsservabili = spazioRilevanza.getTransizioniOsservabili(s);
			//per ogni transizione osservabile uscente dagli stati nello stato del DFA, prendo lo stato di arrivo e lo associo all'etichetta di osservabilita' della transizione
			for(Pair<Transizione, StatoRilevanzaRete> transizione : transizioniOsservabili) {
				Set<StatoRilevanzaRete> statiArrivo;
				// se l'etichetta c'e' gia' come chiave, prendo la sua lista di stati di arrivo e aggiungo lo stato appena trovato
				if(mappa.containsKey(transizione.getKey().getEtichettaOsservabilita())) {
					statiArrivo = mappa.get(transizione.getKey().getEtichettaOsservabilita());
					statiArrivo.add(transizione.getValue());
				}
				// se l'etichetta non c'era nella mappa, creo la coppia chiave valore nella mappa
				else {
					statiArrivo = new HashSet<>();
					statiArrivo.add(transizione.getValue());
					mappa.put(transizione.getKey().getEtichettaOsservabilita(), statiArrivo);
				}
			}
		}
		return mappa;
	}
	
	
	/**
	 * Un'operazione di ricerca nel dizionario acquisisce in ingresso un'osservazione lineare (sequenza di eventi osservabili associata a una traiettoria dello spazio di rilevanza)
	 * e produce in uscita la diagnosi associata allo stato raggiunto nel DFA, a partire da quello iniziale, col cammino (unico) contraddistinto dall'osservazione lineare stessa
	 * @param osservazioneLineare
	 * @return
	 * @throws Exception 
	 */
	public Set<Set<String>> ricerca(List<String> osservazioneLineare) throws Exception{
		StatoDizionario statoCorrente = this.statoIniziale;
		for(String etichetta : osservazioneLineare) {
			boolean found = false;
			for(Pair<String, StatoDizionario> transizioneOut : mappaDizionario.get(statoCorrente)) {
				if(etichetta.equals(transizioneOut.getKey())) {
					statoCorrente = transizioneOut.getValue();
					found = true;
					break;
				}
			}
			if(!found) {
				throw new Exception("Osservazione lineare " + osservazioneLineare + " non corrisponde a nessuna traiettoria della rete!");
			}
		}
		return statoCorrente.getDiagnosi();		
	}
	
	
	// cerca se gli stati output sono raggiungibili partendo dagli stati input
		// fa una ricerca BFS tra gli stati dello statoDizionario, partendo da ogni stato input
		private Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> coppieIO(StatoDizionario sDiz, SpazioRilevanza spazioRilevanza) {
			Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>>coppie = new LinkedHashSet<>();
			for(StatoRilevanzaRete sInput : sDiz.getInput()) {
				Map<StatoRilevanzaRete,Boolean>visitati = new HashMap<>();
				Queue<StatoRilevanzaRete>coda = new LinkedList<>();
				// stato da cui parto e' stato visitato
				visitati.put(sInput, true);
				coda.add(sInput);
				
				while(!coda.isEmpty()) {
					StatoRilevanzaRete s = coda.remove();
					// se da stato input sInput riesco a raggiungere lo stato output s, aggiungo la coppia <in, out> a IO
					if(sDiz.getOutput().contains(s)) {
						coppie.add(new Pair<>(sInput, s));
					}
					// aggiungo alla coda gli stati successivi solo se fanno parte dello stato del dizionario corrente e se non li ho gia' visitati
					for(Pair<Transizione, StatoRilevanzaRete> transizione : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(s)) {
						StatoRilevanzaRete sNext = transizione.getValue();
						if(sDiz.getStatiRilevanza().contains(sNext) && !visitati.containsKey(sNext)) {
							visitati.put(sNext, true);
							coda.add(sNext);
						}
					}
				}
			}
			return coppie;
		}
		
		
		private Set<StatoRilevanzaRete>inputSubset(StatoDizionario sPrecedente, String etichetta, StatoDizionario statoCorrente, SpazioRilevanza spazioRilevanza){
			Set<StatoRilevanzaRete>result = new HashSet<>();
			for(StatoRilevanzaRete sOut : sPrecedente.getOutput()) {
				for(Pair<Transizione, StatoRilevanzaRete>transizione : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(sOut)) {
					StatoRilevanzaRete sIn = transizione.getValue();
					// se da output stato dizionario precedente esce transizione con etichetta oss. indicata ed entra in uno stato input dello stato dizionario corrente, allora quello stato input
					// fara' parte del sottinsieme input relativo all'etichetta in ingresso dello stato del dizionario corrente
					if(transizione.getKey().getEtichettaOsservabilita().equals(etichetta) && statoCorrente.getInput().contains(sIn)) {
						result.add(sIn);
					}
				}
			}
			return result;
		}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(Stati NFA nello stato DFA): [etichetta osservabile -> stato DFA destinazione]\n");
		for(StatoDizionario s : mappaDizionario.keySet()) {
			sb.append(s + ": ");
			for(Pair<String, StatoDizionario> transizione : mappaDizionario.get(s)) {
				sb.append("[" + transizione.getKey() + " -> " + transizione.getValue() + "] ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	public void ridenominaStati(){
		String nome = "d";
		int i =0;
		for (StatoDizionario statoDizionario : mappaDizionario.keySet()) {
			if (isNull(statoDizionario.getRidenominazione())){
				statoDizionario.setRidenominazione(nome + i);
				i++;
			}
		}
	}
	
	
	//lo stato destinazione dell'ultima transizione viene stampato null :|
	public String toStringRidenominato(){
		StringBuilder sb = new StringBuilder();
		sb.append("(Stato DFA rinominato): [etichetta osservabile -> stato DFA rinominato destinazione]\n");
		for(StatoDizionario s : mappaDizionario.keySet()) {
			sb.append(s.getRidenominazione() + ": ");
			for(Pair<String, StatoDizionario> transizione : mappaDizionario.get(s)) {

				sb.append("[" + transizione.getKey() + " -> " +transizione.getValue().getRidenominazione() + "], ");
			}
			sb.append("Diagnosi: " + s.getDiagnosi());
			sb.append("\n");
		}
		return sb.toString();
	}


	public Map<StatoDizionario, Set<Pair<String, StatoDizionario>>> getMappaDizionario(){
		return this.mappaDizionario;
	}
	
}
