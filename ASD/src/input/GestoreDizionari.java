package input;

import javafx.util.Pair;
import myLib.Stringhe;
import reteAutomi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GestoreDizionari {

    private ReteAutomi ra;

    public GestoreDizionari(ReteAutomi ra) {
        this.ra = ra;
    }


    public GestoreDizionari() {
    }


    public Dizionario estensioneDizionario(Dizionario diz, ReteAutomi ra, Automa oss){
        EstendiDizionario ed = new EstendiDizionario(diz, ra, oss);
        diz = ed.estendi();
        System.out.println(ed.buonFine());
        diz.ridenominaStati();
        return diz;
    }

    public Set<Set<String>> effettuaRicerca(ArrayList<String> osservazioneLineare, Dizionario dizionario) throws Exception{
        return dizionario.ricerca(osservazioneLineare);
    }

    public void effettuaMonitoraggioRevisione(ArrayList<String> osservazioneLineare, Dizionario dizionario, SpazioRilevanza sr) throws IOException {
        dizionario.monitoraggio(osservazioneLineare, sr);
    }

    public Dizionario calcolaDizionario(SpazioRilevanza sr) {
        return ridenominaDizionario(new Dizionario(sr));

    }

    public Dizionario ridenominaDizionario(Dizionario diz){
        diz.ridenominaStati();
        return diz;
    }

    public SpazioRilevanza calcolaSpazioRilevanzaPrefisso(ReteAutomi ra, int distanza) {
        return ridenominaSpazio(new SpazioRilevanza(ra, distanza));
    }

    public SpazioRilevanza calcolaSpazioRilevanzaOss(ReteAutomi ra, Automa oss) {
        return ridenominaSpazio(new SpazioRilevanza(ra, oss));
    }

    public SpazioRilevanza ridenominaSpazio(SpazioRilevanza sr){
        sr.ridenominaStati();
        return sr;
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
