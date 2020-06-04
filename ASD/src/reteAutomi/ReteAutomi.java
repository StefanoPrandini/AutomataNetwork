package reteAutomi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import static java.util.Objects.isNull;

public class ReteAutomi {
	
	private ArrayList<Automa> automi;
	private ArrayList<Link> links;
	private LinkedHashMap<Automa, List<Transizione>> mappaAutomiTransizioniAbilitate = new LinkedHashMap<>();
	private String nome;
	
	public ReteAutomi(String nome, ArrayList<Automa> automi,	ArrayList<Link> links) {
		this.nome = nome;
		this.automi = automi;
		this.links = links;
		inizializzaRete();
	}


	/**
	 * inizializza automi e links
	 */
	public void inizializzaRete(){
		inizializzaAutomi();
		inizializzaLinks();
		aggiornaMappaAutomiTransizioniAbilitate();
	}

	/**
	 *
	 * aggiorno la hashmap usando come chiave id dell'automa e con contenuto la lista delle transizioni eseguibili
	 *
	 * @return
	 */
	public void aggiornaMappaAutomiTransizioniAbilitate(){
		LinkedHashMap<Automa, List<Transizione>> result = new LinkedHashMap<>();
		for (Automa automa : automi) {
			//transizioni disponibili nello stato corrente
			ArrayList<Transizione> transizioniUscenti = automa.getTransizioniUscentiDaStatoCorrente();
//			System.out.println("trans usc sCorrente: " + transizioniUscenti);

			//lista, da riempire, delle transizioni con eventi disponibili allo scatto
			ArrayList<Transizione> transizioniConEventiAbilitati = new ArrayList<>();
			for (Transizione transizione : transizioniUscenti) {
				
				//se la transizione e' con eventi in entrata e in uscita null
				if (transizione.eventiEntrataEUscitaNull()){
					transizioniConEventiAbilitati.add(transizione);
				}

				//se l'evento in ingresso e' null o corrisponde a quello presente sul link indicato dall'evento
				else if (isNull(transizione.getEventoIngresso()) ||
						transizione.getEventoIngresso().equals(transizione.getEventoIngresso().getLink().getEvento())){

					//devo controllare anche che i link di uscita siano vuoti, se la lista ne contiene e non è null
					boolean linkUscitaDisponibili = false;
					if (!transizione.getEventiUscita().isEmpty() ){
						linkUscitaDisponibili = linkDestinazioneDisponibili(transizione.getEventiUscita());
					}

					//se i link di uscita degli eventi sono vuoti allora aggiungo alla lista delle transizioni che possono scattare
					if (linkUscitaDisponibili){
						transizioniConEventiAbilitati.add(transizione);
					}
				}
			}
			//aggiungo alla mappa l'automa e le rispettive transizioni che possono scattare
			result.put(automa, transizioniConEventiAbilitati);
		}

		this.mappaAutomiTransizioniAbilitate = result;
	}

	public HashMap<Automa, List<Transizione>> getMappaAutomiTransizioniAbilitate() {
		return mappaAutomiTransizioniAbilitate;
	}

	/**
	 * controlla che i link di destinazione siano vuoti
	 * @param eventi, lista non null
	 * @return true se link disponibili
	 */
	public boolean linkDestinazioneDisponibili(ArrayList<Evento> eventi){
		for (Evento evento : eventi) {
			if (isNull(evento)) return false;
			if (!evento.getLink().isVuoto()){
				return false;
			}
		}
		return true;
	}
	

