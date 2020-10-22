package main;

import myLib.VerificaDati;

import java.io.*;

public class ProvaT  {
    public static void main(String[] args) throws IOException, ClassNotFoundException {


        String s = "1";

        if (VerificaDati.isCifraSingola(s))
            System.out.println("lol");
        else System.out.println("no");


    }

}
