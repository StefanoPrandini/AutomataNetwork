package input;

import myLib.InputDati;
import reteAutomi.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class GestoreDizionari {

    private Algoritmo algoritmo;

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

    public SpazioRilevanza calcolaSpazioRilevanza(Input input) {
        algoritmo = new SpazioRilevanza(input);
        Thread thread = new Thread(algoritmo);
        thread.start();
        String stop = InputDati.leggiStringa("Calcolo spazio rilevanza in corso, inserisci 'stop' per fermare: ");
        while (! stop.equalsIgnoreCase("stop") && thread.isAlive() && !algoritmo.isInInterruzione()){
            if (stop.equalsIgnoreCase("stop")){
                interrompiAlgoritmo();
                thread.interrupt();
            }
            else if (stop.equalsIgnoreCase("ok")){
                break;
            }
            stop = InputDati.leggiStringa("Calcolo spazio rilevanza in corso, inserisci 'stop' per fermare: ");
        }
        return ridenominaSpazio((SpazioRilevanza)algoritmo);
    }

    public SpazioRilevanza ridenominaSpazio(SpazioRilevanza sr){
        sr.ridenominaStati();
        return sr;
    }

    public Dizionario calcolaDizionario(Input input) {
        Dizionario diz = new Dizionario(input);
        diz.run();
        return ridenominaDizionario(diz);
    }

    public Dizionario ridenominaDizionario(Dizionario diz){
        diz.ridenominaStati();
        return diz;
    }


    public void interrompiAlgoritmo(){
        this.algoritmo.stop();
    }
}
