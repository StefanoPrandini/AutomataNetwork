package reteAutomi;

import javafx.util.Pair;
import java.util.*;
import static java.util.Objects.isNull;

/**
 * Spazio di rilevanza e' un automa -> grafo: StatiRilevanza sono i vertici e transizioni sono gli archi
 */
public class SpazioRilevanza2 {
	private ReteAutomi ra;
	
	// ogni stato di rilevanza della rete viene mappato con tutte le coppie <transizioneUscente, statoRilevanzaSuccessivo>
	private Map<StatoRilevanzaRete, ArrayList<Pair<Transizione, StatoRilevanzaRete>>> mappaStatoRilevanzaTransizioni;
	
	public SpazioRilevanza2(ReteAutomi ra) {
		this.ra = ra;
		this.mappaStatoRilevanzaTransizioni = new LinkedHashMap<StatoRilevanzaRete, ArrayList<Pair<Transizione, StatoRilevanzaRete>>>();
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

			//this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, transizioniAbilitate);
			ArrayList<Pair<Transizione, StatoRilevanzaRete>> listaAdiacenza = new ArrayList<>();
			
			for(Transizione t : transizioniAbilitate) {
				// se vengono provate transizioni diverse (uscenti dallo stesso statoRilevanza), tra una e l'altra la rete deve essere riportata nello statoRilevanza di partenza
				setReteAutomi(statoRilevanza);
				StatoRilevanzaRete nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(t, statoRilevanza.getDecorazione());

				listaAdiacenza.add(new Pair<Transizione, StatoRilevanzaRete>(t, nuovoStatoRilevanza));
				// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
				if(!mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) {
					coda.add(nuovoStatoRilevanza);
				}
			}
			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, listaAdiacenza);
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
		for(Pair<String, String> statoCorrenteAutoma : statoRilevanza.getStatiCorrentiAutoma()) {
			ra.trovaAutoma(statoCorrenteAutoma.getKey()).setStatoCorrente(statoCorrenteAutoma.getValue());
		}
		for(Pair<String, Evento> eventoSuLink : statoRilevanza.getContenutoLinks()) {
			ra.trovaLink(eventoSuLink.getKey()).setEvento(eventoSuLink.getValue());
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


	public Map<StatoRilevanzaRete, ArrayList<Pair<Transizione, StatoRilevanzaRete>>> getMappaStatoRilevanzaTransizioni(){
		return this.mappaStatoRilevanzaTransizioni;
	}


	public ArrayList<StatoRilevanzaRete> getStatiRilevanza(){
		return new ArrayList<>(this.mappaStatoRilevanzaTransizioni.keySet());
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
