package reteAutomi;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
		ArrayList<String>decorazioneIniziale = new ArrayList<>();
		//la rete deve essere nella condizione iniziale
		StatoRilevanzaRete statoIniziale = new StatoRilevanzaRete(ra, decorazioneIniziale);
		
		System.out.println("STATO INIZIALE: " + statoIniziale);
		
		coda.add(statoIniziale);
		
		while(!coda.isEmpty()) {
			StatoRilevanzaRete statoRilevanza = coda.remove();
						
//			System.out.println("\nstato rilevanza: " + statoRilevanza);
//			System.out.println("coda size: " + coda.size());
						
			// faccio andare la rete nella condizione descritta dallo statoRilevanza appena estratto, cosi' poi posso usare i metodi di ReteAutomi 
			// per cercare le transizioni abilitate e gli stati successivi
			setReteAutomi(statoRilevanza);
						
			ArrayList<Transizione>transizioniAbilitate = ra.getTutteTransizioniAbilitate();
			
//			System.out.println("mappa automi trans abilitate: " + ra.getMappaAutomiTransizioniAbilitate());
//			System.out.println("trans abilitate: " + transizioniAbilitate + "\n");

			
			this.mappaStatoRilevanzaTransizioni.put(statoRilevanza, transizioniAbilitate);
			
			for(Transizione t : transizioniAbilitate) {
				StatoRilevanzaRete nuovoStatoRilevanza = calcolaStatoRilevanzaSucc(t, statoRilevanza.getDecorazione());
				// se c'e' gia' nella mappa non lo aggiungo alla coda -> fare equals a statoRilevanza
				if(!mappaStatoRilevanzaTransizioni.containsKey(nuovoStatoRilevanza)) {
					coda.add(nuovoStatoRilevanza);
				}
			}
		}
	}


	private StatoRilevanzaRete calcolaStatoRilevanzaSucc(Transizione t, ArrayList<String> decorazione) {		
		
		ArrayList<Pair<String, Evento>> contenutoLinks = new ArrayList<>();
		ArrayList<Pair<String, String>> statiAutomi = new ArrayList<>();
		
		ra.svolgiTransizione(t);
		
		for(Link l : ra.getLinks()) {
			contenutoLinks.add(new Pair<>(l.getNome(), l.getEvento()));
		}
		for(Automa a : ra.getAutomi()) {
			statiAutomi.add(new Pair<>(a.getNome(), a.getStatoCorrente().getNome()));
		}
		//aggiungo eventuale etichetta di rilevanza
		if (t.hasEtichettaRilevanza() && !decorazione.contains(t.getEtichettaRilevanza())){
			decorazione.add(t.getEtichettaRilevanza());
		}
		return new StatoRilevanzaRete(contenutoLinks, statiAutomi, decorazione);
		
		/**

		// link pieni ed eventi, stati dell'automa, decorazione


		//valuta esecuzione rispetto a statoRilevanza --> link liberi, automa nello stato giusto, eventi necessari presenti su link

		if (isEseguibile(t)){
			//svuoto links in entrata
			Evento inEntrata = t.getEventoIngresso();
			contenutoLinks.add(svolgiEventoInEntrata(inEntrata));

			//riempio links in uscita
			ArrayList<Evento> inUscita = t.getEventiUscita();
			contenutoLinks.addAll(linkUscitaAggiornati(inUscita));

			//aggiorno automa
			//ATTENZIONE, SIMULA
			ra.simulaPassaggioDiStato(t);
			for (Automa automa : ra.getAutomi()) {
				statiAutomi.add(new Pair<>(automa.getId(), automa.getStatoCorrente().getId()));
			}

			return new StatoRilevanzaRete(contenutoLinks, statiAutomi , decorazione);
		}

		return null;
		*/
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
}
