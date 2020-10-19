package main;

import myLib.InputDati;
import myLib.Stringhe;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class ProvaT  {
    public static void main(String[] args) {
        String input= InputDati.leggiStringa(Stringhe.INSERIMENTO_OSSERVAZIONE);
        ArrayList<String> osservazioneLineare = new ArrayList<>(Arrays.asList(input.split(", ")));
        for (String s : osservazioneLineare) {
            System.out.println("hai inserito " + s);
        }

        String input2= InputDati.leggiStringa(Stringhe.INSERIMENTO_OSSERVAZIONE);
        ArrayList<String> aggiunte = new ArrayList<>(Arrays.asList(input2.split(", ")));
        osservazioneLineare.addAll(aggiunte);
        for (String s : osservazioneLineare) {
            System.out.println("hai inserito " + s);
        }
        System.out.println(osservazioneLineare);
    }


}
