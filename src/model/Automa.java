package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Objects.isNull;


public class Automa implements Serializable {
	//identificatori interi univoci
	private static AtomicInteger ai = new AtomicInteger(0);
	private int id;
	private String nome;

	private Stato statoIniziale;
	/**
	 * Mappa gli Stati con le loro Transizioni uscenti
	 */
	private Map<Stato, List<Transizione>> mappaStatoTransizioni;
	private Stato statoCorrente;
	

	/**
	 */
	public Automa(String nome, ArrayList<Stato> stati, ArrayList<Transizione> transizioni, String nomeStatoIniziale) {
		this.id = ai.incrementAndGet();
		this.nome = nome;
		for(Stato s : stati) {
			if (s.getNome().equals(nomeStatoIniziale)){
				this.statoIniziale = s;
				this.statoCorrente = s;
			}
		}
		this.mappaStatoTransizioni = new LinkedHashMap<>();
		if (!(isNull(stati) || isNull(transizioni))) {
			creaMappaStatiTransizioni(stati, transizioni);
		}
	}

	/**
	 * precondizione: la transizione deve essere abilitata (automa nello stato corrente, evento in ingresso presente su link e link liberi per eventi in uscita)
	 * @param transizione da svolgere
	 * @return int result code
	 */
	public void eseguiTransizione(Transizione transizione){
		this.statoCorrente = transizione.getStatoArrivo();
	}

	public ArrayList<Transizione> getTransizioniUscenti(Stato s){
		ArrayList<Transizione> result = new ArrayList<>();
		result.addAll(mappaStatoTransizioni.get(s));
		return result;
	}
	
	public ArrayList<Transizione> getTransizioniUscenti(int idStato){
		ArrayList<Transizione> result = new ArrayList<>();
		for(Stato s : mappaStatoTransizioni.keySet()) {
			if(s.getId() == idStato) {
				result.addAll(mappaStatoTransizioni.get(s));
			}
		}
		return result;
	}

	public ArrayList<Transizione> getTransizioniUscentiDaStatoCorrente() {
		return getTransizioniUscenti(statoCorrente);
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


	public Stato getStatoIniziale() {
		return this.statoIniziale;
	}
	
	public void setStatoIniziale(Stato statoIniziale) {
		this.statoIniziale = statoIniziale;
	}


	public void setStatoCorrente(Stato statoCorrente) {
		this.statoCorrente = statoCorrente;
	}
	
	public void setStatoCorrente(String nome) {
		for(Stato s : getMappaStatoTransizioni().keySet()) {
			if(s.getNome().equals(nome)) {
				this.statoCorrente = s;
			}
		}
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


	public String getNome() {
		return nome;
	}
	
	
	@Override
	public String toString() {
		String s =  "Automa{" +
					"id=" + id +
					", nome=" + nome +
					", statoIniziale=" + statoIniziale.getNome() +
					", statoCorrente=" + statoCorrente.getNome() + '}';

		StringBuilder sb = new StringBuilder();

		sb.append( "\n	Stati: ");
		sb.append(mappaStatoTransizioni.keySet());
		sb.append( "\n	Transizioni: ");
		sb.append(mappaStatoTransizioni.values());


		/**
		 * for (Stato stato : mappaStatoTransizioni.keySet()) {
		 * 			sb.append("\n");
		 * 			sb.append(stato.toString());
		 * 			sb.append("    \n");
		 * 			sb.append(mappaStatoTransizioni.get(stato));
		 * 			}
		 */

		return s + sb.toString();
	}
	
	
	public String toStringOss() {
		StringBuilder sb = new StringBuilder();
		sb.append("Automa OSSERVAZIONE:");
		sb.append("\n\tnome:\t\t" + nome);
		sb.append( "\n\tStati:\t\t" + mappaStatoTransizioni.keySet());
		sb.append("\n\tstatoIniziale:\t" + statoIniziale.getNome());
		sb.append("\n\tstatoCorrente:\t" + statoCorrente.getNome());
		sb.append( "\n\tTransizioni:\t[");
		for(List<Transizione> tt : mappaStatoTransizioni.values()) {
			for(Transizione t : tt) {
				sb.append(t.toStringOss() + "\n\t\t\t");
			}	
		}
		sb.setLength(sb.length()-6); //toglie ', \n\t\t\t' finale
		sb.append("]");

		return sb.toString();
	}

	
	public boolean isInStatoFinale() {
		return getTransizioniUscentiDaStatoCorrente().size() == 0;
	}


}
