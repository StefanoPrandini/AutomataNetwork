package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.isNull;


public class ProvaTM {


    public static void main(String[] args) {

        String lol = "";
        System.out.println(isNull(lol));

        ArrayList<String> loller = new ArrayList<String>(Arrays.asList(lol.split(", ")));


        for (String s : loller) {
            System.out.println("el: " + s);
        }

        /**

        ArrayList<Thread> trs = new ArrayList<>();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Scanner s = new Scanner(System.in);

        System.out.println("inserisci");
        while (Integer.parseInt(s.next()) != 5){
            System.out.println("non zero");
        }
        int res = 0;
        for (int j = 0; j < Integer.parseInt(s.next()); j++) {
            int nome = j;
            ProvaT x = new ProvaT(j+10, nome);

            try {

                //submit produce un Future<Integer>
                res = res + exec.submit(x).get();
            }catch (InterruptedException ie){
                System.out.println(ie);
            }catch (ExecutionException ee){
                System.out.println(ee);
            }


        }


        System.out.println(res);


        exec.shutdown();

**/
    }
}
