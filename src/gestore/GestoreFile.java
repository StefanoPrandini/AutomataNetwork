package gestore;

import parser.InputParser;
import parser.OsservazioneParser;
import model.Automa;
import model.ReteAutomi;
import java.io.*;
import java.util.ArrayList;

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



    public void stampaLogAlgoritmo(String titolo, ArrayList<String> logAlgoritmo){


    }


}
