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
			
		String nomeJSON = "Osservazione.json";
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
		int distanzaMax = 0;//SpazioRilevanza.ESPLORAZIONE_COMPLETA;

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
		
		

		/**
		Stato s1 = new Stato("s1");
		Stato s2 = new Stato("s2");
		Stato s3 = new Stato("s3");
		Stato s4 = new Stato("s4");
		Stato s5 = new Stato("s5");
		ArrayList<Stato> stati1 = new ArrayList<Stato>(Arrays.asList(s1,s2,s3));
		ArrayList<Stato> stati2 = new ArrayList<Stato>(Arrays.asList(s4, s5));
		
		Automa c1 = new Automa("c1", stati1, null, s1);
		Automa c2 = new Automa("c2", stati2, null, s4);
		
		Link l1 = new Link("l1", c1, c2);
		Link l2 = new Link("l2", c2, c1);

		Evento e1 = new Evento("e1", l2);
		Evento e2 = new Evento("e2", l1);
		Evento e3 = new Evento("e3", l2);
		Evento e4 = new Evento("e3", l1);
		
		ArrayList<Evento> eventi1 = new ArrayList<>(Arrays.asList(e1));
		ArrayList<Evento> eventi2 = new ArrayList<>(Arrays.asList(e2));
		ArrayList<Evento> eventi3 = new ArrayList<>(Arrays.asList(e3));
		
		Transizione t1 = new Transizione("t1", s1, s2, null, eventi1, null, null);
		Transizione t2 = new Transizione("t2", s2, s3, e3, eventi2, null, null);
		Transizione t3 = new Transizione("t3", s2, s3, e4, eventi3, null, null);

		ArrayList<Transizione> transizioni = new ArrayList<>(Arrays.asList(t1,t2,t3));

		c1.creaMappaStatiTransizioni(stati1, transizioni);
		
		System.out.println("Automa:\n" + c1);
		System.out.println("\n");
		System.out.println("mappa stato-transizioni:\n" + c1.getMappaStatoTransizioni());
		
		ArrayList<Automa>automi = new ArrayList<>(Arrays.asList(c1,c2));
		ArrayList<Link> links = new ArrayList<>(Arrays.asList(l1,l2));
		
		// NULLPOINTEREXCEPTION: probabilmente in Automa.getTransizioniUscentiDaStato() -> get(s) ?
		ReteAutomi ra = new ReteAutomi("ra", automi, links);
		ra.inizializzaRete();
		
		System.out.println(ra.getMappaAutomiTransizioniAbilitate());
		*/
	}
}
