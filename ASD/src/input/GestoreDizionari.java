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

    public Set<Set<String>> effettuaRicerca(InputOutput inputOutput) throws Exception{
        /*
        Set<Set<String>> diagnosi;
        .setRicerca(true);
        Thread thread = new Thread(dizionario);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.RICERCA_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while (! stop.equalsIgnoreCase(Stringhe.STOP) && thread.isAlive() && !dizionario.isInInterruzione()){
            if (stop.equalsIgnoreCase(Stringhe.STOP)){
                interrompiAlgoritmo();
                thread.interrupt();
            }
            else if (stop.equalsIgnoreCase(Stringhe.OK)){
                break;
            }
            stop = InputDati.leggiStringa(Stringhe.RICERCA_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }

        return dizionario.ricerca(osservazioneLineare);
        */
         return null;
    }

    public void effettuaMonitoraggioRevisione(ArrayList<String> osservazioneLineare, Dizionario dizionario, SpazioRilevanza sr) throws IOException {
        dizionario.monitoraggio(osservazioneLineare, sr);
    }

    public SpazioRilevanza calcolaSpazioRilevanza(InputOutput input) {
        algoritmo = new SpazioRilevanza(input);
        Thread thread = new Thread(algoritmo);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.CALCOLO_SPAZIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while (! stop.equalsIgnoreCase(Stringhe.STOP) && thread.isAlive() && !algoritmo.isInInterruzione()){
            if (stop.equalsIgnoreCase(Stringhe.STOP)){
                interrompiAlgoritmo();
                thread.interrupt();
            }
            else if (stop.equalsIgnoreCase(Stringhe.OK)){
                break;
            }
            stop = InputDati.leggiStringa(Stringhe.CALCOLO_SPAZIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }
        return ridenominaSpazio((SpazioRilevanza)algoritmo);
    }

    public Dizionario calcolaDizionario(InputOutput input) {

        algoritmo = new Dizionario(input);
        Thread thread = new Thread(algoritmo);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.CALCOLO_DIZIONARIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while (! stop.equalsIgnoreCase(Stringhe.STOP) && thread.isAlive() && !algoritmo.isInInterruzione()){
            if (stop.equalsIgnoreCase(Stringhe.STOP)){
                interrompiAlgoritmo();
                thread.interrupt();
            }
            else if (stop.equalsIgnoreCase(Stringhe.OK)){
                break;
            }
            stop = InputDati.leggiStringa(Stringhe.CALCOLO_DIZIONARIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
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
