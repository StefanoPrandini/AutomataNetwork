package reteAutomi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;


public class Automa {
	//identificatori interi univoci
	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;

	private Stato statoIniziale;
	private Map<Stato, List<Transizione>> mappaStatoTransizioni;
	private Stato statoCorrente;
	

	/**
	 * ATTENZIONE Lo stato iniziale è un doppione 
	 * @param stati
	 * @param transizioni
	 * @param statoIniziale
	 */
	public Automa(ArrayList<Stato> stati, ArrayList<Transizione> transizioni, Stato statoIniziale) {
		this.id = ai.incrementAndGet();
		this.statoIniziale = statoIniziale;
		this.statoCorrente = statoIniziale;
		this.mappaStatoTransizioni = new HashMap<>();
		if (!(isNull(stati) && isNull(transizioni))) creaMappaStatiTransizioni(stati, transizioni);
	}




	/**
	 * precondizione: la transizione deve essere abilitata (automa nello stato corrente, evento in ingresso presente su link)
	 * @param transizione da svolgere
	 * @return int result code
	 */
	public void eseguiTransizione(Transizione transizione){
		this.statoCorrente = transizione.getStatoArrivo();
	}

	public ArrayList<Transizione> getTransizioniUscentiDaStato(Stato s){
		ArrayList<Transizione> result = new ArrayList<>();
		result.addAll(mappaStatoTransizioni.get(s));
		return result;
	}

	public ArrayList<Transizione> getTransizioniAbilitate() {
		return getTransizioniUscentiDaStato(statoCorrente);
	}

	public void inizializzaAutoma(){
		this.statoCorrente = statoIniziale;
	}

	public void creaMappaStatiTransizioni(ArrayList<Stato> stati, ArrayList<Transizione> transizioni) {
		for (Stato stato : stati) {

			ArrayList<Transizione> transizioniUscenti = new ArrayList<>();
			for (Transizione transizione : transizioni) {
				if (transizione.getStatoPartenza().equals(stato)){
					transizioniUscenti.add(transizione);
				}

			}
			this.mappaStatoTransizioni.put(stato, transizioniUscenti);
		}
	}

	public Map<Stato, List<Transizione>> getMappaStatoTransizioni() {
		return mappaStatoTransizioni;
	}


	public Stato getStatoCorrente(){
		return this.statoCorrente;
	}
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		String s =  "Automa{" +
				"id=" + id +
				", statoIniziale=" + statoIniziale.getId();

		String ss =
				", statoCorrente=" + statoCorrente.getId() +
						'}';

		StringBuilder sb = new StringBuilder();

		sb.append( "\nStati: ");
		sb.append(mappaStatoTransizioni.keySet());
		sb.append( "\nTransizioni: ");
		sb.append(mappaStatoTransizioni.values());


		/**
		 * for (Stato stato : mappaStatoTransizioni.keySet()) {
		 * 			sb.append("\n");
		 * 			sb.append(stato.toString());
		 * 			sb.append("    \n");
		 * 			sb.append(mappaStatoTransizioni.get(stato));
		 * 			}
		 */

		return s + ss + sb.toString();
	}


	public void setStatoIniziale(Stato statoIniziale) {
		this.statoIniziale = statoIniziale;
	}




	public void setStatoCorrente(Stato statoCorrente) {
		this.statoCorrente = statoCorrente;
	}


	/**
	 * se non trova transizione ritorna null --> non sarebbe meglio lanciare eccezione ?
	 * @param idTransizione
	 * @return Transizione con id OR null
	 */
	public Transizione trovaTransizioneDaId(Integer idTransizione){
		for (List<Transizione> transizioni : mappaStatoTransizioni.values()) {
			for (Transizione transizione : transizioni) {
				if (transizione.getId() == idTransizione) return transizione;
			}
		}
		return null;
	}
}
