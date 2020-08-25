package reteAutomi;

import javafx.util.Pair;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Objects.isNull;

/**
 * Spazio di rilevanza e' un automa -> grafo: StatiRilevanza sono i vertici e transizioni sono gli archi
 * ha due costruttori: uno per costruire spazio completo o prefisso con distanza, uno per costruire lo spazio partendo da una osservazione
 */
public class SpazioRilevanza {	
	public static final int ESPLORAZIONE_COMPLETA = -1;

	//tengo un insieme di tutti gli stati di rilevanza per evitare di riferirmi a stati uguali che sono oggetti diversi
	private Set<StatoRilevanzaRete> statiRilevanza;
	// ogni stato di rilevanza della rete viene mappato con tutte le coppie <transizioneUscente, statoRilevanzaSuccessivo>
	private Map<StatoRilevanzaRete, List<Pair<Transizione, StatoRilevanzaRete>>> mappaStatoRilevanzaTransizioni;
	private StatoRilevanzaRete statoRilevanzaIniziale;
	private int distanzaMax;
	
	/**
	 * se costruttore chiamato con distanzaMax e senza osservazione, lo spazio non deve essere creato a partire da un'osservazione
 	 * @param distanzaMax distanza massima a cui arrivare nella ricerca, -1 per ricerca completa
	 */
	public SpazioRilevanza(ReteAutomi rete, int distanzaMax) {
		this.statiRilevanza = new LinkedHashSet<>(); //insieme con elementi in ordine di inserimento
		this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<>(); // mappa con chiavi in ordine di inserimento
		this.distanzaMax = distanzaMax;
		creaSpazioRilevanza(rete);
	}
	

