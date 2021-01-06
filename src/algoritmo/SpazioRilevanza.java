package algoritmo;

import gestore.GestoreInputOutput;
import javafx.util.Pair;
import model.*;
import myLib.Stringhe;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;

/**
 * Spazio di rilevanza e' un automa -> grafo: StatiRilevanza sono i vertici e transizioni sono gli archi
 * ha due costruttori: uno per costruire spazio completo o prefisso con distanza, uno per costruire lo spazio partendo da una osservazione
 */
public class SpazioRilevanza extends Algoritmo implements Serializable  {

	private static final long serialVersionUID = 1L;
	public static final int ESPLORAZIONE_COMPLETA = -1;
	private GestoreInputOutput input;

	//tengo un insieme di tutti gli stati di rilevanza per evitare di riferirmi a stati uguali che sono oggetti diversi
	private Set<StatoRilevanzaRete> statiRilevanza;
	// ogni stato di rilevanza della rete viene mappato con tutte le coppie <transizioneUscente, statoRilevanzaSuccessivo>
	private Map<StatoRilevanzaRete, List<Pair<Transizione, StatoRilevanzaRete>>> mappaStatoRilevanzaTransizioni;
	private StatoRilevanzaRete statoRilevanzaIniziale;
	private String nomeRete;


	
	/**
	 * se costruttore chiamato con distanzaMax e senza osservazione, lo spazio non deve essere creato a partire da un'osservazione
	 */
	public SpazioRilevanza(GestoreInputOutput input) {
		this.statiRilevanza = new LinkedHashSet<>(); //insieme con elementi in ordine di inserimento
		this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<>(); // mappa con chiavi in ordine di inserimento
		this.input = input;
		this.nomeRete = input.getRete().getNome();

	}

	@Override
	public void run(){
		if (input.isDaOsservazione()){
			setInizio(System.nanoTime());
			creaSpazioRilevanzaDaOsservazione(input.getRete(), input.getOsservazione());
			setFine(System.nanoTime());
		}
		else {
			setInizio(System.nanoTime());
			creaSpazioRilevanza(input.getRete());
			setFine(System.nanoTime());
		}
	}
	

//	se spazio non viene creato da osservazione, puo' essere o completo o un prefisso (distanza < valore fissato)
	public void creaSpazioRilevanza(ReteAutomi rete) {
		Queue<StatoRilevanzaRete> coda = new LinkedList<>();
		Set<String>decorazioneIniziale = new HashSet<>();
		//la rete deve essere nella condizione iniziale
		StatoRilevanzaRete statoIniziale = new StatoRilevanzaRete(rete, decorazioneIniziale);
		statoIniziale.setDistanza(0);
		statiRilevanza.add(statoIniziale);
		this.statoRilevanzaIniziale = statoIniziale;
		coda.add(statoIniziale);
		
		while(!coda.isEmpty() && ! isInInterruzione()) {
			StatoRilevanzaRete statoRilevanza = coda.remove();	
			// faccio andare la rete nella condizione descritta dallo statoRilevanza appena estratto, cosi' poi posso usare i metodi di ReteAutomi 
			// per cercare le transizioni abilitate e gli stati successivi
			rete.setReteAutomi(statoRilevanza.getStatiCorrentiAutoma(), statoRilevanza.getContenutoLinks());
			ArrayList<Transizione>transizioniAbilitate = rete.getTutteTransizioniAbilitate();
			ArrayList<Pair<Transizione, StatoRilevanzaRete>> listaAdiacenza = new ArrayList<>();
			
			for(Transizione t : transizioniAbilitate) {
				if (isInInterruzione()) {
					break;
				}

				// se vengono provate transizioni diverse (uscenti dallo stesso statoRilevanza), tra una e l'altra la rete deve essere riportata nello statoRilevanza di partenza
				rete.setReteAutomi(statoRilevanza.getStatiCorrentiAutoma(), statoRilevanza.getContenutoLinks());
				int distanza = statoRilevanza.getDistanza();
				StatoRilevanzaRete nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione(), statiRilevanza);
				if (t.hasEtichettaOsservabilita()){
					distanza++;
				}

				listaAdiacenza.add(new Pair<Transizione, StatoRilevanzaRete>(t, nuovoStatoRilevanza));
				// se ricerca completa o distanza "attuale" e' <= del max andiamo avanti
				if (input.getDistanzaMax() == ESPLORAZIONE_COMPLETA  || distanza <= input.getDistanzaMax()){
					// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
					if(!mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) {
						nuovoStatoRilevanza.setDistanza(distanza);
						statiRilevanza.add(nuovoStatoRilevanza);
						coda.add(nuovoStatoRilevanza);
					}
					else {
						//trovo lo stato nello spazio coincidente con quello appena generato
						StatoRilevanzaRete statoGiaInSpazio = 
								mappaStatoRilevanzaTransizioni.keySet()
								.stream()
								.filter(statoRilevanzaRete -> statoRilevanzaRete.equals(nuovoStatoRilevanza))
								.collect(Collectors.toList())
								.get(0);
						//distanza viene aggiornata se ne trovo una minore
						if (statoGiaInSpazio.getDistanza() > distanza){
							statoGiaInSpazio.setDistanza(distanza);
						}
					}
				}
			}

			/** PROVE INTERRUZIONE
			try {
				Thread.sleep(100);
			}catch (InterruptedException ie){
				ie.printStackTrace();
			}
			 **/

			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, listaAdiacenza);
		}


		if ( ! isInInterruzione() ) System.out.println(Stringhe.CALCOLO_SPAZIO_COMPLETO);

		this.setTerminato(true);

	}

