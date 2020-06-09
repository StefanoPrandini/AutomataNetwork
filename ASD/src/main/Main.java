package main;

import reteAutomi.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Main {



	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Terna t = new Terna("p", 22, "t");

		Terna t2 = new Terna("p", "s", "t");
		System.out.println(t);


		/*Automa c1 = new Automa("c1", null, null, null);
		Automa c2 = new Automa("c2",null, null, null);

		Link l1 = new Link("l1", c1, c2);
		Link l2 = new Link("l2", c2, c1);



		Stato s1 = new Stato("s1");
		Stato s2 = new Stato("s2");
		Stato s3 = new Stato("s3");
		Stato s4 = new Stato("s4");
		Stato s5 = new Stato("s5");

		Evento e1 = new Evento("e1",l2);
		Evento e2 = new Evento("e2",l1);
		Evento e3 = new Evento("e3",l2);
		Evento e4 = new Evento("e4",l1);
		Evento e7 = new Evento("e7",l2);


		ArrayList<Evento> evs1 = new ArrayList<>();
		evs1.add(e2);
		ArrayList<Evento> evs2 = new ArrayList<>();
		evs2.add(e4);
		ArrayList<Evento> evs3 = new ArrayList<>();
		evs3.add(null);
		ArrayList<Evento> evs4 = new ArrayList<>();
		evs4.add(e3);
		ArrayList<Evento> evs5 = new ArrayList<>();
		evs5.add(e1);
*/
		/*

		//Evento e0 = new Evento(l2);

		//l2.setEvento(e0);

		Transizione t1 = new Transizione("t1",s1, s2, null, evs1, "rilevanza1", "oss1");
		Transizione t2 = new Transizione("t2",s2, s3, e3, evs2, "rilevanza2", "oss2");
		Transizione t3 = new Transizione("t3",s2, s3, e7, evs3, "rilevanza3", "oss3");

		ArrayList<Transizione> ts1 = new ArrayList<>();
		ArrayList<Stato> ss1 = new ArrayList<>();

		ss1.add(s1);
		ss1.add(s2);
		ss1.add(s3);

		ts1.add(t1);
		ts1.add(t2);
		ts1.add(t3);

		c1.setStatoIniziale(s1);
		c1.inizializzaAutoma();


		c1.creaMappaStatiTransizioni(ss1, ts1);

		Transizione t4 = new Transizione("t4",s4, s5, e2, evs4, "rilevanza4", "oss4");
		Transizione t5 = new Transizione("t5",s5, s5, e4, evs5, "rilevanza5", "oss5");

		ArrayList<Transizione> ts2 = new ArrayList<>();
		ArrayList<Stato> ss2 = new ArrayList<>();

		ss2.add(s4);
		ss2.add(s5);

		ts2.add(t4);
		ts2.add(t5);

		c2.setStatoIniziale(s4);
		c2.inizializzaAutoma();

		c2.creaMappaStatiTransizioni(ss2, ts2);

		ArrayList<Automa>automi = new ArrayList<>();
		automi.add(c1);
		automi.add(c2);
		ArrayList<Link> links = new ArrayList<>();
		links.add(l1);
		links.add(l2);

		ReteAutomi ra = new ReteAutomi("r1",automi, links);
		//System.out.println(ra);
		ra.aggiornaMappaAutomiTransizioniAbilitate();



		System.out.println(ra.getAutomi().get(0).getStatoCorrente());
		System.out.println(ra.getAutomi().get(1).getStatoCorrente());

				*/
/**
		ra.svolgiTransizione(ra.getMappaAutomiTransizioniAbilitate().get(automi.get(0)).get(0));

		System.out.println(ra.getAutomi().get(0).getStatoCorrente());

		ra.aggiornaMappaIdAutomiTransizioniAbilitate();
		System.out.println(ra.getAutomi().get(1).getStatoCorrente());

		ra.svolgiTransizione(ra.getMappaAutomiTransizioniAbilitate().get(automi.get(1)).get(0));

		System.out.println(ra.getAutomi().get(1).getStatoCorrente());

		 **/		/*

		ArrayList<Transizione> daSvolgere = new ArrayList<>();
		for (List<Transizione> listaTransizioni : ra.getMappaAutomiTransizioniAbilitate().values()) {
			daSvolgere.addAll(listaTransizioni);
		}

		ArrayList<String> ril = new ArrayList<>();
		ArrayList<String> oss = new ArrayList<>();


		ArrayList<StatoRilevanzaRete> statiRilevanza = new ArrayList<>();

		while (!daSvolgere.isEmpty()){
			Transizione nextTr = daSvolgere.get(0);
			System.out.println("eseguo transizione " + nextTr);
			ra.svolgiTransizione(nextTr);
			ril.add(nextTr.getEtichettaRilevanza());
			oss.add(nextTr.getEtichettaOsservabilita());

			statiRilevanza.add(new StatoRilevanzaRete(ra, new HashSet<>()));



			ra.aggiornaMappaAutomiTransizioniAbilitate();


			daSvolgere.clear();
			for (List<Transizione> listaTransizioni : ra.getMappaAutomiTransizioniAbilitate().values()) {
				daSvolgere.addAll(listaTransizioni);
			}

		}


		System.out.println("stato finale");
		System.out.println(ra.getAutomi().get(0).getStatoCorrente());
		System.out.println(ra.getAutomi().get(1).getStatoCorrente());

		System.out.println(statiRilevanza.get(0).toString());

		//SpazioRilevanza sr = new SpazioRilevanza(statiRilevanza, ril, oss);


		*/


	}


	/**
	 *
	 *
	 *
	 *
	 *
	 *
	 automi.add(new Automa(null, null, null));
	 automi.add(new Automa(null, null, null));

	 link.add(new Link(null, null));
	 link.add(new Link(null, null));
	 link.add(new Link(null, null));
	 link.add(new Link(null, null));
	 link.add(new Link(null, null));
	 link.add(new Link(null, null));

	 for (int i = 0; i < 10; i++) {

	 stati.add(new Stato());
	 stati1.add(new Stato());
	 tr.add(new Transizione(null, null, null, null, "r"+i, "f"+i));
	 tr.add(new Transizione(null, null, null, null, "r"+i, "f"+i));
	 tr1.add(new Transizione(null, null, null, null, "r"+i, "f"+i));
	 tr1.add(new Transizione(null, null, null, null, "r"+i, "f"+i));
	 eventi.add(new Evento(null));

	 }




	 for (Link link1 : link) {
	 link1.setAutomaPartenza(automi.get(ThreadLocalRandom.current().nextInt(0, automi.size() )));
	 link1.setAutomaArrivo(automi.get(ThreadLocalRandom.current().nextInt(0, automi.size())));
	 }

	 for (Transizione transizione : tr) {
	 transizione.setStatoPartenza(stati.get(ThreadLocalRandom.current().nextInt(0, stati.size())));
	 transizione.setStatoArrivo(stati.get(ThreadLocalRandom.current().nextInt(0, stati.size())));
	 transizione.setEventoIngresso(eventi.get(ThreadLocalRandom.current().nextInt(0, eventi.size())));
	 transizione.addEventoUscita(eventi.get(ThreadLocalRandom.current().nextInt(0, eventi.size())));
	 transizione.addEventoUscita(eventi.get(ThreadLocalRandom.current().nextInt(0, eventi.size())));
	 }

	 for (Transizione transizione : tr1) {
	 transizione.setStatoPartenza(stati1.get(ThreadLocalRandom.current().nextInt(0, stati1.size())));
	 transizione.setStatoArrivo(stati1.get(ThreadLocalRandom.current().nextInt(0, stati1.size())));
	 transizione.setEventoIngresso(eventi.get(ThreadLocalRandom.current().nextInt(0, eventi.size())));
	 transizione.addEventoUscita(eventi.get(ThreadLocalRandom.current().nextInt(0, eventi.size())));
	 transizione.addEventoUscita(eventi.get(ThreadLocalRandom.current().nextInt(0, eventi.size())));
	 }

	 for (Evento evento : eventi) {
	 evento.setLink(link.get(ThreadLocalRandom.current().nextInt(0, link.size())));
	 }


	 tr.get(0).setStatoPartenza(stati.get(0));
	 tr.get(0).setEventiUscita(new ArrayList<>());
	 tr.get(0).setEventoIngresso(null);
	 tr.get(0).addEventoUscita(new Evento(new Link(automi.get(0), automi.get(1))));

	 automi.get(0).creaMappaStatiTransizioni(stati, tr);
	 automi.get(0).setStatoIniziale(stati.get(0));
	 automi.get(0).inizializzaAutoma();
	 automi.get(1).creaMappaStatiTransizioni(stati1, tr1);
	 automi.get(1).setStatoIniziale(stati1.get(0));
	 automi.get(1).inizializzaAutoma();


	 //stampa di tutti gli automi e link in maniera decente
	 System.out.println(automi.get(0));
	 //System.out.println(automi.get(1));
	 for (Link link1 : link) {
	 System.out.println(link1);
	 }



	 ReteAutomi ra = new ReteAutomi(automi, link);
	 //System.out.println(ra.mappaIdAutomiTransizioniAbilitate());
	 StringBuilder sb = new StringBuilder();

	 for (Integer id : ra.mappaIdAutomiTransizioniAbilitate().keySet()) {
	 sb.append("automa ");
	 sb.append(id);
	 sb.append(" ---->\n");
	 sb.append(ra.mappaIdAutomiTransizioniAbilitate().get(id));
	 sb.append("\n");
	 }

	 System.out.println(sb.toString());

	 ra.svolgiTransizione(automi.get(0), tr.get(0));
	 System.out.println(automi.get(0).getStatoCorrente());
	 */

}
