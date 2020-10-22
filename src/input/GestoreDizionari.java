package input;

import myLib.InputDati;
import myLib.Stringhe;
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

    public Set<Set<String>> effettuaRicerca(InputOutput inputOutput, Dizionario diz) throws Exception{
        inputOutput.setRicerca(true);
        diz.setInputOutput(inputOutput);
        Thread thread = new Thread(diz);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.RICERCA_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while ( ! stop.equalsIgnoreCase(Stringhe.STOP) ){ //&& thread.isAlive() && !diz.isInInterruzione()
            if (diz.isTerminato()){
                break;
            }
            stop = InputDati.leggiStringa(Stringhe.RICERCA_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }
        if (stop.equalsIgnoreCase(Stringhe.STOP) && ! diz.isTerminato()){
            interrompiAlgoritmo();
            thread.interrupt();
        }
        inputOutput.setRicerca(false);
        return diz.getInputOutput().getRisultatoRicerca();

    }

    public void effettuaMonitoraggioRevisione(ArrayList<String> osservazioneLineare, Dizionario dizionario, SpazioRilevanza sr) throws IOException {
        dizionario.monitoraggio(osservazioneLineare, sr);
    }

    public SpazioRilevanza calcolaSpazioRilevanza(InputOutput input) {
        algoritmo = new SpazioRilevanza(input);
        Thread thread = new Thread(algoritmo);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.CALCOLO_SPAZIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while (! stop.equalsIgnoreCase(Stringhe.STOP) ){ //&& thread.isAlive() && !algoritmo.isInInterruzione()
            if (algoritmo.isTerminato() ) break;
            stop = InputDati.leggiStringa(Stringhe.CALCOLO_SPAZIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }
        if (stop.equalsIgnoreCase(Stringhe.STOP) && !algoritmo.isTerminato()){
            interrompiAlgoritmo();
            thread.interrupt();
        }
        return ridenominaSpazio((SpazioRilevanza)algoritmo);
    }

    public Dizionario calcolaDizionario(InputOutput input) {

        algoritmo = new Dizionario(input);
        Thread thread = new Thread(algoritmo);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.CALCOLO_DIZIONARIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while (! stop.equalsIgnoreCase(Stringhe.STOP) ){ //&& thread.isAlive() && !algoritmo.isInInterruzione()
             if (algoritmo.isTerminato()){
                break;
            }
            stop = InputDati.leggiStringa(Stringhe.CALCOLO_DIZIONARIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }
        if (stop.equalsIgnoreCase(Stringhe.STOP)){
            interrompiAlgoritmo();
            thread.interrupt();
        }
        return ridenominaDizionario((Dizionario) algoritmo);
    }

    public Dizionario ridenominaDizionario(Dizionario diz){
        diz.ridenominaStati();
        return diz;
    }

    public SpazioRilevanza ridenominaSpazio(SpazioRilevanza sr){
        sr.ridenominaStati();
        return sr;
    }

    public void interrompiAlgoritmo(){
        this.algoritmo.stop();
    }
}