//	spazioRilevanza puo' essere creato a partire da una osservazione (un automa)
//	lascio comunque la distanza per non dover rifare il dizionario
	private void creaSpazioRilevanzaDaOsservazione(ReteAutomi rete, Automa osservazione) {
		Queue<StatoRilevanzaRete> coda = new LinkedList<>();
		Set<String>decorazioneIniziale = new HashSet<>();
		//la rete deve essere nella condizione iniziale
		StatoRilevanzaRete statoIniziale = new StatoRilevanzaRete(rete, decorazioneIniziale);
		statoIniziale.setDistanza(0);
		statoIniziale.setStatoOsservazione(osservazione.getStatoIniziale());
		statiRilevanza.add(statoIniziale);
		this.statoRilevanzaIniziale = statoIniziale;
		coda.add(statoIniziale);

		while(!coda.isEmpty() && ! isInInterruzione()) {
			StatoRilevanzaRete statoRilevanza = coda.remove();	
			// faccio andare la rete nella condizione descritta dallo statoRilevanza appena estratto, cosi' poi posso usare i metodi di ReteAutomi 
			// per cercare le transizioni abilitate e gli stati successivi
			rete.setReteAutomi(statoRilevanza.getStatiCorrentiAutoma(), statoRilevanza.getContenutoLinks());
			ArrayList<Transizione>transizioniAbilitate = rete.getTutteTransizioniAbilitate();
			ArrayList<Pair<Transizione, StatoRilevanzaRete>> listaAdiacenza = new ArrayList<>();
			for(Transizione t : transizioniAbilitate) {
				if (isInInterruzione()) break;
				// se vengono provate transizioni diverse (uscenti dallo stesso statoRilevanza), tra una e l'altra la rete deve essere riportata nello statoRilevanza di partenza
				rete.setReteAutomi(statoRilevanza.getStatiCorrentiAutoma(), statoRilevanza.getContenutoLinks());
				int distanza = statoRilevanza.getDistanza();
				osservazione.setStatoCorrente(statoRilevanza.getStatoOsservazione());
				StatoRilevanzaRete nuovoStatoRilevanza = null;
				
				boolean statoFinale = osservazione.isInStatoFinale();
				boolean mettiInCoda = true;
				
				if (t.hasEtichettaOsservabilita()){
					// aggiungo anche livello successivo a quello finale per trovare gli output degli stati finali
					if(statoFinale) {
						nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione(), statiRilevanza);
					}
					else {
						distanza++;
						ArrayList<String> etichetteOsservazione = etichetteOsservazione(osservazione);
						//la transizione, oltre che essere abilitata, deve anche essere presente nell'osservazione
						if(etichetteOsservazione.contains(t.getEtichettaOsservabilita())) {
							if( ! statoFinale) {
								avanzaOsservazione(osservazione, t.getEtichettaOsservabilita());
							}
							nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione(), statiRilevanza);
							nuovoStatoRilevanza.setStatoOsservazione(osservazione.getStatoCorrente());
						}
						else {
//							se c'e' la transizione osservabile uscente ma non e' concorde con l'osservazione, tengo lo stato successivo solo per aggiornare gli output dello stato precedente
							nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione(), statiRilevanza);
							mettiInCoda = false;
						}
					}
				}
				else {
					nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione(), statiRilevanza);
					nuovoStatoRilevanza.setStatoOsservazione(statoRilevanza.getStatoOsservazione());
				}

