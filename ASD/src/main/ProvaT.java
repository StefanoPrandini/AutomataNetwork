package main;

import input.InputParser;
import myLib.InputDati;
import myLib.Stringhe;
import reteAutomi.InputOutput;
import reteAutomi.ReteAutomi;
import reteAutomi.SpazioRilevanza;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static java.util.Objects.isNull;
import static main.Main.visualizzaOsservazioniLineariDisponibili;

public class ProvaT  {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        String nomeJSON = "AltraRete.json";
        // percorso della rete iniziale, in formato JSON
        String systemPathJSON;
        if(System.getProperty("os.name").equals("Mac OS X")) {
            systemPathJSON = System.getProperty("user.dir") + File.separator + "ASD" + File.separator + "JSON" + File.separator;
        }
        else systemPathJSON = System.getProperty("user.dir") + File.separator + "JSON" + File.separator;

        String pathJSON = systemPathJSON + nomeJSON;

        InputParser parser = new InputParser(pathJSON);
        ReteAutomi ra = null;
        try {
            ra = parser.parseRete();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO
        }

        int distanzaMax = SpazioRilevanza.ESPLORAZIONE_COMPLETA;
        InputOutput input = new InputOutput();
        input.setRete(ra);
        input.setDistanzaMax(distanzaMax);

        SpazioRilevanza spazioRilevanzaRete = new SpazioRilevanza(input);
        Thread threadRilevanza = new Thread(spazioRilevanzaRete);
        threadRilevanza.start();
        System.out.println(spazioRilevanzaRete.getHashRete());




    }

}
