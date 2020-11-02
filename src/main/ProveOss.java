package main;

import parser.InputParser;
import model.*;

import java.io.File;

public class ProveOss {
	public static void main(String[] args)  {
			
//		String nomeJSON = "altraRete.json";
		String nomeJSON = "reteIniziale.json";
		// percorso della rete, in formato JSON
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
		

		System.out.println("\n\n\nOSSERVAZIONE FROM JSON:\n");
		// OSSERVAZIONE DA JSON:
		String osservazioneJSON = "Osservazione1.json";
//		String osservazioneJSON = "Osservazione2.json";
		
		// percorso dell'osservazione, in formato JSON
		String pathOsservazioneJSON;
		if(System.getProperty("os.name").equals("Mac OS X")) {
			pathOsservazioneJSON = System.getProperty("user.dir") + File.separator + "ASD" + File.separator + "JSON" + File.separator + osservazioneJSON;
		}
		else pathOsservazioneJSON = System.getProperty("user.dir") + File.separator + "JSON" + File.separator + osservazioneJSON;
		System.out.println(pathOsservazioneJSON);

		//OsservazioneParser OsservazioneParser = new OsservazioneParser(pathOsservazioneJSON);
		// Automa osservazione = null;
		try {
			//osservazione = OsservazioneParser.getOsservazione();
		} catch (Exception e) {
			e.printStackTrace();
			//TODO
		}
		
		// System.out.println(osservazione.toStringOss());
		
//		-----------------------------------------------------------------------------------------------------------------------------------------


		/**
		System.out.println("\nSPAZIO RILEVANZA DA OSSERVAZIONE:");
		SpazioRilevanza spazioRilevanzaDaOss = new SpazioRilevanza(ra, osservazione);

		System.out.println(spazioRilevanzaDaOss.getStatiRilevanza().size() + " stati\n");
//		System.out.println(spazioRilevanzaDaOss);
		
		System.out.println("Ridenominazione stati:");
		spazioRilevanzaDaOss.ridenominaStati();
		for (StatoRilevanzaRete statoRilevanzaRete : spazioRilevanzaDaOss.getStatiRilevanza()) {
			System.out.println(statoRilevanzaRete + " -> " + statoRilevanzaRete.getRidenominazione());
		}
		
		// per vedere come prendere stato rilevanza successivo
		// alcuni stati destinazione a null: quelli che non fanno parte dello spazio ma la cui transizione serve per gli output
		System.out.println("\nStatoRilvanza partenza -> Transizione -> StatoRilevanza arrivo:");
		for(StatoRilevanzaRete sr : spazioRilevanzaDaOss.getStatiRilevanza()) {
			for(Pair<Transizione, StatoRilevanzaRete> transizione : spazioRilevanzaDaOss.getMappaStatoRilevanzaTransizioni().get(sr)) {
				System.out.println(sr.getRidenominazione() + " -> " + transizione.getKey().getNome() + " -> " + transizione.getValue().getRidenominazione());
			}
		}
		
//		-----------------------------------------------------------------------------------------------------------------------------------------

		Dizionario dizionario = new Dizionario(spazioRilevanzaDaOss);
		System.out.println("\n\nDizionario:\n" + dizionario);
		dizionario.ridenominaStati();
		System.out.println("Dizionario ridenominato:\n" + dizionario.toStringRidenominato());
		
//		-----------------------------------------------------------------------------------------------------------------------------------------
		
		System.out.println("\nRicerca nel dizionario: ");
//		System.out.println("(Osservazione -> Decorazione stato di arrivo)");

//		reteIniziale
//		List<String>osservazioneLineare = new ArrayList<String>(Arrays.asList("o3","o2"));
		List<String>osservazioneLineare = new ArrayList<String>(Arrays.asList("o3","o2","o3","o2"));
		//altraRete
		List<String>osservazioneLineare2 = new ArrayList<String>(Arrays.asList("act","opn","sby"));
//		List<String>osservazioneLineare2 = new ArrayList<String>(Arrays.asList("act","opn","sby","act", "cls"));

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
		
//		-----------------------------------------------------------------------------------------------------------------------------------------

		System.out.println("\nInput e Output:");
		for(StatoDizionario s : dizionario.getMappaDizionario().keySet()) {
			System.out.println("Stato " + s.getRidenominazione() + " -> Input: " + s.getInputToString() + ", Output: " + s.getOutputToString());
		}
		
		System.out.println("\nCoppie IO:");
		for(StatoDizionario s : dizionario.getMappaDizionario().keySet()) {
			System.out.println("Stato " + s.getRidenominazione() + " -> coppie I/O: " + s.getIOtoString());
		}
		
//		-----------------------------------------------------------------------------------------------------------------------------------------

		System.out.println("\nMonitoraggio + revisione:");
		try {
			dizionario.monitoraggio(osservazioneLineare, spazioRilevanzaDaOss);
			System.out.println("Osservazione: " + osservazioneLineare);
			for (Terna terna : dizionario.getTerne()) {
				System.out.println("Terna " + terna);
			}
		} catch (IOException e) {
			System.out.println("L'osservazione lineare " + osservazioneLineare + " non corrisponde a nessuna traiettoria della rete!");
		}
		
		System.out.println("\nMonitoraggio + revisione:");
		try {
			dizionario.monitoraggio(osservazioneLineare2, spazioRilevanzaDaOss);
			System.out.println("Osservazione: " + osservazioneLineare2);
			for (Terna terna : dizionario.getTerne()) {
				System.out.println("Terna " + terna);
			}
		} catch (IOException e) {
			System.out.println("L'osservazione lineare " + osservazioneLineare2 + " non corrisponde a nessuna traiettoria della rete!");
		}
		
//		-----------------------------------------------------------------------------------------------------------------------------------------
//		ESTENSIONE DINAMICA DIZIONARIO
		
		System.out.println("\n\n\nESTENSIONE DINAMICA DEL DIZIONARIO:\n");
		// OSSERVAZIONE DA JSON:
		String osservazionePerEstensioneJSON = "OsservazionePerEstensione.json";
		
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
			Set<Set<String>>decorazione = dizionario.ricerca(osservazioneLineare);
			System.out.println("Osservazione lineare " + osservazioneLineare + " -> " + decorazione);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		 **/
	}

}
