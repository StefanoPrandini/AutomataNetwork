package gestore;

import algoritmo.Algoritmo;
import algoritmo.Dizionario;
import algoritmo.EstendiDizionario;
import algoritmo.SpazioRilevanza;
import myLib.InputDati;
import myLib.Stringhe;

public class GestoreDizionari {



    public void estensioneDizionario(GestoreInputOutput inputOutput){

        //TODO WIP, attendere risposte di ste su estensione
        EstendiDizionario ed = new EstendiDizionario(inputOutput);

        Thread thread = new Thread(ed);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.ESTENSIONE_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while ( ! stop.equalsIgnoreCase(Stringhe.STOP) ){ //&& thread.isAlive() && !diz.isInInterruzione()
            if (ed.isTerminato()){
                break;
            }
            stop = InputDati.leggiStringa(Stringhe.ESTENSIONE_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }
        if (stop.equalsIgnoreCase(Stringhe.STOP) && ! ed.isTerminato()){
            interrompiAlgoritmo(ed);
            thread.interrupt();
        }
        System.out.println(ed.buonFine());
    }

    public void effettuaRicerca(GestoreInputOutput inputOutput, Dizionario dizionario){
        inputOutput.setRicerca(true);
        dizionario.setInputOutput(inputOutput);
        dizionario.inizializzaInInterruzione();
        Thread thread = new Thread(dizionario);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.RICERCA_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while ( ! stop.equalsIgnoreCase(Stringhe.STOP) ){ //&& thread.isAlive() && !dizionario.isInInterruzione()
            if (dizionario.isRicercaTerminata()){
                break;
            }
            stop = InputDati.leggiStringa(Stringhe.RICERCA_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }
        if (stop.equalsIgnoreCase(Stringhe.STOP) && ! dizionario.isRicercaTerminata()){
            System.out.println(Stringhe.INTERRUZIONE);
            interrompiAlgoritmo(dizionario);
            thread.interrupt();
        }
        inputOutput.setRicerca(false);
    }

    public void effettuaMonitoraggioRevisione(GestoreInputOutput inputOutput, Dizionario dizionario){

        inputOutput.setMonitoraggio(true);
        dizionario.setInputOutput(inputOutput);
        dizionario.inizializzaInInterruzione();
        dizionario.setTerminato(false);
        Thread thread = new Thread(dizionario);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.MONITORAGGIO_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while ( ! stop.equalsIgnoreCase(Stringhe.STOP) ){ //&& thread.isAlive() && !diz.isInInterruzione()
            if (dizionario.isTerminato()){
                break;
            }
            stop = InputDati.leggiStringa(Stringhe.MONITORAGGIO_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }
        if (stop.equalsIgnoreCase(Stringhe.STOP) && ! dizionario.isTerminato()){
            System.out.println(Stringhe.INTERRUZIONE);
            interrompiAlgoritmo(dizionario);
            thread.interrupt();
        }
        inputOutput.setMonitoraggio(false);
    }

    public SpazioRilevanza calcolaSpazioRilevanza(GestoreInputOutput input) {
        Algoritmo algoritmo = new SpazioRilevanza(input);
        Thread thread = new Thread(algoritmo);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.CALCOLO_SPAZIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while (! stop.equalsIgnoreCase(Stringhe.STOP) ){ //&& thread.isAlive() && !algoritmo.isInInterruzione()
            if (algoritmo.isTerminato() ) break;
            stop = InputDati.leggiStringa(Stringhe.CALCOLO_SPAZIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        }
        if (stop.equalsIgnoreCase(Stringhe.STOP) && !algoritmo.isTerminato()){
            System.out.println(Stringhe.INTERRUZIONE);
            interrompiAlgoritmo(algoritmo);
            thread.interrupt();
        }
        return ridenominaSpazio((SpazioRilevanza)algoritmo);
    }

    public Dizionario calcolaDizionario(GestoreInputOutput input) {

        Algoritmo algoritmo = new Dizionario(input);
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
            System.out.println(Stringhe.INTERRUZIONE);
            interrompiAlgoritmo(algoritmo);
            thread.interrupt();
        }
        return ridenominaDizionario((Dizionario) algoritmo);
    }

    public Dizionario ridenominaDizionario(Dizionario diz){
        diz.ridenominaStati();
        System.out.println(diz.compendio());
        return diz;
    }

    public SpazioRilevanza ridenominaSpazio(SpazioRilevanza sr){
        sr.ridenominaStati();
        System.out.println(sr.compendio());
        return sr;
    }

    public void interrompiAlgoritmo(Algoritmo algoritmo){
        algoritmo.stop();
    }


}
