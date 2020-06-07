package reteAutomi;

import java.util.HashSet;
import java.util.Set;

/**
 * Stato dell'Automa a stati Finiti Deterministico (DFA): e' un insieme di stati dell'Automa a stati Finiti Non deterministico (NFA)
 * Il DFA si ottiene dalla determinazione tramite Subset Construction del NFA
 *
 */
public class StatoRilevanzaReteDeterminizzata {
	private Set<StatoRilevanzaRete> statiRilevanza;
	private String ridenominazione;
	private Set<Set<String>> diagnosi = new HashSet<>(); // insieme delle decorazioni degli statiRilevanza contenuti
	
	public StatoRilevanzaReteDeterminizzata(Set<StatoRilevanzaRete> statiRilevanza) {
		this.statiRilevanza = statiRilevanza;
		for(StatoRilevanzaRete s : statiRilevanza) {
			diagnosi.add(s.getDecorazione());
		}
	}


	public Set<StatoRilevanzaRete> getStatiRilevanza() {
		return statiRilevanza;
	}
	
	public void setStatiRilevanza(Set<StatoRilevanzaRete> statiRilevanza) {
		this.statiRilevanza = statiRilevanza;
	}


	public String getRidenominazione() {
		return ridenominazione;
	}


	public void setRidenominazione(String ridenominazione) {
		this.ridenominazione = ridenominazione;
	}


	public Set<Set<String>> getDiagnosi() {
		return diagnosi;
	}


	public void setDiagnosi(Set<Set<String>> diagnosi) {
		this.diagnosi = diagnosi;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for(StatoRilevanzaRete s : statiRilevanza) {
			sb.append(s.getRidenominazione() + ", ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append(")");
		return sb.toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((statiRilevanza == null) ? 0 : statiRilevanza.hashCode());
		return result;
	}

	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatoRilevanzaReteDeterminizzata)) return false;
        StatoRilevanzaReteDeterminizzata that = (StatoRilevanzaReteDeterminizzata) o;
        return this.statiRilevanza.equals(that.getStatiRilevanza());
    }

}
