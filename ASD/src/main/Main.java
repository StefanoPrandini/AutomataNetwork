package main;

import input.GestoreDizionari;
import input.GestoreFile;
import input.InputParser;
import myLib.InputDati;
import myLib.MyMenu;
import myLib.Stringhe;
import reteAutomi.Dizionario;
import reteAutomi.ReteAutomi;
import reteAutomi.SpazioRilevanza;

import java.io.File;

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
				//"Dizionari parziali",
				filepath = inputNomeFileJSON();
			}
			case 7: {
				//"Estensione dizionario"
				filepath = inputNomeFileJSON();
			}
			case 8: {
				//"Riassunto automi"
				filepath = inputNomeFileJSON();
			}
			case 9: {
				//salva sessione
			}
			case 10: {
				//carica sessione
				if(sessioniDisponibili()){
					MyMenu sottoMenu10 = new MyMenu(Stringhe.TITOLO_MENU_10, Stringhe.OPZIONI_MENU_10);
					int scelta10 = sottoMenu10.scegli();
					switcher10(scelta10);
				}
				break;
			}



			default: break;
		}


	}

	private static void switcher10(int scelta10) {
		String filepath = InputDati.leggiStringa(Stringhe.INSERISCI_SESSIONE);
		File folder = new File(Stringhe.SAVE_FOLDER);
		File[] files = folder.listFiles();
		File sessione = null;
		boolean flag = false;
		for (File file : files) {
			if (file.toString().equals(filepath)){
				sessione = file;
				flag = true;
				break;
			}
		}
		if ( ! flag){
			System.out.println("Nessuna sessione presente con il nome specificato");
		}else {
			if (scelta10 == 1){
				System.out.println("Carico sessione");
				caricaSessione(sessione);
				System.out.println("Sessione caricata");
			}
			else if (scelta10 == 2){
				System.out.println("Elimino sessione");
				eliminaSessione(filepath);
				System.out.println("Sessione eliminata");
				
			}
		}
		
		
	}

	private static void eliminaSessione(String filepath) {
	}

	private static void caricaSessione(File sessione) {
	}

	private static boolean sessioniDisponibili() {
		File folder = new File(Stringhe.SAVE_FOLDER);
		File[] files = folder.listFiles();
		boolean flag = false;
		if (files.length == 0){
			System.out.println("Nessuna sessione trovata");
		}
		else{
			System.out.println("Sessioni disponibili: ");
			for (File file : files) {
				System.out.println(file);
			}
			flag = true;
		}
		return flag;
	}

	private static void switcherSottoMenu4(int sceltaMenu4, GestoreDizionari gd) {
		switch (sceltaMenu4){
			case 1: {
				//effettuaMonitoraggio(gd);
				break;
			}
			case 2: {
				//TODO
				if ( !isNull(diz)) ;//gestoreDizionari.infoDizionario();
				else System.out.println(Stringhe.NESSUN_DIZIONARIO);
				break;
			}
			case 3: {
				//TODO
				if ( !isNull(sr)) ;//gestoreDizionari.infoSpazioRilevanza();
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
				if ( !isNull(diz)) System.out.println(gestoreDizionari.infoDizionario(diz));
				else System.out.println(Stringhe.NESSUN_DIZIONARIO);
				break;
			}
			case 3: {
				//TODO
				if ( !isNull(sr)) System.out.println(gestoreDizionari.infoSpazioRilevanza(sr));
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
			System.out.println(sr);
			System.out.println(Stringhe.FINITO);
			System.out.println(Stringhe.COSTRUZIONE_DIZIONARIO);
			diz = gestoreDizionari.costruisciDizionarioCompleto(sr);
			System.out.println(diz);
			System.out.println(Stringhe.FINITO);
		}
		else{
			System.out.println(Stringhe.NESSUNA_RETE_CARICATA);
		}
	}



}
