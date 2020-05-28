package main;

import java.util.ArrayList;
import java.util.Arrays;

import reteAutomi.Automa;
import reteAutomi.Evento;
import reteAutomi.Link;
import reteAutomi.ReteAutomi;
import reteAutomi.Stato;
import reteAutomi.Transizione;

public class Prove {
	public static void main(String[] args) {
				
		Stato s1 = new Stato();
		Stato s2 = new Stato();
		Stato s3 = new Stato();
		Stato s4 = new Stato();
		Stato s5 = new Stato();
		ArrayList<Stato> stati = new ArrayList<Stato>(Arrays.asList(s1,s2,s3,s4,s5));
		
		Automa c1 = new Automa(stati, null, s1);
		Automa c2 = new Automa(stati, null, s2);
		
		Link l1 = new Link(c1, c2);
		Link l2 = new Link(c2, c1);

		Evento e1 = new Evento(l2);
		Evento e2 = new Evento(l1);
		Evento e3 = new Evento(l2);
		Evento e4 = new Evento(l1);
		
		ArrayList<Evento> eventi1 = new ArrayList<>(Arrays.asList(e1));
		ArrayList<Evento> eventi2 = new ArrayList<>(Arrays.asList(e2));
		ArrayList<Evento> eventi3 = new ArrayList<>(Arrays.asList(e3));
		
		Transizione t1 = new Transizione(s1, s2, null, eventi1, null, null);
		Transizione t2 = new Transizione(s2, s3, e3, eventi2, null, null);
		Transizione t3 = new Transizione(s2, s3, e4, eventi3, null, null);

		ArrayList<Transizione> transizioni = new ArrayList<>(Arrays.asList(t1,t2,t3));

		c1.creaMappaStatiTransizioni(stati, transizioni);
		
		System.out.println("Automa:\n" + c1);
		System.out.println("\n");
		System.out.println("mappa stato-transizioni:\n" + c1.getMappaStatoTransizioni());
		
		ArrayList<Automa>automi = new ArrayList<>(Arrays.asList(c1,c2));
		ArrayList<Link> links = new ArrayList<>(Arrays.asList(l1,l2));
		
		// NULLPOINTEREXCEPTION: probabilmente in Automa.getTransizioniUscentiDaStato() -> get(s) ?
		ReteAutomi ra = new ReteAutomi(automi, links);
		ra.inizializzaRete();
		
		System.out.println(ra.getMappaAutomiTransizioniAbilitate());

	}

}
