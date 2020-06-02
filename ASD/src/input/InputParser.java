package input;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
// In order to use Gson to parse JSON in Java, you need to add the library as a dependency. You can get the latest version from Maven repository
import com.google.gson.*;
import reteAutomi.Automa;
import reteAutomi.Link;
import reteAutomi.Stato;


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
	
	public String filePath;
	public ArrayList<Link> links;
	public ArrayList<Automa> automi;
	
	public InputParser(String filePath) {
		this.filePath = filePath;
		this.links = new ArrayList<>();
		this.automi = new ArrayList<>();
	}

	public void parseRete() {
		getLinksFromJson();
		getAutomiFromJson();
	}
	
	private void getLinksFromJson(){
		JsonArray jLinks = null;
		try {
			JsonElement content = JsonParser.parseReader(new FileReader(filePath));
			JsonObject jObject = content.getAsJsonObject();
			jLinks = jObject.getAsJsonArray("link");
		} 
		catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(int i=0; i < jLinks.size(); i++) {
			String nome = jLinks.get(i).getAsJsonObject().get("idLink").getAsString();
			this.links.add(new Link(nome, null, null));
		}
	}
	
	private void getAutomiFromJson(){
		JsonArray jAutomi = null;
		try {
			JsonElement content = JsonParser.parseReader(new FileReader(filePath));
			JsonObject jObject = content.getAsJsonObject();
			jAutomi = jObject.getAsJsonArray("automi");
		} 
		catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i=0; i < jAutomi.size(); i++) {
			String nome = jAutomi.get(i).getAsJsonObject().get("idAutoma").getAsString();
			String staatoIniziale = jAutomi.get(i).getAsJsonObject().get("statoIniziale").getAsString();
			ArrayList<Stato> stati = getStatiFromJSON(jAutomi.get(i));

			
			this.automi.add(new Automa(nome, stati, null, new Stato(staatoIniziale)));
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
	
	public ArrayList<Link> getLinks(){
		return this.links;
	}	
	
	public ArrayList<Automa> getAutomi(){
		return this.automi;
	}

}
