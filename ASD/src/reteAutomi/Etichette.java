package reteAutomi;

import java.util.ArrayList;

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
