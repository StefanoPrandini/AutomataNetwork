package main;

import input.InputParser;
import reteAutomi.*;
import javafx.util.Pair;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class Prove {
	public static void main(String[] args)  {
			
		String nomeJSON = "AltraRete.json";
//		String nomeJSON = "AltraRete.json";
		// percorso della rete iniziale, in formato JSON
		String pathJSON;
		if(System.getProperty("os.name").equals("Mac OS X")) {
			pathJSON = System.getProperty("user.dir") + File.separator + "ASD" + File.separator + "JSON" + File.separator + nomeJSON;
		}
		else pathJSON = System.getProperty("user.dir") + File.separator + "JSON" + File.separator + nomeJSON;


		InputParser parser = new InputParser(pathJSON);
		ReteAutomi ra = null;
		try {
			ra = parser.parseRete();
		} catch (Exception e) {
			e.printStackTrace();
			//TODO
		}

		System.out.println("Automa 0:\n" + ra.getAutomi().get(0) + "\n");
		System.out.println("Automa 1:\n" + ra.getAutomi().get(1) + "\n");
		System.out.println("Rete Automi:\n" + ra + "\n");

		System.out.println("\nSPAZIO RILEVANZA:");


		//parametro per creazione sottospazi
		//un sottospazio di fatto crea il prefisso del dizionario
		int distanzaMax = SpazioRilevanza.ESPLORAZIONE_COMPLETA;

		SpazioRilevanza spazioRilevanzaRete = new SpazioRilevanza(ra, distanzaMax);

//		spazioRilevanzaRete.creaSpazioRilevanza();
// 		System.out.println(spazioRilevanzaRete); 
		System.out.println(spazioRilevanzaRete.getStatiRilevanza().size() + " stati\n");
		//System.out.println(spazioRilevanzaRete.getTransizioni().size() + " transizioni");
		System.out.println(spazioRilevanzaRete);

		spazioRilevanzaRete.ridenominaStati();
		for (StatoRilevanzaRete statoRilevanzaRete : spazioRilevanzaRete.getStatiRilevanza()) {
			System.out.println(statoRilevanzaRete + " --> " + statoRilevanzaRete.getRidenominazione());
		}
		
		//per vedere come prendere stato rilevanza successivo:
		System.out.println("\n");
		System.out.println("[(StatoRilvanza partenza) -> Transizione -> (StatoRilevanza arrivo)]:");
		for(StatoRilevanzaRete sr : spazioRilevanzaRete.getStatiRilevanza()) {
			for(Pair<Transizione, StatoRilevanzaRete> srd : spazioRilevanzaRete.getMappaStatoRilevanzaTransizioni().get(sr)) {
				System.out.println(sr.getRidenominazione() + " -> " + srd.getKey().getNome() + " -> " + srd.getValue().getRidenominazione());
			}
		}		
		DizionarioCompleto dizionario = new DizionarioCompleto(spazioRilevanzaRete);

		dizionario.ridenominaStati();
		System.out.println("\nDizionario:\n" + dizionario);
		System.out.println("\nDizionario ridenominato:\n" + dizionario.toStringRidenominato());
		
		System.out.println("\nRicerca nel dizionario: ");
		System.out.println("(Osservazione -> Decorazione stato di arrivo)");

		List<String>osservazioneLineare = new ArrayList<String>(Arrays.asList("o3","o2"));
//		List<String>osservazioneLineare = new ArrayList<String>(Arrays.asList("o3","o2","o3","o2"));
		//altra rete
		List<String>osservazioneLineare2 = new ArrayList<String>(Arrays.asList("act","opn","sby","act", "cls"));

		try {
			Set<Set<String>>decorazione = dizionario.ricerca(osservazioneLineare);
			System.out.println("Osservazione lineare " + osservazioneLineare + " -> " + decorazione);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			Set<Set<String>>decorazione = dizionario.ricerca(osservazioneLineare2);
			System.out.println("Osservazione lineare " + osservazioneLineare2 + " -> " + decorazione);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("\nInput e Output:");
		for(StatoDizionario s : dizionario.getMappaDizionario().keySet()) {
			System.out.println("Stato: " + s.getRidenominazione() + ", Input: " + s.getInputToString() + ", Output: " + s.getOutputToString());
		}
		
		System.out.println("\nCoppie IO:");
		for(StatoDizionario s : dizionario.getMappaDizionario().keySet()) {
			System.out.println("Stato " + s.getRidenominazione() + ": I-O = " + s.getIOtoString());
		}

		System.out.println("\nMonitoraggio + revisione:");
		try {
			dizionario.monitoraggio(osservazioneLineare, spazioRilevanzaRete);
			System.out.println("Osservazione: " + osservazioneLineare);
			for (Terna terna : dizionario.getTerne()) {
				System.out.println("Terna " + terna);
			}
		} catch (IOException e) {
			System.out.println("L'osservazione " + osservazioneLineare + " non corrisponde a nessuna traiettoria della rete!");
		}
		
		
		
		
	}
}
