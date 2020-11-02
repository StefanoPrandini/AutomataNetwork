package gestore;

import myLib.Stringhe;
import parser.InputParser;
import parser.OsservazioneParser;
import model.Automa;
import model.ReteAutomi;
import java.io.*;
import java.util.Date;

import static java.util.Objects.isNull;

public class GestoreFile {


    /**
     *      CARICACAMENTO DA JSON
     */

    public ReteAutomi caricaReteDaJSON(String pathRete) throws Exception{
        InputParser ip = new InputParser(pathRete);
        ReteAutomi ra = ip.parseRete();
        return ra;
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


    private boolean esisteFileDaAppendere(String filepath){
        File folder = new File(Stringhe.LOG_PATH);
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getAbsolutePath().equals(filepath)){
                return true;
            }
        }
        return false;
    }




    public void stampaLogAlgoritmo(String logDestinazione, String nomeRete, String compendio, String tempoTrascorso){
        BufferedWriter writer = null;
        String s = String.format(Stringhe.EVENTO_LOG, new Date(), nomeRete, compendio, tempoTrascorso);
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
