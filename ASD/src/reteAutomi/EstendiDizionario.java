package reteAutomi;

import static java.util.Objects.isNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import javafx.util.Pair;

/**
 * Quando si estende il dizionario non si puo' usare lo spazio di rilevanza, servono quindi metodi specifici
 */
public class EstendiDizionario {
	
	private Dizionario dizionario;
	private ReteAutomi rete;
	private Automa osservazione;
	// quando si fa estensione del dizionario non si puo' usare spazio rilevanza, tengo gli stati di rilevanza per non ripeterli
	private Set<StatoRilevanzaRete> statiRilevanza;
	private Set<Transizione> transizioniNonInTraiettoria;
	
	public EstendiDizionario(Dizionario dizionario, ReteAutomi rete, Automa osservazione) {
		this.dizionario = dizionario;
		this.rete = rete;
		this.osservazione = osservazione;
		this.transizioniNonInTraiettoria = new LinkedHashSet<>();
	}
	
	public Dizionario estendi() {
		osservazione.setStatoCorrente(osservazione.getStatoIniziale());
		dizionario.getStatoIniziale().addIndice(osservazione.getStatoIniziale());
		
		statiRilevanza = new LinkedHashSet<>();
//		non si può usare spazio di rilevanza, tengo gli stati per evitare di ripeterli
		for(StatoDizionario s : dizionario.getStatiDizionario()) {
			statiRilevanza.addAll(s.getStatiRilevanza());
		}
		
		LinkedList<StatoDizionario> coda = new LinkedList<>();
		coda.add(dizionario.getStatoIniziale());
		
//		aggiungo gli stati del dizionario alla coda quando gli si aggiunge un indice
		while( ! coda.isEmpty()) {
			
			StatoDizionario statoDiz = coda.remove();
//			servono gli stati di rilevanza per costruire lo stato di rilevanza successivo
//			potrebbero gia' essere stati aggiunti oppure no, non importa, e' un insieme
			statiRilevanza.addAll(statoDiz.getStatiRilevanza());
			
			for(Indice indice : statoDiz.getIndici()) {
				if( ! indice.isMarked()) {
					osservazione.setStatoCorrente(indice.getStato());
					for(Transizione transizioneOsservazione : osservazione.getTransizioniUscentiDaStatoCorrente()) {
//						buonFine se la transizione nell'osservazione e' presente in una traiettoria della rete
						Boolean buonFine = false;
//						quando lo stato e' finale nel dizionario parziale, e' presente nella mappa ma non ha transizioni uscenti: vedo dove vanno le sue transizioni osservabili (estendo)
						if(dizionario.getMappaDizionario().get(statoDiz).isEmpty()) {
							estendiStato(statoDiz, transizioneOsservazione, coda);
						}
//						transizioni uscenti dallo stato del dizionario
						for(Pair<String, StatoDizionario> transizioneDiz : dizionario.getMappaDizionario().get(statoDiz)) {
//							se l'etichetta coincide:
							if(transizioneDiz.getKey().equals(transizioneOsservazione.getEtichettaOsservabilita())) {
//								aggiungo l'indice allo stato destinazione
								transizioneDiz.getValue().addIndice(transizioneOsservazione.getStatoArrivo());
//								aggiungo lo stato destinazione alla coda 
								coda.add(transizioneDiz.getValue());
								buonFine =  true;
							}
//							se non c'e' gia' uno stato nel dizionario raggiungibile con questa etichetta, verifico se puo' essere esteso in questa direzione:
							if( ! buonFine) {
								buonFine = estendiStato(statoDiz, transizioneOsservazione, coda);
							}
						}
						
						if( ! buonFine) {
//							se ci sono transizioni nell'osservazione che non sono presenti in nessuna traiettoria della rete
							transizioniNonInTraiettoria.add(transizioneOsservazione);
						}
					}
//					considerate tutte le transizioni uscenti dal nodo dell'osservazione, MARCARE INDICE
					indice.setMarked(true);
				}
			}
		}
//		stati di rilevanza venivano ridenominati nello spazio di rilevanza, che qua non posso usare
//		chiamo gli stati nuovi in modo diverso (quelli che erano già presenti hanno gia' il nome) -> anche quando carichero' il dizionario?
		String nome = "x";
		int i = 0;
		for (StatoRilevanzaRete s : statiRilevanza) {
			if (isNull(s.getRidenominazione())){
				s.setRidenominazione(nome+i);
				i++;
			}
		}
		
//		formare coppie I/O
		for(StatoDizionario statoDizionario : dizionario.getMappaDizionario().keySet()) {
			//collega I e O, ricerca BFS per vedere se esiste cammino
			Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> IO = coppieIO(statoDizionario);
			statoDizionario.setIO(IO);
		}
		return dizionario;
	}
	
	
	private boolean estendiStato(StatoDizionario statoDiz, Transizione transizioneOss, LinkedList<StatoDizionario> coda) {
//		generare chiusura silenziosa degli stati raggiungibili da stati Output con etichetta corrispondente
		Set<StatoRilevanzaRete> raggiuntiDaOutputs = new LinkedHashSet<>();
		for(StatoRilevanzaRete output : statoDiz.getOutput()) {
//		non posso usare lo spazio di rilevanza, prendo informazioni dalla rete
//		devo portarla allo stato che sto considerando, per vedere che transizioni sono abilitate ecc.
			rete.setReteAutomi(output.getStatiCorrentiAutoma(), output.getContenutoLinks());
			for(Transizione transizioneRete : rete.getTutteTransizioniAbilitate()) {
				if( ! isNull(transizioneRete.getEtichettaOsservabilita()) && transizioneRete.getEtichettaOsservabilita().equals(transizioneOss.getEtichettaOsservabilita())) {
					StatoRilevanzaRete nuovoStatoRilevanza = SpazioRilevanza.calcolaStatoRilevanzaSucc(rete, transizioneRete, output.getDecorazione(), statiRilevanza);
					raggiuntiDaOutputs.add(nuovoStatoRilevanza);											
				}
			}
		}
		
//		se lo stato non ha output (cioe' non ha transizioni osservabili uscenti) non posso estendere
		if(raggiuntiDaOutputs.isEmpty()) {
			return false;
		}
//		ora ho tutti gli stati destinazione delle transizioni che partono dagli Output e che hanno l'etichetta corrispondente all'Osservazione:
//		ne calcolo la epsClosure
		Set<StatoRilevanzaRete> epsClosure = epsClosure(raggiuntiDaOutputs);
		
		// cerco stati output del nuovo stato del dizionario tra gli stati di rilevanza che lo compongono
		Set<StatoRilevanzaRete>statiOutput = new HashSet<>();
		for(StatoRilevanzaRete s : epsClosure) {
			rete.setReteAutomi(s.getStatiCorrentiAutoma(), s.getContenutoLinks());
			for(Transizione t : rete.getTutteTransizioniAbilitate()) {
				if( ! isNull(t.getEtichettaOsservabilita())) {
					statiOutput.add(s);
					break;
				}
			}
		}
		StatoDizionario nuovoStatoDiz = new StatoDizionario(epsClosure, statiOutput);
										
		for(StatoDizionario statoGiaIncontrato : dizionario.getStatiDizionario()) {
			if(statoGiaIncontrato.equals(nuovoStatoDiz)) {
				nuovoStatoDiz = statoGiaIncontrato;
				break;
			}
		}
//		gli stati Input del nuovo stato del dizionario sono quelli raggiungibili dagli stati Output dello stato precedente (con etichetta corretta)
		nuovoStatoDiz.aggiungiInput(raggiuntiDaOutputs);
		
		Set<Pair<String, StatoDizionario>> transizioni = new LinkedHashSet<>();
//		se mappa contiene gia' la chiave, aggiungo ai valori esistenti, altrimenti e' l'unico elemento
		if(dizionario.getMappaDizionario().containsKey(statoDiz)) {
			transizioni = dizionario.getMappaDizionario().get(statoDiz);
		}
		transizioni.add(new Pair<>(transizioneOss.getEtichettaOsservabilita(), nuovoStatoDiz));
		dizionario.getMappaDizionario().put(statoDiz, transizioni);

//		indici e' un insieme: se c'e' gia' non lo aggiunge (indipendentemente se marcato o no)
		nuovoStatoDiz.addIndice(transizioneOss.getStatoArrivo());
		if( ! dizionario.getMappaDizionario().containsKey(nuovoStatoDiz)) {
			dizionario.getMappaDizionario().put(nuovoStatoDiz, new LinkedHashSet<>());
			dizionario.getStatiDizionario().add(nuovoStatoDiz);
		}
//		quando aggiungo un indice metto in coda lo stato
		coda.add(nuovoStatoDiz);
		
		return true;
}
	

