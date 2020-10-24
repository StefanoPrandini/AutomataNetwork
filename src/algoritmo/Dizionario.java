package algoritmo;

import java.io.Serializable;
import java.util.*;

import gestore.GestoreInputOutput;
import javafx.util.Pair;
import model.StatoDizionario;
import model.StatoRilevanzaRete;
import model.Terna;
import model.Transizione;
import myLib.Stringhe;

import static java.util.Objects.isNull;

import java.io.IOException;

/**
 * Dizionario Completo delle osservazioni:
 * DFA (Automa a stati Finiti Deterministico) risultante dalla determinazione dell'intero spazio di rilevanza (tramite Subset Construction), in cui gli stati sono corredati
 * dalla loro diagnosi (insieme di tutte le decorazioni degli stati del NFA contenuti nello stato del DFA)
 *
 */
public class Dizionario extends Algoritmo implements Serializable {
	private static final long serialVersionUID = 1L;
	//tengo un insieme di tutti gli stati di rilevanza della rete determinizzata per evitare di riferirmi a stati uguali che sono oggetti diversi
	private Set<StatoDizionario> statiDizionario;
	//mappo stati Rilevanza del DFA con coppie<etichettaO, statoArrivo>
	private Map<StatoDizionario, Set<Pair<String, StatoDizionario>>> mappaDizionario;
	private StatoDizionario statoIniziale;
	private LinkedList<Terna> terne;
	private GestoreInputOutput inputOutput;
	
	public Dizionario(GestoreInputOutput inputOutput) {
		this.statiDizionario = new LinkedHashSet<>(); // insieme con elementi in ordine di inserimento
		this.mappaDizionario = new LinkedHashMap<>(); // mappa con chiavi in ordine di inserimento
		this.terne = new LinkedList<>(); //insieme di terne in ordine di inserimento
		this.inputOutput = inputOutput;
	}


