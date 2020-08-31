package input;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
// In order to use Gson to parse JSON in Java, you need to add the library as a dependency. You can get the latest version from Maven repository
import com.google.gson.*;
import myLib.Stringhe;
import reteAutomi.Automa;
import reteAutomi.Evento;
import reteAutomi.Link;
import reteAutomi.ReteAutomi;
import reteAutomi.Stato;
import reteAutomi.Transizione;


public class InputParser {
	/**
	 * creo links (senza automi)
	 * creo stati
	 * creo eventi
 	 * creo transizioni
	 * creo automi
  	 * aggiungo automi ai links creati
	 * creo rete
	 */
	
	private JsonObject json;
	private ArrayList<Link> links;
	private ArrayList<Automa> automi;
	
	public InputParser(String filePath) {
		try {
			this.json = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
		} catch (JsonIOException jioe) {
			System.out.println(Stringhe.ERRORE_JSON);
		} catch (JsonSyntaxException jse) {
			System.out.println(Stringhe.ERRORE_FORMATTAZIONE);
		}catch (FileNotFoundException fnfe) {
			System.out.println(Stringhe.ERRORE_FILEPATH);
		}


		this.links = new ArrayList<>();
		this.automi = new ArrayList<>();
	}

	public ReteAutomi parseRete() throws Exception {
		getLinksFromJson();
		getAutomiFromJson();
		addAutomiToLinks();
		
		String idRete = json.get("idRete").getAsString();
		return new ReteAutomi(idRete, this.automi, this.links);
		
	}

	private void getLinksFromJson(){
		JsonArray jLinks = null;
		jLinks = json.getAsJsonArray("links");
		
		for(int i=0; i < jLinks.size(); i++) {
			String nome = jLinks.get(i).getAsJsonObject().get("idLink").getAsString();
			this.links.add(new Link(nome, null, null));
		}
	}
	
	private void getAutomiFromJson() throws Exception{
		JsonArray jAutomi = null;
		jAutomi = json.getAsJsonArray("automi");
		
		for(int i=0; i < jAutomi.size(); i++) {
			String nome = jAutomi.get(i).getAsJsonObject().get("idAutoma").getAsString();
			String nomeStatoIniziale = jAutomi.get(i).getAsJsonObject().get("statoIniziale").getAsString();
			ArrayList<Stato> stati = getStatiFromJSON(jAutomi.get(i));
			ArrayList<Transizione> transizioni = getTransizioniFromJSON(jAutomi.get(i), stati);
			
			Automa automa = new Automa(nome, stati, transizioni, nomeStatoIniziale);
			this.automi.add(automa);
			
		}
	}
	
	private ArrayList<Stato> getStatiFromJSON(JsonElement automa){
		JsonArray jStati = automa.getAsJsonObject().getAsJsonArray("stati");
		ArrayList<Stato> stati = new ArrayList<>();
		for(int i=0; i<jStati.size(); i++) {
			stati.add(new Stato(jStati.get(i).getAsString()));
		}
		return stati;
	}
	
	private ArrayList<Transizione> getTransizioniFromJSON(JsonElement automa, ArrayList<Stato> stati) throws Exception{
		JsonArray jTransizioni = automa.getAsJsonObject().getAsJsonArray("transizioni");
		ArrayList<Transizione> transizioni = new ArrayList<>();
		for(int i=0; i<jTransizioni.size(); i++) {
			JsonObject jTrans = jTransizioni.get(i).getAsJsonObject();
			String nome = jTrans.get("idTransizione").getAsString();
			//non devo creare stati nuovi: li cerco tra gli stati dell'automa in considerazione e se non ci sono -> restano null, vedere cosa fare
			Stato statoPartenza = null;
			Stato statoArrivo = null;
			for(Stato s : stati) {
				if(s.getNome().equals(jTrans.get("statoPartenza").getAsString())) {
					statoPartenza = s;
				}
				if(s.getNome().equals(jTrans.get("statoArrivo").getAsString())) {
					statoArrivo = s;
				}
			}
			if (statoPartenza == null) {
				throw new Exception("Stato di partenza " + jTrans.get("statoPartenza").getAsString() + " non trovato!");	
			}
			if (statoArrivo == null) {
				throw new Exception("Stato di arrivo " + jTrans.get("statoArrivo").getAsString() + " non trovato!");	
			}
			
			String idEventoIn = jTrans.getAsJsonObject("eventoIngresso").get("idEvento").getAsString();
			// se nel json "idEvento" == "null", non c'e' l'evento in ingresso
			Evento eventoIn = null;
			if(!idEventoIn.equals("null")) {
				eventoIn = new Evento(idEventoIn, findLink(jTrans.getAsJsonObject("eventoIngresso").get("link").getAsString()));
			}
			JsonArray jEventiOut = jTrans.getAsJsonArray("eventiUscita");
			ArrayList<Evento> eventiOut = new ArrayList<>();
			for(int j=0; j<jEventiOut.size(); j++) {
				JsonObject jEvento = jEventiOut.get(j).getAsJsonObject();
				Evento eventoOut = new Evento(jEvento.get("idEvento").getAsString(), findLink(jEvento.get("link").getAsString()));
				eventiOut.add(eventoOut);
			}
			String etichettaR = null;
//			se e' eps resta a null
			if(!jTrans.get("etichettaRilevanza").getAsString().equals("eps")) {
				etichettaR = jTrans.get("etichettaRilevanza").getAsString();
			}
			String etichettaO = null;
//			se e' eps resta a null
			if(!jTrans.get("etichettaOsservabilita").getAsString().equals("eps")) {
				etichettaO = jTrans.get("etichettaOsservabilita").getAsString();
			}

			transizioni.add(new Transizione(nome, statoPartenza, statoArrivo, eventoIn, eventiOut, etichettaR, etichettaO));
		}
		return transizioni;
	}
	
	private Link findLink(String nomeLink) {
		for(Link link : this.links) {
			if(link.getNome().equals(nomeLink)) {
				return link;
			}
		}
		return null;
	}
	
	private Automa findAutoma(String nomeAutoma) {
		for(Automa automa : this.automi) {
			if(automa.getNome().equals(nomeAutoma)) {
				return automa;
			}
		}
		return null;
	}
	
	private void addAutomiToLinks() {
		for(Link link : this.links) {
			JsonArray jLinks = json.getAsJsonArray("links");
			for(int i=0; i<jLinks.size(); i++) {
				if(jLinks.get(i).getAsJsonObject().get("idLink").getAsString() == link.getNome()) {
					link.setAutomaPartenza(findAutoma(jLinks.get(i).getAsJsonObject().get("automaIniziale").getAsString()));
					link.setAutomaArrivo(findAutoma(jLinks.get(i).getAsJsonObject().get("automaFinale").getAsString()));
				}
			}
		}
	}

	public ArrayList<Link> getLinks(){
		return this.links;
	}	
	
	public ArrayList<Automa> getAutomi(){
		return this.automi;
	}

}
