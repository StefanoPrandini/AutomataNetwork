package model;

import java.io.Serializable;
import java.util.*;
import javafx.util.Pair;

/**
 * Stato dell'Automa a stati Finiti Deterministico (DFA): e' un insieme di stati dell'Automa a stati Finiti Non deterministico (NFA)
 * Il DFA si ottiene dalla determinazione tramite Subset Construction del NFA
 *
 */
public class StatoDizionario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Set<StatoRilevanzaRete> statiRilevanza;
	private String ridenominazione;
	private Set<Set<String>> diagnosi; // insieme delle decorazioni di rilevanza degli statiRilevanza contenuti
	
	private Set<StatoRilevanzaRete> input;
	private Set<StatoRilevanzaRete> output;
	private Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> IO;
	
//	indici e' un set: l'equals andra' fatto solo sul nome e non sul marked (utlima riga pag. 86)
	private Set<Indice> indici;
	
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
		
		this.diagnosi = new LinkedHashSet<>();
		for(StatoRilevanzaRete s : statiRilevanza) {
			this.diagnosi.add(s.getDecorazione());
		}
		
		this.output = statiOutput;
		this.input = new LinkedHashSet<>();
		this.IO = new LinkedHashSet<>();
		this.indici = new LinkedHashSet<>();
	}



	public Set<StatoRilevanzaRete> getStatiRilevanza() {
		return statiRilevanza;
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

	public Set<StatoRilevanzaRete> getInput() {
		return input;
	}
	
	public String getInputToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(StatoRilevanzaRete sIn : this.input) {
			sb.append(sIn.getRidenominazione() + ", ");
		}
		// se c'e' solo la quadra iniziale non devo rimuovere caratteri alla fine
		if(sb.length()>1) {
			sb.delete(sb.length()-2, sb.length());
		}
		sb.append("]");
		return sb.toString();
	}


	public void setInput(Set<StatoRilevanzaRete> input) {
		this.input = input;
	}


	public Set<StatoRilevanzaRete> getOutput() {
		return output;
	}
	
	public String getOutputToString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(StatoRilevanzaRete sOut : this.output) {
			sb.append(sOut.getRidenominazione() + ", ");
		}
		// se c'e' solo la quadra iniziale non devo rimuovere caratteri alla fine
		if(sb.length()>1) {
			sb.delete(sb.length()-2, sb.length());
		}
		sb.append("]");
		return sb.toString();
	}


	public void setOutput(Set<StatoRilevanzaRete> output) {
		this.output = output;
	}


	public Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> getIO() {
		return IO;
	}
	
	public String getIOtoString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Pair<StatoRilevanzaRete, StatoRilevanzaRete> coppia : this.IO) {
			sb.append("<" + coppia.getKey().getRidenominazione() + ", " + coppia.getValue().getRidenominazione() + "> , ");
		}
		// se c'e' solo la quadra iniziale non devo rimuovere caratteri alla fine
		if(sb.length()>1) {
			sb.delete(sb.length()-3, sb.length());
		}
		sb.append("]");
		return sb.toString();
	}


	public void setIO(Set<Pair<StatoRilevanzaRete, StatoRilevanzaRete>> IO) {
		this.IO = IO;
	}
	
	
	public void aggiungiInput(Set<StatoRilevanzaRete> nuoviInput) {
		for(StatoRilevanzaRete s : nuoviInput) {
			this.input.add(s);
		}
	}
	
	
	public Set<StatoRilevanzaRete>getIfromO(Set<StatoRilevanzaRete> outputs) {
		Set<StatoRilevanzaRete> inputs = new HashSet<>();
		for(StatoRilevanzaRete output : outputs) {
			for(Pair<StatoRilevanzaRete, StatoRilevanzaRete> IO : this.IO) {
				if(IO.getValue().equals(output)) {
					inputs.add(IO.getKey());
				}
			}
		}
		return inputs;
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

	public Set<Indice> getIndici(){
		return this.indici;
	}

	public void addIndice(Stato stato) {
		Indice indice = new Indice(stato);
//		e' un set: aggiunge solo se non c'e' gia' (il nome, indipendenemente da marked)
		indici.add(indice);
	}

}
