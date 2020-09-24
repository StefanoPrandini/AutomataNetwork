package input;

import javafx.util.Pair;
import myLib.Stringhe;
import reteAutomi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreDizionari {

    private ReteAutomi ra;

    public GestoreDizionari(ReteAutomi ra) {
        this.ra = ra;
    }

    public SpazioRilevanza costruisciSpazioRilevanza(){
        SpazioRilevanza sr = new SpazioRilevanza(this.ra, SpazioRilevanza.ESPLORAZIONE_COMPLETA);
        sr.ridenominaStati();
        return sr;
    }

    public Dizionario costruisciDizionarioCompleto(SpazioRilevanza spazioRilevanza){
        Dizionario diz = new Dizionario(spazioRilevanza);
        diz.ridenominaStati();
        return diz;
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

        sb.append("Stato di rilevanza -> Transizione -> Stato di rilevanza");
        for(StatoRilevanzaRete statoRilevanzaRete : spazioRilevanza.getStatiRilevanza()) {
            for(Pair<Transizione, StatoRilevanzaRete> srd : spazioRilevanza.getMappaStatoRilevanzaTransizioni().get(statoRilevanzaRete)) {
                sb.append(statoRilevanzaRete.getRidenominazione() + " -> " + srd.getKey().getNome() + " -> " + srd.getValue().getRidenominazione() + "\n");
            }
        }

        return sb.toString();
    }

    public List<Terna> effettuaMonitoraggio(Dizionario diz, ArrayList<String> osservazioneLineare, SpazioRilevanza spazioR ) {
        try {
            diz.monitoraggio(osservazioneLineare, spazioR);
        } catch (IOException e) {
            if (diz.getTerne().isEmpty()) System.out.println(Stringhe.OSSERVAZIONE_NON_CORRISPONDE);
        }
        return diz.getTerne();
    }

    public Pair<SpazioRilevanza, Dizionario> costruisciPrefisso(int lunghezzaPrefisso) {
        SpazioRilevanza sr = new SpazioRilevanza(this.ra, lunghezzaPrefisso);
        sr.ridenominaStati();
        Dizionario diz = new Dizionario(sr);
        diz.ridenominaStati();
        return new Pair<>(sr, diz);
    }
}
