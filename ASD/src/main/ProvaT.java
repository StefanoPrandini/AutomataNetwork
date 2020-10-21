package main;

import myLib.InputDati;
import myLib.Stringhe;

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

import static java.util.Objects.isNull;
import static main.Main.visualizzaOsservazioniLineariDisponibili;

public class ProvaT  {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        visualizzaOsservazioniLineariDisponibili();
        String filepath = InputDati.leggiStringa(Stringhe.INSERISCI_SESSIONE);
        System.out.println(filepath);
        filepath = Stringhe.SAVE_FOLDER+filepath;
        System.out.println(filepath);

        ArrayList<String> osservazioneLineare;

        ObjectInputStream objectinputstream;
        FileInputStream streamIn;

        streamIn = new FileInputStream(filepath);
        objectinputstream = new ObjectInputStream(streamIn);
        osservazioneLineare = (ArrayList<String> ) objectinputstream.readObject();

        if (! isNull(objectinputstream) ) {
            objectinputstream.close();
        }

        System.out.println(osservazioneLineare);


    }

}
