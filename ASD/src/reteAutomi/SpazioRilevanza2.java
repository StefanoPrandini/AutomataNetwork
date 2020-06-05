package reteAutomi;

import javafx.util.Pair;

import java.util.*;

/**
 * Spazio di rilevanza e' un automa -> grafo: StatiRilevanza sono i vertici e transizioni sono gli archi
 */
public class SpazioRilevanza2 {
	private ReteAutomi ra;
	
	private Map<StatoRilevanzaRete, List<Transizione>> mappaStatoRilevanzaTransizioni;
	
	public SpazioRilevanza2(ReteAutomi ra) {
		this.ra = ra;
		this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<>();
	}
	
	
	public void creaSpazioRilevanza() {
		Queue<StatoRilevanzaRete> coda = new LinkedList<>();
		Set<String>decorazioneIniziale = new HashSet<>();
		//la rete deve essere nella condizione iniziale
		StatoRilevanzaRete statoIniziale = new StatoRilevanzaRete(ra, decorazioneIniziale);
				
		coda.add(statoIniziale);
		
		while(!coda.isEmpty()) {
			StatoRilevanzaRete statoRilevanza = coda.remove();
															
			// faccio andare la rete nella condizione descritta dallo statoRilevanza appena estratto, cosi' poi posso usare i metodi di ReteAutomi 
			// per cercare le transizioni abilitate e gli stati successivi
			setReteAutomi(statoRilevanza);
						
			ArrayList<Transizione>transizioniAbilitate = ra.getTutteTransizioniAbilitate();
			
			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, transizioniAbilitate);
			
			for(Transizione t : transizioniAbilitate) {
				// se vengono provate transizioni diverse (uscenti dallo stesso statoRilevanza), tra una e l'altra la rete deve essere riportata nello statoRilevanza di partenza
				setReteAutomi(statoRilevanza);
				StatoRilevanzaRete nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(t, statoRilevanza.getDecorazione());
				// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
				if(!mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) {
					coda.add(nuovoStatoRilevanza);
				}
			}
		}
	}


	private StatoRilevanzaRete calcolaStatoRilevanzaSucc(Transizione t, Set<String> decorazione) {		
		Set<String>newDecorazione = new HashSet<>(decorazione);		
		ra.svolgiTransizione(t);

		//aggiungo eventuale etichetta di rilevanza
		if (t.hasEtichettaRilevanza() && !newDecorazione.contains(t.getEtichettaRilevanza())){
			newDecorazione.add(t.getEtichettaRilevanza());
		}
		return new StatoRilevanzaRete(ra, newDecorazione);
	}


	/**
	 * precondizione: gli eventi sono destinati a link effettivamente liberi
	 * @param eventi
	 * @return

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
	 */
	
	/**
	 * Porta la rete di automi nella situazione descritta dallo stato di rilevanza (statiCorrenti degli automi ed eventi sui link)
	 * @param statoRilevanza
	 */
	public void setReteAutomi(StatoRilevanzaRete statoRilevanza) {
		for(Pair<String, String> statiCorrentiAutoma : statoRilevanza.getStatiCorrentiAutoma()) {
			ra.trovaAutoma(statiCorrentiAutoma.getKey()).setStatoCorrente(statiCorrentiAutoma.getValue());
		}
		for(Pair<String, Evento> eventiSuLink : statoRilevanza.getContenutoLinks()) {
			ra.trovaLink(eventiSuLink.getKey()).setEvento(eventiSuLink.getValue());
		}
		
		ra.aggiornaMappaAutomiTransizioniAbilitate();
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



	public ArrayList<StatoRilevanzaRete> getStatiRilevanza(){
		return new ArrayList<>(this.mappaStatoRilevanzaTransizioni.keySet());
	}


	public ArrayList<Transizione> getTransizioni(){
		ArrayList< Transizione> result  = new ArrayList<>();

		for (List<Transizione> transiziones : mappaStatoRilevanzaTransizioni.values()) {
			result.addAll(transiziones);
		}
		return result;
	}
}