//				arrivo qua anche quando osservazione e' in stato finale: aggiorno lista di adiacenza ma non aggiungo alla coda
				if( ! isNull(nuovoStatoRilevanza)) { 
					listaAdiacenza.add(new Pair<Transizione, StatoRilevanzaRete>(t, nuovoStatoRilevanza));
					// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
					if( ! mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) { 
						// se e' in stato finale e ha etichetta non vado avanti: non aggiungo alla coda
						// se e' in stato finale ma non ha etichetta sono nella eps-closure, vado avanti
						// mettiInCoda e' falso quando c'e' una transizione oss. non concorde con l'Osservazione: stato destinazione non lo metto in coda
						if( ! (statoFinale && t.hasEtichettaOsservabilita()) && mettiInCoda) {
							nuovoStatoRilevanza.setDistanza(distanza);
							statiRilevanza.add(nuovoStatoRilevanza);
							// se l'osservazione e' nello stato finale, gli stati successivi servono solo per trovare l'output, 
							// non devono essere aggiunti alla coda
							coda.add(nuovoStatoRilevanza);			
						}			
					}
					else {
						// aggiorno la distanza dello stato gia' nella mappa
						for(StatoRilevanzaRete statoGiaInSpazio : mappaStatoRilevanzaTransizioni.keySet()) {
							if(statoGiaInSpazio.equals(nuovoStatoRilevanza)) {
								if (statoGiaInSpazio.getDistanza() > distanza){
									statoGiaInSpazio.setDistanza(distanza);
								}
							}
						}
					}
				}
			}
			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, listaAdiacenza);
		}
		if ( ! isInInterruzione() ) System.out.println(Stringhe.CALCOLO_SPAZIO_COMPLETO);
		this.setTerminato(true);
	}


	private ArrayList<String> etichetteOsservazione(Automa osservazione) {
		ArrayList<String>etichette = new ArrayList<String>();
		for(Transizione t : osservazione.getTransizioniUscenti(osservazione.getStatoCorrente())) {
			if( ! t.getEtichettaOsservabilita().equals("eps")) {
				etichette.add(t.getEtichettaOsservabilita());
			}
		}
		return etichette;
	}
	
	
	private void avanzaOsservazione(Automa osservazione, String etichettaOsservazione) {
		for(Transizione t : osservazione.getTransizioniUscenti(osservazione.getStatoCorrente())) {
			if(t.getEtichettaOsservabilita().equals(etichettaOsservazione)) {
				osservazione.eseguiTransizione(t);
			}
		}
	}
	
	
