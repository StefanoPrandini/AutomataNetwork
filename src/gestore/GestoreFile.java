package gestore;

import algoritmo.Dizionario;
import myLib.Stringhe;
import parser.InputParser;
import parser.OsservazioneParser;
import model.Automa;
import model.ReteAutomi;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.util.Objects.isNull;

public class GestoreFile {

    /**
     *      CARICACAMENTO DA JSON
     */

    public ReteAutomi caricaReteDaJSON(String pathRete) throws Exception{
        InputParser ip = new InputParser(pathRete);
        return ip.parseRete();
    }

    public Automa caricaOsservazioneDaJSON(String pathOss) throws Exception {
        OsservazioneParser op = new OsservazioneParser(pathOss);
        return op.getOsservazione();
    }




    /**
     *      CARICACAMENTO DA SESSIONI PRECEDENTI
     */


    public Object caricaDaSessione(String path) throws Exception {
        ObjectInputStream objectinputstream;
        FileInputStream streamIn;

        streamIn = new FileInputStream(path);
        objectinputstream = new ObjectInputStream(streamIn);
        Object oggetto;

        oggetto = objectinputstream.readObject();

        if (! isNull(objectinputstream) ) {
            objectinputstream.close();
        }
        return oggetto;
    }


    private static boolean esisteFileDaAppendere(String filepath){
        File folder = new File(Stringhe.LOG_PATH);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getAbsolutePath().equals(filepath)){
                return true;
            }
        }
        return false;
    }

    public static void stampaLogAlgoritmo(String logDestinazione, String nomeRete, String compendio, long tempoTrascorso){
        BufferedWriter writer = null;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date ora = new Date();
        String s = String.format(Stringhe.EVENTO_LOG, sdf.format(ora), nomeRete, compendio, tempoTrascorso);
        try {
            writer = new BufferedWriter(new FileWriter(logDestinazione, esisteFileDaAppendere(logDestinazione)));
            writer.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }
    }

    public static void stampaLogAlgoritmoMonitoraggio(ArrayList<String> osservazioneLineare, ArrayList<String> res, long tempoTrascorso){
        BufferedWriter writer = null;
        String logDestinazione = Stringhe.FILE_LOG_MONITORAGGIO;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date ora = new Date();


        StringBuilder sb = new StringBuilder();
        for (String re : res) {
            sb.append(re + "\n\t\t");
        }
        String s = String.format(Stringhe.EVENTO_LOG, sdf.format(ora), osservazioneLineare.toString(), sb.toString(), tempoTrascorso);
        try {
            writer = new BufferedWriter(new FileWriter(logDestinazione, esisteFileDaAppendere(logDestinazione)));
            writer.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }
    }

    public static void stampaLogAlgoritmoRicerca(Dizionario dizionario){
        long tempoTrascorso = dizionario.tempoEsecuzione();
        BufferedWriter writer = null;
        String logDestinazione = Stringhe.FILE_LOG_RICERCA;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date ora = new Date();
        String risultato;


        ArrayList<String> osservazioneLineare = dizionario.getInputOutput().getOsservazioneLineareRicerca();
        //non ci sono risultati
        if (isNull(dizionario.getDiagnosi()) && !dizionario.isRisultatoParziale()) {
            risultato = Stringhe.NESSUN_RISULTATO;
        } else if (dizionario.isRisultatoParziale()){
            //c'e' un risultato parziale
             risultato = String.format(Stringhe.RISULTATO_RICERCA_PARZIALE, dizionario.getOsservazioneLineareParziale(), dizionario.getEtichettaMancanteInRicerca(), dizionario.getDiagnosi());
        }
        else {

            risultato = dizionario.getDiagnosi().toString();
        }
        String s = String.format(Stringhe.EVENTO_LOG, sdf.format(ora), osservazioneLineare.toString(), risultato, tempoTrascorso);
        try {
            writer = new BufferedWriter(new FileWriter(logDestinazione, esisteFileDaAppendere(logDestinazione)));
            writer.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void stampaLogAlgoritmoEstensione(String nomeOss, String risultato, long tempoTrascorso){
        BufferedWriter writer = null;
        String logDestinazione = Stringhe.FILE_LOG_ESTENSIONE;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date ora = new Date();
        String s = String.format(Stringhe.EVENTO_LOG, sdf.format(ora), nomeOss, risultato, tempoTrascorso);
        try {
            writer = new BufferedWriter(new FileWriter(logDestinazione, esisteFileDaAppendere(logDestinazione)));
            writer.write(s);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

}
