package input;

import myLib.Stringhe;
import reteAutomi.Automa;
import reteAutomi.Dizionario;
import reteAutomi.ReteAutomi;

public class GestoreFile {

    private String pathRete;
    private String pathOss;
    private String pathDiz;

    public GestoreFile() {
    }

    public ReteAutomi caricaRete() throws Exception{
        InputParser ip = new InputParser(pathRete);
        ReteAutomi ra = null;
        System.out.println("Carico " + pathRete);
        ra = ip.parseRete();
        return ra;
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
