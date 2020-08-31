package input;

import myLib.BelleStringhe;
import myLib.Stringhe;
import reteAutomi.ReteAutomi;

public class GestoreFile {

    private String path;

    public GestoreFile(String filepath) {
        this.path = filepath;

    }

    public ReteAutomi caricaRete() {
        InputParser ip = new InputParser(path);
        ReteAutomi ra = null;
        try {
            ra = ip.parseRete();
            System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO, ra.getNome()));
        } catch (Exception e) {
            System.out.println(Stringhe.ERRORE_CARICAMENTO);
        }
        return ra;
    }


    public ReteAutomi caricaOsservazione() {
        ReteAutomi osservazione = null;
        InputParser ip = new InputParser(path);
        try {
            osservazione = ip.parseRete();
            System.out.println(String.format(Stringhe.CARICAMENTO_RIUSCITO, osservazione.getNome()));
        } catch (Exception e) {
            System.out.println(Stringhe.ERRORE_CARICAMENTO);
        }
        return osservazione;
    }
}
