package parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
// In order to use Gson to parse JSON in Java, you need to add the library as a dependency. You can get the latest version from Maven repository
import com.google.gson.*;
import model.Automa;
import model.Stato;
import model.Transizione;


public class OsservazioneParser {
	
    private JsonObject json;

    public OsservazioneParser(String filePath) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        this.json = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
    }


    public Automa getOsservazione() throws Exception {
    	String nome = json.get("idAutoma").getAsString();
		String nomeStatoIniziale = json.get("statoIniziale").getAsString();
		ArrayList<Stato> stati = getStatiFromJSON(json);
		ArrayList<Transizione> transizioni = getTransizioniFromJSON(json, stati);
		
		Automa automa = new Automa(nome, stati, transizioni, nomeStatoIniziale);
		return automa;
    }

    private ArrayList<Stato> getStatiFromJSON(JsonObject automa){
        JsonArray jStati = automa.getAsJsonArray("stati");
        ArrayList<Stato> stati = new ArrayList<>();
        for(int i=0; i<jStati.size(); i++) {
            stati.add(new Stato(jStati.get(i).getAsString()));
        }
        return stati;
    }

    
    private ArrayList<Transizione> getTransizioniFromJSON(JsonObject automa, ArrayList<Stato> stati) throws Exception{
        JsonArray jTransizioni = automa.getAsJsonArray("transizioni");
        ArrayList<Transizione> transizioni = new ArrayList<>();
        for(int i=0; i<jTransizioni.size(); i++) {
            JsonObject jTrans = jTransizioni.get(i).getAsJsonObject();
            String nome = null;//jTrans.get("idTransizione").getAsString();
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

            String etichettaO = null;
            if(!jTrans.get("etichettaOsservabilita").getAsString().equals("eps")) {
                etichettaO = jTrans.get("etichettaOsservabilita").getAsString();
            }

            transizioni.add(new Transizione(nome, statoPartenza, statoArrivo, null, null, null, etichettaO));
        }
        return transizioni;
    }

}
