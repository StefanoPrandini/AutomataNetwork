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

/**
 * Dizionario Completo delle osservazioni:
 * DFA (Automa a stati Finiti Deterministico) risultante dalla determinazione dell'intero spazio di rilevanza (tramite Subset Construction), in cui gli stati sono corredati
 * dalla loro diagnosi (insieme di tutte le decorazioni degli stati del NFA contenuti nello stato del DFA)
 *
 */
public class Dizionario extends Algoritmo implements Serializable {
	private static final long serialVersionUID = 6562299391009567691L;
	//tengo un insieme di tutti gli stati di rilevanza della rete determinizzata per evitare di riferirmi a stati uguali che sono oggetti diversi
	private Set<StatoDizionario> statiDizionario;
	//mappo stati Rilevanza del DFA con coppie<etichettaO, statoArrivo>
	private Map<StatoDizionario, Set<Pair<String, StatoDizionario>>> mappaDizionario;
	private StatoDizionario statoIniziale;
	private LinkedList<Terna> terne;
	private GestoreInputOutput inputOutput;
	private Set<Set<String>> diagnosi;
	private String etichettaMancanteInRicerca;
	private ArrayList<String> osservazioneLineareParziale;
	private boolean risultatoParziale;


	public Dizionario(GestoreInputOutput inputOutput) {
		this.statiDizionario = new LinkedHashSet<>(); // insieme con elementi in ordine di inserimento
		this.mappaDizionario = new LinkedHashMap<>(); // mappa con chiavi in ordine di inserimento
		this.terne = new LinkedList<>(); //insieme di terne in ordine di inserimento
		this.diagnosi = new LinkedHashSet<>();
		this.inputOutput = inputOutput;
	}


