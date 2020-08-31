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
        return this.sr;
    }

    public Dizionario costruisciDizionarioCompleto(){
        this.diz = new Dizionario(sr);
        return this.diz;
    }

    public String infoDizionario(){
        StringBuilder sb = new StringBuilder();
        sb.append("Dizionario:\n");

        sb.append(diz);

        sb.append("Dizionario ridenominato: \n");
        sb.append(diz.toStringRidenominato());

        return sb.toString();
    }



    public String infoSpazioRilevanza(){
        StringBuilder sb = new StringBuilder();
        sb.append("SPAZIO RILEVANZA:\n");
        sb.append(sr.getStatiRilevanza().size() + " stati\n");
        sb.append(sr);

        for(StatoRilevanzaRete statoRilevanzaRete : sr.getStatiRilevanza()) {
            for(Pair<Transizione, StatoRilevanzaRete> srd : sr.getMappaStatoRilevanzaTransizioni().get(statoRilevanzaRete)) {
                sb.append(statoRilevanzaRete.getRidenominazione() + " -> " + srd.getKey().getNome() + " -> " + srd.getValue().getRidenominazione() + "\n");
            }
        }

        return sb.toString();
    }
}