	/**
	 * precondizione: la transizione deve essere eseguibile
	 * @param t
	 */
	public void svolgiTransizione(Transizione t){
		//se l'evento in ingresso non e' null lo svolgo
		if (!isNull(t.getEventoIngresso())){
			t.getEventoIngresso().svolgiEvento();
		}
		//se gli eventi in uscita non sono null li aggiungo ai link dedicati
		if (!isNull(t.getEventiUscita())){
			for (Evento evento : t.getEventiUscita()) {
				evento.aggiungiEventoSuLink();
			}
		}
		//eseguo il passaggio di stato sull'automa
		for (Automa automa : automi) {
			for (List<Transizione> listaTrans : automa.getMappaStatoTransizioni().values()) {
				if (listaTrans.contains(t)){
					automa.eseguiTransizione(t);
					
					aggiornaMappaAutomiTransizioniAbilitate();
					
					return;
				}
			}
		}
	}


	public void svolgiTransizioneDaId(int idAutoma, int idTransizione){
		svolgiTransizione(trovaAutoma(idAutoma).trovaTransizioneDaId(idTransizione));
	}

	/**
	 * porta automi della rete allo stato iniziale
	 */
	public void inizializzaAutomi(){
		for (Automa automa : automi) {
			automa.inizializzaAutoma();
		}
	}

	/**
	 * imposta gli eventi dei link a null
	 */
	public void inizializzaLinks(){
		for (Link link : links) {
			link.aggiungiEvento(null);
		}
	}


	public Automa trovaAutoma(int id){
		return automi.stream()
				.filter(automa -> id == automa.getId())
				.findAny()
				.orElse(null);
	}
	
	public Automa trovaAutoma(String nome){
		return automi.stream()
				.filter(automa -> nome == automa.getNome())
				.findAny()
				.orElse(null);
	}
	
	public Link trovaLink(int id) {
		return links.stream()
				.filter(link -> id == link.getId())
				.findAny()
				.orElse(null);
	}
	
	public Link trovaLink(String nome) {
		return links.stream()
				.filter(link -> nome == link.getNome())
				.findAny()
				.orElse(null);
	}

	
	public ArrayList<Transizione> transizioniAbilitateDaIdAutoma(int idAutoma){
		ArrayList<Transizione> result = new ArrayList<>();
		Automa a = trovaAutoma(idAutoma);
		if (isNull(a)) return null;
		result.addAll(mappaAutomiTransizioniAbilitate.get(a));
		return result;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Rete di automi: " + automi.size() + " automi | " + links.size() +" link\n");

		for (Automa automa : automi) {
			ArrayList<String>nomiStati = new ArrayList<>();
			for(Stato s : automa.getMappaStatoTransizioni().keySet()) {
				nomiStati.add(s.getNome());
			}
			ArrayList<String>nomiTransizioni = new ArrayList<>();
			for(List<Transizione> listT : automa.getMappaStatoTransizioni().values()) {
				for(Transizione t : listT) {
					nomiTransizioni.add(t.getNome());
				}
				
			}
			sb.append("  Automa " + automa.getNome() + ": " +
					nomiStati.size() + " stati " + nomiStati + ", " +
					nomiTransizioni.size() + " transizioni " + nomiTransizioni + "\n");
		}

		for (Link link : links) {
			sb.append("  Link " + link.getNome() + ": " +
					link.getAutomaPartenza().getNome() + " -> " + link.getAutomaArrivo().getNome());
		}
		
		return sb.toString();
	}

	
	public ArrayList<Automa> getAutomi() {
		return automi;
	}

	
	public ArrayList<Link> getLinks() {
		return links;
	}
	
	public String getNome() {
		return nome;
	}
	

	public ArrayList<Transizione> getTutteTransizioniAbilitate(){
		ArrayList<Transizione> result = new ArrayList<>();
		for (List<Transizione> listaTransizioni : mappaAutomiTransizioniAbilitate.values()) {
			result.addAll(listaTransizioni);
		}
		return result;

	}


	public void simulaPassaggioDiStato(Transizione transizione){
		for (Automa automa : automi) {
			for (List<Transizione> listaTrans : automa.getMappaStatoTransizioni().values()) {
				if (listaTrans.contains(transizione)){
					automa.setStatoCorrente(transizione.getStatoArrivo());
					return;
				}
			}
		}
	}
}
