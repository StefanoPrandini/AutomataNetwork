package main;

import myLib.InputDati;
import myLib.Stringhe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static myLib.Utility.dataFormattata;

public class ProvaT  {


    public static void main(String[] args) {

        long startTime = System.nanoTime();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
    }



    private static String creaCartellaSessione(String input) throws IOException {
        String nomeCartella = input + dataFormattata();
        Path path = Paths.get(Stringhe.SESSIONI_INTERE_FOLDER + nomeCartella + File.separator);
        Files.createDirectory(path);
        return path.toString();
    }

/**
    private static boolean isClasseBella(Class classe){
        return classe.getName().equals("algoritmo.Dizionario");
    }
*/
}