	/**
	 * se costruttore chiamato con osservazione, non si fissa una distanzaMax
	 * @param osservazione l'osservazione da cui costruire lo spazio di rilevanza
	 */
	public SpazioRilevanza(ReteAutomi rete, Automa osservazione) {
		this.statiRilevanza = new LinkedHashSet<>(); //insieme con elementi in ordine di inserimento
		this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<>(); // mappa con chiavi in ordine di inserimento
		this.distanzaMax = ESPLORAZIONE_COMPLETA;
		creaSpazioRilevanzaDaOsservazione(rete, osservazione);
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
		
		while(!coda.isEmpty()) {
			StatoRilevanzaRete statoRilevanza = coda.remove();	
			// faccio andare la rete nella condizione descritta dallo statoRilevanza appena estratto, cosi' poi posso usare i metodi di ReteAutomi 
			// per cercare le transizioni abilitate e gli stati successivi
			setReteAutomi(rete, statoRilevanza);
			ArrayList<Transizione>transizioniAbilitate = rete.getTutteTransizioniAbilitate();
			ArrayList<Pair<Transizione, StatoRilevanzaRete>> listaAdiacenza = new ArrayList<>();
			
			for(Transizione t : transizioniAbilitate) {
				// se vengono provate transizioni diverse (uscenti dallo stesso statoRilevanza), tra una e l'altra la rete deve essere riportata nello statoRilevanza di partenza
				setReteAutomi(rete, statoRilevanza);
				int distanza = statoRilevanza.getDistanza();
				StatoRilevanzaRete nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione());
				if (t.hasEtichettaOsservabilita()){
					distanza++;
				}

				listaAdiacenza.add(new Pair<Transizione, StatoRilevanzaRete>(t, nuovoStatoRilevanza));
				// se ricerca completa o distanza "attuale" e' <= del max andiamo avanti
				if (distanzaMax == ESPLORAZIONE_COMPLETA  || distanza <= distanzaMax){
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

						if (statoGiaInSpazio.getDistanza() > distanza){
							statoGiaInSpazio.setDistanza(distanza);
						}
					}
				}
			}
			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, listaAdiacenza);
		}
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

		while(!coda.isEmpty()) {
			StatoRilevanzaRete statoRilevanza = coda.remove();	
			// faccio andare la rete nella condizione descritta dallo statoRilevanza appena estratto, cosi' poi posso usare i metodi di ReteAutomi 
			// per cercare le transizioni abilitate e gli stati successivi
			setReteAutomi(rete, statoRilevanza);
			ArrayList<Transizione>transizioniAbilitate = rete.getTutteTransizioniAbilitate();
			ArrayList<Pair<Transizione, StatoRilevanzaRete>> listaAdiacenza = new ArrayList<>();
			for(Transizione t : transizioniAbilitate) {
				// se vengono provate transizioni diverse (uscenti dallo stesso statoRilevanza), tra una e l'altra la rete deve essere riportata nello statoRilevanza di partenza
				setReteAutomi(rete, statoRilevanza);
				int distanza = statoRilevanza.getDistanza();
				osservazione.setStatoCorrente(statoRilevanza.getStatoOsservazione());
				StatoRilevanzaRete nuovoStatoRilevanza = null;
				//la transizione, oltre che essere abilitata, deve anche essere presente nell'osservazione
				if (t.hasEtichettaOsservabilita()){
					distanza++;
					ArrayList<String> etichetteOsservazione = etichetteOsservazione(osservazione);
					// se osservazione e' in stato finale, aggiungo comunque gli stati successivi allo spazio di rilevanza:
					// servono per trovare gli output degli stati finali
					// non bisogna aggiungerli alla coda, cosi' non va avanti
					if(etichetteOsservazione.contains(t.getEtichettaOsservabilita()) || osservazione.isInStatoFinale()) {
						if( ! osservazione.isInStatoFinale()) {
							avanzaOsservazione(osservazione, t.getEtichettaOsservabilita());
						}
						nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione());
						nuovoStatoRilevanza.setStatoOsservazione(osservazione.getStatoCorrente());
					}
				}
				else {
					nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione());
					nuovoStatoRilevanza.setStatoOsservazione(statoRilevanza.getStatoOsservazione());
				}

				if( ! isNull(nuovoStatoRilevanza)) {
					listaAdiacenza.add(new Pair<Transizione, StatoRilevanzaRete>(t, nuovoStatoRilevanza));
					// se ricerca completa o distanza "attuale" e' <= del max andiamo avanti
					// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
					if( ! mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) {
						nuovoStatoRilevanza.setDistanza(distanza);
						statiRilevanza.add(nuovoStatoRilevanza);
						// se l'osservazione e' nello stato finale, gli stati successivi servono solo per trovare l'output,
						// non devono essere aggiunti alla coda
						if(osservazione.isInStatoFinale()) {
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


	private StatoRilevanzaRete calcolaStatoRilevanzaSucc(ReteAutomi rete, Transizione t, Set<String> decorazione) {		
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

	
	/**
	 * Porta la rete di automi nella situazione descritta dallo stato di rilevanza (statiCorrenti degli automi ed eventi sui link)
	 * @param statoRilevanza
	 */
	public void setReteAutomi(ReteAutomi rete, StatoRilevanzaRete statoRilevanza) {
		for(Pair<String, String> statoCorrenteAutoma : statoRilevanza.getStatiCorrentiAutoma()) {
			rete.trovaAutoma(statoCorrenteAutoma.getKey()).setStatoCorrente(statoCorrenteAutoma.getValue());
		}
		for(Pair<String, Evento> eventoSuLink : statoRilevanza.getContenutoLinks()) {
			rete.trovaLink(eventoSuLink.getKey()).setEvento(eventoSuLink.getValue());
		}
		
		rete.aggiornaMappaAutomiTransizioniAbilitate();
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(StatoRilevanzaRete statoR : mappaStatoRilevanzaTransizioni.keySet()) {
			sb.append("Stato rilevanza: " + statoR +":\n");
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
		for(Pair<Transizione, StatoRilevanzaRete> transizione : mappaStatoRilevanzaTransizioni.get(statoRilevanza)) {
			if(transizione.getKey().hasEtichettaOsservabilita()) {
				result.add(new Pair<>(transizione.getKey(), transizione.getValue()));
			}
		}
		return result;
	}
/*
	public ArrayList<Transizione> getTransizioni(){
		ArrayList< Transizione> result  = new ArrayList<>();

		for (List<Transizione> transiziones : mappaStatoRilevanzaTransizioni.values()) {
			result.addAll(transiziones);
		}
		return result;
	}*/

	public int getDistanzaMax() {
		return distanzaMax;
	}

	public void setDistanzaMax(int distanzaMax) {
		this.distanzaMax = distanzaMax;
	}
}
