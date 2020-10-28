package main;

import myLib.InputDati;
import myLib.Stringhe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static myLib.Utility.dataFormattata;

public class ProvaT  {


    public static void main(String[] args) {

        String in = InputDati.leggiStringa("lol");
        try {
            System.out.println(creaCartellaSessione(in));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static String creaCartellaSessione(String input) throws IOException {
        String nomeCartella = input + dataFormattata();
        Path path = Paths.get(Stringhe.SESSIONI_INTERE_FOLDER + nomeCartella + File.separator);
        Files.createDirectory(path);
        return path.toString();
    }


    private static boolean isClasseBella(Class classe){
        return classe.getName().equals("algoritmo.Dizionario");
    }
}
