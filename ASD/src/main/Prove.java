package main;

import input.InputParser;
import input.OsservazioneParser;
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
			
		String nomeJSON = "ReteIniziale.json";
//		String nomeJSON = "AltraRete.json";
		// percorso della rete iniziale, in formato JSON
		String systemPathJSON;
		if(System.getProperty("os.name").equals("Mac OS X")) {
			systemPathJSON = System.getProperty("user.dir") + File.separator + "ASD" + File.separator + "JSON" + File.separator;
		}
		else systemPathJSON = System.getProperty("user.dir") + File.separator + "JSON" + File.separator;

		String pathJSON = systemPathJSON + nomeJSON;

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
		int distanzaMax = 2; // SpazioRilevanza.ESPLORAZIONE_COMPLETA;

		SpazioRilevanza spazioRilevanzaRete = new SpazioRilevanza(ra, distanzaMax);

//		spazioRilevanzaRete.creaSpazioRilevanza();
// 		System.out.println(spazioRilevanzaRete); 
		System.out.println(spazioRilevanzaRete.getStatiRilevanza().size() + " stati\n");
		//System.out.println(spazioRilevanzaRete.getTransizioni().size() + " transizioni");


		spazioRilevanzaRete.ridenominaStati();
		System.out.println(spazioRilevanzaRete);
		for (StatoRilevanzaRete statoRilevanzaRete : spazioRilevanzaRete.getStatiRilevanza()) {
			System.out.println(statoRilevanzaRete + " --> " + statoRilevanzaRete.getRidenominazione());
		}
		
		//per vedere come prendere stato rilevanza successivo:
		System.out.println("\n");
		System.out.println("[(StatoRilevanza partenza) -> Transizione -> (StatoRilevanza arrivo)]:");
		for(StatoRilevanzaRete sr : spazioRilevanzaRete.getStatiRilevanza()) {
			for(Pair<Transizione, StatoRilevanzaRete> srd : spazioRilevanzaRete.getMappaStatoRilevanzaTransizioni().get(sr)) {
				System.out.println(sr.getRidenominazione() + " -> " + srd.getKey().getNome() + " -> " + srd.getValue().getRidenominazione());
			}
		}		
		Dizionario dizionario = new Dizionario(spazioRilevanzaRete);

		dizionario.ridenominaStati();
		System.out.println("\nDizionario:\n" + dizionario);
		System.out.println("\nDizionario ridenominato:\n" + dizionario.toStringRidenominato());
		
		System.out.println("\nRicerca nel dizionario: ");
		System.out.println("(Osservazione -> Decorazione stato di arrivo)");

		List<String>osservazioneLineare = new ArrayList<String>(Arrays.asList("o3","o2"));
//		List<String>osservazioneLineare = new ArrayList<String>(Arrays.asList("o3","o2","o3","o2"));
		//altra rete
		List<String>osservazioneLineare2 = new ArrayList<String>(Arrays.asList("act","opn","sby","act","cls"));
		List<String>osservazioneLineare3 = new ArrayList<String>(Arrays.asList("act","opn","sby","cls","act"));

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
			dizionario.monitoraggio(osservazioneLineare2, spazioRilevanzaRete);
			System.out.println("Osservazione: " + osservazioneLineare2);
			for (Terna terna : dizionario.getTerne()) {
				System.out.println("Terna " + terna);
			}
		} catch (IOException e) {
			System.out.println("L'osservazione " + osservazioneLineare2 + " non corrisponde a nessuna traiettoria della rete!");
		}
		
//		-----------------------------------------------------------------------------------------------------------------------------------------
//		ESTENSIONE DINAMICA DIZIONARIO
		
		System.out.println("\n\n\nESTENSIONE DINAMICA DEL DIZIONARIO:\n");
		// OSSERVAZIONE DA JSON:
		String osservazionePerEstensioneJSON = "OsservazionePerEstensione2.json";
		
		// percorso dell'osservazione, in formato JSON
		String pathOsservazionePerEstensione;
		if(System.getProperty("os.name").equals("Mac OS X")) {
			pathOsservazionePerEstensione = System.getProperty("user.dir") + File.separator + "ASD" + File.separator + "JSON" + File.separator + osservazionePerEstensioneJSON;
		}
		else pathOsservazionePerEstensione = System.getProperty("user.dir") + File.separator + "JSON" + File.separator + osservazionePerEstensioneJSON;


		OsservazioneParser OsservazionePerEstensioneParser = new OsservazioneParser(pathOsservazionePerEstensione);
		Automa osservazionePerEstensione = null;
		try {
			osservazionePerEstensione = OsservazionePerEstensioneParser.getOsservazione();
		} catch (Exception e) {
			e.printStackTrace();
			//TODO
		}
		
		System.out.println("Osservazione per l'estensione:");
		System.out.println(osservazionePerEstensione.toStringOss() + "\n");
		
		EstendiDizionario ed = new EstendiDizionario(dizionario, ra, osservazionePerEstensione);
		dizionario = ed.estendi();
		System.out.println(ed.buonFine());
		
		System.out.println("\nRidenominazione stati di rilevanza:");
		for(StatoRilevanzaRete s : ed.getStatiRilevanza()) {
			System.out.println(s + " -> " + s.getRidenominazione());
		}
		
		System.out.println("\n" + dizionario.toString());
		dizionario.ridenominaStati();
		System.out.println(dizionario.toStringRidenominato());
		
		System.out.println("\nInput e Output:");
		for(StatoDizionario s : dizionario.getMappaDizionario().keySet()) {
			System.out.println("Stato " + s.getRidenominazione() + " -> Input: " + s.getInputToString() + ", Output: " + s.getOutputToString());
		}
		
		System.out.println("\nCoppie IO:");
		for(StatoDizionario s : dizionario.getMappaDizionario().keySet()) {
			System.out.println("Stato " + s.getRidenominazione() + " -> coppie I/O: " + s.getIOtoString());
		}
		
//		RICERCA nel dizionario esteso
		System.out.println("\nRicerca nel dizionario esteso:");
		try {
			Set<Set<String>>decorazione = dizionario.ricerca(osservazioneLineare3);
			System.out.println("Osservazione lineare " + osservazioneLineare3 + " -> " + decorazione);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
}
