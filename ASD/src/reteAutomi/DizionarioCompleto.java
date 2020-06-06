package reteAutomi;

import java.util.Map;

/**
 * Dizionario Completo delle osservazioni:
 * DFA (Automa a stati Finiti Deterministico) risultante dalla determinazione dell'intero spazio di rilevanza (tramite Subset Construction), in cui gli stati sono corredati
 * dalla loro diagnosi (insieme di tutte le decorazioni degli stati del NFA contenuti nello stato del DFA)
 * @author Stefano
 *
 */
public class DizionarioCompleto {
	
	private Map<StatoRilevanzaRete, Transizione> map;
	
	public DizionarioCompleto(SpazioRilevanza spazioRilevanza) {
		determinazioneSpazio(spazioRilevanza);
	}

}
