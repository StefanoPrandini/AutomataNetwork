package myLib;

import gestore.GestoreInputOutput;

import java.text.SimpleDateFormat;
import java.util.Date;

import static myLib.InputDati.leggiStringa;

public class Utility {

    public static String creaNomeFile() {
        Date ora = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        return formatter.format(ora).trim().replace(' ', '_');
    }


    public static String inputNomeFileJSON(){
        String fileJSON = leggiStringa(Stringhe.INSERISCI_JSON);
        return fileJSON;
    }

    public static boolean rispondeNo(String risposta){
        if (risposta.equalsIgnoreCase("n") || risposta.equalsIgnoreCase("no"))
            return true;
        return false;
    }

    public static boolean rispostaValida(String risposta){
        for (String s : Stringhe.RISPOSTE_VALIDE) {
            if (risposta.equalsIgnoreCase(s))return true;
        }
        return false;
    }

    public static void stampaLog(GestoreInputOutput inputOutput) {
        System.out.println(Stringhe.TITOLO_LOG);
        for (String s : inputOutput.getLogMonitoraggio()) {
            System.out.println(s);
        }
    }
}
