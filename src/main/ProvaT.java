package main;

import algoritmo.Dizionario;
import algoritmo.SpazioRilevanza;
import input.OsservazioneParser;
import model.Automa;
import model.ReteAutomi;
import myLib.Stringhe;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static myLib.Utility.creaNomeFile;

public class ProvaT  {


    public static void main(String[] args) {


    }


    private static boolean isClasseBella(Class classe){
        return classe.getName().equals("algoritmo.Dizionario");
    }
}
