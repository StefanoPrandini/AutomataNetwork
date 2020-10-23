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

import static java.util.Objects.isNull;

public class GestoreFile {

    private String pathRete;
    private String pathOss;
    private String pathDiz;

    public GestoreFile() {
    }

    public ReteAutomi caricaReteDaJSON() throws Exception{
        InputParser ip = new InputParser(pathRete);
        ReteAutomi ra = ip.parseRete();
        return ra;
    }

    public ReteAutomi caricaReteDaSessione() throws Exception{
        ObjectInputStream objectinputstream;
        FileInputStream streamIn;

        streamIn = new FileInputStream(pathRete);
        objectinputstream = new ObjectInputStream(streamIn);
        ReteAutomi ra = (ReteAutomi ) objectinputstream.readObject();
        System.out.println(ra);

        if (! isNull(objectinputstream) ) {
            objectinputstream.close();
        }

        return ra;


    }

    public SpazioRilevanza caricaSpazioRilevanza(String filepath) throws IOException, ClassNotFoundException {


        ObjectInputStream objectinputstream;
        FileInputStream streamIn;

        streamIn = new FileInputStream(filepath);
        objectinputstream = new ObjectInputStream(streamIn);
        SpazioRilevanza sr = (SpazioRilevanza ) objectinputstream.readObject();

        if (! isNull(objectinputstream) ) {
            objectinputstream.close();
        }

        return sr;

    }

    public Dizionario caricaDizionario(){
        Dizionario dizionario = null;
        InputParser ip = new InputParser(pathDiz);
        return dizionario;
    }

    public Automa caricaOsservazione() throws Exception {
        OsservazioneParser op = new OsservazioneParser(pathOss);
        return op.getOsservazione();
    }

    public ArrayList<String> caricaOsservazioneLineare(String nome) throws IOException, ClassNotFoundException {

        ArrayList<String> osservazioneLineare;

        ObjectInputStream objectinputstream;
        FileInputStream streamIn;

        streamIn = new FileInputStream(nome);
        objectinputstream = new ObjectInputStream(streamIn);
        osservazioneLineare = (ArrayList<String> ) objectinputstream.readObject();

        if (! isNull(objectinputstream) ) {
            objectinputstream.close();
        }

        return osservazioneLineare;

    }

    public Automa caricaAutomaOss(String filepath) throws ClassNotFoundException, IOException {

        Automa osservazione;

        ObjectInputStream objectinputstream;
        FileInputStream streamIn;

        streamIn = new FileInputStream(Stringhe.SAVE_FOLDER + filepath);
        objectinputstream = new ObjectInputStream(streamIn);
        osservazione = (Automa) objectinputstream.readObject();

        if (! isNull(objectinputstream) ) {
            objectinputstream.close();
        }

        return osservazione;

    }

    public void setPathRete(String pathRete) {
        this.pathRete = pathRete;
    }

    public void setPathOss(String pathOss) {
        this.pathOss = pathOss;
    }

    public void setPathDiz(String pathDiz) {
        this.pathDiz = pathDiz;
    }


}
