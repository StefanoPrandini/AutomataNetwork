package main;

import input.GestoreDizionari;
import input.GestoreFile;
import myLib.InputDati;
import myLib.MyMenu;
import myLib.Stringhe;
import reteAutomi.Dizionario;
import reteAutomi.ReteAutomi;
import reteAutomi.SpazioRilevanza;

import static java.util.Objects.isNull;

public class Main {

	private static ReteAutomi ra;
	private static ReteAutomi oss;
	private static SpazioRilevanza sr;
	private static Dizionario diz;

	public static void main(String[] args) {



		MyMenu m = new MyMenu(Stringhe.TITOLO, Stringhe.OPZIONI);

		int scelta = m.scegli();
		while (scelta !=0) {
			switcher(scelta);
			scelta = m.scegli();
		}
	}




	//TODO opzioni aggiungibili:
	//spazio di rilevanza
	private static void switcher(int scelta){
		String filepath;
		GestoreFile gf;
		GestoreDizionari gd;

		switch(scelta){
			case 1: {
				//carica rete automi
				filepath = inputNomeFileJSON();
				gf = new GestoreFile(filepath);
				ra = gf.caricaRete();


				//TODO
				break;
			}
			case 2: {
				//carica osservazione
				filepath = inputNomeFileJSON();
				gf = new GestoreFile(filepath);
				oss = gf.caricaOsservazione();
			}

			case 3: {
				//Dizionario completo

				MyMenu sottoMenu3 = new MyMenu(Stringhe.TITOLO_SOTTO_MENU_DIZIONARIO_COMPLETO, Stringhe.OPZIONI_SOTTO_MENU_DIZIONARIO_COMPLETO);
				int sceltaSottoMenu3 = sottoMenu3.scegli();
				if ( ! isNull(ra) ){
					gd = new GestoreDizionari(ra);
					switchSottoMenu3(sceltaSottoMenu3, gd);
				}
				else{
					System.out.println(Stringhe.NESSUNA_RETE_CARICATA);
				}
				break;
			}
			case 4: {
				//"Monitoraggio",

				MyMenu sottoMenu4 = new MyMenu(Stringhe.TITOLO_SOTTO_MENU_MONITORAGGIO, Stringhe.OPZIONI_SOTTO_MENU_MONITORAGGIO);
				int sceltaMenu4 = sottoMenu4.scegli();
				gd = new GestoreDizionari(ra);
				switcherSottoMenu4(sceltaMenu4, gd);
				break;
			}
			case 5: {
				//"Prefisso",
				filepath = inputNomeFileJSON();
			}
			case 6: {
				//"Dizionario parziale da prefisso",
				filepath = inputNomeFileJSON();
			}
			case 7: {
				//"Dizionario parziale da osservazione",
				filepath = inputNomeFileJSON();
			}
			case 8: {
				//"Estensione dizionario"
				filepath = inputNomeFileJSON();
			}
			default: break;
		}


	}

	private static void switcherSottoMenu4(int sceltaMenu4, GestoreDizionari gd) {
		switch (sceltaMenu4){
			case 1: {
				effettuaMonitoraggio(gd);
				break;
			}
			case 2: {
				//TODO
				if ( !isNull(diz)) gestoreDizionari.infoDizionario();
				else System.out.println(Stringhe.NESSUN_DIZIONARIO);
				break;
			}
			case 3: {
				//TODO
				if ( !isNull(sr)) gestoreDizionari.infoSpazioRilevanza();
				else System.out.println(Stringhe.NESSUN_S_R);
				break;
			}
		}

	}

	private static void switchSottoMenu3(int sceltaSottoMenu3, GestoreDizionari gestoreDizionari) {
		switch (sceltaSottoMenu3){
			case 1: {
				costruisciDizionarioCompleto(gestoreDizionari);
				break;
			}
			case 2: {
				//TODO
				if ( !isNull(diz)) gestoreDizionari.infoDizionario();
				else System.out.println(Stringhe.NESSUN_DIZIONARIO);
				break;
			}
			case 3: {
				//TODO
				if ( !isNull(sr)) gestoreDizionari.infoSpazioRilevanza();
				else System.out.println(Stringhe.NESSUN_S_R);
				break;
			}
		}
	}

	private static String inputNomeFileJSON(){
		String fileJSON = InputDati.leggiStringa(Stringhe.INSERIRE_PERCORSO_FILE);
		return fileJSON;
	}

	private static boolean reteAutomiCaricata(){
		return !isNull(ra);
	}

	private static void costruisciDizionarioCompleto(GestoreDizionari gestoreDizionari){
		if (reteAutomiCaricata()){
			gestoreDizionari = new GestoreDizionari(ra);
			System.out.println(Stringhe.COSTRUZIONE_S_R);
			sr = gestoreDizionari.costruisciSpazioRilevanza();
			System.out.println(Stringhe.FINITO);
			System.out.println(Stringhe.COSTRUZIONE_DIZIONARIO);
			diz = gestoreDizionari.costruisciDizionarioCompleto();
			System.out.println(Stringhe.FINITO);
		}
		else{
			System.out.println(Stringhe.NESSUNA_RETE_CARICATA);
		}
	}



}