//	static cosi' puo' essere usato anche quando si fa l'estensione del dizionario, nella quale non si deve istanziare lo spazio di rilevanza
	public static StatoRilevanzaRete calcolaStatoRilevanzaSucc(ReteAutomi rete, Transizione t, Set<String> decorazione, Set<StatoRilevanzaRete> statiRilevanza) {		
		Set<String>newDecorazione = new HashSet<>(decorazione);		
		rete.svolgiTransizione(t);

		//aggiungo eventuale etichetta di rilevanza
		if (t.hasEtichettaRilevanza() && !newDecorazione.contains(t.getEtichettaRilevanza())){
			newDecorazione.add(t.getEtichettaRilevanza());
		}
		StatoRilevanzaRete newStato = new StatoRilevanzaRete(rete, newDecorazione);
		// se lo stato e' gia' presente come chiave nella mappa, lo cerco e ritorno quello: se ne restituissi uno nuovo non sarebbero lo stesso oggetto
		// e avrei problemi quando faccio ridenominazione		
		for(StatoRilevanzaRete statoGiaIncontrato : statiRilevanza) {
			if(statoGiaIncontrato.equals(newStato)) {
				return statoGiaIncontrato;
			}
		}
		return newStato;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(StatoRilevanzaRete statoR : mappaStatoRilevanzaTransizioni.keySet()) {
			String ridenominazione = "";
			if ( ! isNull(statoR.getRidenominazione())){
				ridenominazione = " " + statoR.getRidenominazione();
			}
			sb.append("Stato rilevanza" + ridenominazione + ": " + statoR +":\n");
			sb.append("Transizioni uscenti: " + mappaStatoRilevanzaTransizioni.get(statoR) + "\n\n");
		}
		return sb.toString();
	}

	public void ridenominaStati(){
		String nome = "a";
		int i = 0;
		for (StatoRilevanzaRete statoRilevanzaRete : mappaStatoRilevanzaTransizioni.keySet()) {
			if (isNull(statoRilevanzaRete.getRidenominazione())){
				statoRilevanzaRete.setRidenominazione(nome+i);
				i++;
			}

		}
	}


	public Map<StatoRilevanzaRete, List<Pair<Transizione, StatoRilevanzaRete>>> getMappaStatoRilevanzaTransizioni(){
		return this.mappaStatoRilevanzaTransizioni;
	}


	public ArrayList<StatoRilevanzaRete> getStatiRilevanza(){
		return new ArrayList<>(this.mappaStatoRilevanzaTransizioni.keySet());
	}


	public StatoRilevanzaRete getStatoRilevanzaIniziale() {
		return statoRilevanzaIniziale;
	}

	
	public Set<Pair<Transizione, StatoRilevanzaRete>> getTransizioniOsservabili(StatoRilevanzaRete statoRilevanza){
		Set<Pair<Transizione, StatoRilevanzaRete>> result = new HashSet<>();
		if(! mappaStatoRilevanzaTransizioni.containsKey(statoRilevanza)) {
			return result;
		}
		else {
			for(Pair<Transizione, StatoRilevanzaRete> transizione : mappaStatoRilevanzaTransizioni.get(statoRilevanza)) {
				if(transizione.getKey().hasEtichettaOsservabilita()) {
					result.add(new Pair<>(transizione.getKey(), transizione.getValue()));
				}
			}
			return result;
		}
		
	}



	public String getNomeRete() {
		return nomeRete;
	}

	public String compendio() {
		int numeroTransizioni = 0;
		for (List<Pair<Transizione, StatoRilevanzaRete>> value : getMappaStatoRilevanzaTransizioni().values()) {
			numeroTransizioni += value.size();
		}
		return  new StringBuilder().append(String.format(Stringhe.INFO_SPAZIO_RILEVANZA, getStatiRilevanza().size(), numeroTransizioni)).toString();
	}

	public String log(){
		int numeroTransizioni = 0;
		for (List<Pair<Transizione, StatoRilevanzaRete>> value : getMappaStatoRilevanzaTransizioni().values()) {
			numeroTransizioni += value.size();
		}
		return  getStatiRilevanza().size() + " stati " + numeroTransizioni + " transizioni";
	}
}