	@Override
	public void run() {
		if (inputOutput.isRicerca()){
			setInizio(System.nanoTime());
			ricerca(inputOutput.getOsservazioneLineareRicerca());
			setRicerca(false);
			setFine(System.nanoTime());
		}
		else if (inputOutput.isMonitoraggio()){
			setInizio(System.nanoTime());
			monitoraggio(inputOutput.getOsservazioneLineareMonitoraggio(), inputOutput.getSpazioRilevanza());
			setMonitoraggio(false);
			setFine(System.nanoTime());
		}
		else {
			setInizio(System.nanoTime());
			determinizzazioneSpazio(inputOutput.getSpazioRilevanza());
			setFine(System.nanoTime());
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

			/** PROVE INTERRUZIONE
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 **/
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
	

	public synchronized void ricerca(List<String> osservazioneLineare){
		StatoDizionario statoCorrente = this.statoIniziale;
		boolean found = false;
		boolean trovatoQualcosa = false;
		osservazioneLineareParziale = new ArrayList<>();

		for(String etichetta : osservazioneLineare) {
			found = false;
			if (isInInterruzione()){
				setRicercaTerminata(true);
				break;
			}

			/** PROVE INTERRUZIONE
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 **/

			for(Pair<String, StatoDizionario> transizioneOut : this.mappaDizionario.get(statoCorrente)) {
				if(etichetta.equals(transizioneOut.getKey())) {
					statoCorrente = transizioneOut.getValue();
					if (! trovatoQualcosa) trovatoQualcosa = true;
					diagnosi = statoCorrente.getDiagnosi();
					found = true;
					osservazioneLineareParziale.add(etichetta);
					break;
				}

				if (isInInterruzione()){
					this.setRicercaTerminata(true);
					break;
				}
			}

			/** PROVE INTERRUZIONE
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 **/

			//se non trovo qualcosa corrispondente all'etichetta, ma qualcosa ho trovato
			if(!found && trovatoQualcosa) {
				this.setRicercaTerminata(true);
				etichettaMancanteInRicerca = etichetta;
				break;
			}
		}
		if ( ! isInInterruzione()){
			System.out.println(Stringhe.RICERCA_COMPLETA);
		}
		this.setRicercaTerminata(true);
		//se ho trovato qualcosa, ma l'ultima iterazione sull'etichetta non è andata a buon fine
		if (trovatoQualcosa && !found) {
			risultatoParziale = true;
		} else if(trovatoQualcosa && found){
			// se ho trovato qualcosa ed ho terminato correttamente tutte le iterazioni
			risultatoParziale = false; //nnon e' risultato parziale, ma completo
		}
		else {
			//se non ho trovato nulla, mai
			diagnosi = null;
			risultatoParziale = false;
		}
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


	public void monitoraggio(List<String> osservazioneLineare, SpazioRilevanza spazioRilevanza ) {
		terne.clear(); // in caso sia gia' stato eseguito un monitoraggio
		Queue<String> etichette = new LinkedList<>(osservazioneLineare);
		String alfa = "alfa";
		int indice = 0;
		String nomeCompleto = alfa + indice;
		Terna ternaIniziale = new Terna(nomeCompleto, new HashSet<>(), statoIniziale, statoIniziale.getDiagnosi());
		terne.add(ternaIniziale);

		indice++;
		nomeCompleto = alfa + indice;
		while(!etichette.isEmpty() && ! isInInterruzione() ){

			Terna corrente = terne.getLast();
			String etichetta = etichette.remove();

			Terna nuova = produciTerna(spazioRilevanza, corrente, etichetta, nomeCompleto);
			if (isNull(nuova)){
				break;
			}
			this.inputOutput.addEventoToLog("Prodotta terna " + nuova + " da " + etichetta);

			terne.add(nuova);

			revisione(terne, osservazioneLineare, spazioRilevanza);
			if (isInInterruzione()) break;

			indice++;
			nomeCompleto = alfa + indice;

			/** PROVE INTERRUZIONE
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			 **/
		}
		if ( ! isInInterruzione()) System.out.println(Stringhe.MONITORAGGIO_COMPLETO);
		this.setTerminato(true);
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
				this.inputOutput.addEventoToLog("Terna " + ternaPrec.getNome() + " modificata dopo revisione: " + ternaPrec);
			}
		}
	}


	//viene ricevuta un'etichetta osservabile
	//cerca in dizionario lo stato successivo raggiungibile dallo stato corrente (della terna) attraverso transizione con etichetta
	//stato corrente diventa stato precedente e stato raggiunto diventa stato corrente nuovo
	//produce in uscita la terna
	public Terna produciTerna(SpazioRilevanza sr, Terna ternaCorrente, String etichettaOss, String nome){
		Terna result = null;
		for (Pair<String, StatoDizionario> coppiaEtichettaStato : mappaDizionario.get(ternaCorrente.getStatoDizionario())) {
			if (coppiaEtichettaStato.getKey().equals(etichettaOss)){
				Set<StatoRilevanzaRete>inputSubset = inputSubset(ternaCorrente.getStatoDizionario(), etichettaOss, coppiaEtichettaStato.getValue(), sr);
				result = new Terna(nome, inputSubset, coppiaEtichettaStato.getValue(), coppiaEtichettaStato.getValue().getDiagnosi());
				break;
			}
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
			if (mappaDizionario.get(s).isEmpty()){
				sb.append("stato finale");
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
			if (mappaDizionario.get(s).isEmpty()){
				sb.append("stato finale");
			}
			if(togliVirgola) {
				sb.setLength(sb.length() - 2);
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

	private void setRicerca(boolean ricerca){
		this.inputOutput.setRicerca(ricerca);
	}
	private void setMonitoraggio(boolean b) {
		this.inputOutput.setMonitoraggio(b);
	}

	public GestoreInputOutput getInputOutput() {
		return inputOutput;
	}

	public void setInputOutput(GestoreInputOutput inputOutput) {
		this.inputOutput = inputOutput;
	}

	public Set<Set<String>> getDiagnosi() {
		return this.diagnosi;
	}

	public String compendio() {
		int numeroTransizioni = 0;
		for (Set<Pair<String, StatoDizionario>> value : getMappaDizionario().values()) {
			numeroTransizioni += value.size();
		}
		return  new StringBuilder().append(String.format(Stringhe.INFO_DIZIONARIO, getStatiDizionario().size(),numeroTransizioni)).toString();

	}

	public String getInfoStatoDaRidenominazione(String ridenominazione){
		boolean esisteStato = false;
		StringBuilder sb = new StringBuilder();
		for (StatoDizionario statoDizionario : mappaDizionario.keySet()) {
			if (statoDizionario.getRidenominazione().equals(ridenominazione)){

				sb.append("\nStato del dizionario " + statoDizionario.getRidenominazione());
				sb.append("\n\nStati di rilevanza: {");
				StringBuilder temp = new StringBuilder();
				for (StatoRilevanzaRete statoRilevanzaRete : statoDizionario.getStatiRilevanza()) {
					temp.append(statoRilevanzaRete.getRidenominazione() + ", ");
				}
				String s = temp.toString();
				s = s.substring(0, s.length() -2);
				sb.append(s + "}");
				sb.append("\nDiagnosi stato: " + statoDizionario.getDiagnosi());
				sb.append("\nTransizioni etichettate: {");
				if ( ! mappaDizionario.get(statoDizionario).isEmpty()) {
					StringBuilder temp2 = new StringBuilder();
					for (Pair<String, StatoDizionario> coppia : mappaDizionario.get(statoDizionario)) {
						temp2.append(coppia.getKey() + " -> " + coppia.getValue().getRidenominazione() + ", ");
					}
					String s1 = temp2.toString();
					s1 = s1.substring(0, s1.length() - 2);
					sb.append(s1);
				}
				sb.append("}");
				esisteStato = true;
			}
		}
		if (esisteStato) return sb.toString();
		return String.format(Stringhe.NESSUNO_STATO, ridenominazione);
	}


	public String logCostruzione(){
		int numeroTransizioni = 0;
		for (Set<Pair<String, StatoDizionario>> value : getMappaDizionario().values()) {
			numeroTransizioni += value.size();
		}
		return  getStatiDizionario().size() + " stati " + numeroTransizioni + " transizioni";
	}

	public String getEtichettaMancanteInRicerca() {
		return etichettaMancanteInRicerca;
	}

	public ArrayList<String> getOsservazioneLineareParziale() {
		return osservazioneLineareParziale;
	}

	public boolean isRisultatoParziale() {
		return risultatoParziale;
	}
}
