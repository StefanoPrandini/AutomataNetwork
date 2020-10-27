package main;

import algoritmo.Dizionario;
import algoritmo.SpazioRilevanza;
import input.OsservazioneParser;
import model.Automa;
import model.ReteAutomi;
import myLib.Stringhe;

import java.io.*;
import java.util.ArrayList;

import static myLib.Utility.creaNomeFile;

public class ProvaT  {


    public static void main(String[] args) {

        try {
            OsservazioneParser op = new OsservazioneParser(Stringhe.EXAMPLE_PATH + "/OsservazionePerEstensione.json");
            Automa a = op.getOsservazione();
            System.out.println(a);
            String nome = "automaOss" + Stringhe.ESTENSIONE_AUTOMA_OSS;
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(Stringhe.SAVE_FOLDER + nome)));
            oos.writeObject(a);
            oos.close();
            System.out.println(String.format(Stringhe.SALVATAGGIO_OK, nome));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static boolean isClasseBella(Class classe){
        return classe.getName().equals("algoritmo.Dizionario");
    }
}
