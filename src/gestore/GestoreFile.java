package gestore;

import input.InputParser;
import input.OsservazioneParser;
import myLib.Stringhe;
import model.Automa;
import algoritmo.Dizionario;
import model.ReteAutomi;
import algoritmo.SpazioRilevanza;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

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
}
