package input;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
// In order to use Gson to parse JSON in Java, you need to add the library as a dependency. You can get the latest version from Maven repository
import com.google.gson.*;
import reteAutomi.Automa;
import reteAutomi.Evento;
import reteAutomi.Link;
import reteAutomi.ReteAutomi;
import reteAutomi.Stato;
import reteAutomi.Transizione;


public class OsservazioneParser {
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
    private Automa automa;

    public OsservazioneParser(String filePath) {
        try {
            this.json = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void getAutomaFromJson() throws Exception{
        JsonArray jAutoma = null;
        jAutoma = json.getAsJsonArray("automi");

        for(int i=0; i < jAutoma.size(); i++) {
            String nome = jAutoma.get(i).getAsJsonObject().get("idAutoma").getAsString();
            String nomeStatoIniziale = jAutoma.get(i).getAsJsonObject().get("statoIniziale").getAsString();
            ArrayList<Stato> stati = getStatiFromJSON(jAutoma.get(i));
            ArrayList<Transizione> transizioni = getTransizioniFromJSON(jAutoma.get(i), stati);

            Automa automa = new Automa(nome, stati, transizioni, nomeStatoIniziale);
            //this.automi.add(automa);

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


            String etichettaO = null;
            if(!jTrans.get("etichettaOsservabilita").getAsString().equals("eps")) {
                etichettaO = jTrans.get("etichettaOsservabilita").getAsString();
            }

            transizioni.add(new Transizione(nome, statoPartenza, statoArrivo, null, null, null, etichettaO));
        }
        return transizioni;
    }





}
