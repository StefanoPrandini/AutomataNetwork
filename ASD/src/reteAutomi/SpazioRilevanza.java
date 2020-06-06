package reteAutomi;

import javafx.util.Pair;
import java.util.*;
import static java.util.Objects.isNull;

/**
 * Spazio di rilevanza e' un automa -> grafo: StatiRilevanza sono i vertici e transizioni sono gli archi
 */
public class SpazioRilevanza {	
	//tengo un insieme di tutti gli stati di rilevanza per evitare di riferirmi a stati uguali che sono oggetti diversi
	private Set<StatoRilevanzaRete> statiRilevanza;
	// ogni stato di rilevanza della rete viene mappato con tutte le coppie <transizioneUscente, statoRilevanzaSuccessivo>
	private Map<StatoRilevanzaRete, List<Pair<Transizione, StatoRilevanzaRete>>> mappaStatoRilevanzaTransizioni;
	private StatoRilevanzaRete statoRilevanzaIniziale;
	
	public SpazioRilevanza(ReteAutomi rete) {
		this.statiRilevanza = new LinkedHashSet<>(); //insieme con elementi in ordine di inserimento
		this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<StatoRilevanzaRete, List<Pair<Transizione, StatoRilevanzaRete>>>();
		creaSpazioRilevanza(rete);
	}
	
	
	public void creaSpazioRilevanza(ReteAutomi rete) {
		Queue<StatoRilevanzaRete> coda = new LinkedList<>();
		Set<String>decorazioneIniziale = new HashSet<>();
		//la rete deve essere nella condizione iniziale
		StatoRilevanzaRete statoIniziale = new StatoRilevanzaRete(rete, decorazioneIniziale);
		statiRilevanza.add(statoIniziale);
		this.statoRilevanzaIniziale = statoIniziale;
				
		coda.add(statoIniziale);
		
		while(!coda.isEmpty()) {
			StatoRilevanzaRete statoRilevanza = coda.remove();
															
			// faccio andare la rete nella condizione descritta dallo statoRilevanza appena estratto, cosi' poi posso usare i metodi di ReteAutomi 
			// per cercare le transizioni abilitate e gli stati successivi
			setReteAutomi(rete, statoRilevanza);
						
			ArrayList<Transizione>transizioniAbilitate = rete.getTutteTransizioniAbilitate();

			//this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, transizioniAbilitate);
			ArrayList<Pair<Transizione, StatoRilevanzaRete>> listaAdiacenza = new ArrayList<>();
			
			for(Transizione t : transizioniAbilitate) {
				// se vengono provate transizioni diverse (uscenti dallo stesso statoRilevanza), tra una e l'altra la rete deve essere riportata nello statoRilevanza di partenza
				setReteAutomi(rete, statoRilevanza);
				StatoRilevanzaRete nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(rete, t, statoRilevanza.getDecorazione());
				statiRilevanza.add(nuovoStatoRilevanza);

				listaAdiacenza.add(new Pair<Transizione, StatoRilevanzaRete>(t, nuovoStatoRilevanza));
				// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
				if(!mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) {
					coda.add(nuovoStatoRilevanza);
				}
			}
			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, listaAdiacenza);
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
}
