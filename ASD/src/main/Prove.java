package main;

import java.util.ArrayList;
import java.util.Arrays;
import com.google.gson.JsonArray;
import input.InputParser;
import reteAutomi.Automa;
import reteAutomi.Evento;
import reteAutomi.Link;
import reteAutomi.ReteAutomi;
import reteAutomi.Stato;
import reteAutomi.Transizione;

public class Prove {
	public static void main(String[] args) {
		
		String JSONPath = "C:\\Users\\Stefano\\git\\AutomataNetwork\\ASD\\JSON\\ReteIniziale.json";
		
		InputParser parser = new InputParser(JSONPath);
		parser.parseRete();
		ArrayList<Link> links = parser.getLinks();
		ArrayList<Automa> automi = parser.getAutomi();
		
		System.out.println("nomi link da JSON:");
		for(Link l : links) {
			System.out.println(l.getNome());
		}
		System.out.println("\nnomi automi da JSON:");
		for(Automa a : automi) {
			System.out.println(a);
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
