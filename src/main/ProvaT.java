package main;

import myLib.InputDati;
import myLib.Stringhe;
import myLib.VerificaDati;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class ProvaT  {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


ArrayList<String> res = new ArrayList<>();
        String input= InputDati.leggiStringa("msg");
        ArrayList<String> in = new ArrayList<>(Arrays.asList(input.split(",")));
        for (String s : in) {
            s = s.trim();
            if ( ! s.equals(Stringhe.STRINGA_VUOTA)) res.add(s);

        }

        for (String re : res) {
            System.out.println(re);
        }



    }

}
