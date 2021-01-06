package gestore;

import algoritmo.Algoritmo;
import algoritmo.Dizionario;
import algoritmo.EstendiDizionario;
import algoritmo.SpazioRilevanza;
import myLib.InputDati;
import myLib.Stringhe;

public class GestoreDizionari {

    public void estensioneDizionario(GestoreInputOutput inputOutput){

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
        GestoreFile.stampaLogAlgoritmoEstensione(inputOutput.getOsservazione().getNome(), ed.buonFine(), ed.tempoEsecuzione());
    }

    public void effettuaRicerca(GestoreInputOutput inputOutput, Dizionario dizionario){
        inputOutput.setRicerca(true);
        dizionario.setInputOutput(inputOutput);
        dizionario.inizializzaInInterruzione();
        Thread thread = new Thread(dizionario);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.RICERCA_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while ( ! stop.equalsIgnoreCase(Stringhe.STOP) ){
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
        GestoreFile.stampaLogAlgoritmoRicerca(dizionario);

    }

    public void effettuaMonitoraggioRevisione(GestoreInputOutput inputOutput, Dizionario dizionario){

        inputOutput.setMonitoraggio(true);
        dizionario.setInputOutput(inputOutput);
        dizionario.inizializzaInInterruzione();
        dizionario.setTerminato(false);
        Thread thread = new Thread(dizionario);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.MONITORAGGIO_IN_CORSO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while ( ! stop.equalsIgnoreCase(Stringhe.STOP) ){
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
        GestoreFile.stampaLogAlgoritmoMonitoraggio(inputOutput.getOsservazioneLineareMonitoraggio(), inputOutput.getLogMonitoraggio(), dizionario.tempoEsecuzione());
    }

    public SpazioRilevanza calcolaSpazioRilevanza(GestoreInputOutput input) {
        Algoritmo algoritmo = new SpazioRilevanza(input);
        Thread thread = new Thread(algoritmo);
        thread.start();
        String stop = InputDati.leggiStringa(Stringhe.CALCOLO_SPAZIO + String.format(Stringhe.INSERISCI_PER_INTERROMPERE, Stringhe.STOP));
        while (! stop.equalsIgnoreCase(Stringhe.STOP) ){
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
        while (! stop.equalsIgnoreCase(Stringhe.STOP) ){
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
        GestoreFile.stampaLogAlgoritmo(Stringhe.FILE_LOG_COSTRUZIONE_DIZIONARIO, diz.getInputOutput().getRete().getNome(), diz.logCostruzione(), diz.tempoEsecuzione());
        return diz;
    }

    public SpazioRilevanza ridenominaSpazio(SpazioRilevanza sr){
        sr.ridenominaStati();
        System.out.println(sr.compendio());
        GestoreFile.stampaLogAlgoritmo(Stringhe.FILE_LOG_COSTRUZIONE_SPAZIO, sr.getNomeRete(), sr.log(), sr.tempoEsecuzione());
        return sr;
    }

    public void interrompiAlgoritmo(Algoritmo algoritmo){
        algoritmo.stop();
    }


}
