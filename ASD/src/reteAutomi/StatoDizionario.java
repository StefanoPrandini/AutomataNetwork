package reteAutomi;

import java.util.*;
import javafx.util.Pair;

/**
 * Stato dell'Automa a stati Finiti Deterministico (DFA): e' un insieme di stati dell'Automa a stati Finiti Non deterministico (NFA)
 * Il DFA si ottiene dalla determinazione tramite Subset Construction del NFA
 *
 */
public class StatoDizionario {
	private Set<StatoRilevanzaRete> statiRilevanza;
	private String ridenominazione;
	private Set<Set<String>> diagnosi; // insieme delle decorazioni degli statiRilevanza contenuti
	
	private Set<StatoRilevanzaRete> input;
	private Set<StatoRilevanzaRete> output;
	private Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> I_O;
	
	/**
	public StatoRilevanzaReteDeterminizzata(Set<StatoRilevanzaRete> statiRilevanza) {
		this.statiRilevanza = statiRilevanza;
		
		this.diagnosi = new HashSet<>();
		for(StatoRilevanzaRete s : statiRilevanza) {
			this.diagnosi.add(s.getDecorazione());
		}
		
		this.input = new HashSet<>();
		this.output = new HashSet<>();
		this.I_O = new HashSet<>();
	}
	*/
	
	public StatoDizionario(Set<StatoRilevanzaRete> statiRilevanza, Set<StatoRilevanzaRete> statiOutput) {
		this.statiRilevanza = statiRilevanza;
		
		this.diagnosi = new HashSet<>();
		for(StatoRilevanzaRete s : statiRilevanza) {
			this.diagnosi.add(s.getDecorazione());
		}
		
		this.output = statiOutput;
		this.input = new HashSet<>();
		this.I_O = new HashSet<>();
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
	
	public Set<StatoRilevanzaRete> getInput() {
		return input;
	}


	public void setInput(Set<StatoRilevanzaRete> input) {
		this.input = input;
	}


	public Set<StatoRilevanzaRete> getOutput() {
		return output;
	}


	public void setOutput(Set<StatoRilevanzaRete> output) {
		this.output = output;
	}


	public Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> getI_O() {
		return I_O;
	}


	public void setI_O(Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> i_O) {
		I_O = i_O;
	}
	
	
	public void aggiungiInput(Set<StatoRilevanzaRete> nuoviInput) {
		for(StatoRilevanzaRete s : nuoviInput) {
			this.input.add(s);
		}
	}


	/**
	 * Brutto
	 */
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
        if (!(o instanceof StatoDizionario)) return false;
        StatoDizionario that = (StatoDizionario) o;
        return this.statiRilevanza.equals(that.getStatiRilevanza());
    }

}
