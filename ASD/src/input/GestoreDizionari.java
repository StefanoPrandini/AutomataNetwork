package input;

import javafx.util.Pair;
import reteAutomi.*;

public class GestoreDizionari {

    private ReteAutomi ra;
    private SpazioRilevanza sr;
    private Dizionario diz;




    public GestoreDizionari(ReteAutomi ra) {
        this.ra = ra;
    }

    public SpazioRilevanza costruisciSpazioRilevanza(){
        this.sr = new SpazioRilevanza(this.ra, SpazioRilevanza.ESPLORAZIONE_COMPLETA);
        this.sr.ridenominaStati();
        return this.sr;
    }

    public Dizionario costruisciDizionarioCompleto(SpazioRilevanza spazioRilevanza){
        this.diz = new Dizionario(spazioRilevanza);
        this.diz.ridenominaStati();
        return this.diz;
    }

    public String infoDizionario(Dizionario dizionario){
        StringBuilder sb = new StringBuilder();
        sb.append("Dizionario:\n");

        sb.append(dizionario);

        sb.append("Dizionario ridenominato: \n");
        sb.append(dizionario.toStringRidenominato());

        return sb.toString();
    }



    public String infoSpazioRilevanza(SpazioRilevanza spazioRilevanza){
        StringBuilder sb = new StringBuilder();
        sb.append("SPAZIO RILEVANZA:\n");
        sb.append(spazioRilevanza.getStatiRilevanza().size() + " stati\n");
        sb.append(spazioRilevanza);

        for(StatoRilevanzaRete statoRilevanzaRete : spazioRilevanza.getStatiRilevanza()) {
            for(Pair<Transizione, StatoRilevanzaRete> srd : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(statoRilevanzaRete)) {
                sb.append(statoRilevanzaRete.getRidenominazione() + " -> " + srd.getKey().getNome() + " -> " + srd.getValue().getRidenominazione() + "\n");
            }
        }

        return sb.toString();
    }
}