	@Override
	public void run() {
		if (inputOutput.isRicerca()){
			try {
				inputOutput.setRisultatoRicerca(ricerca(inputOutput.getOsservazioneLineare()));
				setRicerca(false);
			} catch (Exception e) {

			}
		}
		else {
			determinizzazioneSpazio(inputOutput.getSpazioRilevanza());
		}

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
		
		while(!coda.isEmpty() && ! isInInterruzione()) {
			StatoDizionario stato = coda.remove();
			statiDizionario.add(stato);
			// mappo le etichette di osservabilita' delle transizioni con gli stati di destinazione di tali transizioni (servono per calcolare eps-closure)
			// cosi' ho una associazione tra le etichette di osservabilita' e gli stati in cui portano (che dovranno essere raggruppati)
			Map<String, Set<StatoRilevanzaRete>>transizioniOsservabiliUscenti = cercaTransizioniOsservabiliUscenti(spazioRilevanza, stato); // transizioni osservabili uscenti da stato dizionario (precedente)
			Set<Pair<String, StatoDizionario>>coppieTransizione_NuovoStato = new HashSet<>();
			
			// per ogni etichetta osservabile delle transizioni uscenti, calcolo la epsClosure degli stati destinazione di tali transizioni
			for(String etichettaO : transizioniOsservabiliUscenti.keySet()) {
				// input del nuovo stato del dizionario = stati destinazione delle transizioni osservabili uscenti da un altro stato del dizionario
				Set<StatoRilevanzaRete>input = transizioniOsservabiliUscenti.get(etichettaO); // dallo stato precedente
				
				// se gli stati in "input" sono oltre la distanza massima mi fermo 
				boolean stop = false;
				for(StatoRilevanzaRete s : input) {
					if(s.oltreDistanzaMax()) {
						stop = true;
						break; // stati in "input" sono tutti alla stessa distanza
					}
				}
				
				if(!stop) {
					Set<StatoRilevanzaRete>epsClosure = epsClosure(spazioRilevanza, input);
					// cerco stati output dello stato del dizionario tra gli stati di rilevanza che lo compongono
					Set<StatoRilevanzaRete>output = new HashSet<>();
					for(StatoRilevanzaRete s : epsClosure) {
						if(!spazioRilevanza.getTransizioniOsservabili(s).isEmpty() ) {
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
							break;
						}
					}
					statoArrivo.aggiungiInput(input);

					if(!mappaDizionario.containsKey(statoArrivo)) {
						statiDizionario.add(statoArrivo);
						coda.add(statoArrivo);
					}
					coppieTransizione_NuovoStato.add(new Pair<>(etichettaO, statoArrivo));
				}
			}
			mappaDizionario.put(stato, coppieTransizione_NuovoStato);
		}
		
		for(StatoDizionario statoDizionario : this.mappaDizionario.keySet()) {
			//collega I e O, ricerca BFS per vedere se esiste cammino
			Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> IO = coppieIO(statoDizionario, spazioRilevanza);
			statoDizionario.setIO(IO);
		}

		if ( ! isInInterruzione() ) System.out.println(Stringhe.COSTRUZIONE_DIZIONARIO_COMPLETA);
		this.setTerminato(true);
	}
	
	
	// epsClosure(set<StatiRilevanza>) = set di stati di rilevanza raggiungibili da qualunque degli stati in ingresso, 
	// tramite una eps-transizione (transizione con etichetta di osservabilita' = eps = null)
	private Set<StatoRilevanzaRete> epsClosure(SpazioRilevanza spazioRilevanza, Set<StatoRilevanzaRete> stati){
		//gli stati di partenza fanno parte della loro eps-closure
		Set<StatoRilevanzaRete> result = new HashSet<>(stati);
		//metto gli stati passati in una coda: dovro' aggiungere stati da verificare
		Queue<StatoRilevanzaRete>codaStati = new LinkedList<>(stati);

		while(!codaStati.isEmpty() && ! isInInterruzione()) {
			StatoRilevanzaRete s = codaStati.remove();
			if ( ! isNull(spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(s))) {
				for (Pair<Transizione, StatoRilevanzaRete> transizione : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(s)) {
					// se l'etichetta della transizione uscente e' eps (null)
					if (isNull(transizione.getKey().getEtichettaOsservabilita())) {
						// lo aggiungo alla coda per vedere se anche le sue transizioni uscenti hanno etichetta null: se si' le aggiungo alla eps-closure
						if (!codaStati.contains(s)) {
							codaStati.add(transizione.getValue());
							result.add(transizione.getValue());
						}
					}
				}
			}
			// prendo le transizioni uscenti dallo statoRilevanza dalla mappa nello spazioRilevanza
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


	public void ridenominaStati(){
		String nome = "d";
		int i =0;
		for (StatoDizionario statoDizionario : mappaDizionario.keySet()) {
			statoDizionario.setRidenominazione(nome + i);
			i++;
			
		}
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
			if (isInInterruzione()){
				return statoCorrente.getDiagnosi();
			}
			for(Pair<String, StatoDizionario> transizioneOut : this.mappaDizionario.get(statoCorrente)) {
				if(etichetta.equals(transizioneOut.getKey())) {
					statoCorrente = transizioneOut.getValue();
					found = true;
					break;
				}
				if (isInInterruzione()){
					return statoCorrente.getDiagnosi();
				}
			}
			if(!found) {
				this.setTerminato(true);
				System.out.println(Stringhe.RICERCA_COMPLETA);
				throw new Exception();
			}
		}
		System.out.println(Stringhe.RICERCA_COMPLETA);
		this.setTerminato(true);
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
				if ( ! isNull(spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(s))) {
					for (Pair<Transizione, StatoRilevanzaRete> transizione : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(s)) {
						StatoRilevanzaRete sNext = transizione.getValue();
						if (sDiz.getStatiRilevanza().contains(sNext) && !visitati.containsKey(sNext)) {
							visitati.put(sNext, true);
							coda.add(sNext);
						}
					}
				}

			}
		}
		return coppie;
	}

	// stati di rilevanza dello stato del dizionario corrente raggiungibili da transizione con etichetta osservabile indicata, dallo stato del dizionario precedente
	public Set<StatoRilevanzaRete>inputSubset(StatoDizionario sPrecedente, String etichetta, StatoDizionario statoCorrente, SpazioRilevanza spazioRilevanza){
		Set<StatoRilevanzaRete>result = new HashSet<>();
		for(StatoRilevanzaRete sOut : sPrecedente.getOutput()) {
			for(Pair<Transizione, StatoRilevanzaRete>transizione : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(sOut)) {
				StatoRilevanzaRete sIn = transizione.getValue();
				// se da output stato dizionario precedente esce transizione con etichetta oss. indicata ed entra in uno stato input dello stato dizionario corrente, allora quello stato input
				// fara' parte del sottinsieme input relativo all'etichetta in ingresso dello stato del dizionario corrente

				if(etichetta.equals(transizione.getKey().getEtichettaOsservabilita()) && statoCorrente.getInput().contains(sIn)) {
					result.add(sIn);
				}
			}
		}
		return result;
	}
	
	
	// stati di rilevanza dello stato del dizionario precedente da cui escono transizioni con etichetta osservabile indicata, 
	// che entrano in stati di rilevanza dello stato del dizionario corrente
	public Set<StatoRilevanzaRete>outputSubset(StatoDizionario sPrecedente, String etichetta, StatoDizionario statoCorrente, SpazioRilevanza spazioRilevanza){
		Set<StatoRilevanzaRete>result = new HashSet<>();
		for(StatoRilevanzaRete sOut : sPrecedente.getOutput()) {
			for(Pair<Transizione, StatoRilevanzaRete>transizione : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(sOut)) {
				StatoRilevanzaRete sIn = transizione.getValue();
				// se da output stato dizionario precedente esce transizione con etichetta oss. indicata ed entra in uno stato input dello stato dizionario corrente, allora quello stato output
				// fara' parte del sottinsieme output relativo all'etichetta in ingresso dello stato del dizionario  precedente
	
				if(etichetta.equals(transizione.getKey().getEtichettaOsservabilita()) && statoCorrente.getInput().contains(sIn)) {
					result.add(sOut);
				}
			}
		}
		return result;
	}		


	public void monitoraggio(List<String> osservazioneLineare, SpazioRilevanza spazioRilevanza ) throws IOException {
		terne.clear(); // in caso sia gia' stato eseguito un monitoraggio
		Queue<String> etichette = new LinkedList<>(osservazioneLineare);
		String alfa = "alfa";
		int indice = 0;
		String nomeCompleto = alfa + indice;
		Terna ternaIniziale = new Terna(nomeCompleto, new HashSet<>(), statoIniziale, statoIniziale.getDiagnosi());
		terne.add(ternaIniziale);

		indice++;
		nomeCompleto = alfa + indice;
		while(!etichette.isEmpty()){

			Terna corrente = terne.getLast();
			String etichetta = etichette.remove();
			try {
				Terna nuova = produciTerna(spazioRilevanza, corrente, etichetta, nomeCompleto);
				System.out.println("Prodotta terna " + nuova + " da " + etichetta);

				terne.add(nuova);

				revisione(terne, osservazioneLineare, spazioRilevanza);

				indice++;
				nomeCompleto = alfa + indice;
			}catch (IOException ioe){
				if (terne.size() == 1) {
					System.out.println("Nessuna traiettoria coincidente con l'osservazione");
					return;
				}

			}


		}
	}
	
	
	private void revisione(LinkedList<Terna> terne, List<String> osservazione, SpazioRilevanza spazioRilevanza) {
		// nuova lista per non modificare oggetto in input
		List<String>etichette = new LinkedList<>(osservazione);
		// i = 0 non viene fatto, si modifica sempre la terna precedente
		for(int i = terne.size()-1; i > 0; i--) {
			Terna terna = terne.get(i);
			Terna ternaPrec = terne.get(i-1);
			
			Set<StatoRilevanzaRete> outputEff = outputSubset(ternaPrec.getStatoDizionario(), etichette.get(i-1), terna.getStatoDizionario(), spazioRilevanza);
			Set<Set<String>>newDiagnosi = new HashSet<>();
			for(StatoRilevanzaRete s : outputEff) {
				newDiagnosi.add(s.getDecorazione());
			}
			ternaPrec.setDiagnosi(newDiagnosi);
			
			Set<StatoRilevanzaRete> inputEff = ternaPrec.getStatoDizionario().getIfromO(outputEff);
			// Se l'insieme Ieff coincide con l'insieme I, la revisione termina
			if(ternaPrec.getInsiemeI().equals(inputEff)) {
				return;
			}
			// Altrimenti, l'insieme degli stati I della terna viene sovrascritto dall'insieme Ieff e la revisione continua
			else {
				ternaPrec.setInsiemeI(inputEff);
				System.out.println("Terna " + ternaPrec.getNome() + " modificata dopo revisione: " + ternaPrec);
			}
		}
	}


	//viene ricevuto un'etichetta osservabile
	//cerca in dizionario lo stato successivo raggiungibile dallo stato corrente (della terna) attraverso transizione con etichetta oemga
	//stato corrente diventa stato precendente e stato raggiunto diventa stato corrente nuovo
	//produci in uscita la terna
	//
	public Terna produciTerna(SpazioRilevanza sr, Terna ternaCorrente, String etichettaOss, String nome) throws IOException{
		Terna result = null;
		boolean esiste = false;
		for (Pair<String, StatoDizionario> coppiaEtichettaStato : mappaDizionario.get(ternaCorrente.getStatoDizionario())) {
			if (coppiaEtichettaStato.getKey().equals(etichettaOss)){
				esiste = true;
				Set<StatoRilevanzaRete>inputSubset = inputSubset(ternaCorrente.getStatoDizionario(), etichettaOss, coppiaEtichettaStato.getValue(), sr);
				result = new Terna(nome, inputSubset, coppiaEtichettaStato.getValue(), coppiaEtichettaStato.getValue().getDiagnosi());
				break;
			}
		}
		if (!esiste){
			throw new IOException();
		}
		
		return result;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
 		sb.append("(Stati NFA nello stato DFA): [etichetta osservabile -> (Stati NFA stato DFA destinazione)]\n");
 		for(StatoDizionario s : mappaDizionario.keySet()) {
			sb.append("\t");
 			sb.append(s + ": "); 
			boolean togliVirgola = false;
 			for(Pair<String, StatoDizionario> transizione : mappaDizionario.get(s)) {
 				sb.append("[" + transizione.getKey() + " -> " + transizione.getValue() + "], "); 
 				togliVirgola = true;
			}
 			if(togliVirgola) {
				sb.setLength(sb.length() - 2); // togliere virgola
			}
 			sb.append("\n");
		}
		return sb.toString();
	}

	
	public String toStringRidenominato(){
		StringBuilder sb = new StringBuilder();
		sb.append("Stato DFA ridenominato: [etichetta osservabile -> stato DFA destinazione]\n");
		for(StatoDizionario s : mappaDizionario.keySet()) {
			sb.append("\t");
			sb.append(s.getRidenominazione() + ": ");
			boolean togliVirgola = false;
			for(Pair<String, StatoDizionario> transizione : mappaDizionario.get(s)) {
 				sb.append("[" + transizione.getKey() + " -> " + transizione.getValue().getRidenominazione() + "], "); 
 				togliVirgola = true;
			}
//			sb.append(" | Diagnosi: " + s.getDiagnosi());
			if(togliVirgola) {
				sb.setLength(sb.length() - 2); // togliere virgola
			}
			sb.append("\n");
		}
		return sb.toString();
	}


	public Map<StatoDizionario, Set<Pair<String, StatoDizionario>>> getMappaDizionario(){
		return this.mappaDizionario;
	}

	public List<Terna> getTerne() {
		return terne;
	}
	

	public StatoDizionario getStatoIniziale() {
		return statoIniziale;
	}

	
	public Set<StatoDizionario> getStatiDizionario() {
		return statiDizionario;
	}

	public void setRicerca(boolean ricerca){
		this.inputOutput.setRicerca(ricerca);
	}

	public GestoreInputOutput getInputOutput() {
		return inputOutput;
	}

	public void setInputOutput(GestoreInputOutput inputOutput) {
		this.inputOutput = inputOutput;
	}
}
