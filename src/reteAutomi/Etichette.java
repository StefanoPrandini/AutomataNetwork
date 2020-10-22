package reteAutomi;

import java.util.ArrayList;


/**
 * serve?
 * in caso, serve anche associare un'etichetta ad una transizione? -relazione di funzione-
 */

public class Etichette {



	private ArrayList<String> etichetteRilevanza;
	private ArrayList<String> etichetteOsservabilita;
	
	public Etichette(ArrayList<String> etichetteRilevanza, ArrayList<String> etichetteOsservabilita) {
		this.etichetteRilevanza = etichetteRilevanza;
		this.etichetteOsservabilita = etichetteOsservabilita;
	}



	public ArrayList<String> getEtichetteRilevanza() {
		return etichetteRilevanza;
	}

	public ArrayList<String> getEtichetteOsservabilita() {
		return etichetteOsservabilita;
	}

}
