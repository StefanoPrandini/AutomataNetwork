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

    public Dizionario calcolaDizionario(SpazioRilevanza sr) {
        return ridenominaDizionario(new Dizionario(sr));
    }

    public Dizionario ridenominaDizionario(Dizionario diz){
        diz.ridenominaStati();
        return diz;
    }

}
