package main;

import reteAutomi.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

	static ArrayList<Transizione> tr = new ArrayList<>();
	static ArrayList<Stato> stati = new ArrayList<>();
	static ArrayList<Transizione> tr1 = new ArrayList<>();
	static ArrayList<Stato> stati1 = new ArrayList<>();
	static ArrayList<Evento> eventi = new ArrayList<>();
	static ArrayList<Link> link = new ArrayList<>();
	static ArrayList<Automa> automi = new ArrayList<>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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

	}

}