	private Set<StatoRilevanzaRete> epsClosure(Set<StatoRilevanzaRete> stati) {
		Set<StatoRilevanzaRete> epsClosure = new LinkedHashSet<>(stati);
		Queue<StatoRilevanzaRete> coda = new LinkedList<StatoRilevanzaRete>(stati);
		statiRilevanza.addAll(stati);
		
		while( ! coda.isEmpty()) {
			StatoRilevanzaRete stato = coda.remove();
			rete.setReteAutomi(stato.getStatiCorrentiAutoma(), stato.getContenutoLinks());
			for(Transizione t : rete.getTutteTransizioniAbilitate()) {
//				se transizioni non hanno etichetta di osservabilita' aggiungo lo stato di rilevanza alla epsClosure
				if(isNull(t.getEtichettaOsservabilita())) {
					StatoRilevanzaRete nuovoStatoRilevanza = SpazioRilevanza.calcolaStatoRilevanzaSucc(rete, t, stato.getDecorazione(), statiRilevanza);
					
					if(!coda.contains(nuovoStatoRilevanza)) {						
						coda.add(nuovoStatoRilevanza);
						epsClosure.add(nuovoStatoRilevanza);
						statiRilevanza.add(nuovoStatoRilevanza);
					}
				}
			}
		}
		return epsClosure;
	}
	
	
	private Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> coppieIO(StatoDizionario sDiz) {
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
				rete.setReteAutomi(s.getStatiCorrentiAutoma(), s.getContenutoLinks());
				// aggiungo alla coda gli stati successivi solo se fanno parte dello stato del dizionario corrente e se non li ho gia' visitati
				for(Transizione transizione : rete.getTutteTransizioniAbilitate()) {
					StatoRilevanzaRete sNext = SpazioRilevanza.calcolaStatoRilevanzaSucc(rete, transizione, s.getDecorazione(), statiRilevanza);
					
					if(sDiz.getStatiRilevanza().contains(sNext) && !visitati.containsKey(sNext)) {
						visitati.put(sNext, true);
						coda.add(sNext);
					}
				}
			}
		}
		return coppie;
	}


	public Set<StatoRilevanzaRete> getStatiRilevanza() {
		return statiRilevanza;
	}
	

//	informa se ci sono transizioni nell'osservazione fornita per eseguire l'estensione dinamica del dizionario che non corrispondono a nessuna traiettoria della rete
	public String buonFine() {
		StringBuilder sb = new StringBuilder();

		if(transizioniNonInTraiettoria.isEmpty()) {
			sb.append("Estensione andata a buon fine! (l'osservazione e' inerente a una o piu' traiettorie della rete)");
		}
		else {
			sb.append("L'estensione e' stata eseguita, tuttavia ci sono transizioni nell'osservazione fornita che non non sono presenti "
					+ "in nessuna traiettoria della rete (e che quindi non sono state considerate):\n");
			for(Transizione t : transizioniNonInTraiettoria) {
				sb.append(t.getStatoPartenza().getNome() + " -> " + t.getEtichettaOsservabilita() + " -> " + t.getStatoArrivo().getNome());
			}
		}
		return sb.toString();
	}

}
